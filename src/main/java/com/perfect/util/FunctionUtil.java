package com.perfect.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.TypeUtils;


/**
 * @Description APP通用方法
 *
 * @author Gaonan
 */
public class FunctionUtil {

    /**
     * 初始化
     */
    public static void initialise(Class<?> clazz) throws Exception {
        Properties properties = ResourceUtil.getProperties("dirty-filter.properties");
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            int modifiers = field.getModifiers();
            boolean accord = Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers);
            if (!accord) {
                continue;
            }

            String key = StringUtils.lowerCase(field.getName());
            String value = properties.getProperty(key);
            if (StringUtils.isBlank(value)) {
                continue;
            }

            field.setAccessible(true);
            Field root = Field.class.getDeclaredField("root");
            root.setAccessible(true);
            Field declared = Field.class.getDeclaredField("modifiers");
            declared.setAccessible(true);

            declared.set(root, modifiers & ~Modifier.FINAL);
            declared.set(field, modifiers & ~Modifier.FINAL);

            // 反射赋值
            Class<?> type = field.getType();
            Object bean = null;
            if (type.isArray()) {
                bean = JSON.parseObject(value, type);
            } else {
                bean = TypeUtils.castToJavaBean(value, type);
            }
            // 设置访问标志
            field.set(null, bean);

            declared.set(root, modifiers | Modifier.FINAL);
            declared.set(field, modifiers | Modifier.FINAL);

            declared.setAccessible(false);
            root.setAccessible(false);
            field.setAccessible(false);
        }
    }

}
