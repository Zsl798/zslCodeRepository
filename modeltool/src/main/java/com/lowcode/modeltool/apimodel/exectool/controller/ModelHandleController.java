package com.lowcode.modeltool.apimodel.exectool.controller;


import com.lowcode.modeltool.apimodel.exectool.common.ReturnResultHelper;
import com.lowcode.modeltool.apimodel.exectool.facade.ModelHandleFacade;
import com.lowcode.modeltool.tool.command.ExecResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

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
}
