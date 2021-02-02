package com.perfect.sensitive.node;

import com.perfect.sensitive.matcher.AbstractSensitive;

/**
 * @Description 字符节点
 *
 * @author Gaonan
 */
public class WordsNode extends SensitiveNode<Character, WordsNode> {

    public WordsNode() {
        super();
    }

    @Override
    public void generateTrie(String config, Character[] toChar, AbstractSensitive sensitive) {
        SensitiveNode nextNode = null;
        for (int index = 1; index < toChar.length; index++) {
            Character nodeKey = toChar[index];
            if (nextNode == null) {
                nextNode = nodeMap.computeIfAbsent(nodeKey, key -> new WordsNode());
                continue;
            }
            // 已包含叶子节点
            if (nextNode.containsKey(nodeKey)) {
                nextNode = nextNode.get(nodeKey);
                continue;
            }
            // 添加叶子节点
            WordsNode newNode = new WordsNode();
            nextNode.put(nodeKey, newNode);
            nextNode = newNode;
        }
        // 终结状态
        if (nextNode != null) {
            nextNode.config = config;
        }
    }

}