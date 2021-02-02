package com.perfect.sensitive.matcher.polity;

import com.perfect.sensitive.matcher.AbstractMatcher;
import com.perfect.sensitive.node.PinyinNode;

/**
 * @Description 涉政敏感词---特殊字符过滤（不过滤数字和字母），只要包含某个 敏感字段&拼音 就过滤
 *
 * @author Gaonan
 */
public class PolityMatcher extends AbstractMatcher<PolitySensitive, String> {

    /**
     * 遍历索引
     */
    private int eachIndex = 0;

    /**
     * 构造函数
     *
     * @param message
     * @param pinyins
     * @param filter
     */
    public PolityMatcher(String message, String[] pinyins, PolitySensitive filter) {
        super(message, filter);
        this.pinyins = pinyins;
    }

    /**
     * 更新节点
     *
     * @param eachNode
     * @param startIndex
     * @return
     */
    private PinyinNode updateNode(PinyinNode eachNode, int startIndex) {
        for (int index = startIndex; index < pinyins.length; index++) {
            PinyinNode nextNode = eachNode.get(pinyins[index]);
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
        }
        return null;
    }

    @Override
    public boolean contains(String pinyin) {
        eachIndex = eachIndex + 1;
        PinyinNode eachNode = sensitive.getPinyin(pinyin);
        if (eachNode == null) {
            return false;
        }

        eachNode = this.updateNode(eachNode, eachIndex);
        return eachNode == null ? false : true;
    }

    @Override
    public String find(String pinyin) {
        eachIndex = eachIndex + 1;
        PinyinNode eachNode = sensitive.getPinyin(pinyin);
        if (eachNode == null) {
            return null;
        }

        eachNode = this.updateNode(eachNode, eachIndex);
        return eachNode == null ? null : eachNode.config;
    }

}
