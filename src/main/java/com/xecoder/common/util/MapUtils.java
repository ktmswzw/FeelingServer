package com.xecoder.common.util;

import java.util.Map;

/**
 * Created by  moxz
 * User: 224911261@qq.com
 * 2016/1/21-20:04
 * Feeling.com.xecoder.common.util
 */
public class MapUtils extends org.apache.commons.collections.MapUtils {

/**
 * source中与target不重复部分的的值加入到target
 *
 * @param target The target map where to put new entries.
 * @param source The source map from which read the entries.
 */
public static <K, V> void putAllIfAbsent(Map<K, V> target, Map<K, V> source) {

        for (Map.Entry<K, V> entry : source.entrySet()) {
        if (!target.containsKey(entry.getKey())) {
        target.put(entry.getKey(), entry.getValue());
        }
        }
        }
        }
