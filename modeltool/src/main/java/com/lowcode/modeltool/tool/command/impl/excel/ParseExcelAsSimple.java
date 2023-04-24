package com.lowcode.modeltool.tool.command.impl.excel;


import com.lowcode.modeltool.tool.fisok.raw.kit.StringKit;
import com.lowcode.modeltool.tool.model.TableEntity;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2023/02/23
 * @desc : 传统的简单模式从EXCEL导入表结构
 * 从原来的逻辑里剥离出来
 */
public class ParseExcelAsSimple {
    public void parseToTableEntities(Workbook workBook, List<TableEntity> tableEntities){
        Sheet sheet = workBook.getSheetAt(0);   //读取目录sheet
        for(int i=2;i<sheet.getLastRowNum();i++){
            Row row = sheet.getRow(i);
            if(row == null){
                continue;
            }
            Cell defKeyCell = row.getCell(0);
            Cell defNameCell = row.getCell(1);
            Cell commentCell = row.getCell(2);
            if(defKeyCell == null){
                continue;
            }
            String defKey = ExcelCommonUtils.getCellValue(defKeyCell).strValue("");
            String defName = ExcelCommonUtils.getCellValue(defNameCell).strValue("");
            String comment = ExcelCommonUtils.getCellValue(commentCell).strValue("");
            if(StringKit.isBlank(defKey)){
                continue;
            }

            TableEntity entity = new TableEntity();
            entity.setDefKey(defKey);
            entity.setDefName(defName);
            entity.setComment(comment);
            entity.setRowNo(tableEntities.size()+1);
            tableEntities.add(entity);

            ExcelCommonUtils.parseTableEntity(4,0,workBook,defKey,entity);
            entity.fillFieldsCalcValue();
        }
    }

}
