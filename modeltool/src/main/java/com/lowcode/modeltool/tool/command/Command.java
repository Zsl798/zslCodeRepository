package com.lowcode.modeltool.tool.command;

import java.util.Map;

/**
 * @author zhangsonglin
 * @since 1.0
 * @version 2023年4月24日
 * @version 2023年4月24日
 * @desc : 执行命令的抽像
 */
public interface Command<T> {
    T exec(Map<String, String> params);
}
