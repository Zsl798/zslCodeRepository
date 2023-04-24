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
import com.lowcode.modeltool.tool.fisok.raw.kit.StringKit;
import com.lowcode.modeltool.tool.model.ColumnField;
import com.lowcode.modeltool.tool.model.TableEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2021/12/24
 * @desc : ORACLE数据库方言,通过SQL查询元数据
 */
public class DBDialectGBaseByQuery extends DBDialectMetaByQuery {
    private static Logger logger = LoggerFactory.getLogger(DBDialectGBaseByQuery.class);

    @Override
    protected String getQueryTablesSQL() {
//        return "SELECT\n" +
//                "    t.table_name AS tbl_name,\n" +
//                "    c.comments AS tbl_comment,\n" +
//                "    t.OWNER AS db_name\n" +
//                "FROM all_tables t left join all_tab_comments c on t.OWNER=c.OWNER and t.TABLE_NAME=c.TABLE_NAME\n" +
//                "where t.OWNER=?";
        return "SELECT\n" +
                "    t.tabid as tbl_id,\n" +
                "    t.tabname AS tbl_name,\n" +
                "    c.comments AS tbl_comment,\n" +
                "    OWNER AS db_name\n" +
                "FROM\n" +
                "    systables t left join syscomms c on t.tabid = c.tabid\n" +
                "WHERE OWNER = ?";
    }

    @Override
    protected String getQueryTableColumnsSQL() {
        return "SELECT\n" +
                "    t.tabname AS tbl_name,\n" +
                "    '' AS tbl_comment,\n" +
                "    col.colname AS col_name,\n" +
                "    clc.comments AS col_comment,\n" +
                "    col.coltype AS col_type,\n" +
                "    col.collength AS col_len,\n" +
                "    col.colattr as col_attr,\n" +
                "    cld.\"default\" as col_default\n" +
                "FROM\n" +
                "    syscolumns col\n" +
                "        left join syscolcomms clc on col.tabid=clc.tabid and col.colno=clc.colno\n" +
                "        left join sysdefaults cld on col.tabid=cld.tabid and col.colno=cld.colno,\n" +
                "    systables t\n" +
                "WHERE\n" +
                "    col.tabid = t.tabid\n" +
                "    and t.OWNER = ?\n" +
                "    and upper(t.tabname) = upper(?)";
    }
    Map<Integer,String> typeMapping = new HashMap<Integer,String>(){{
        put(0,"CHAR");
        put(1,"SMALLINT");
        put(2,"INTEGER");
        put(3,"FLOAT");
        put(4,"SMALLFLOAT");
        put(5,"DECIMAL");
        put(6,"SERIAL");
        put(7,"DATE");
        put(8,"MONEY");
        put(10,"DATETIME");
        put(11,"BYTE");
        put(12,"TEXT");
        put(13,"VARCHAR");
        put(14,"INTERVAL");
        put(15,"NCHAR");
        put(16,"NVARCHAR");
        put(17,"INT8");
        put(18,"SERIAL8");
        put(45,"BOOLEAN");
        put(52,"BIGINT");
        put(53,"BIGSERIAL");
    }};

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

//            coltype 以及长度 https://blog.csdn.net/qq_39280087/article/details/122584385
            Integer colType = rs.getInt("col_type");
            Integer colLen = rs.getInt("col_len");
            Integer colAttr = rs.getInt("col_attr");
            String defaultValue = rs.getString("col_default");
            if(StringKit.isNotBlank(defaultValue)){
                defaultValue = StringKit.trim(defaultValue);
            }

            //超过256的，要从中剥离出not null
            int realColType = colType;
            if(colType>256){
                realColType = colType - 256;
                field.setNotNull(true);
            }
            //通过数字映射取数据类型
            String typeName = typeMapping.get(realColType);
            if(StringKit.isBlank(typeName)){
                typeName = "VARCHAR";
            }
            field.setType(typeName);

            //类型为数字时，要通过长度反向计算长度以精度
            if((colType == 3 || colType == 4 || colType == 5 || colType == 8)
                    && colLen > 256){
                int mod = colLen % 256;
                if(mod > 0){
                    field.setLen((colLen-mod)/256);
                    field.setScale(mod);
                }
            }else if(colType != 1 && colType != 2 && colType != 6
                    && colType != 7&& colType != 10 && colType != 14 && colType != 17
                    && colType != 18&& colType != 45
                    && colType != 52 && colType != 53){
                field.setLen(colLen);
            }

            if(colAttr == 128){
                field.setPrimaryKey(true);
                field.setNotNull(true);
            }else{
                field.setPrimaryKey(false);
                field.setNotNull(false);
            }
            if (typeName.toLowerCase().indexOf("char") >= 0) {
                defaultValue = "'" + defaultValue + "'";
            }
            field.setDefaultValue(defaultValue);
        }

        JdbcKit.close(pstmt);
        JdbcKit.close(rs);

        return tableEntity;
    }
}
