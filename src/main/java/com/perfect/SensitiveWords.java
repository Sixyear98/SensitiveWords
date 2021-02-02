package com.perfect;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;

import com.perfect.pinyin.ChinesePinyin;
import com.perfect.sensitive.SensitiveInit;
import com.perfect.sensitive.matcher.AbstractMatcher;
import com.perfect.sensitive.matcher.AbstractSensitive;
import com.perfect.support.ApplyConfig;
import com.perfect.util.ResourceUtil;

/**
 * @Description 文字过滤工具 将一些限制性文字进行过滤
 *
 * @author Gaonan
 */
public class SensitiveWords {

	/**
	 * 小写字母‘a’值
	 */
	private static final int LOWER_A = 'a';

	/**
	 * 小写字母‘z’值
	 */
	private static final int LOWER_Z = 'z';

	/**
	 * 大写字母‘A’值
	 */
	private static final int UPPER_A = 'A';

	/**
	 * 大写字母‘Z’值
	 */
	private static final int UPPER_Z = 'Z';

	/**
	 * 数字值
	 */
	private static final int[] NUMBER = new int[]{'0', '9'};

	/**
	 * 词根
	 */
	private static final int[] EXTENSION = new int[]{'㐀', '䶵'};
	private static final int[] UNIFIED = new int[]{'一', '龥'};
	private static final int[] RADICALS = new int[]{'⺀', '⻳'};

	/**
	 * 过滤时要被替换的字符
	 */
	public static final char REPLACE_CHAR = '*';

	/**
	 * 初始化资源
	 *
	 * @throws Exception
	 */
	public static void initialise() throws Exception {
		// 加载拼音资源
		ChinesePinyin.initialise();

		for (String config : ApplyConfig.CONFIG) {
			String PATH = ApplyConfig.DICT_PATH + File.separator + config;
			File file = ResourceUtil.getFile(PATH);
			try (InputStream stream = new FileInputStream(file)) {
				initialize(stream, file.getName());
			}
		}
	}

	/**
	 * 加载数据
	 *
	 * @param stream
	 * @param fileName
	 * @throws Exception
	 */
	private static void initialize(InputStream stream, String fileName) throws Exception {
		try (InputStreamReader input = new InputStreamReader(stream); BufferedReader reader = new BufferedReader(input)) {
			Set<String> lineWords = new LinkedHashSet();
			String line = null;
			while ((line = reader.readLine()) != null) {
				lineWords.add(line);
			}

			SensitiveInit enumInit = SensitiveInit.getInit(fileName);
			AbstractSensitive sensitive = enumInit.getSensitive();
			sensitive.initialize(lineWords);
		}
	}

	/**
	 * 重新加载敏感字
	 *
	 * @param file
	 * @throws Exception
	 */
	public static void reload(File file) throws Exception {
		for (SensitiveInit init : SensitiveInit.values()) {
			AbstractSensitive sensitive = init.getSensitive();
			sensitive.clear();
		}
		initialise();
	}

	/**
	 * 是否包含敏感字
	 *
	 * @param message
	 * return 0：不包含 其他值 {@link SensitiveInit#getSensitive()}
	 */
	public static int contains(String message) {
		if (StringUtils.isBlank(message)) {
			return 0;
		}

		Character[] words = SensitiveWords.messageChars(message);
		int type = SensitiveWords.containsWords(message, words);
		if (type != 0) {
			return type;
		}

		String[] pinyins = SensitiveWords.messagePinyin(words);
		type = SensitiveWords.containsPinyin(message, pinyins, SensitiveInit.checkPinyin);
		if (type != 0) {
			return type;
		}

		pinyins = SensitiveWords.orderPinyin(pinyins);
		type = SensitiveWords.containsPinyin(message, pinyins, SensitiveInit.checkPolity);
		if (type != 0) {
			return type;
		}
		return 0;
	}

	private static int containsWords(String message, Character[] words) {
		for (AbstractSensitive sensitive : SensitiveInit.checkWords) {
			AbstractMatcher matcher = sensitive.matcher(message, words);
			for (Character word : words) {
				if (matcher.contains(word)) {
					return sensitive.getType();
				}
			}
		}
		return 0;
	}

	private static int containsPinyin(String message, String[] pinyins, List<AbstractSensitive> checks) {
		for (AbstractSensitive sensitive : checks) {
			AbstractMatcher matcher = sensitive.matcher(message, pinyins);
			for (String pinyin : pinyins) {
				if (matcher.contains(pinyin)) {
					return sensitive.getType();
				}
			}
		}
		return 0;
	}

	/**
	 * 查找敏感字---效率不高慎用
	 *
	 * @param message
	 * return
	 */
	public static String find(String message) {
		if (StringUtils.isBlank(message)) {
			return "";
		}

		StringBuilder builder = new StringBuilder();
		Character[] words = SensitiveWords.messageChars(message);
		StringBuilder result = SensitiveWords.findWords(message, words);
		if (result != null && result.length() > 0) {
			builder.append(result);
		}
		String[] pinyins = SensitiveWords.messagePinyin(words);
		result = SensitiveWords.findPinyin(message, pinyins, SensitiveInit.checkPinyin);
		if (result != null && result.length() > 0) {
			builder.append(result);
		}
		pinyins = SensitiveWords.orderPinyin(pinyins);
		result = SensitiveWords.findPinyin(message, pinyins, SensitiveInit.checkPolity);
		if (result != null && result.length() > 0) {
			builder.append(result);
		}
		return builder.toString();
	}

	private static StringBuilder findWords(String message, Character[] words) {
		StringBuilder builder = new StringBuilder();
		for (AbstractSensitive sensitive : SensitiveInit.checkWords) {
			AbstractMatcher matcher = sensitive.matcher(message, words);
			for (Character word : words) {
				String result = matcher.find(word);
				if (StringUtils.isEmpty(result)) {
					continue;
				}
				builder.append(result).append("|");
			}
		}
		return builder;
	}

	private static StringBuilder findPinyin(String message, String[] pinyins, List<AbstractSensitive> checks) {
		StringBuilder builder = new StringBuilder();
		for (AbstractSensitive sensitive : checks) {
			AbstractMatcher matcher = sensitive.matcher(message, pinyins);
			for (String pinyin : pinyins) {
				String result = matcher.find(pinyin);
				if (StringUtils.isEmpty(result)) {
					continue;
				}
				builder.append(result).append("|");
			}
		}
		return builder;
	}

	/**
	 * 过滤覆写应过滤掉的字符---效率不高慎用
	 *
	 * @param message
	 * return
	 */
	public static String filter(String message) {
		if (StringUtils.isBlank(message)) {
			return message;
		}
		String result = SensitiveWords.find(message);
		if (StringUtils.isEmpty(result)) {
			return message;
		}
		char[] toChar = result.toCharArray();
		for (char value : toChar) {
			message = StringUtils.replaceChars(message, value, SensitiveWords.REPLACE_CHAR);
		}
		return message;
	}

	/**
	 * 字符串转换字符集
	 *
	 * @param message
	 * @return
	 */
	public static Character[] messageChars(String message) {
		char[] toChar = message.toCharArray();
		Character[] words = new Character[toChar.length];
		int index = 0;
		for (char eachValue : toChar) {
			if (SensitiveWords.upperCharacter(eachValue)) {
				words[index] = Character.toLowerCase(eachValue);
			} else {
				words[index] = eachValue;
			}
			index++;
		}
		return words;
	}

	/**
	 * 字符串转换字符集
	 *
	 * @param words
	 * @return
	 */
	public static String[] messagePinyin(Character[] words) {
		List<String> result = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        for (Character element : words) {
        	if (SensitiveWords.numberCharacter(element)) {
				continue;
			}
            if (SensitiveWords.lowerCharacter(element)) {
                builder.append(element);
                continue;
            }
			if (builder.length() > 0) {
				String analyze = builder.toString();
				SensitiveWords.analyzePinyin(analyze, result);
				builder.setLength(0);
			}
            if (SensitiveWords.chineseCharacter(element)) {
                String pinyin = ChinesePinyin.singlePinyin(element);
                result.add(pinyin);
            }
        }
		if (builder.length() > 0) {
			String analyze = builder.toString();
			SensitiveWords.analyzePinyin(analyze, result);
			builder.setLength(0);
		}
        String[] pinyins = new String[result.size()];
        return result.toArray(pinyins);
	}

	private static void analyzePinyin(String analyze, List<String> result) {
		List<String> eachGroup = new ArrayList<>();
		result.add(analyze);
		for (int index = 0; index < analyze.length(); index++) {
			for (int loop = index + 2; loop <= analyze.length(); loop++) {
				String pinyin = StringUtils.substring(analyze, index, loop);
				if ((!eachGroup.contains(pinyin)) && ChinesePinyin.containsPinyin(pinyin)) {
					result.add(pinyin);
					eachGroup.add(pinyin);
				}
			}
		}
	}

	/**
	 * 是否是大写字母
	 *
	 * @param eachWord
	 * @return
	 */
	public static boolean upperCharacter(char eachWord) {
		return eachWord >= SensitiveWords.UPPER_A && eachWord <= SensitiveWords.UPPER_Z;
	}

	/**
	 * 是否是小写字母
	 *
	 * @param eachWord
	 * @return
	 */
	public static boolean lowerCharacter(Character eachWord) {
		return eachWord >= SensitiveWords.LOWER_A && eachWord <= SensitiveWords.LOWER_Z;
	}

	/**
	 * 是否是数字字符
	 *
	 * @param eachWord
	 * @return
	 */
	public static boolean numberCharacter(Character eachWord) {
		return eachWord >= SensitiveWords.NUMBER[0] && eachWord <= SensitiveWords.NUMBER[1];
	}

	/**
	 * 是否是大写写字母
	 *
	 * @param eachWord
	 * @return
	 */
	public static boolean chineseCharacter(Character eachWord) {
		if (eachWord >= RADICALS[0] && eachWord <= RADICALS[1]) {
			return true;
		}
		if (eachWord >= EXTENSION[0] && eachWord <= EXTENSION[1]) {
			return true;
		}
		if (eachWord >= UNIFIED[0] && eachWord <= UNIFIED[1]) {
			return true;
		}
		return false;
	}

	/**
	 * 字符无效
	 *
	 * @param eachWord
	 * @return
	 */
	public static boolean isInvalid(Character eachWord) {
		if (SensitiveWords.lowerCharacter(eachWord)) {
			return false;
		}
		if (SensitiveWords.numberCharacter(eachWord)) {
			return false;
		}
		if (SensitiveWords.chineseCharacter(eachWord)) {
			return false;
		}
		return true;
	}

	/**
	 * 字符拼音排序
	 *
	 * @param words
	 * @return
	 */
	public static String[] orderPinyin(String[] words) {
		Set<String> wordSet = new TreeSet<>();
		for (String eachWord : words) {
			wordSet.add(eachWord);
		}
		String[] result = new String[wordSet.size()];
		int index = 0;
		for (String value : wordSet) {
			result[index] = value;
			index ++;
		}
		return result;
	}

}
