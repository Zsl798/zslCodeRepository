package com.lowcode.modeltool.tool.command.impl;

import com.lowcode.modeltool.tool.command.ExecResult;
import com.lowcode.modeltool.tool.fisok.raw.kit.IOKit;
import com.lowcode.modeltool.tool.fisok.raw.kit.JSONKit;
import com.lowcode.modeltool.tool.fisok.raw.kit.StringKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
/**
 * @author zhangsonglin
 * @since 1.0
 * @version 2023年4月24日
 * @version 2023年4月24日
 * @desc : Http解析
 */
@Component
public class HttpParserImpl extends AbstractDBCommand<ExecResult> {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public ExecResult exec(Map<String, String> params) {

        String httpUrl = params.get("url");

        ExecResult ret = new ExecResult();
        try {
            ret.setBody(doGet(httpUrl));
            ret.setStatus(ExecResult.SUCCESS);
            System.out.println(JSONKit.toJsonString(ret, true));
        } catch (Exception e) {
            String message = e.getMessage();
            if (StringKit.isBlank(message)) {
                message = e.toString();
            }
            ret.setBody(message);
            ret.setStatus(ExecResult.FAILED);
            logger.error("", e);
        }

        return ret;
    }

    public static String doGet(String httpUrl) {
        //链接
        HttpURLConnection connection = null;
        InputStream is = null;
        BufferedReader br = null;
        StringBuffer result = new StringBuffer();
        try {
            //创建连接
            URL url = new URL(httpUrl);
            connection = (HttpURLConnection) url.openConnection();
            //设置请求方式
            connection.setRequestMethod("GET");
            //设置连接超时时间
            connection.setConnectTimeout(15000);
            //设置读取超时时间
            connection.setReadTimeout(15000);
            //开始连接
            connection.connect();
            //获取响应数据
            if (connection.getResponseCode() == 200) {
                //获取返回的数据
                is = connection.getInputStream();
                if (is != null) {
                    br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    String temp = null;
                    while ((temp = br.readLine()) != null) {
                        result.append(temp);
                    }
                }
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOKit.close(br);
            IOKit.close(is);
            IOKit.close(connection);// 关闭远程连接
        }
        return result.toString();
    }
}
