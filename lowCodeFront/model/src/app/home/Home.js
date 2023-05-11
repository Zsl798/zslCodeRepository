/* eslint-disable semi */
/* eslint-disable comma-dangle */
import React, { useState } from 'react';
import _ from 'lodash/object';

import {
  Icon,
  Modal,
  Message,
  openModal,
  Button,
  FormatMessage,
  SearchInput,
  Tooltip,
  Pagination,
} from 'components';
// eslint-disable-next-line import/extensions,import/named

// eslint-disable-next-line import/named
// import { platform } from '../../lib/middle';
import ProjectEdit from '../container/projectedit';
import { getPrefix } from '../../lib/prefixUtil';
// import { version } from '../../../package';

import * as template from '../../lib/template';
import { updateItem, queryItemList } from '../../api/project';

// const CodeImg = ({currentPrefix}) => {
//   return <div className={`${currentPrefix}-home-container-codeimg`}>
//     <div><FormatMessage id='home.optBookTitle'/></div>
//     <div>
//       <div>
//         <img src='./asset/codeimage/web.png' alt=''/>
//         <span><FormatMessage id='home.optBookTitle1'/></span>
//       </div>
//       <p/>
//       <div>
//         <img src='./asset/codeimage/app.jpeg' alt=''/>
//         <span><FormatMessage id='home.optBookTitle2'/></span>
//       </div>
//     </div>
//   </div>;
// };
const platform = 'json';
export default React.memo(
  ({
    prefix,
    importProject,
    createProject,
    openTemplate,
    renameProject,
    updateHistory,
    deleteProject,
    lang,
    config,
  }) => {
    console.log('传过来的config', config);

    const currentPrefix = getPrefix(prefix);
    const [searchValue, setSearchValue] = useState('');
    const proInfo = (h) => {
      debugger;
      Modal.info({
        title: FormatMessage.string({ id: 'home.proInfo' }),
        message: <ProjectEdit data={h} reading lang={lang} />,
        contentStyle: {
          height: '50vh',
          overflow: 'auto',
        },
      });
    };
    const _createProject = (projectData) => {
      let modal = null;
      let newDataSource = {};
      let oldData = {};
      const onOk = () => {
        debugger;
        if (
          !newDataSource.modelName ||
          // (!newDataSource.path && platform === 'json')
          !platform === 'json'
        ) {
          Modal.error({
            title: FormatMessage.string({ id: 'optFail' }),
            message: FormatMessage.string({ id: 'formValidateMessage' }),
          });
        } else {
          // if (projectData && Object.keys(oldData).length > 0) {
          //   renameProject(newDataSource, oldData, projectData);
          if (projectData) {
            //已有的项目
            const params = {
              projectId: newDataSource.projectId,
              projectName: newDataSource.projectName,
              remark: newDataSource.remark,
            };
            updateItem(params).then((res) => {
              console.log('updated!', res);
              const configInfo = config?.data[0];
              const queryParams = {
                projectName: configInfo.projectName,
                pageNo: configInfo.currentPage,
                pageSize: configInfo.pageSize,
              };
              queryItemList(queryParams).then((listInfo) => {
                const newListInfo = {
                  ...listInfo,
                  ...queryParams,
                };
                renameProject(newListInfo);
              });
            });
          } else {
            //新建项目
            if (!newDataSource.remark) {
              newDataSource.remark = newDataSource.projectName;
            }
            //新建项目(_.omit返回去掉特定属性的对象)
            // createProject(_.omit(newDataSource, ['path']), newDataSource.path);
            createProject(newDataSource);
          }
          modal && modal.close();
        }
      };
      const onCancel = () => {
        modal && modal.close();
      };
      const onChange = (project) => {
        newDataSource = project;
      };
      const dataFinish = (data) => {
        if (data) {
          newDataSource = data;
          oldData = data;
        } else {
          modal && modal.close();
        }
      };
      modal = openModal(
        <ProjectEdit
          data={projectData}
          lang={lang}
          onChange={onChange}
          dataFinish={dataFinish}
        />,
        {
          title: projectData?.projectName
            ? FormatMessage.string({ id: 'home.editDataModel' })
            : FormatMessage.string({ id: 'home.createDataModel' }),
          buttons: [
            <Button type="primary" key="ok" onClick={onOk}>
              <FormatMessage id="button.ok" />
            </Button>,
            <Button key="onCancel" onClick={onCancel}>
              <FormatMessage id="button.cancel" />
            </Button>,
          ],
        }
      );
    };
    const proConfig = (h) => {
      _createProject(h);
    };
    const removeHistory = (h) => {
      updateHistory('remove', h);
    };
    const removeProject = (h) => {
      Modal.confirm({
        title: FormatMessage.string({ id: 'deleteConfirmTitle' }),
        message: FormatMessage.string({ id: 'deleteFromDiskConfirm' }),
        lang,
        onOk: () => {
          deleteProject(h);
        },
      });
    };
    const openTemplateClick = (data, t) => {
      openTemplate(data, t);
    };
    const onProjectClick = (h) => {
      importProject(h.projectId);
    };
    const _menuClick = (m, p) => {
      if (m.key === 'removeHistory') {
        removeHistory(p);
      } else {
        removeProject(p);
      }
    };
    const onMouseOver = (e) => {
      const target = e.currentTarget;
      const child = target.children[1];
      child.style.display = 'block';
    };
    const onMouseLeave = (e) => {
      const target = e.currentTarget;
      target.children[1].style.display = 'none';
    };
    const onChange = (e) => {
      setSearchValue(e.target.value);
    };
    const keyDown = (e, finalValue) => {
      debugger;
      if (e.key === 'Enter') {
        const params = {
          projectName: finalValue,
          pageNo: 1,
          pageSize: config?.data[0]?.pageSize,
        };
        queryItemList(params).then((listInfo) => {
          console.log('search-queryItemList', listInfo);
          const newListInfo = {
            ...listInfo,
            ...params,
          };
          updateHistory('update', newListInfo);
        });
      }
    };
    const Tool = ({ p }) => {
      const dropDownMenus = [
        // {
        //   key: 'removeHistory',
        //   name: FormatMessage.string({ id: 'home.removeHistory' }),
        // },
        {
          key: 'removeProject',
          name: FormatMessage.string({ id: 'home.removeProject' }),
        },
      ];
       // iframe获取Vue传递过来的信息
        window.addEventListener("message", getVueMsg);
        function getVueMsg(event){
          const res = event.data;
           if(res.cmd == 'myVue'){
            const params = {
              projectName: res.params.info,
              pageNo: 1,
              pageSize: 10,
            };
            queryItemList(params).then((listInfo) => {
              console.log('search-queryItemList', listInfo);
              const newListInfo = {
                ...listInfo,
                ...params,
              };
              updateHistory('update', newListInfo);
            });
          }

        };

      return (
        <div
          className={`${currentPrefix}-home-container-body-right-list-tab-item-tool`}
        >
          <span>
            <Icon type="icon-xinxi" onClick={() => proInfo(p)} />
          </span>
          <span>
            <Icon type="edit.svg" onClick={() => proConfig(p)} />
          </span>
          <span onMouseOver={onMouseOver} onMouseLeave={onMouseLeave}>
            <span>
              <Icon type="more.svg" />
            </span>
            <span>
              {dropDownMenus.map((m) => {
                return (
                  <div key={m.key} onClick={() => _menuClick(m, p)}>
                    {m.name}
                  </div>
                );
              })}
            </span>
          </span>
        </div>
      );
    };
    // const openUrl = (url) => {
    //   const href = url;
    //   if (platform === 'json') {
    //     // eslint-disable-next-line global-require,import/no-extraneous-dependencies
    //     // require('electron').shell.openExternal(href);
    //   } else {
    //     const a = document.createElement('a');
    //     a.href = href;
    //     a.click();
    //   }
    // };
    const reg = new RegExp(
      (searchValue || '').replace(/[-\/\\^$*+?.()|[\]{}]/g, '\\$&'),
      'i'
    );
    return (
      <div className={`${currentPrefix}-home-container`}>
        <div className={`${currentPrefix}-home-container-body`}>
          <div className={`${currentPrefix}-home-container-body-right`}>
            <div className={`${currentPrefix}-home-container-body-right-list`}>
              <div
                className={`${currentPrefix}-home-container-body-right-list-header`}
              >
                <div
                  className={`${currentPrefix}-home-container-body-right-list-header-button`}
                  onClick={() => _createProject()}
                >
                  <div
                    className={`${currentPrefix}-home-container-body-right-list-header-button-title`}
                  >
                    <FormatMessage id="toolbar.create" />
                  </div>
                </div>
              </div>

              <div>
                <div
                  className={`${currentPrefix}-home-container-body-right-ad-container`}
                >
                  <div
                    className={`${currentPrefix}-home-container-body-right-list-split`}
                  ></div>
                  <div
                    className={`${currentPrefix}-home-container-body-right-list-title`}
                  >
                    <FormatMessage id="home.projectList" />
                  </div>

                </div>

                <div
                  className={`${currentPrefix}-home-container-body-right-list-tab`}
                >
                  <div
                    className={`${currentPrefix}-home-container-body-right-list-tab-header`}
                  >
                    {/* <div
                      className={`${currentPrefix}-home-container-body-right-list-tab-header-title
                 ${currentPrefix}-home-container-body-right-list-tab-header-title-selected`}
                    >
                      <FormatMessage id="home.recently" />
                    </div> */}
                  </div>
                  <div
                    className={`${currentPrefix}-home-container-body-right-list-tab-content project-content`}
                  >
                    {/* {config?.data[0]?.projectHistories
                      ?.filter((p) => {
                        // p &&
                          (reg.test(p.describe || '') ||
                            reg.test(p.name || ''));
                      }) */}
                    {config?.data[0]?.projectHistories?.map((p, index) => {
                      return (
                        <Tooltip
                          offsetLeft={-18}
                          offsetTop={-2}
                          force
                          title={<Tool p={p} />}
                          placement="top"
                          key={index}
                          className={`${currentPrefix}-home-container-body-right-list-tab-item-tooltip`}
                        >
                          <div
                            onClick={() => {
                              onProjectClick(p);
                            }}
                            className={`${currentPrefix}-home-container-body-right-list-tab-item`}
                          >
                            <div
                                className={`${currentPrefix}-home-container-body-right-list-tab-item-title`}
                            >
                              {p.projectName}
                            </div>
                            <div
                              className={`${currentPrefix}-home-container-body-right-list-tab-item-body`}
                            >
                              <div
                                className={`${currentPrefix}-home-container-body-right-list-tab-item-body-icon`}
                              >
                                {p.avatar ? (
                                  <img src={p.avatar} alt="avatar" />
                                ) : (
                                  <Icon type="fa-file" />
                                )}
                              </div>
                              {/* <div
                                className={`${currentPrefix}-home-container-body-right-list-tab-item-body-desc`}
                              >
                                {p.remark}
                              </div> */}
                            </div>
                          </div>
                        </Tooltip>
                      );
                    })}
                  </div>
                  <div
                    className={`${currentPrefix}-home-container-body-right-list-tab-pagination`}
                  >
                    <Pagination
                      totalItems={config?.data[0]?.projectCount}
                      pageSize={config?.data[0]?.pageSize}
                      pageNo={config?.data[0]?.currentPage}
                      cb={(pageNo, pageSize) => {
                        console.log(pageNo, pageSize);
                        const params = {
                          projectName: config?.data[0]?.projectName,
                          pageNo,
                          pageSize,
                        };
                        queryItemList(params).then((listInfo) => {
                          console.log('page-queryItemList', listInfo);
                          const newListInfo = {
                            ...listInfo,
                            ...params,
                          };
                          updateHistory('update', newListInfo);
                        });
                      }}
                    />
                  </div>
                </div>
              </div>

              {/* <div
                className={`${currentPrefix}-home-container-body-right-list-demo`}
              >
                <div
                  className={`${currentPrefix}-home-container-body-right-list-title`}
                >
                  <FormatMessage id="home.exampleProject" />
                </div>
                <div
                  className={`${currentPrefix}-home-container-body-right-list-tab-content`}
                >
                  {Object.keys(template).map((t, index) => {
                    const p = template[t];
                    return (
                      <div
                        key={index}
                        onClick={() => openTemplateClick(p, t)}
                        className={`${currentPrefix}-home-container-body-right-list-tab-item`}
                      >
                        <div
                          className={`${currentPrefix}-home-container-body-right-list-tab-item-title`}
                        >
                          {p.projectName}
                        </div>
                        <div
                          className={`${currentPrefix}-home-container-body-right-list-tab-item-body`}
                        >
                          <div
                            className={`${currentPrefix}-home-container-body-right-list-tab-item-body-icon`}
                          >
                            {p.avatar ? (
                              <img src={p.avatar} alt="avatar" />
                            ) : (
                              <Icon type="fa-file" />
                            )}
                          </div>
                          <div
                            className={`${currentPrefix}-home-container-body-right-list-tab-item-body-desc`}
                          >
                            {p.describe}
                          </div>
                        </div>
                      </div>
                    );
                  })}
                </div>
              </div> */}
            </div>
          </div>
        </div>
      </div>
    );
  }
);
