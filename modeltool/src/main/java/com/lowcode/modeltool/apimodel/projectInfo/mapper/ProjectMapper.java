package com.lowcode.modeltool.apimodel.projectInfo.mapper;

import com.lowcode.modeltool.apimodel.projectInfo.model.ProjectInfoVO;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectMapper {
    /**
     * 根据建模ID查询建模对象
     * @param id
     * @return
     */
    ProjectInfoVO queryProjectInfoById(String id);
}
