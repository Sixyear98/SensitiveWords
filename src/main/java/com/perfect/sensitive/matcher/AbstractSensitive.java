package com.perfect.sensitive.matcher;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;

/**
 * @Description 敏感过滤
 *
 * @author Gaonan
 */
public abstract class AbstractSensitive<T> {

    /**
     * 敏感提示类型
     */
    private final int type;

    /**
     * 构造函数
     *
     * @param type
     */
    public AbstractSensitive(int type) {
        this.type = type;
    }

    /**
     * 初始化
     *
     * @param lineWords
     * @return
     */
    public void initialize(Set<String> lineWords) {
        for (String line : lineWords) {
            if (StringUtils.isBlank(line)) {
                continue;
            }
            this.appendWords(line);
        }
    }

    /**
     * 添加单个单词
     *
     * @param words
     * @return
     */
    public abstract void appendWords(String words);

    /**
     * 构建敏感字匹配实例
     *
     * @param message
     * @param param
     * @return
     */
    public abstract AbstractMatcher matcher(String message, T[] param);

    /**
     * 清理数据
     *
     * @return
     */
    public abstract void clear();

    /**
     * 敏感提示类型
     *
     * @return
     */
    public int getType() {
        return type;
    }

}
