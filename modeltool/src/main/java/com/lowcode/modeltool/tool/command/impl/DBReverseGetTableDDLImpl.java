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

import com.lowcode.modeltool.tool.command.ExecResult;
import com.lowcode.modeltool.tool.dialect.DBDialect;
import com.lowcode.modeltool.tool.dialect.DBDialectMatcher;
import com.lowcode.modeltool.tool.fisok.raw.kit.JdbcKit;
import com.lowcode.modeltool.tool.fisok.raw.kit.StringKit;
import com.lowcode.modeltool.tool.fisok.sqloy.core.DBType;
import com.lowcode.modeltool.tool.fisok.sqloy.kit.DBTypeKit;
import com.lowcode.modeltool.tool.model.TableEntity;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhangsonglin
 * @since 1.0
 * @version 2023年4月24日
 * @version 2023年4月24日
 * @desc : 数据库逆向，解析表清单的字段以及索引
 */
@Component
public class DBReverseGetTableDDLImpl extends AbstractDBCommand<ExecResult> {
    @Override
    public ExecResult exec(Map<String, String> params) {
        super.init(params);
        String tables = params.get("tables").toUpperCase();
        if (StringKit.isBlank(tables)) {
            throw new IllegalArgumentException("parameter [tables] not exists");
        }
        List<String> tableList = Arrays.stream(tables.split(","))
                .collect(Collectors.toList());

        ExecResult ret = new ExecResult();

        Connection conn = null;
        try {
            conn = createConnect();
            String schemaOwner = extProps.get("schemaOwner");
            //兼容4.2.2版本之前的写法，主要针对hive
            if(StringKit.isBlank(schemaOwner)){
                schemaOwner = extProps.get("metaDb");
            }

            List<TableEntity> tableEntities = fillTableEntities(conn, tableList,schemaOwner);
            ret.setStatus(ExecResult.SUCCESS);
            ret.setBody(tableEntities);
        } catch (Exception e) {
            ret.setStatus(ExecResult.FAILED);
            ret.setBody(e.getMessage());
            logger.error("", e);
        } finally {
            JdbcKit.close(conn);
        }
        return ret;
    }


    /**
     * 获取所有数据表实体的字段及索引
     *
     * @param conn
     * @param tableNameList
     * @param schemaOwner
     * @return
     */
    protected List<TableEntity> fillTableEntities(Connection conn, List<String> tableNameList,String schemaOwner) {
        List<TableEntity> tableEntities = new ArrayList<TableEntity>();

        try {
            DatabaseMetaData meta = conn.getMetaData();

            DBType dbType = DBTypeKit.getDBType(conn);
            DBDialect dbDialect = DBDialectMatcher.getDBDialect(dbType,extProps);

            for (String tableName : tableNameList) {
                TableEntity tableEntity = dbDialect.createTableEntity(conn, meta, tableName,schemaOwner);
                if (tableEntity == null) {
                    continue;
                }
                tableEntity.fillFieldsCalcValue();
                tableEntities.add(tableEntity);
            }
        } catch (SQLException e) {
            logger.error("读取表清单出错", e);
            throw new RuntimeException("读取表清单出错|" + e.getMessage(), e);
        }

        return tableEntities;
    }
}
