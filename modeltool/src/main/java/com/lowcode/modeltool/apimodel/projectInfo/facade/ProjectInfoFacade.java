/***********************************************************************************************
 * Copyright (C) 2023 China Southern Power Grid Digital Platform Technology Company Co.,Ltd
 * All Rights Reserved.
 * 本软件为南方电网数字平台科技（广东）有限公司研制。
 * 未经本公司正式书面同意，其他任何个人、团体不得使用、复制、修改或发布本软件。
 ***********************************************************************************************/

package com.lowcode.modeltool.apimodel.projectInfo.facade;


import com.lowcode.modeltool.apimodel.projectInfo.mapper.ProjectMapper;
import com.lowcode.modeltool.apimodel.projectInfo.model.ProjectInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 建模项目信息表service
 * @author zhangsonglin
 * @since 1.0
 * @version 2023年4月24日
 * @version 2023年4月24日
 */
@Service(value = "projectInfoFacade")
public class ProjectInfoFacade{
    @Autowired
    ProjectMapper projectMapper;
    /**
     * 根据建模ID查询建模对象
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ProjectInfoVO queryProjectInfoById(String id) {
        return projectMapper.queryProjectInfoById(id);
    }


}
