/* eslint-disable comma-dangle */
/* eslint-disable arrow-parens */
import React, { useState, useEffect } from 'react';
import {
  Loading,
  Modal,
  Input,
  UploadInput,
  PathSelectInput,
  Select,
  FormatMessage,
  Icon,
  textarea,
  openModal,
  Button,
} from 'components';

import { images } from './images';
import _ from 'lodash/object';

import './style/index.less';
// eslint-disable-next-line import/named
// import { readJsonPromise, platform, dirname } from '../../../lib/middle';
import { getPrefix } from '../../../lib/prefixUtil';
//import { queryProjectForModel } from '../../../api/dataModel';

const platform = 'json';
const Option = Select.Option;
export default React.memo(
  ({ prefix, data = {}, reading, onChange, dataFinish}) => {
    const { id } = data;
    const [dataSource, updateDataSource] = useState({});
    const [loading, updateLoading] = useState(!!id);
    const dataTypeSupports = [];
    const params = {
      developerId: 'SuperAdmin',
    };
    /*queryProjectForModel(params).then((listInfo) => {
      console.log(listInfo);
      listInfo.forEach(item => {
        dataTypeSupports.push({
          key: item.projectId,
          value: item.projectName
        })
      });
    });*/
    useEffect(() => {
      
      if (id) {
        //若是编辑已有的文件
        // setTimeout(() => {
        //   readJsonPromise(path).then((result) => {
        //     const newData = {
        //       ...result,
        //       path: dirname(path),
        //     };
        //     updateDataSource(newData);
        //     dataFinish && dataFinish();
        //     updateLoading(false);
        //   }).catch(() => {
        //     Modal.error({
        //       title: FormatMessage.string({id: 'optFail'}),
        //       message: FormatMessage.string({id: 'project.getProjectDataError'})});
        //     updateLoading(false);
        //     dataFinish && dataFinish();
        //   });
        // }, 100);
        setTimeout(() => {
          new Promise((res, rej) => {
            res();
          }).then(() => {
            const newData = { ...data };
            updateDataSource(newData);
            dataFinish && dataFinish(newData);
            updateLoading(false);
          });
        }, 100);
      }
    }, []);
    const uploadBefore = (file) => {
      // 图标大小56*56 不超过10KB
      return new Promise((res, rej) => {
        if (file.size / 1024 > 10) {
          Modal.error({
            title: FormatMessage.string({ id: 'optFail' }),
            message: FormatMessage.string({
              id: 'project.avatarValidate.validate',
            }),
          });
          rej();
        }
        // 创建临时的img标签
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = (event) => {
          const baseData = event.target.result;
          const image = new Image();
          image.src = baseData;
          image.onload = () => {
            if (image.width !== 56 || image.height !== 56) {
              Modal.error({
                title: FormatMessage.string({ id: 'optFail' }),
                message: FormatMessage.string({
                  id: 'project.avatarValidate.validate',
                }),
              });
              rej();
            } else {
              res();
            }
          };
        };
      });
    };
    const _onChange = (value, name) => {
      const newDataSource = {
        ...dataSource,
        [name]: value,
      };
      updateDataSource(newDataSource);
      onChange && onChange(newDataSource);
    };
    const currentPrefix = getPrefix(prefix);
    const pickAvatar = () => {
      let pick;
      let modal;
      const onOk = () => {
        _onChange(pick, 'avatar');
        modal.close();
      };
      const onCancel = () => {
        modal.close();
      };
      const List = () => {
        const [selected, setSelected] = useState(-1);
        const _setSelected = (i, index) => {
          pick = i;
          setSelected(index);
        };
        return (
          <div className={`${currentPrefix}-pro-edit-img-list`}>
            {images.map((i, index) => {
              return (
                <div
                  onClick={() => _setSelected(i, index)}
                  key={index}
                  className={`${currentPrefix}-pro-edit-img-list-item-${
                    selected === index ? 'selected' : 'normal'
                  }`}
                >
                  <img
                    src={i}
                    alt={FormatMessage.string({ id: 'project.noAvatar' })}
                  />
                </div>
              );
            })}
          </div>
        );
      };
      modal = openModal(<List />, {
        title: FormatMessage.string({ id: 'project.pickAvatar' }),
        buttons: [
          <Button key="ok" onClick={onOk} type="primary">
            <FormatMessage id="button.ok" />
          </Button>,
          <Button key="cancel" onClick={onCancel}>
            <FormatMessage id="button.cancel" />
          </Button>,
        ],
      });
    };
    const timeTransform = (time) => {
      const newDate = new Date();
      newDate.setTime(time);
      return newDate.toLocaleString();
    };
    return (
      <Loading
        visible={loading}
        title={FormatMessage.string({ id: 'project.getProjectData' })}
      >
        <div className={`${currentPrefix}-form ${currentPrefix}-pro-edit`}>
          <div className={`${currentPrefix}-form-item`}>
             
            <span
              className={`${currentPrefix}-form-item-label`}
              title={FormatMessage.string({ id: 'dataModel.modelName' })}
            >
               <span
                  className={`${currentPrefix}-form-item-label-require`}
                  style={{ display: reading ? 'none' : 'inline' }}
                >{}
              </span>
              <FormatMessage id="dataModel.modelName" />
            </span>
            <span className={`${currentPrefix}-form-item-component`}>
              {reading ? (
                dataSource.modelName
              ) : (
                <Input maxLength='128'
                  onChange={(e) => _onChange(e.target.value, 'modelName')}
                  value={dataSource.modelName}
                />
              )}
            </span>
          </div>
          {/* {
        platform === 'json' ? <div className={`${currentPrefix}-form-item`}>
          <span
            className={`${currentPrefix}-form-item-label`}
            title={FormatMessage.string({id: 'project.path'})}
          >
            <span className={`${currentPrefix}-form-item-label-require`} style={{display: reading ? 'none' : 'inline'}}>{}</span>
            <span>
              <FormatMessage id='project.path'/>
            </span>
          </span>
          <span className={`${currentPrefix}-form-item-component`}>
            {
              reading ? dataSource.path : <PathSelectInput onChange={value => _onChange(value, 'path')} value={dataSource.path}/>
            }
          </span>
        </div> : null
      } */}
          <div className={`${currentPrefix}-form-item`}>
            <span
              className={`${currentPrefix}-form-item-label`}
              title={FormatMessage.string({ id: 'dataModel.modelCode' })}
            >
               <span
                  className={`${currentPrefix}-form-item-label-require`}
                  style={{ display: reading ? 'none' : 'inline' }}
                >{}
              </span>
              <FormatMessage id="dataModel.modelCode" />
            </span>
            <span className={`${currentPrefix}-form-item-component`}>
              {reading ? (
                dataSource.modelCode
              ) : (
                <Input maxLength='128'
                   onChange={(e) => _onChange(e.target.value, 'modelCode')}
                  value={dataSource.modelCode}
                />
              )}
            </span>
          </div>
          <div className={`${currentPrefix}-form-item`}>
            <span
              className={`${currentPrefix}-form-item-label`}
              title={FormatMessage.string({ id: 'dataModel.projectName' })}
            >
               <span
                  className={`${currentPrefix}-form-item-label-require`}
                  style={{ display: reading ? 'none' : 'inline' }}
                >{}
              </span>
              <FormatMessage id="dataModel.projectName" />
            </span>
            <span className={`${currentPrefix}-form-item-component`}>
            <Select
                    notAllowEmpty
                    // defaultValue={'111'}
                    allowClear={false}
                    // onChange={e => dbChange(e, 'projectId')}
                  >
                    {dataTypeSupports
                      .map(type => (<Option
                        key={type.id}
                        value={type.value}
                      >
                        {/* {type.defKey} */}
                      </Option>))}
                  </Select>
            </span>
          </div>
          <div className={`${currentPrefix}-form-item`}>
            <span
              className={`${currentPrefix}-form-item-label`}
              title={FormatMessage.string({ id: 'dataModel.dutyName' })}
            >
            <FormatMessage id="dataModel.dutyName" />
            </span>
            <span className={`${currentPrefix}-form-item-component`}>
              {reading ? (
                dataSource.dutyName
              ) : (
                <Input maxLength='64'
                  onChange={(e) => _onChange(e.target.value, 'dutyName')}
                  value={dataSource.dutyName}
                />
              )}
            </span>
          </div>
          <div className={`${currentPrefix}-form-item`}>
            <span
              className={`${currentPrefix}-form-item-label`}
              title={FormatMessage.string({ id: 'dataModel.remark' })}
            >
            <FormatMessage id="dataModel.remark" />
            </span>
            <span className={`${currentPrefix}-form-item-component`}>
              {reading ? (
                dataSource.remark
              ) : (
                <textarea
                  rows='5' 
                  maxLength='256'
                  onChange={(e) => _onChange(e.target.value, 'remark')}
                  value={dataSource.remark}
                />
              )}
            </span>
          </div>
          {/* <div className={`${currentPrefix}-form-item`}>
            <span
              className={`${currentPrefix}-form-item-label`}
              title={FormatMessage.string({ id: 'project.avatar' })}
            >
              <FormatMessage id="project.avatar" />
            </span>
            <span
              className={`${currentPrefix}-form-item-component ${currentPrefix}-form-item-component-upload`}
            >
              {reading ? (
                dataSource.avatar && (
                  <img
                    src={dataSource.avatar}
                    alt={FormatMessage.string({ id: 'project.noAvatar' })}
                  />
                )
              ) : (
                <UploadInput
                  suffix={
                    <span
                      onClick={pickAvatar}
                      className={`${currentPrefix}-form-item-component-upload-in`}
                    >
                      <Icon
                        type="fa-bars"
                        title={FormatMessage.string({
                          id: 'project.pickAvatar',
                        })}
                      />
                    </span>
                  }
                  onChange={(value) => _onChange(value, 'avatar')}
                  value={dataSource.avatar}
                  accept="image/png"
                  base64
                  uploadBefore={uploadBefore}
                  placeholder={FormatMessage.string({
                    id: 'project.avatarValidate.placeholder',
                  })}
                />
              )}
            </span>
          </div> */}
          <div
            className={`${currentPrefix}-form-item`}
            style={{ display: reading ? '' : 'none' }}
          >
            <span
              className={`${currentPrefix}-form-item-label`}
              title={FormatMessage.string({ id: 'dataModel.createDate' })}
            >
              <FormatMessage id="dataModel.createDate" />
            </span>
            <span className={`${currentPrefix}-form-item-component`}>
              {dataSource.createTime?timeTransform(dataSource.createTime):''}
            </span>
          </div>
          <div
            className={`${currentPrefix}-form-item`}
            style={{ display: reading ? '' : 'none' }}
          >
            <span
              className={`${currentPrefix}-form-item-label`}
              title={FormatMessage.string({ id: 'dataModel.updateDate' })}
            >
              <FormatMessage id="dataModel.updateDate" />
            </span>
            <span className={`${currentPrefix}-form-item-component`}>
              {dataSource.modifyTime ?timeTransform(dataSource.modifyTime) : ''}
            </span>
          </div>
        </div>
      </Loading>
    );
  }
);
