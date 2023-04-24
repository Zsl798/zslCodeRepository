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
package com.lowcode.modeltool.tool.dialect;


import com.lowcode.modeltool.tool.dialect.impl.*;
import com.lowcode.modeltool.tool.fisok.sqloy.core.DBType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2021/6/14
 * @desc :
 */
public class DBDialectMatcher {

    private static Map<DBType,DBDialect> dbTypeMap = new HashMap<DBType,DBDialect>(){{
        put(DBType.MYSQL,new DBDialectMySQL());
        put(DBType.ORACLE,new DBDialectOracle());
        put(DBType.POSTGRESQL,new DBDialectPostgreSQL());
        put(DBType.DB2,new DBDialectDB2());
        put(DBType.DM,new DBDialectDM());
        put(DBType.SQL_SERVER,new DBDialectSQLServer());
        put(DBType.KINGBASE,new DBDialectKingbase());
        put(DBType.SQLITE,new DBDialectSQLite());
        put(DBType.GBASE,new DBDialectGBaseByQuery());
    }};

    /**
     * 数据库方言解析类，默认的情况下，使用默认的
     * @param dbType
     * @return
     */
    public static DBDialect getDBDialect(DBType dbType,Map<String,String> extProps){

        DBDialect dbDialect = dbTypeMap.get(dbType);
        if(dbDialect == null){
            dbDialect = new DBDialect();
        }

        //通过SQL查询元数据信息
        if("1".equals(extProps.get("metaFromSQL"))){
//            String schemaOwner = extProps.get("schemaOwner");
            if(dbDialect instanceof DBDialectMySQL){
                dbDialect = new DBDialectMySQLByQuery();
            }else if(dbDialect instanceof DBDialectPostgreSQL){
                dbDialect = new DBDialectPostgreSQLByQuery();
            }else if(dbDialect instanceof DBDialectOracle){
                dbDialect = new DBDialectOracleByQuery();
            }
        }

        //针对hive进行特殊处理
        String hiveMeta = extProps.get("hive-meta");
        if("PostgreSQL".equals(hiveMeta)){
            dbDialect = new DBDialectHivePostgreSQL();
        }else if("MySQL".equals(hiveMeta)){
            dbDialect = new DBDialectHiveMySQL();
        }
        return dbDialect;
    }
}
