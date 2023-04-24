package com.lowcode.modeltool.tool.command.kit;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author zhangsonglin
 * @since 1.0
 * @version 2023年4月24日
 * @version 2023年4月24日
 * @desc : 连接以及解析处理工具类
 */
public abstract class ConnParseKit {
    private static Logger logger = LoggerFactory.getLogger(ConnParseKit.class);

    /**
     * 从注释文字中解析表的业务名（中文名）
     *
     * @param remarks
     * @return Left为字段名，Right为字段注释
     */
    public static Pair parseNameAndComment(String remarks) {
//        String[] delimters = new String[]{" ", ",", "，", ";", "\t", "\n"};
        String[] delimters = new String[]{"\t", "\n"};

        //有括号的字段，不解析其中的注释了
        if (remarks.indexOf("(") > 0
                && remarks.indexOf(")") < 0
                && remarks.indexOf("(") < remarks.indexOf(")")
        ) {
            return Pair.of(remarks, "");
        }


        Pair<String, String> pair = null;
        for (int i = 0; i < delimters.length; i++) {
            String delimter = delimters[i];
            int idxDelimter = remarks.indexOf(delimter);
            if (idxDelimter > 0) {
                String left = remarks.substring(0, idxDelimter);
                String right = remarks.substring(idxDelimter + 1);
                pair = Pair.of(left, right);
            }
            if (pair != null) {
                break;
            }
        }
        if (pair == null) {
            pair = Pair.of(remarks, "");
        }
        return pair;
    }
}
