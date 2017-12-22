/*
 *
 */

package me.melvins.labs.utils;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Bean Utility Functions.
 *
 * @author Mels
 */
public final class BeanUtils {

    /**
     * Privatized Default Constructor To Avoid Object Instantiation.
     */
    private BeanUtils() {
    }

    /**
     * Fill Bean with appropirate Key-Value pairs.
     *
     * @param headers Key-Value pair to be formed into a POJO.
     * @param clazz   The POJO to populate the Key-Value pairs.
     * @return The POJO populated with coressponding Keys in the Map.
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static Object fillBean(Map<String, Object> headers, Class clazz)
            throws InstantiationException, IllegalAccessException {

        Object obj = clazz.newInstance();

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);

            for (Map.Entry<String, Object> entry : headers.entrySet()) {
                if (field.getName().equalsIgnoreCase(entry.getKey())) {
                    field.set(obj, entry.getValue());
                }
            }
        }

        return obj;
    }

}
