package com.perfect.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 汉字拼音整理
 */
public class OrderPinyin {

    private static String reader = "hanyu_pinyin.txt";

    private static String write = "hanyu_pinyin0.txt";

    public static void order() {
        try {

            BufferedWriter out = new BufferedWriter(new FileWriter(write));
            InputStream stream = ResourceUtil.getStream(reader);
            try (InputStreamReader input = new InputStreamReader(stream); BufferedReader reader = new BufferedReader(input)) {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    String[] array = line.split(",");
                    out.write(array[0]);
                    out.write("\n");
                }
            }
            out.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }


    public static void main(String[] args) {
		OrderPinyin.order();
    }

}
