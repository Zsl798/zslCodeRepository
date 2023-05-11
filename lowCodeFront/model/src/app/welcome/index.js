import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';

import {
  Progressbar,
  Modal,
  UpdateMessage,
  FormatMessage,
  Button,
} from 'components';
import { fail, success, pageType, CONFIG } from '../../lib/variable';
import './style/index.less';
import {
  getUserConfigData,
  removeHistory,
  updateHistory,
  saveUserConfigSome,
} from '../../actions/config';
import Home from '../home';
import { setMemoryCache } from '../../lib/cache';
import {
  openProject,
  updateProject,
  closeProject,
  createProject,
  renameProject,
  removeProject,
  saveProject,
  autoSaveProject,
  openDemoProject,
} from '../../actions/core';
import { openLoading, closeLoading } from '../../actions/common';
import { ConfigContent } from '../../lib/context';
import { getPrefix } from '../../lib/prefixUtil';
import { queryItemList } from '../../api/project';

// eslint-disable-next-line import/named
const platform = 'json';
const Welcome = React.memo(({ prefix, config, ...restProps }) => {
  const [percent, updatePercent] = useState(50);
  // 由于显示该标题时还无法获取到用户数据，无法支持国际化
  const [title, updateTitle] = useState('获取配置数据...');
  const [loadingTitle] = useState('加载中');
  const getUserData = restProps.getUserData;
  useEffect(() => {
    //请求文件信息
    const params = {
      projectName: '',
      pageNo : 1,
      pageSize : 18,
    };
     queryItemList(params).then((listInfo) => {
      // 第一个页面打开 读取各个配置文件
      console.log('init-queryItemList', listInfo);
      const dataModelListInfo = {
        ...listInfo,
        ...params,
      };
      getUserData(dataModelListInfo);
    });
  }, []);
  useEffect(() => {
    if (config.result === success) {
      // 读取配置信息成功
      updatePercent(90);
      setMemoryCache(CONFIG, config);
      if (platform === 'json') {
        updateTitle(FormatMessage.string({ id: 'welcomeVersionTitle' }));
        updatePercent(100);
      } else {
        setTimeout(() => {
          updatePercent(100);
        }, 1000);
      }
    } else if (config.result === fail) {
      // 读取配置信息失败
      Modal.error({ title: config?.data.toString() });
    }
  }, [config.result]);
  const currentPrefix = getPrefix(prefix);
  if (percent === 100) {
    return (
      <ConfigContent.Provider value={config}>
        <Home config={config} {...restProps} />
      </ConfigContent.Provider>
    );
  }
  return (
    <div className={`${currentPrefix}-welcome`}>
      {/* <div className={`${currentPrefix}-welcome-header`}>
        <div className={`${currentPrefix}-welcome-header-logo`}>{}</div>
        <div className={`${currentPrefix}-welcome-header-name`}>
          <div>元数建模</div>
          <div>CHINER</div>
        </div>
      </div> */}
      {/* <div className={`${currentPrefix}-welcome-body`}>
        <div className={`${currentPrefix}-welcome-body-left`}>
          <div className={`${currentPrefix}-welcome-body-left-bg`}>{}</div>
        </div>
        <div className={`${currentPrefix}-welcome-body-right`}>
          <div className={`${currentPrefix}-welcome-body-right-circle`}>{}</div>
          <div className={`${currentPrefix}-welcome-body-right-icon`}>{}</div>
          <div className={`${currentPrefix}-welcome-body-right-loading`}>
            <span>{loadingTitle}</span>
            <span
              className={`${currentPrefix}-welcome-body-right-loading-block`}
            >
              {}
            </span>
            <span
              className={`${currentPrefix}-welcome-body-right-loading-block`}
            >
              {}
            </span>
            <span
              className={`${currentPrefix}-welcome-body-right-loading-block`}
            >
              {}
            </span>
          </div>
          <div className={`${currentPrefix}-welcome-body-right-progress`}>
            <Progressbar
              title={title}
              percent={percent}
              className={`${currentPrefix}-welcome-progressbar`}
              showPercent
            />
          </div>
          <div className={`${currentPrefix}-welcome-body-right-copy`}>
            The most popular database table design tool in China
          </div>
        </div>
      </div> */}
      {/* eslint-disable-next-line max-len */}
      {/*<Progressbar title={title} percent={percent} className={`${currentPrefix}-welcome-progressbar`} showPercent/>*/}
    </div>
  );
});

//将state的属性映射到props上
const mapStateToProps = (state) => {
  return {
    projectInfo: state.core.info,
    dataSource: state.core.data,
    versionsData: state.core.versionsData,
    config: state.config,
    common: state.common,
  };
};
//将action的所有方法绑定到props上
const mapDispatchToProps = (dispatch) => {
  return {
    getUserData: (listInfo) => {
      dispatch(getUserConfigData(listInfo));
    },
    saveUserData: (data) => {
      dispatch(saveUserConfigSome(data));
    },
    openTemplate: (h, t, title) => {
      dispatch(openDemoProject(h, t, title, pageType[2]));
    },
    open: (title, id, suffix, isDemoProject) => {
      dispatch(openProject(title, pageType[2], id, suffix, isDemoProject));
    },
    close: () => {
      dispatch(closeProject(pageType[1]));
    },
    update: (data) => {
      dispatch(updateProject(data));
    },
    // rename: (newData, oldData, title, dataInfo) => {
    //   dispatch(renameProject(newData, oldData, title, dataInfo));
    // },
    rename: (newData, title) => {
      dispatch(renameProject(newData, title));
    },
    delete: (data, title) => {
      dispatch(removeProject(data, title));
    },
    save: (data, title, saveAs, callback) => {
      dispatch(saveProject(data, saveAs, callback));
    },
    autoSave: (data) => {
      // 静悄悄保存 无需任何提示
      dispatch(autoSaveProject(data));
    },
    // create: (data, path, title) => {
    create: (data, title) => {
      // dispatch(createProject(data, path, title, pageType[2]));
      dispatch(createProject(data, title, pageType[2]));
    },
    openLoading: (title) => {
      dispatch(openLoading(title));
    },
    closeLoading: () => {
      dispatch(closeLoading());
    },
    updateHistory: (opt, ...data) => {
      switch (opt) {
        case 'remove':
          dispatch(removeHistory(...data));
          break;
        case 'update':
          dispatch(updateHistory(...data));
          break;
        default:
          break;
      }
    },
  };
};

export default connect(mapStateToProps, mapDispatchToProps)(Welcome);
