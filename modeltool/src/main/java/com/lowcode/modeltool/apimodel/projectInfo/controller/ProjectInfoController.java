package com.lowcode.modeltool.apimodel.projectInfo.controller;


import com.lowcode.modeltool.apimodel.projectInfo.facade.ProjectInfoFacade;
import com.lowcode.modeltool.apimodel.projectInfo.model.ProjectInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping("/queryProjectInfoById/{id}")
    public ProjectInfoVO queryProjectInfoById(@PathVariable("id") String id) {
        return projectInfoFacade.queryProjectInfoById(id);
    }

    @Value("${server.port}")
    String port;

    @GetMapping("/hello/{name}")
    public String home(@PathVariable("name")String name) {
        return "hi " + name + " ,this is EurekaClient , i am from port:" + port;
    }
}
