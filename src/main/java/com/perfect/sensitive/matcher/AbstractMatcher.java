package com.perfect.sensitive.matcher;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.perfect.SensitiveWords;

/**
 * @Description 敏感匹配
 *
 * @author Gaonan
 * @param <S>
 * @param <P>
 */
public abstract class AbstractMatcher<S extends AbstractSensitive, P> {

    /**
     * 校验语句字符数据
     */
    public String message;

    /**
     * 校验语句拼音数组数据
     */
    public Character[] words = null;

    /**
     * 校验语句拼音数组数据
     */
    public String[] pinyins = null;

    /**
     * 敏感字处理实例
     */
    public final S sensitive;

    /**
     * 构造函数
     *
     * @param message
     * @param sensitive
     */
    public AbstractMatcher(String message, S sensitive) {
        this.message = message;
        this.sensitive = sensitive;
    }

    /**
     * 计算循环次数
     *
     * @param t
     * @param index
     * @param interval
     * @param <T>
     * @return
     */
    public final <T> int calcLoop(T[] t, int index, int interval) {
        int times = index + interval;
        if (interval > 0) {
            return times > t.length ? t.length : times;
        }
        times = t.length;
        return times;
    }

    /**
     * 是否包含敏感字
     *
     * @param param
     * @return
     */
    public abstract boolean contains(P param);

    /**
     * 查找敏感字
     *
     * @param param
     * @return
     */
    public abstract String find(P param);

    /**
     * 过滤覆写应过滤掉的字符
     *
     * @param param
     * @return
     */
    public final String filter(P param) {
        String result = this.find(param);
        if (StringUtils.isEmpty(result)) {
            return message;
        }
        char[] toChar = result.toCharArray();
        for (char value : toChar) {
            message = StringUtils.replaceChars(message, value, SensitiveWords.REPLACE_CHAR);
        }
        return message;
    }

}
