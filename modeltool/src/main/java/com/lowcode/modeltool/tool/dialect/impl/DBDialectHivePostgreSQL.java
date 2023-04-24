package com.lowcode.modeltool.tool.dialect.impl;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2022/09/09
 * @desc : Hive使用PostgreSQL作元数据存储
 */
public class DBDialectHivePostgreSQL extends DBDialectMetaByQuery {
    @Override
    protected String getQueryTablesSQL() {
        return "SELECT\n" +
                "    tbl.\"TBL_NAME\" AS tbl_name -- 表名\n" +
                "     ,tbl_params.\"PARAM_VALUE\" AS tbl_comment -- 表注释\n" +
                "     ,db.\"NAME\" AS db_name -- 数据库名\n" +
                "FROM\n" +
                "    \"SDS\" SDS\n" +
                "        LEFT JOIN \"TBLS\" tbl ON sds.\"SD_ID\" = tbl.\"SD_ID\"\n" +
                "        LEFT JOIN \"TABLE_PARAMS\" tbl_params ON tbl.\"TBL_ID\" = tbl_params.\"TBL_ID\" AND tbl_params.\"PARAM_KEY\" = 'comment'\n" +
                "        LEFT JOIN \"PARTITION_KEYS\" partkey ON tbl.\"TBL_ID\" = partkey.\"TBL_ID\"\n" +
                "        LEFT JOIN \"DBS\" db ON tbl.\"DB_ID\" = db.\"DB_ID\" -- 过滤数据库\n" +
                "WHERE\n" +
                "    tbl.\"TBL_NAME\" IS NOT NULL\n" +
                " AND db.\"NAME\" = ? \n" ;
    }

    @Override
    protected String getQueryTableColumnsSQL() {
        return "SELECT\n" +
                "tbl.\"TBL_NAME\" AS tbl_name -- 表名\n" +
                ",tbl_params.\"PARAM_VALUE\" AS tbl_comment -- 表注释\n" +
                ",col.\"COLUMN_NAME\" AS col_name -- 字段名称\n" +
                ",col.\"COMMENT\" AS col_comment -- 字段注释\n" +
                ",col.\"TYPE_NAME\" AS column_type -- 字段类型\n" +
                ",col.\"INTEGER_IDX\" AS column_sequence -- 字段值\n" +
                ",db.\"NAME\" AS db_name \n" +
                "FROM\n" +
                "\"SDS\" sds\n" +
                "LEFT JOIN \"TBLS\" tbl ON sds.\"SD_ID\" = tbl.\"SD_ID\"\n" +
                "LEFT JOIN \"TABLE_PARAMS\" tbl_params ON tbl.\"TBL_ID\" = tbl_params.\"TBL_ID\" AND tbl_params.\"PARAM_KEY\" = 'comment'\n" +
                "LEFT JOIN \"PARTITION_KEYS\" partkey ON tbl.\"TBL_ID\" = partkey.\"TBL_ID\"\n" +
                "LEFT JOIN \"DBS\" db ON tbl.\"DB_ID\" = db.\"DB_ID\"\n" +
                "LEFT JOIN \"COLUMNS_V2\" col ON sds.\"CD_ID\" = col.\"CD_ID\" -- 过滤数据库\n" +
                "WHERE\n" +
                " db.\"NAME\" = ?" +
                " AND UPPER(tbl.\"TBL_NAME\") = ?\n" +
                " ORDER BY col.\"INTEGER_IDX\" ASC";
    }
}
