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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zhangsonglin
 * @since 1.0
 * @version 2023年4月24日
 * @version 2023年4月24日
 * @desc : 数据库逆向，解析表清单功能
 */
@Component
public class DBReverseGetAllTablesListImpl extends AbstractDBCommand<ExecResult> {

    @Override
    public ExecResult exec(Map<String, String> params) {
        super.init(params);

        ExecResult ret = new ExecResult();

        //获取连接正常的情况下，进入下一步
        Connection conn = null;
        List<TableEntity> tableEntities = null;
        try {
            conn = createConnect();
            tableEntities = fetchTableEntities(conn);
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
     * 获取所有数据表列表
     *
     * @param conn
     * @return
     */
    protected List<TableEntity> fetchTableEntities(Connection conn) throws SQLException {
        List<TableEntity> tableEntities = new ArrayList<>();
        try {
            String schemaOwner = extProps.get("schemaOwner");
            //兼容4.2.2版本之前的写法，主要针对hive
            if(StringKit.isBlank(schemaOwner)){
                schemaOwner = extProps.get("metaDb");
            }
            DBType dbType = DBTypeKit.getDBType(conn);
            DBDialect dbDialect = DBDialectMatcher.getDBDialect(dbType,extProps);
            tableEntities = dbDialect.getAllTables(conn,schemaOwner);
        } catch (SQLException e) {
            logger.error("读取表清单出错", e);
            throw new RuntimeException(e);
        }

        return tableEntities;
    }
}
