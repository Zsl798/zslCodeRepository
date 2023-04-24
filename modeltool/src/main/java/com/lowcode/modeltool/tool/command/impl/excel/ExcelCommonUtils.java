package com.lowcode.modeltool.tool.command.impl.excel;


import com.lowcode.modeltool.tool.fisok.raw.kit.IOKit;
import com.lowcode.modeltool.tool.fisok.raw.kit.StringKit;
import com.lowcode.modeltool.tool.fisok.raw.lang.ValueObject;
import com.lowcode.modeltool.tool.model.ColumnField;
import com.lowcode.modeltool.tool.model.TableEntity;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2023/02/23
 * @desc : 本领域的EXCEL工具处理类
 */
public class ExcelCommonUtils {
    public static Workbook getWorkbook(File file) {
        InputStream inputStream = null;
        InputStream newIs = null;
        Workbook workBook = null;
        try {
            inputStream = new FileInputStream(file);
            newIs = IOKit.convertToByteArrayInputStream(inputStream);
            try {
                newIs.reset();
                workBook = new XSSFWorkbook(OPCPackage.open(newIs));
            } catch (OfficeXmlFileException e) {
                newIs.reset();
                workBook = new HSSFWorkbook(newIs);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e1) {
            e1.printStackTrace();
        } finally {
            try {
                if (inputStream != null) inputStream.close();
                if (newIs != null) newIs.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return workBook;
    }

    public static ValueObject getCellValue(Cell cell) {
        if (cell == null) {
            return ValueObject.valueOf("");
        }
        CellType cellType = cell.getCellType();
        switch (cellType) {
            case STRING:
                String strValue = cell.getStringCellValue();
                if ("[]".equals(strValue)) {
                    strValue = "";
                }
                return ValueObject.valueOf(strValue);
            case NUMERIC:
                return ValueObject.valueOf(cell.getNumericCellValue());
            default:
                return ValueObject.valueOf("");
        }
    }

    /**
     * 解析表结构
     * @param startRow
     * @param workBook
     * @param table
     * @param entity
     */
    public static void parseTableEntity(int startRow,int startCol,Workbook workBook, String table, TableEntity entity){
        Sheet sheet = workBook.getSheet(table);
        if(sheet == null){
            return;
        }
        for(int i=startRow;i<=sheet.getLastRowNum();i++){
            Row row = sheet.getRow(i);
            if(row == null){
                continue;
            }
            Cell fieldDefKeyCell = row.getCell(startCol+1);
            Cell fieldDefNameCell = row.getCell(startCol+2);
            Cell fieldDataTypeCell = row.getCell(startCol+3);
            Cell fieldLenCell = row.getCell(startCol+4);
            Cell fieldScaleCell = row.getCell(startCol+5);
            Cell fieldPkCell = row.getCell(startCol+6);
            Cell fieldNotNullCell = row.getCell(startCol+7);
            Cell fieldDefaultValueCell = row.getCell(startCol+8);
            Cell fieldCommentCell = row.getCell(startCol+9);

            Integer len = ExcelCommonUtils.getCellValue(fieldLenCell).intValue(null);
            Integer scale = ExcelCommonUtils.getCellValue(fieldScaleCell).intValue(null);
            String strPrimaryKey = ExcelCommonUtils.getCellValue(fieldPkCell).strValue("");
            String strNotNull = ExcelCommonUtils.getCellValue(fieldNotNullCell).strValue("");

            Boolean primaryKey = "√".equals(strPrimaryKey)||"Y".equals(strPrimaryKey)||"1".equals(strPrimaryKey)||"是".equals(strPrimaryKey);
            Boolean notNull = "√".equals(strNotNull)||"Y".equals(strNotNull)||"1".equals(strNotNull)||"是".equals(strNotNull);

            ColumnField field = new ColumnField();
            field.setDefKey(ExcelCommonUtils.getCellValue(fieldDefKeyCell).strValue(""));
            field.setDefName(ExcelCommonUtils.getCellValue(fieldDefNameCell).strValue(""));
            field.setComment(ExcelCommonUtils.getCellValue(fieldCommentCell).strValue(""));
            field.setDomain("");
            field.setType(ExcelCommonUtils.getCellValue(fieldDataTypeCell).strValue(""));
            field.setLen(len);
            field.setScale(scale);
            field.setPrimaryKey(primaryKey);
            field.setNotNull(notNull);
            field.setDefaultValue(ExcelCommonUtils.getCellValue(fieldDefaultValueCell).strValue(""));
            field.setHideInGraph((entity.getFields().size()+1)>15);
            if(StringKit.isBlank(field.getDefKey())){
                continue;
            }
            entity.getFields().add(field);
        }

    }
}
