package com.perfect.sensitive;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.perfect.sensitive.matcher.AbstractSensitive;
import com.perfect.sensitive.matcher.common.CommonSensitive;
import com.perfect.sensitive.matcher.medium.MediumSensitive;
import com.perfect.sensitive.matcher.polity.PolitySensitive;
import com.perfect.sensitive.matcher.senior.SeniorSensitive;

/**
 * @Description 敏感初始
 *
 * @author Gaonan
 */
public enum SensitiveInit {

//    /** 违禁 */
//    VIOLATE("violate.txt", new CommonSensitive(100)),
//
//    /** 色情 */
//    SEXY("sexy.txt", new MediumSensitive(200)),
//
//    /** 谩骂 */
//    ABUSE("abuse.txt", new CommonSensitive(300)),
//
//    /** 广告 */
//    ADVERT("advert.txt", new CommonSensitive(400)),
//
//    /** 恐暴 */
//    RIOT("riot.txt", new SeniorSensitive(500)),

    /** 涉政 */
    POLITY("polity.txt", new PolitySensitive(600)),
    ;

    /** 文件配置 */
    private String config;

    /** 过滤策略 */
    private AbstractSensitive sensitive;

    /** 词敏感 */
    public static List<AbstractSensitive> checkWords = new ArrayList<>();

    /** 拼音敏感 */
    public static List<AbstractSensitive> checkPinyin = new ArrayList<>();

    /** 涉政敏感 */
    public static List<AbstractSensitive> checkPolity = new ArrayList<>();

    static {
        for (SensitiveInit init : SensitiveInit.values()) {
            AbstractSensitive sensitive = init.getSensitive();
            if (sensitive == null) {
                continue;
            }
            if (sensitive instanceof SeniorSensitive) {
                checkPinyin.add(sensitive);
                continue;
            }
            if (sensitive instanceof PolitySensitive) {
                checkPolity.add(sensitive);
                continue;
            }
            checkWords.add(sensitive);
        }
    }

    /**
     * 默认构造
     *
     * @param config
     * @param sensitive
     */
    SensitiveInit(String config, AbstractSensitive sensitive) {
        this.config = config;
        this.sensitive = sensitive;
    }

    /**
     * 过滤类型
     *
     * @param config
     * @return
     */
    public static SensitiveInit getInit(String config) {
        for (SensitiveInit init : SensitiveInit.values()) {
            if (StringUtils.equalsIgnoreCase(init.config, config)) {
                return init;
            }
        }
        return POLITY;
    }

    public String getConfig() {
        return config;
    }

    public AbstractSensitive getSensitive() {
        return sensitive;
    }

    @Override
    public String toString() {
        if (sensitive == null) {
            return "SensitiveResource{" + "config='" + config + "}";
        }
        return "SensitiveResource{" + "config='" + config + ", type=" + sensitive.getType() + "}";
    }

}