package com.lowcode.modeltool.tool.command.impl.excel;


import com.lowcode.modeltool.tool.fisok.raw.kit.StringKit;
import com.lowcode.modeltool.tool.model.GroupTopic;
import com.lowcode.modeltool.tool.model.TableEntity;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2023/02/23
 * @desc : 主题分组模式下，从EXCEL导入数据表结构
 */
public class ParseExcelAsTopic {
    public void parseToTableEntities(Workbook workBook, List<TableEntity> tableEntities,List<GroupTopic> groupTopicList){
        Sheet sheet = workBook.getSheetAt(0);
        //读取分组，并将分组的行范围计算出来
        Map<String,Integer[]> groupTopicRecords = new TreeMap<>();
        List<CellRangeAddress> mergedCells = sheet.getMergedRegions();
        for(int i=0;i<mergedCells.size();i++){
            CellRangeAddress cellRange = mergedCells.get(i);
            int firstRow = cellRange.getFirstRow();
            int lastRow = cellRange.getLastRow();
            if(firstRow == lastRow){
                continue;
            }
            Cell firstCell = sheet.getRow(firstRow).getCell(0);
            String textValue = ExcelCommonUtils.getCellValue(firstCell).strValue("");
            if(StringKit.isBlank(textValue)){
                continue;
            }
            groupTopicRecords.put(textValue,new Integer[]{firstRow,lastRow});
        }
        //读取每个分组下的表清单以及表内容
        groupTopicRecords.forEach((topicText, range)->{
            int firstRow = range[0];
            int lastRow = range[1];

            //计算并构建分组对象
            GroupTopic topic = new GroupTopic();
            String[] topicKv = topicText.replaceAll("\\s+","").split("-");
            if(topicKv.length==2){
                topic.setDefKey(topicKv[0]);
                topic.setDefName(topicKv[1]);
            }else{
                topic.setDefKey("TPC"+groupTopicList.size());
                topic.setDefName(topicText);
            }


            for(int i=firstRow;i<=lastRow;i++){
                Row row = sheet.getRow(i);
                String tableDefKey = ExcelCommonUtils.getCellValue(row.getCell(1)).strValue("");
                String tableDefName = ExcelCommonUtils.getCellValue(row.getCell(2)).strValue("");
                String tableDefComment = ExcelCommonUtils.getCellValue(row.getCell(3)).strValue("");
                if(StringKit.isBlank(tableDefKey) && StringKit.isBlank(tableDefName)){
                    continue;
                }
                TableEntity entity = new TableEntity();
                entity.setId(StringKit.uuid("-").toUpperCase());
                entity.setDefKey(tableDefKey);
                entity.setDefName(tableDefName);
                entity.setComment(tableDefComment);
                entity.setRowNo(tableEntities.size()+1);
                tableEntities.add(entity);

                ExcelCommonUtils.parseTableEntity(2,2,workBook,tableDefKey,entity);
                entity.fillFieldsCalcValue();

                topic.getRefEntities().add(entity.getId());
            }

            groupTopicList.add(topic);
        });

    }


}
