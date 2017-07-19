
package com.biz.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;


public final class Lists {
    private Lists() {
    }


    public static <E> ArrayList<E> newArrayList() {
        return new ArrayList<E>();
    }


    public static <E> ArrayList<E> newArrayList(E... elements) {

        int capacity = computeArrayListCapacity(elements.length);
        ArrayList<E> list = new ArrayList<E>(capacity);
        Collections.addAll(list, elements);
        return list;
    }

    public static int getLength(final Object array) {
        if (array == null) {
            return 0;
        }
        return Array.getLength(array);
    }
    private static int computeArrayListCapacity(int arraySize) {

        return saturatedCast(5L + arraySize + (arraySize / 10));
    }

    private static int saturatedCast(long value) {
        if (value > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        if (value < Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        }
        return (int) value;
    }

}
