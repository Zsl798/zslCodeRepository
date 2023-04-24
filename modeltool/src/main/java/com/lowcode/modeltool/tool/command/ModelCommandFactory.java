package com.lowcode.modeltool.tool.command;


import com.lowcode.modeltool.tool.command.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 *
 * 建模命令构造器工厂类
 * 元数据建模Factory
 * @author zhangsonglin
 * @since 1.0
 * @version 2023年4月24日
 * @version 2023年4月24日
 */
@Component("ModelCommandFactory")
public class ModelCommandFactory {

    @Autowired
    private DBReverseGetAllTablesListImpl dBReverseGetAllTablesListImpl;

    @Autowired
    private DBReverseGetTableDDLImpl dBReverseGetTableDDLImpl;

    @Autowired
    private GenDocxImpl genDocxImpl;

    @Autowired
    private ParseDDLToTableImpl parseDDLToTableImpl;

    @Autowired
    private ParsePDMFileImpl parsePDMFileImpl;

    @Autowired
    private PingLoadDriverClassImpl pingLoadDriverClassImpl;

    public Command getCommand(Class queryClazz) {
        if (queryClazz.isAssignableFrom(DBReverseGetAllTablesListImpl.class)) {
            return dBReverseGetAllTablesListImpl;
        }else if(queryClazz.isAssignableFrom(DBReverseGetTableDDLImpl.class)){
            return dBReverseGetTableDDLImpl;
        }else if(queryClazz.isAssignableFrom(GenDocxImpl.class)){
            return genDocxImpl;
        }else if(queryClazz.isAssignableFrom(ParseDDLToTableImpl.class)){
            return parseDDLToTableImpl;
        }else if(queryClazz.isAssignableFrom(ParsePDMFileImpl.class)){
            return parsePDMFileImpl;
        }else if(queryClazz.isAssignableFrom(PingLoadDriverClassImpl.class)){
            return pingLoadDriverClassImpl;
        }
        return null;
    }
}
