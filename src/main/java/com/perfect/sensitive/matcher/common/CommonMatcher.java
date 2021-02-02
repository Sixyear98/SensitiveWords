package com.perfect.sensitive.matcher.common;

import com.perfect.SensitiveWords;
import com.perfect.sensitive.matcher.AbstractMatcher;
import com.perfect.sensitive.node.WordsNode;

/**
 * @Description 特殊字符过滤（不过滤数字和字母），匹配配置中的敏感字
 *
 * @author Gaonan
 */
public class CommonMatcher extends AbstractMatcher<CommonSensitive, Character> {

    /**
     * 匹配节点
     */
    private WordsNode eachNode = null;

    /**
     * 构造函数
     *
     * @param message
     * @param sensitive
     */
    public CommonMatcher(String message, CommonSensitive sensitive) {
        super(message, sensitive);
    }

    /**
     * 更新节点
     *
     * @param node
     * @param word
     * @return
     */
    private WordsNode updateNode(WordsNode node, Character word) {
        if (node == null) {
            return sensitive.getWords(word);
        }
        node = node.get(word);
        if (node == null) {
            return sensitive.getWords(word);
        }
        return node;
    }

    @Override
    public boolean contains(Character word) {
        // 过滤特殊字符
        if (SensitiveWords.isInvalid(word)) {
            return false;
        }
        eachNode = this.updateNode(eachNode, word);
        // 是否是结束节点
        if (eachNode != null && eachNode.isTerminal()) {
            return true;
        }
        return false;
    }

    @Override
    public String find(Character word) {
        // 过滤特殊字符
        if (SensitiveWords.isInvalid(word)) {
            return null;
        }
        eachNode = this.updateNode(eachNode, word);
        // 是否是结束节点
        if (eachNode != null && eachNode.isTerminal()) {
            return eachNode.config;
        }
        return null;
    }

}
