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
package com.lowcode.modeltool.tool.dialect.impl;


import com.lowcode.modeltool.tool.fisok.raw.kit.JdbcKit;
import com.lowcode.modeltool.tool.model.ColumnField;
import com.lowcode.modeltool.tool.model.TableEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2023/02/24
 * @desc : PostgreSQL通过SQL查询元数据
 */
public class DBDialectPostgreSQLByQuery extends DBDialectMetaByQuery {
    private static Logger logger = LoggerFactory.getLogger(DBDialectOracleByQuery.class);

    @Override
    protected String getQueryTablesSQL() {
        return "SELECT\n" +
                "    tb.TABLE_NAME as tbl_name,\n" +
                "    d.description as tbl_comment,\n" +
                "    tb.table_schema as db_name\n" +
                "FROM\n" +
                "    information_schema.tables tb\n" +
                "        JOIN pg_class C ON C.relname = tb.\n" +
                "        TABLE_NAME LEFT JOIN pg_description d ON d.objoid = C.oid\n" +
                "        AND d.objsubid = '0'\n" +
                "WHERE\n" +
                "        tb.table_schema = ?";
    }

    @Override
    protected String getQueryTableColumnsSQL() {
        return "SELECT\n" +
                "\tcol.TABLE_NAME as tbl_name,\n" +
                "\t'' as tbl_comment,\n" +
                "\tcol.ordinal_position AS col_index,\n" +
                "\tcol.COLUMN_NAME as col_name,\n" +
                "\td.description as col_comment,\n" +
                "\tcol.udt_name as data_type ,\n" +
                "\tcol.numeric_precision as num_precision,\n" +
                "\tcol.character_maximum_length as data_length,\n" +
                "\tcol.numeric_scale as num_scale,\n" +
                "\tcol.is_identity as is_primary_key,\n" +
                "\tcol.is_nullable as is_nullable,\n" +
                "\tcol.column_default as default_value\n" +
                "FROM\n" +
                "\tinformation_schema.COLUMNS col\n" +
                "\tJOIN pg_class C ON C.relname = col.\n" +
                "\tTABLE_NAME LEFT JOIN pg_description d ON d.objoid = C.oid \n" +
                "\tAND d.objsubid = col.ordinal_position \n" +
                "WHERE\n" +
                "\t1 = 1 \n" +
                "\tAND col.table_schema = ? \n" +
                "\tAND UPPER(col.TABLE_NAME)=?\n" +
                "ORDER BY\n" +
                "\tcol.ordinal_position asc\n" +
                "\t";
    }

    @Override
    public TableEntity createTableEntity(Connection conn, DatabaseMetaData meta, String tableName, String schema) throws SQLException {
        String sql = getQueryTableColumnsSQL();
        logger.debug(sql);

        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, schema);
        pstmt.setString(2, tableName);


        TableEntity tableEntity = new TableEntity();
        tableEntity.setDefKey(tableName);

        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            tableEntity.setDefKey(rs.getString("tbl_name"));
            tableEntity.setComment(rs.getString("tbl_comment"));
            ColumnField field = new ColumnField();
            tableEntity.getFields().add(field);
            field.setDefKey(rs.getString("col_name"));
            field.setDefName(rs.getString("col_comment"));

            String dataType = rs.getString("data_type");
            Integer dataLength = rs.getInt("data_length");
            Integer numScale = rs.getInt("num_scale");
            Integer numPrecision = rs.getInt("num_precision");
            if(dataType.toLowerCase().indexOf("clob")>=0
                    || dataType.toLowerCase().indexOf("blob") >=0
                    || dataType.toLowerCase().indexOf("int") >=0
            )
            {
                numPrecision = null;
                dataLength = null;
                numScale = null;
            }
            String isNullable = rs.getString("is_nullable");//  Y|N
            String isPrimaryKey = rs.getString("is_primary_key");//
            String defaultValue = rs.getString("default_value");//

            //数据类型以及长度
            field.setType(dataType);
            if (numPrecision != null && numPrecision > 0) {
                field.setLen(numPrecision);
                if (numScale != null && numScale > 0) {
                    field.setScale(numScale);
                }
            } else if (dataLength != null && dataLength > 0) {
                field.setLen(dataLength);
            }
            field.setNotNull("NO".equals(isNullable));
            field.setPrimaryKey("YES".equals(isPrimaryKey));
            if (dataType.toLowerCase().indexOf("char") >= 0) {
                defaultValue = "'" + defaultValue + "'";
            }
            field.setDefaultValue(defaultValue);
        }

        JdbcKit.close(pstmt);
        JdbcKit.close(rs);

        return tableEntity;
    }
}
