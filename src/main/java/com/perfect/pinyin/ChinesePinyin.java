package com.perfect.pinyin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;

import com.perfect.support.ApplyConfig;
import com.perfect.util.ResourceUtil;

/**
 * 拼音资源
 */
public class ChinesePinyin {

    /**
     * 汉字编码->拼音字典
     */
    private static Map<Integer, String> chineseTrie = new TreeMap<>();

    /**
     * 拼音字典
     */
    private static Set<String> pinyinTrie = new TreeSet<>();

    /**
     * KEY最小值
     */
    public static int MIN_VALUE = Integer.MAX_VALUE;

    /**
     * KEY最大值
     */
    public static int MAX_VALUE = 0;


    private ChinesePinyin() {
    }

    /**
     * 初始化资源
     *
     * @throws Exception
     */
    public static void initialise() throws Exception {
        File file = ResourceUtil.getFile(ApplyConfig.HANYU_PINYIN);
        try (InputStream stream = new FileInputStream(file)) {
            ChinesePinyin.initialise(stream);
        }
    }

    /**
     * 加载拼音
     *
     * @param stream 拼音文件输入流
     * @throws Exception
     */
    private static void initialise(InputStream stream) throws Exception {
        try (InputStreamReader input = new InputStreamReader(stream); BufferedReader reader = new BufferedReader(input)) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] array = line.split(" ");
                if (array.length != 2) {
                    continue;
                }
                Integer key = Integer.valueOf(array[0], 16);

                chineseTrie.put(key, array[1]);
                pinyinTrie.add(array[1]);

                MIN_VALUE = key < MIN_VALUE ? key : MIN_VALUE;
                MAX_VALUE = key > MAX_VALUE ? key : MAX_VALUE;
            }
        }
    }

    /**
     * 重新加载拼音
     *
     * @param file 拼音文件输入流
     * @throws Exception
     */
    public static void reload(File file) throws Exception {
        initialise();
    }

    /**
     * 是否包含拼音
     *
     * @param pinyin
     * @return
     */
    public static boolean containsPinyin(String pinyin) {
        return pinyinTrie.contains(pinyin);
    }

    /**
     * 汉语拼音
     *
     * @param chinese
     * @return
     */
    public static String singlePinyin(char chinese) {
        int trieKey = chinese;
        if (trieKey < MIN_VALUE || trieKey > MAX_VALUE) {
            return Character.toString(chinese);
        }
        String pinyin = chineseTrie.get(trieKey);
        if (StringUtils.isBlank(pinyin)) {
            return Character.toString(chinese);
        }
        return pinyin;
    }

    /**
     * 汉语拼音
     *
     * @param words
     * @param separate
     * @return
     */
    public static String wordsPinyin(String words, String separate) {
        if (StringUtils.isEmpty(words)) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        char[] toChar = words.toCharArray();
        boolean isAppend = false;
        for (int index = 0; index < toChar.length; index ++) {
            int trieKey = toChar[index];
            if (trieKey < MIN_VALUE || trieKey > MAX_VALUE) {
                if (isAppend) {
                    builder.append(separate);
                    isAppend = false;
                }
                continue;
            }
            // 查找拼音
            String pinyin = chineseTrie.get(trieKey);
            if (pinyin == null) {
                continue;
            }
            if (index != 0) {
                builder.append(separate);
            }
            builder.append(pinyin);
            isAppend = true;
        }

        return builder.toString();
    }

}
