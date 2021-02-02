package com.perfect.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.LinkedHashSet;
import java.util.Set;

import com.perfect.support.ApplyConfig;
import com.perfect.support.OperatingSystem;

/**
 * 敏感字整理
 */
public class OrderWords {

    public static void order() {
        try {
            for (String config : ApplyConfig.CONFIG) {
                String PATH = ApplyConfig.DICT_PATH + File.separator + config;
                File file = ResourceUtil.getFile(PATH);
                try (InputStream stream = new FileInputStream(file)) {
                    OrderWords.order(stream, file.getName());
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * 加载数据
     *
     * @param stream
     * @throws Exception
     */
    private static void order(InputStream stream, String name) throws Exception {
        Set<String> lineWords = new LinkedHashSet();
        try (InputStreamReader input = new InputStreamReader(stream); BufferedReader reader = new BufferedReader(input)) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                for (String words : lineWords) {
                    if (words.length() > line.length()) {
                        if (words.contains(line)) {
                            System.out.println(words + "------" + line);
                        }
                        continue;
                    }
                    if (line.contains(words)) {
                        System.out.println(line + "------" + words);
                    }
                }
                lineWords.add(line);
            }
        }

        try (FileOutputStream output = new FileOutputStream(name);
             OutputStreamWriter writer = new OutputStreamWriter(output);
             BufferedWriter buffer = new BufferedWriter(writer)) {
            for (String words : lineWords) {
                if (words.length() <= 1) {
                    System.out.println(words);
                    continue;
                }
                buffer.write(words);
                buffer.newLine();
            }
            buffer.flush();
        }

    }


    public static void main(String[] args) throws Exception {

        OperatingSystem.initialise();
        FunctionUtil.initialise(ApplyConfig.class);

        OrderWords.order();
    }

}
