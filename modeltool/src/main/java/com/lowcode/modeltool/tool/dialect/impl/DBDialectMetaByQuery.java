package com.lowcode.modeltool.tool.dialect.impl;


import com.lowcode.modeltool.tool.command.kit.ConnParseKit;
import com.lowcode.modeltool.tool.dialect.DBDialect;
import com.lowcode.modeltool.tool.fisok.raw.kit.JdbcKit;
import com.lowcode.modeltool.tool.fisok.raw.kit.StringKit;
import com.lowcode.modeltool.tool.model.ColumnField;
import com.lowcode.modeltool.tool.model.TableEntity;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 * 通过meta-query查询元数据
 */
public abstract class DBDialectMetaByQuery extends DBDialect {
    private static Logger logger = LoggerFactory.getLogger(DBDialectMetaByQuery.class);
    /**
     * 查询表清单
     * @return
     */
    protected abstract String getQueryTablesSQL();

    /**
     * 查询字段列表
     * @return
     */
    protected abstract String getQueryTableColumnsSQL();

    @Override
    public List<TableEntity> getAllTables(Connection conn, String schema) throws SQLException {
        String sql = getQueryTablesSQL();
        logger.debug(sql);
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1,schema);

        List<TableEntity> tableEntities = new ArrayList<>();

        ResultSet rs = pstmt.executeQuery();
        while (rs.next()){
            TableEntity tableEntity = new TableEntity();
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
            tableEntities.add(tableEntity);
        }

        JdbcKit.close(pstmt);
        JdbcKit.close(rs);

        return tableEntities;
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
            tableEntity.setComment(rs.getString("tbl_comment"));
            ColumnField field = new ColumnField();
            tableEntity.getFields().add(field);
            field.setDefKey(rs.getString("col_name"));
            field.setDefName(rs.getString("col_comment"));
            field.setType(rs.getString("column_type"));
        }

        JdbcKit.close(pstmt);
        JdbcKit.close(rs);

        return tableEntity;
    }
}
