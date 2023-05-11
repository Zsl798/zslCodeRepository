package com.lowcode.modeltool.apimodel.projectInfo.controller;


import com.lowcode.modeltool.apimodel.projectInfo.facade.ProjectInfoFacade;
import com.lowcode.modeltool.apimodel.projectInfo.model.ProjectInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 建模项目信息表Controller实现类
 * @author zhangsonglin
 * @since 1.0
 * @version 2023年4月24日
 * @version 2023年4月24日
 */
@Api(tags = "建模信息表")
@RestController
@RequestMapping("/projectInfo")
public class ProjectInfoController{
    /** 注入建模项目信息表Facade */
    @Autowired
    private ProjectInfoFacade projectInfoFacade;
    /**
     * 根据建模ID查询建模对象
     *
     * @param id
     * @return ProjectInfoVO
     */
    @ApiOperation("根据建模ID查询建模对象")
    @GetMapping("/loadById/{id}")
    public ProjectInfoVO loadById(@PathVariable("id") String id) {
        return projectInfoFacade.queryProjectInfoById(id);
    }

    @Value("${server.port}")
    String port;

    @GetMapping("/hello/{name}")
    public String home(@PathVariable("name")String name) {
        return "hi " + name + " ,this is EurekaClient , i am from port:" + port;
    }

    /**
     * 带分页查询项目列表
     *
     * @param projectInfo 项目信息
     * @return 项目查询结果
     */
    @ApiOperation("带分页查询项目列表")
    @PostMapping("/queryProjectListByPage")
    public Map<String,Object> queryProjectListByPage(@RequestBody ProjectInfoVO projectInfo) {
        return projectInfoFacade.queryProjectListByPage(projectInfo);
    }
}
