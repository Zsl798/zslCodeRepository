package com.lowcode.modeltool.apimodel.exectool.controller;


import com.lowcode.modeltool.apimodel.exectool.common.ReturnResultHelper;
import com.lowcode.modeltool.apimodel.exectool.facade.ModelHandleFacade;
import com.lowcode.modeltool.apimodel.exectool.util.FileUtils;
import com.lowcode.modeltool.apimodel.projectInfo.facade.ProjectInfoFacade;
import com.lowcode.modeltool.apimodel.projectInfo.model.ProjectInfoVO;
import com.lowcode.modeltool.tool.command.ExecResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 元数据建模Controller
 * @author zhangsonglin
 * @since 1.0
 * @version 2023年4月24日
 * @version 2023年4月24日
 */
@Api(tags = "元数据建模应用")
@RestController
@RequestMapping("/model")
public class ModelHandleController {

    @Autowired
    private ModelHandleFacade modelHandleFacade;

    /** 注入建模项目信息表Facade */
    @Autowired
    private ProjectInfoFacade projectInfoFacade;

    /**
     * 加载驱动测试
     * @param params
     * @return resultHelper
     */
    @ApiOperation("加载驱动测试")
    @PostMapping("/pingLoadDriver")
    public ReturnResultHelper pingLoadDriver(@RequestBody Map<String, String> params) {
        ExecResult execResult = modelHandleFacade.pingLoadDriver(params);
        ReturnResultHelper resultHelper = new ReturnResultHelper(true,execResult);
        return resultHelper;
    }

    /**
     * 获取数据表清单
     * 逆向解析，获取数据表清单
     * @param params
     * @return resultHelper
     */
    @ApiOperation("获取数据表清单")
    @PostMapping("/reverseGetAllDBTablesList")
    public ReturnResultHelper reverseGetAllDBTablesList(@RequestBody Map<String, String> params) {
        ExecResult execResult = modelHandleFacade.reverseGetAllDBTablesList(params);
        ReturnResultHelper resultHelper = new ReturnResultHelper(true,execResult);
        return resultHelper;
    }

    /**
     * 获取指定数据表DDL
     * 逆向解析，获取指定数据表DDL
     * @param params
     * @return resultHelper
     */
    @ApiOperation("获取指定数据表DDL")
    @PostMapping("/reverseGetDBTableDDL")
    public ReturnResultHelper reverseGetDBTableDDL(@RequestBody Map<String, String> params) {
        ExecResult execResult = modelHandleFacade.reverseGetDBTableDDL(params);
        ReturnResultHelper resultHelper = new ReturnResultHelper(true,execResult);
        return resultHelper;
    }

    /**
     * 解析PDM文件
     * @param params
     * @return resultHelper
     */
    @ApiOperation("解析PDM文件")
    @PostMapping("/parsePDMFile")
    public ReturnResultHelper parsePDMFile(@RequestBody Map<String, String> params) {
        ExecResult execResult = modelHandleFacade.parsePDMFile(params);
        ReturnResultHelper resultHelper = new ReturnResultHelper(true,execResult);
        return resultHelper;
    }

    /**
     * 解析PDM文件
     * @param pdmFile
     * @return resultHelper
     */
    @ApiOperation("解析PDM文件")
    @PostMapping("/parsePDMMultipartFile")
    public ReturnResultHelper parsePDMMultipartFile(@RequestParam("pdmFile") MultipartFile pdmFile) {
        ExecResult execResult = modelHandleFacade.parsePDMMultipartFile(pdmFile);
        ReturnResultHelper resultHelper = new ReturnResultHelper(true,execResult);
        return resultHelper;
    }

    /**
     * 解析excel文件
     * @param params
     * @return resultHelper
     */
    @ApiOperation("解析excel文件")
    @PostMapping("/parseExcelFile")
    public ReturnResultHelper parseExcelFile(@RequestBody Map<String, String> params) {
        ExecResult execResult = modelHandleFacade.parseExcelFile(params);
        ReturnResultHelper resultHelper = new ReturnResultHelper(true,execResult);
        return resultHelper;
    }

    /**
     * 解析excel文件
     * @param excelFile
     * @return resultHelper
     */
    @ApiOperation("解析excel文件")
    @PostMapping("/parseExcelMultipartFile")
    public ReturnResultHelper parseExcelMultipartFile(@RequestParam("excelFile") MultipartFile excelFile) {
        ExecResult execResult = modelHandleFacade.parseExcelMultipartFile(excelFile);
        ReturnResultHelper resultHelper = new ReturnResultHelper(true,execResult);
        return resultHelper;
    }

    /**
     * 解析http请求
     * @param params
     * @return resultHelper
     */
    @ApiOperation("解析http请求")
    @PostMapping("/httpParser")
    public ReturnResultHelper httpParser(@RequestBody Map<String, String> params) {
        ExecResult execResult = modelHandleFacade.httpParser(params);
        ReturnResultHelper resultHelper = new ReturnResultHelper(true,execResult);
        return resultHelper;
    }

    /**
     * 生成WORD文档
     * @param params
     * @return resultHelper
     */
    @ApiOperation("生成WORD文档")
    @PostMapping("/genDocx")
    public ReturnResultHelper genDocx(@RequestBody Map<String, String> params) {
        ExecResult execResult = modelHandleFacade.genDocx(params);
        ReturnResultHelper resultHelper = new ReturnResultHelper(true,execResult);
        return resultHelper;
    }

    /**
     * 通过项目id生成WORD文档
     *
     * @param params
     * @param response
     * @param request
     * @return boolean
     */
    @ApiOperation("通过项目id生成WORD文档")
    @PostMapping("/genDocxByProjectId")
    public boolean genDocxByProjectId(@RequestBody Map<String, Object> params, HttpServletResponse response, HttpServletRequest request) {
        //导出标志位
        boolean createFlag = true;
        //获取需要导出的数据
        ProjectInfoVO projectInfoVO = projectInfoFacade.queryProjectInfoById(String.valueOf(params.get("projectId")));
        //获取根目录
        String rootPath = FileUtils.getOutputRootPath();
        //获取文件名
        String packageName = FileUtils.getPackageName("modelDoc"); // modelDoc_yyyyMMdd_HHmmssSSS
        String path = rootPath+packageName;
        // 生成图片
        List<Map<String, String>> imgData = (List<Map<String, String>>)params.get("imgData");
        modelHandleFacade.createImgFile(imgData,path);
        // 生成文档
        String fullPath = modelHandleFacade.createDocFile(projectInfoVO,path);
        try {
            FileUtils.exportDocFile(response,fullPath,projectInfoVO.getProjectName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        FileUtils.cleanTempFiles(rootPath+packageName);
        return createFlag;
    }

    /**
     * 将DDL语句解析为表结构
     * @param params
     * @return resultHelper
     */
    @ApiOperation("将DDL语句解析为表结构")
    @PostMapping("/parseDDLToTable")
    public ReturnResultHelper parseDDLToTable(@RequestBody Map<String, String> params) {
        ExecResult execResult = modelHandleFacade.parseDDLToTable(params);
        ReturnResultHelper resultHelper = new ReturnResultHelper(true,execResult);
        return resultHelper;
    }

    /**
     * 传入驱动测试数据库连接
     * MODE=MySQL – 兼容模式，H2 兼容多种数据库，该值可以为：DB2、Derby、HSQLDB、MSSQLServer、MySQL、Oracle、PostgreSQL
     * "jdbc:h2:mem:MockChiner;MODE=MySQL;DB_CLOSE_DELAY=-1"
     * @param params
     * @return resultHelper
     */
    @ApiOperation("传入驱动测试数据库连接")
    @PostMapping("/connPingTest")
    public ReturnResultHelper connPingTest(@RequestBody Map<String, String> params) {
        if(!params.containsKey("driverTest")){
            return new ReturnResultHelper(false);
        }
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(params.get("driverTest"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return new ReturnResultHelper(false);
        }finally {
            if(Objects.nonNull(conn)){
                try {
                    conn.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                    return new ReturnResultHelper(false);
                }
            }
        }
        return new ReturnResultHelper(true,conn);
    }




}
