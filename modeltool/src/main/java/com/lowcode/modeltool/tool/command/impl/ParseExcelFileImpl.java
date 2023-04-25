/*
 * Copyright 2019-2029 FISOK(www.fisok.cn).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lowcode.modeltool.tool.command.impl;

import com.lowcode.modeltool.tool.command.Command;
import com.lowcode.modeltool.tool.command.ExecResult;
import com.lowcode.modeltool.tool.command.impl.excel.ExcelCommonUtils;
import com.lowcode.modeltool.tool.command.impl.excel.ParseExcelAsSimple;
import com.lowcode.modeltool.tool.command.impl.excel.ParseExcelAsTopic;
import com.lowcode.modeltool.tool.fisok.raw.kit.JSONKit;
import com.lowcode.modeltool.tool.fisok.raw.kit.StringKit;
import com.lowcode.modeltool.tool.model.GroupTopic;
import com.lowcode.modeltool.tool.model.TableEntity;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangsonglin
 * @since 1.0
 * @version 2023年4月24日
 * @version 2023年4月24日
 * @desc : 解析EXCEL文件
 */
@Component
public class ParseExcelFileImpl implements Command<ExecResult> {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public ExecResult exec(Map<String, String> params) {

        String excelFile = params.get("excelFile");
        File inFile = new File(excelFile);

        List<TableEntity> tableEntities = new ArrayList<>();
        List<GroupTopic> groupTopicList = new ArrayList<>();
        readExcelFile(inFile,tableEntities,groupTopicList);

        ExecResult ret = new ExecResult();
        try {
            ret.setBody(new HashMap<String,Object>(){{
                put("projectName","Excel导入");
                put("tables",tableEntities);
                put("groupTopics",groupTopicList);
            }});
            ret.setStatus(ExecResult.SUCCESS);
            System.out.println(JSONKit.toJsonString(ret,true));
        } catch (Exception e) {
            String message = e.getMessage();
            if(StringKit.isBlank(message)){
                message = e.toString();
            }
            ret.setBody(message);
            ret.setStatus(ExecResult.FAILED);
            logger.error("",e);
        }

        return ret;
    }

    public void readExcelFile(File file,List<TableEntity> tableEntities,List<GroupTopic> groupTopicList){
        Workbook workBook = ExcelCommonUtils.getWorkbook(file);
        Sheet sheet = workBook.getSheetAt(0);   //读取目录sheet
        int lastRowNum = sheet.getLastRowNum();
        if(lastRowNum<=2){
            return;
        }
        String firstHeaderCellText = ExcelCommonUtils.getCellValue(sheet.getRow(1).getCell(0)).strValue("");

        if(firstHeaderCellText.indexOf("主题")>=0){
            //主题模式
            ParseExcelAsTopic parseExcel = new ParseExcelAsTopic();
            parseExcel.parseToTableEntities(workBook,tableEntities,groupTopicList);
        }else{
            //简单模式
            ParseExcelAsSimple parseExcel = new ParseExcelAsSimple();
            parseExcel.parseToTableEntities(workBook,tableEntities);
        }
    }

    public ExecResult parseExcelFile(MultipartFile file) {
        //todo
        return new ExecResult();
    }






}
