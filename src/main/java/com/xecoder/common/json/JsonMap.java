package com.xecoder.common.json;
import java.util.HashMap;

/**
 * Created by  moxz
 * User: 224911261@qq.com
 * 2015/10/20-9:11
 * HabitServer.com.imakehabits.commons.json
 */
public class JsonMap<K, V> extends HashMap<K, V> {

    public V put(K key, V value) {
        if (key == null || value == null || value.equals("") || key.equals(""))
            return null;
        return super.put(key, value);
    }
}
