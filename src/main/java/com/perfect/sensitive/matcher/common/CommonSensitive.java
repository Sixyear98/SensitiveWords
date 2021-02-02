package com.perfect.sensitive.matcher.common;

import java.util.Map;
import java.util.TreeMap;

import com.perfect.SensitiveWords;
import com.perfect.sensitive.matcher.AbstractMatcher;
import com.perfect.sensitive.matcher.AbstractSensitive;
import com.perfect.sensitive.node.WordsNode;

/**
 * @Description 只过滤配置中的 敏感字
 *
 * @author Gaonan
 */
public class CommonSensitive extends AbstractSensitive<Character> {

	/** 字节点 */
	private Map<Character, WordsNode> rootWords = new TreeMap<>();


	public CommonSensitive(int type) {
		super(type);
	}

	@Override
	public void appendWords(String words) {
		// 单词跟节点
		Character[] toChar = SensitiveWords.messageChars(words);
		Character rootKey = toChar[0];
		WordsNode rootNode = rootWords.computeIfAbsent(rootKey, key -> new WordsNode());
		if (words.length() == 1) {
			rootNode.config = words;
			return;
		}
		rootNode.generateTrie(words, toChar, this);
	}

	@Override
	public AbstractMatcher matcher(String message, Character[] words) {
		AbstractMatcher matcher = new CommonMatcher(message, this);
		return matcher;
	}

	@Override
	public void clear() {
		rootWords.clear();
	}

	/**
	 * 获取字符节点
	 *
	 * @param key
	 * @return
	 */
	public WordsNode getWords(Character key) {
		return rootWords.get(key);
	}

}