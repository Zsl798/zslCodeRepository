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
 * @date : 2021/12/24
 * @desc : ORACLE数据库方言,通过SQL查询元数据
 */
public class DBDialectOracleByQuery extends DBDialectMetaByQuery {
    private static Logger logger = LoggerFactory.getLogger(DBDialectOracleByQuery.class);

    @Override
    protected String getQueryTablesSQL() {
        return "SELECT\n" +
                "    t.table_name AS tbl_name,\n" +
                "    c.comments AS tbl_comment,\n" +
                "    t.OWNER AS db_name\n" +
                "FROM all_tables t left join all_tab_comments c on t.OWNER=c.OWNER and t.TABLE_NAME=c.TABLE_NAME\n" +
                "where t.OWNER=?";
    }

    @Override
    protected String getQueryTableColumnsSQL() {
        return "SELECT\n" +
                "\tcol.table_name AS tbl_name,\n" +
                "\t'' AS tbl_comment,\n" +
                "\tcol.column_name AS col_name,\n" +
                "\tclc.comments AS col_comment,\n" +
                "\tcol.data_type AS data_type,\n" +
                "\tcol.data_length as data_length,\n" +
                "\tcol.data_precision AS num_precision,\n" +
                "\tcol.data_scale AS num_scale,\n" +
                "\tcol.nullable AS is_nullable,\n" +
                "\t'' AS is_primary_key,\n" +
                "\tcol.data_default AS default_value \n" +
                "FROM\n" +
                "\tall_tab_columns col\n" +
                "\tLEFT JOIN all_col_comments clc ON col.table_name = clc.table_name \n" +
                "\tAND col.column_name = clc.column_name \n" +
                "WHERE\n" +
                "\tcol.OWNER = ? \n" +
                "\tAND UPPER(col.table_name) = ?";
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
            Integer dataLength = null;
            if(dataType.toLowerCase().indexOf("clob")>=0 || dataType.toLowerCase().indexOf("blob") >=0){
                dataLength = null;
            }else{
                dataLength = rs.getInt("data_length");
            }
            dataLength = rs.getInt("data_length");
            Integer numPrecision = rs.getInt("num_precision");
            Integer numScale = rs.getInt("num_scale");
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
            field.setNotNull("N".equals(isNullable));
            field.setPrimaryKey(false);
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
