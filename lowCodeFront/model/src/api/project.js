import axios from 'axios';
import request from '../utils/request';

/**
 * 保存更新项目信息
 */
export function saveItem(data) {
  return request({
    url: '/projectInfo/saveProject',
    method: 'post',
    data: { ...data },
  });
}

/**
 * 删除项目
 */
export function deleteItem(data) {
  return request({
    url: '/projectInfo/delete',
    method: 'post',
    data: { ...data },
  });
}

/**
 * 修改项目（根据主键ID更新不为空字段值的方法，主键的值不能为空）
 */
export function updateItem(data) {
  return request({
    url: '/projectInfo/updateVONotEmptyColumn',
    method: 'post',
    data: { ...data },
  });
}

/**
 * 分页查询项目列表
 */
export function queryItemList(data) {
  return request({
    url: '/projectInfo/queryProjectListByPage',
    method: 'post',
    data: { ...data },
  });
}


/**
 * 查询项目信息
 */
export function loadItemById(id) {
  return request({
    url: `/projectInfo/loadById/${id}`,
    method: 'get',
  });
}

/**
 * 加载驱动测试
 */
export function pingLoadDriver(data) {
  return request({
    url: '/model/pingLoadDriver',
    method: 'post',
    data: { ...data },
  });
}

/**
 * 获取数据表清单
 */
export function reverseGetAllDBTablesList(data) {
  return request({
    url: '/model/reverseGetAllDBTablesList',
    method: 'post',
    data: { ...data },
  });
}

/**
 * 获取指定数据表DDL
 */
export function reverseGetDBTableDDL(data) {
  return request({
    url: '/model/reverseGetDBTableDDL',
    method: 'post',
    data: { ...data },
  });
}

/**
 * 将DDL语句解析为表结构
 */
export function parseDDLToTableImpl(data) {
  return request({
    url: '/model/parseDDLToTableImpl',
    method: 'post',
    data: { ...data },
  });
}

/**
 * 解析PDM文件
 */
export function parsePDMFile(data) {
  return request({
    url: '/model/parsePDMFile',
    method: 'post',
    data: data,
  });
}

/**
 * 生成WORD文档
 */
export function genDocx(params) {
  return axios({
    url: `${envConfig.REACT_APP_BASE_API}/model/genDocx`,
    method: 'post',
    data: { ...params },
    responseType: 'blob', // 表明返回服务器返回的数据类型
  });
}
