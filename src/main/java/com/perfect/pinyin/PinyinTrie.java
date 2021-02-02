package com.perfect.pinyin;

import java.util.HashMap;
import java.util.Map;

/**
 * 拼音字典---分段优化辅助类
 */
public class PinyinTrie {

    /**
     * 本节点包含的值
     */
    private Map<Integer, String> trieMap = new HashMap<Integer, String>();

    /**
     * 构造函数
     */
    public PinyinTrie() {

    }

    /**
     * 获取拼音
     */
    public String put(Integer key, String value) {
        return trieMap.put(key, value);
    }

    /**
     * 获取拼音
     */
    public String get(Integer key) {
        return trieMap.get(key);
    }

}
