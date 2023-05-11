import { getUserConfig } from '../../lib/middle';
import { saveUserConfig } from '../../lib/json';
import { openLoading, closeLoading, STATUS } from '../common';
import allLangData from '../../lang';
import { setMemoryCache } from '../../lib/cache';
import { CONFIG } from '../../lib/variable';

export const SAVE_USER_CONFIG_SUCCESS = 'SAVE_USER_CONFIG_SUCCESS'; // 保存成功
export const SAVE_USER_CONFIG_FAIL = 'SAVE_USER_CONFIG_FAIL'; // 保存失败
export const GET_USER_CONFIG_SUCCESS = 'GET_USER_CONFIG_SUCCESS'; // 保存成功
export const GET_USER_CONFIG_FAIL = 'GET_USER_CONFIG_FAIL'; // 保存失败

const saveUserConfigSuccess = (data) => {
  return {
    type: SAVE_USER_CONFIG_SUCCESS,
    data,
  };
};

const saveUserConfigFail = (err) => {
  return {
    type: SAVE_USER_CONFIG_FAIL,
    data: err,
  };
};

const getUserConfigSuccess = (data) => {
  return {
    type: GET_USER_CONFIG_SUCCESS,
    data,
  };
};

const getUserConfigFail = (err) => {
  return {
    type: GET_USER_CONFIG_FAIL,
    data: err,
  };
};

export const getUserConfigData = (list) => {
  return (dispatch) => {
    getUserConfig(list)
      .then((data) => {
        dispatch(getUserConfigSuccess(data));
      })
      .catch((err) => {
        dispatch(getUserConfigFail(err));
      });
  };
};

export const saveUserConfigSome = (data) => {
  return (dispatch, getState) => {
    const configData = getState()?.config?.data || [];
    const config = { ...configData[0], ...data };
    saveUserConfigData(
      configData.map((d, index) => {
        if (index === 0) {
          return config;
        }
        return d;
      }),
      allLangData[config.lang].updateConfig
    )(dispatch);
  };
};

export const saveUserConfigData = (data, title, cb) => {
  return (dispatch) => {
    dispatch(openLoading(title));
    saveUserConfig(data)
      .then(() => {
        setMemoryCache(CONFIG, {
          ...data,
          lang: data[0]?.lang,
        });
        cb && cb();
        dispatch(saveUserConfigSuccess(data));
        dispatch(closeLoading(STATUS[1], null));
      })
      .catch((err) => {
        cb && cb(err);
        dispatch(saveUserConfigFail(err));
        dispatch(closeLoading(STATUS[2], err));
      });
  };
};

export const removeHistory = (h) => {
  return (dispatch, getState) => {
    const configData = getState()?.config?.data || [];
    const config = configData[0];
    saveUserConfigData(
      configData.map((d, index) => {
        if (index === 0) {
          return {
            ...d,
            projectHistories: (d.projectHistories || []).filter(
              (p) => p.projectId !== h.projectId
            ),
          };
        }
        return d;
      }),
      allLangData[config.lang].updateConfig
    )(dispatch);
  };
};

// export const addHistory = (data, cb) => {
//   return (dispatch, getState) => {
//     const configData = getState()?.config?.data || [];
//     const config = configData[0];
//     debugger;
//     const d = configData.map((d, index) => {
//       if (index === 0) {
//         // const tempProjectHistories = [...(d.projectHistories || [])].filter(
//         //   (h) => h.path !== data.path
//         // );
//         const tempProjectHistories = [...(d.projectHistories || [])];
//         tempProjectHistories.unshift(data);
//         const res = {
//           ...d,
//           projectHistories: tempProjectHistories,
//         };
//         return res;
//       }
//       return d;
//     });
//     saveUserConfigData(d, allLangData[config.lang].updateConfig, cb)(dispatch);
//   };
// };

//把querylist返回的结果保存在state的config.data[0].projectHistories里
export const addHistory = (newListInfo, cb) => {
  return (dispatch, getState) => {
    const configData = getState()?.config?.data || [];
    const config = configData[0];
    const d = configData.map((d, index) => {
      if (index === 0) {
        // const tempProjectHistories = [...(d.projectHistories || [])].filter(
        //   (h) => h.path !== data.path
        // );
        // const tempProjectHistories = [...(d.projectHistories || [])];
        // tempProjectHistories.unshift(data);
        return {
          ...d,
          projectHistories: newListInfo.list,
          projectCount: newListInfo.count,
          projectName: newListInfo.projectName,
          currentPage: newListInfo.pageNo,
        };
      }
      return d;
    });
    saveUserConfigData(d, allLangData[config.lang].updateConfig, cb)(dispatch);
  };
};

export const updateHistory = (newListInfo) => {
  return (dispatch, getState) => {
    const configData = getState()?.config?.data || [];
    const config = configData[0];
    saveUserConfigData(
      configData.map((d, index) => {
        if (index === 0) {
          return {
            ...d,
            projectHistories: newListInfo.list,
            projectCount: newListInfo.count,
            projectName: newListInfo.projectName,
            currentPage: newListInfo.pageNo,
          };
        }
        return d;
      }),
      allLangData[config.lang].updateConfig
    )(dispatch);
  };
};

// export const updateHistory = (oldData, newData) => {
//   return (dispatch, getState) => {
//     const configData = getState()?.config?.data || [];
//     const config = configData[0];
//     saveUserConfigData(
//       configData.map((d, index) => {
//         if (index === 0) {
//           return {
//             ...d,
//             projectHistories: (d.projectHistories || []).map((h) => {
//               if (h.path === oldData.path) {
//                 return {
//                   describe: newData.describe || '',
//                   name: newData.name || '',
//                   avatar: newData.avatar || '',
//                   path: newData.path,
//                 };
//               }
//               return h;
//             }),
//           };
//         }
//         return d;
//       }),
//       allLangData[config.lang].updateConfig
//     )(dispatch);
//   };
// };
