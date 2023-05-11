package com.lowcode.modeltool.apimodel.projectInfo.mapper;

import com.lowcode.modeltool.apimodel.projectInfo.model.ProjectInfoVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectMapper {
    /**
     * 根据建模ID查询建模对象
     * @param id
     * @return
     */
    ProjectInfoVO queryProjectInfoById(String id);

    /**
     * 查询项目条数
     *
     * @param condition
     * @return
     */
    int queryProjectListCount(ProjectInfoVO condition);

    /**
     * 查询项目列表
     *
     * @param condition
     * @return
     */
    List<ProjectInfoVO> queryProjectList(ProjectInfoVO condition);
}
