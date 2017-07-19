package com.biz.util;

import java.util.HashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by johnzheng on 4/22/16.
 */
public final class Maps {
    public static <K, V> HashMap<K, V> newHashMap() {
        return new HashMap<K, V>();
    }
}
