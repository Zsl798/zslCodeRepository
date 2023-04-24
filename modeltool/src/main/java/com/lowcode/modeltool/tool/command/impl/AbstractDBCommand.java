package com.lowcode.modeltool.tool.command.impl;

import com.lowcode.modeltool.tool.command.Command;
import com.lowcode.modeltool.tool.fisok.raw.kit.JdbcKit;
import com.lowcode.modeltool.tool.fisok.raw.kit.StringKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author zhangsonglin
 * @since 1.0
 * @version 2023年4月24日
 * @version 2023年4月24日
 * @desc : 数据库相关操作命令的抽像
 */
public abstract class AbstractDBCommand<T> implements Command<T> {
    public static final String KEY_DRIVER_CLASS_NAME = "driver_class_name";
    public static final String KEY_URL = "url";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected String driverClassName;
    protected String url;
    protected String username;
    protected String password;
    private Connection dbConn = null;
    protected Map<String,String> extProps = new LinkedHashMap<>();

    public void init(Map<String,String> params){
        //如果有数据库连接了，就不要初始化了
        if(dbConn != null){
            return;
        }
        driverClassName = params.get(KEY_DRIVER_CLASS_NAME);
        url = params.get(KEY_URL);
        username = params.get(KEY_USERNAME);
        password = params.get(KEY_PASSWORD);
        if(url.indexOf("{and}")>0){
            url = url.replaceAll("\\{and\\}","&");
        }
        Iterator<String> iterator = params.keySet().iterator();
        while (iterator.hasNext()){
            String key = StringKit.nvl(iterator.next(),"");
            if(!inRemainProps(key)){
                String value = StringKit.nvl(params.get(key),"");
                extProps.put(key,value);
            }
        }
        if(url.indexOf("?")>0){
            String sURLParam = url.substring(url.indexOf("?")+1);
            if(StringKit.isNotBlank(sURLParam)){
                String[] urlParams = sURLParam.split("&");
                for(String urlParam : urlParams){
                    if(urlParam.indexOf("=")<=0){
                        continue;
                    }else{
                        String k = StringKit.trim(urlParam.split("=")[0]);
                        String v = StringKit.trim(urlParam.split("=")[1]);
                        extProps.put(k,v);
                    }
                }
            }
        }
    }

    /**
     * 键值是否为保留值
     * @param key
     * @return
     */
    private boolean inRemainProps(String key){
        if(key.equalsIgnoreCase(KEY_DRIVER_CLASS_NAME)
            ||key.equalsIgnoreCase(KEY_URL)
            ||key.equalsIgnoreCase(KEY_USERNAME)
            ||key.equalsIgnoreCase(KEY_PASSWORD)){
            return true;
        }else{
            return false;
        }
    }

    public Connection getDbConn() {
        return dbConn;
    }

    public void setDbConn(Connection dbConn) {
        this.dbConn = dbConn;
    }

    public Connection createConnect(){
        if(dbConn != null){
            return dbConn;
        }else{
            Properties props = new Properties();
            if(StringKit.isNotBlank(username)){
                props.put("user", username);
            }
            if(StringKit.isNotBlank(password)){
                props.put("password", password);
            }
            if(extProps.size() > 0){
                props.putAll(extProps);
            }
            return JdbcKit.getConnection(driverClassName, url, props);
        }
    }
}
