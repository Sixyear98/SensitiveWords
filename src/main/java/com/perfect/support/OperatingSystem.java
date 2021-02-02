package com.perfect.support;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * @Description 操作系统相关
 *
 * @author Gaonan
 */
public class OperatingSystem {

    private static Map<String, String> propertyMap = new HashMap<>();

    static {
        /* 设置系统属性 */
        System.setProperty("process", "SensitiveWords");
        System.setProperty("loggerFile", "sensitive_words");
    }

    /**
     * Operating System Name
     */
    public static final String OS_NAME = System.getProperty("os.name");

    private OperatingSystem() {
    }

    /**
     * 初始化
     */
    public static void initialise() {
        String loggerFile = System.getProperty("loggerFile");
        if (OperatingSystem.isWindows()) {
            System.setProperty("directory", "./logger/" + loggerFile);
            return;
        }
        System.setProperty("directory", "/" + loggerFile + "/export/");
    }

    /**
     * 获取工作路径
     *
     * @return
     */
    public static String workPath() {
        return System.getProperty("user.dir");
    }

    /**
     * 是否是 Windows 系统
     *
     * @return
     */
    public static boolean isWindows() {
        return StringUtils.containsIgnoreCase(OS_NAME, "Windows");
    }

    /**
     * 是否是 Linux 系统
     *
     * @return
     */
    public static boolean isLinux() {
        return StringUtils.containsIgnoreCase(OS_NAME, "Linux");
    }

}
