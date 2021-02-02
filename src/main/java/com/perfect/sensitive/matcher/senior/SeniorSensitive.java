package com.perfect.sensitive.matcher.senior;

import java.util.Map;
import java.util.TreeMap;

import com.perfect.SensitiveWords;
import com.perfect.pinyin.ChinesePinyin;
import com.perfect.sensitive.matcher.AbstractMatcher;
import com.perfect.sensitive.matcher.AbstractSensitive;
import com.perfect.sensitive.node.PinyinNode;

/**
 * @Description 特殊字符过滤，匹配配置中的 敏感字&拼音 两字之间不超过一定间距
 *
 * @author Gaonan
 */
public class SeniorSensitive extends AbstractSensitive<String> {

	/** 敏感字拼音节点，保存小写字母 */
	private Map<String, PinyinNode> rootPinyin = new TreeMap<>();


	public SeniorSensitive(int type) {
		super(type);
	}

	@Override
	public void appendWords(String words) {
		Character[] toChar = SensitiveWords.messageChars(words);
		String[] pinyins = SensitiveWords.messagePinyin(toChar);
		// 单词跟节点
		String rootKey = pinyins[0];
		PinyinNode rootNode = rootPinyin.computeIfAbsent(rootKey, key -> new PinyinNode());
		if (words.length() == 1) {
			rootNode.config = words;
			return;
		}
		rootNode.generateTrie(words, pinyins, this);
	}

	@Override
	public AbstractMatcher matcher(String message, String[] pinyins) {
		AbstractMatcher matcher  = new SeniorMatcher(message, pinyins, this);
		return matcher;
	}

	@Override
	public void clear() {
		rootPinyin.clear();
	}

	/**
	 * 获取字符拼音节点
	 *
	 * @param key
	 * @return
	 */
	public PinyinNode getPinyin(String key) {
		return rootPinyin.get(key);
	}

}