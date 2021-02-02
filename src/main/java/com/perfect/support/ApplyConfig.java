package com.perfect.support;

/**
 * @Description 应用配置数据
 *
 * @author Gaonan
 */
public class ApplyConfig {

    /**
     * 敏感字典路径
     */
    public static final String DICT_PATH;

    /**
     * 敏感字字典
     */
    public static final String[] CONFIG;

    /**
     * 汉字拼音字典
     */
    public static final String HANYU_PINYIN;

    /**
     * 两字之间间距 对应（特殊字符过滤，匹配配置中的 敏感字 两字之间不超过一定间距）过滤策略
     */
    public static final int MEDIUM_INTERVAL;

    /**
     * 两字之间间距 对应（特殊字符过滤，匹配配置中的 敏感字&拼音 两字之间不超过一定间距）过滤策略
     */
    public static final int SENIOR_INTERVAL;


    /**
     * 初始化只能放到静态结构体里面
     */
    static {
        DICT_PATH = null;
        CONFIG = null;
        HANYU_PINYIN = null;
        MEDIUM_INTERVAL = 0;
        SENIOR_INTERVAL = 0;
    }

    private ApplyConfig() {
    }

}
