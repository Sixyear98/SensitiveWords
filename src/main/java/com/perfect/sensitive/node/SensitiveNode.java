package com.perfect.sensitive.node;

import java.util.Map;
import java.util.TreeMap;

import com.perfect.sensitive.matcher.AbstractSensitive;

/**
 * @Description 抽象敏感节点
 *
 * @author Gaonan
 * @param <K>
 * @param <V>
 */
public abstract class SensitiveNode<K, V extends SensitiveNode> {

    /**
     * 节点集合
     */
    public Map<K, V> nodeMap;

    /**
     * 敏感字语句
     */
    public String config = null;

    /**
     * 构造函数
     */
    public SensitiveNode() {
        nodeMap = new TreeMap<>();
    }

    /**
     * 是否包含KEY
     *
     * @param key
     * @return
     */
    public final boolean containsKey(K key) {
        return nodeMap.containsKey(key);
    }

    /**
     * 获取节点
     *
     * @param key
     * @return
     */
    public final V get(K key) {
        return nodeMap.get(key);
    }

    /**
     * 添加节点
     *
     * @param key
     * @param value
     * @return
     */
    public final V put(K key, V value) {
        return nodeMap.put(key, value);
    }

    /**
     * 是否是终点节点
     *
     * @return
     */
    public boolean isTerminal() {
        return config != null;
    }

    /**
     * 敏感字语句
     *
     * @return
     */
    public String getConfig() {
        return config;
    }

    /**
     * 将指定的词分成字构建到一棵树中
     *
     * @param config
     * @param keys
     * @param sensitive
     * @return
     */
    public abstract void generateTrie(String config, K[] keys, AbstractSensitive sensitive);

}
