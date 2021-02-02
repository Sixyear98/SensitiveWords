package com.perfect.sensitive.matcher.medium;

import com.perfect.SensitiveWords;
import com.perfect.sensitive.matcher.AbstractMatcher;
import com.perfect.sensitive.node.WordsNode;
import com.perfect.support.ApplyConfig;

/**
 * @Description 特殊字符过滤（不过滤数字和字母），匹配配置中的 敏感字 两字之间不超过一定间距
 *
 * @author Gaonan
 */
public class MediumMatcher extends AbstractMatcher<MediumSensitive, Character> {

    /**
     * 遍历索引
     */
    private int eachIndex = 0;

    /**
     * 构造函数
     *
     * @param message
     * @param words
     * @param sensitive
     */
    public MediumMatcher(String message, Character[] words, MediumSensitive sensitive) {
        super(message, sensitive);
        this.words = words;
    }

    /**
     * 更新节点
     *
     * @param eachNode
     * @param startIndex
     * @return
     */
    private WordsNode updateNode(WordsNode eachNode, int startIndex) {
        int loopTimes = this.calcLoop(words, startIndex, ApplyConfig.MEDIUM_INTERVAL);
        for (int index = startIndex; index < loopTimes; index++) {
            Character eachWord = words[index];
            if (SensitiveWords.isInvalid(eachWord)) {
                continue;
            }
            WordsNode nextNode = eachNode.get(eachWord);
            if (nextNode == null) {
                continue;
            }
            if (nextNode.isTerminal()) {
                return nextNode;
            }
            // 更新数据
            nextNode = this.updateNode(nextNode, index + 1);
            if (nextNode != null) {
                return nextNode;
            }
            loopTimes = this.calcLoop(words, startIndex, ApplyConfig.MEDIUM_INTERVAL);
        }
        return null;
    }

    @Override
    public boolean contains(Character word) {
        eachIndex = eachIndex + 1;
        if (SensitiveWords.isInvalid(word)) {
            return false;
        }

        WordsNode eachNode = sensitive.getWords(word);
        if (eachNode == null) {
            return false;
        }

        eachNode = this.updateNode(eachNode, eachIndex);
        return eachNode == null ? false : true;
    }

    @Override
    public String find(Character word) {
        eachIndex = eachIndex + 1;
        if (SensitiveWords.isInvalid(word)) {
            return null;
        }

        WordsNode eachNode = sensitive.getWords(word);
        if (eachNode == null) {
            return null;
        }

        eachNode = this.updateNode(eachNode, eachIndex);
        return eachNode == null ? null : eachNode.config;
    }

}
