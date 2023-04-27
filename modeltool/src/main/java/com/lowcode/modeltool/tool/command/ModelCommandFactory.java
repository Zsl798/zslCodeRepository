package com.lowcode.modeltool.tool.command;


import com.lowcode.modeltool.tool.command.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;


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
    private HttpParserImpl httpParserImpl;

    @Autowired
    private ParseDDLToTableImpl parseDDLToTableImpl;

    @Autowired
    private ParseExcelFileImpl parseExcelFileImpl;

    @Autowired
    private ParsePDMFileImpl parsePDMFileImpl;

    @Autowired
    private PingLoadDriverClassImpl pingLoadDriverClassImpl;

    private static final Map<Class,AbstractDBCommand> classMap  = new HashMap<>();

    @PostConstruct
    public void init() {
        classMap.put(DBReverseGetAllTablesListImpl.class,dBReverseGetAllTablesListImpl);
        classMap.put(DBReverseGetTableDDLImpl.class,dBReverseGetTableDDLImpl);
        classMap.put(GenDocxImpl.class,genDocxImpl);
        classMap.put(ParseDDLToTableImpl.class,parseDDLToTableImpl);
        classMap.put(ParsePDMFileImpl.class,parsePDMFileImpl);
        classMap.put(PingLoadDriverClassImpl.class,pingLoadDriverClassImpl);
        classMap.put(ParseExcelFileImpl.class,parseExcelFileImpl);
        classMap.put(HttpParserImpl.class,httpParserImpl);
    }

    public Command getCommand(Class queryClazz) {
        return classMap.get(queryClazz);
    }
}

