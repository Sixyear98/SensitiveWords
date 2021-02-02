package com.perfect.sensitive.matcher.polity;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.perfect.SensitiveWords;
import com.perfect.sensitive.matcher.AbstractMatcher;
import com.perfect.sensitive.matcher.AbstractSensitive;
import com.perfect.sensitive.node.PinyinNode;

/**
 * 涉政敏感词---只要包含某个敏感字段，就过滤
 */
public class PolitySensitive extends AbstractSensitive<String> {

	/** 敏感字节点 */
	private Map<String, PinyinNode> rootPinyin = new TreeMap<>();


	public PolitySensitive(int type) {
		super(type);
	}

	@Override
	public void appendWords(String words) {
		Character[] toChar = SensitiveWords.messageChars(words);
		String[] pinyins = SensitiveWords.messagePinyin(toChar);
		pinyins = this.orderPinyins(pinyins);
		// 单词跟节点
		String rootKey = pinyins[0];
		PinyinNode rootNode = rootPinyin.computeIfAbsent(rootKey, key -> new PinyinNode());
		if (pinyins.length == 1) {
			rootNode.config = words;
			return;
		}
		rootNode.generateTrie(words, pinyins, this);
	}

	@Override
	public AbstractMatcher matcher(String message, String[] pinyins) {
		AbstractMatcher matcher  = new PolityMatcher(message, pinyins, this);
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

	/**
	 * 字符拼音排序
	 *
	 * @param pinyins
	 * @return
	 */
	public String[] orderPinyins(String[] pinyins) {
		Set<String> pinyinSet = new TreeSet<>();
		for (String eachValue : pinyins) {
			pinyinSet.add(eachValue);
		}

		String[] result = new String[pinyinSet.size()];
		int index = 0;
		for (String value : pinyinSet) {
			result[index] = value;
			index ++;
		}
		return result;
	}

}