package com.perfect.sensitive.matcher.senior;

import org.apache.commons.lang3.StringUtils;

import com.perfect.SensitiveWords;
import com.perfect.sensitive.matcher.AbstractMatcher;
import com.perfect.sensitive.node.PinyinNode;
import com.perfect.support.ApplyConfig;

/**
 * @Description 特殊字符过滤（不过滤数字和字母），匹配配置中的 敏感字&拼音 两字之间不超过一定间距
 *
 * @author Gaonan
 */
public class SeniorMatcher extends AbstractMatcher<SeniorSensitive, String> {

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
    public SeniorMatcher(String message, String[] pinyins, SeniorSensitive filter) {
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
        int loopTimes = this.calcLoop(pinyins, startIndex, ApplyConfig.SENIOR_INTERVAL);
        PinyinNode nextNode = null;
        for (int index = startIndex; index < loopTimes; index++) {
            nextNode = eachNode.get(pinyins[index]);
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
            loopTimes = this.calcLoop(pinyins, startIndex, ApplyConfig.SENIOR_INTERVAL);
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
