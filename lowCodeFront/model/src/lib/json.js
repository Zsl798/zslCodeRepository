import * as _ from 'lodash/object';
import path from 'path';
import { saveItem } from '../api/project';

export const getUserConfig = (listInfo) => {
  return new Promise((res, rej) => {
    // 获取用户的配置信息
    const defaultUserConfigData = {
      projectHistories: listInfo.list || [], // 历史项目记录
      lang: 'zh', // 当前应用使用的语言
      projectCount: listInfo.count, //总项目数
      projectName: listInfo.projectName || '', //搜索值
      currentPage: listInfo.pageNo || 1, //当前页数
      pageSize: listInfo.pageSize || 18, //每页项目数
    };
    const defaultProjectConfigData = {
      commonFields: [], // 通用字段信息 多个项目可通用
    };
    /*
     * %APPDATA% Windows 中
     * $XDG_CONFIG_HOME or ~/.config Linux 中
     * ~/Library/Application Support macOS 中
     * */
    const getData = (defaultData) => {
      return new Promise((r, j) => {
        // readJsonPromise(path).then((data) => {
        //   r({
        //     ...defaultData,
        //     ...data,
        //   });
        // }).catch(() => {
        //   // 如果用户信息报错 则需要使用默认数据进行覆盖
        //   saveJsonPromise(path, defaultData).then(() => {
        //     r(defaultData);
        //   }).catch((err) => {
        //     rej(err);
        //   })
        // });
        r({
          ...defaultData,
        });
      });
    };

    Promise.all([
      getData(defaultUserConfigData),
      getData(defaultProjectConfigData),
    ])
      .then((result) => {
        setTimeout(() => {
          res(result);
        }, 1000);
      })
      .catch((err) => {
        rej(err);
      });
  });
};

export const saveNormalFile = (dataBuffer) => {
  // 通用的文件保方法
  return new Promise((res, rej) => {
    // fs.writeFile(file, dataBuffer, (err) => {
    //   if (err) {
    //     rej(err);
    //   } else {
    //     res(dataBuffer);
    //   }
    // });
    // const param = {
    //   projectId: dataBuffer.id,
    //   content: JSON.stringify(dataBuffer),
    // };
    // debugger;
    // saveItem(param).then((response) => {
    //   console.log('save successfully!', response);
    //   res(dataBuffer);
    // });
    res(dataBuffer);
  });
};

// 文件名和路径拼接
export const dirSplicing = (dir, fileName) => {
  return path.join(dir, fileName);
};

// export const saveJsonPromise = (filePath, data) => {
export const saveJsonPromise = (data) => {
  return new Promise((res, rej) => {
    // const tempData =
    //   typeof data === 'string' ? data : JSON.stringify(data, null, 2);
    // const tempFilePath = filePath.endsWith('.json')
    //   ? filePath
    //   : `${filePath}.json`;
    if (!data) {
      rej(new Error('saveJsonPromise error'));
    } else {
      // saveNormalFile(tempFilePath, tempData)
      saveNormalFile(data)
        .then((data) => {
          res(data);
        })
        .catch((err) => {
          rej(err);
        });
    }
  });
};

export const saveUserConfig = (data = []) => {
  return new Promise((res, rej) => {
    Promise.all([
      // saveJsonPromise(userConfigPath, data[0] || {}),
      // saveJsonPromise(projectConfigPath, data[1] || {}),
      saveJsonPromise(data[0] || {}),
      saveJsonPromise(data[1] || {}),
    ])
      .then((result) => {
        setTimeout(() => {
          res(result);
        }, 100);
      })
      .catch((err) => {
        rej(err);
      });
  });
};

export const saveJsonPromiseAs = (data, refactor) => {
  const tempData =
    typeof data === 'string' ? data : JSON.stringify(data, null, 2);
  const extensions = [];
  if (process.platform !== 'win32') {
    // mac 下无法识别.XXX.json为后缀的文件
    extensions.push('json');
  } else {
    extensions.push(`${projectSuffix}.json`);
  }
  return new Promise((res, rej) => {
    saveFile(
      tempData,
      [{ name: projectSuffix, extensions: extensions }],
      (path) => {
        if (!path.endsWith('.json')) {
          return `${path}.${projectSuffix}.json`;
        } else if (!path.endsWith(`.${projectSuffix}.json`)) {
          return path.replace(/\.json$/g, `.${projectSuffix}.json`);
        }
        return path;
      },
      {},
      refactor
    )
      .then(({ filePath }) => res(filePath))
      .catch((err) => rej(err));
  });
};

export const readJsonPromise = () => {
  return new Promise((res, rej) => {
    readNormalFile(tempFilePath)
      .then((data) => {
        res();
      })
      .catch((err) => {
        rej(err);
      });
  });
};
