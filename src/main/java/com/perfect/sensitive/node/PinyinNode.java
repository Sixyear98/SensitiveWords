package com.perfect.sensitive.node;

import com.perfect.pinyin.ChinesePinyin;
import com.perfect.sensitive.matcher.AbstractSensitive;

/**
 * @Description 拼音节点
 *
 * @author Gaonan
 */
public class PinyinNode extends SensitiveNode<String, PinyinNode> {

    public PinyinNode() {
        super();
    }

    @Override
    public void generateTrie(String config, String[] keys, AbstractSensitive sensitive) {
        SensitiveNode nextNode = null;
        for (int index = 1; index < keys.length; index++) {
            String nodeKey = keys[index];
            if (nextNode == null) {
                nextNode = nodeMap.computeIfAbsent(nodeKey, key -> new PinyinNode());
                continue;
            }
            // 已包含叶子节点
            if (nextNode.containsKey(nodeKey)) {
                nextNode = nextNode.get(nodeKey);
                continue;
            }
            // 添加叶子节点
            PinyinNode newNode = new PinyinNode();
            nextNode.put(nodeKey, newNode);
            nextNode = newNode;
        }
        // 终结状态
        if (nextNode != null) {
            nextNode.config = config;
        }
    }

}
