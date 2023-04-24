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


import com.lowcode.modeltool.tool.command.kit.ConnParseKit;
import com.lowcode.modeltool.tool.fisok.raw.kit.JdbcKit;
import com.lowcode.modeltool.tool.fisok.raw.kit.StringKit;
import com.lowcode.modeltool.tool.model.ColumnField;
import com.lowcode.modeltool.tool.model.TableEntity;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2022/12/24
 * @desc : MySQL数据库方言,通过SQL查询元数据
 */
public class DBDialectMySQLByQuery extends DBDialectMetaByQuery {
    private static Logger logger = LoggerFactory.getLogger(DBDialectMySQLByQuery.class);

    @Override
    protected String getQueryTablesSQL() {
        return "SELECT\n" +
                "    table_name AS tbl_name,\n" +
                "    table_comment AS tbl_comment,\n" +
                "    table_schema AS db_name\n" +
                "FROM\n" +
                "    information_schema.TABLES\n" +
                "WHERE table_schema = ?\n" +
                "  AND table_type = 'BASE TABLE'" ;
    }

    @Override
    protected String getQueryTableColumnsSQL() {
        return "SELECT\n" +
                "    table_name AS tbl_name,\n" +
                "    '' AS tbl_comment,\n" +
                "    column_name AS col_name,\n" +
                "    column_comment AS col_comment,\n" +
                "    data_type AS data_type,\n" +
                "    character_maximum_length AS data_length,\n" +
                "    numeric_precision AS num_precision,\n" +
                "    numeric_scale AS num_scale,\n" +
                "    is_nullable AS is_nullable,\n" +
                "    column_key AS is_primary_key,\n" +
                "    column_default AS default_value,\n" +
                "    column_type AS data_type_text\n" +
                "FROM\n" +
                "    information_schema.COLUMNS\n" +
                "WHERE table_schema = ? and upper(table_name)=upper(?)";
    }

    @Override
    public TableEntity createTableEntity(Connection conn, DatabaseMetaData meta, String tableName, String schema) throws SQLException {
        String sql = getQueryTableColumnsSQL();
        logger.debug(sql);

        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1,schema);
        pstmt.setString(2,tableName);


        TableEntity tableEntity = new TableEntity();
        tableEntity.setDefKey(tableName);

        ResultSet rs = pstmt.executeQuery();
        while (rs.next()){
            tableEntity.setDefKey(rs.getString("tbl_name"));
            String comment = rs.getString("tbl_comment");

            //如果remark中有分号等分割符，则默认之后的就是注释说明文字
            if(StringKit.isNotBlank(comment)){
                Pair<String, String> pair = ConnParseKit.parseNameAndComment(comment);
                String defName = pair.getLeft();
                String remark = pair.getRight();
                tableEntity.setDefName(defName);
                tableEntity.setComment(remark);
            }
            ColumnField field = new ColumnField();
            tableEntity.getFields().add(field);
            field.setDefKey(rs.getString("col_name"));
            field.setDefName(rs.getString("col_comment"));

            String dataType = rs.getString("data_type");
            Integer dataLength = null;
            if(dataType.equalsIgnoreCase("longtext") || dataType.equalsIgnoreCase("longblob")){
                dataLength = null;
            }else{
                dataLength = rs.getInt("data_length");
            }
            Integer numPrecision = rs.getInt("num_precision");
            Integer numScale = rs.getInt("num_scale");
            String isNullable = rs.getString("is_nullable");//  YES|NO
            String isPrimaryKey = rs.getString("is_primary_key");// PRI
            String defaultValue = rs.getString("default_value");// PRI

            //数据类型以及长度
            field.setType(dataType);
            if (numPrecision != null && numPrecision > 0 && !dataType.equalsIgnoreCase("int")) {
                field.setLen(numPrecision);
                if (numScale != null && numScale > 0) {
                    field.setScale(numScale);
                }
            } else if (dataLength != null && dataLength > 0) {
                field.setLen(dataLength);
            }
            field.setNotNull("NO".equals(isNullable));
            field.setPrimaryKey("PRI".equals(isPrimaryKey));
            if(dataType.toLowerCase().indexOf("char")>=0){
                defaultValue = "'"+defaultValue+"'";
            }
            field.setDefaultValue(defaultValue);
        }

        JdbcKit.close(pstmt);
        JdbcKit.close(rs);

        return tableEntity;
    }
}
