package com.darian.darianLuceneVertx.utils;


import java.lang.reflect.Array;
import java.util.Objects;

/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a> 
 * @date 2020/4/13  1:22
 */
public class NumberUtils {

    public static int max(int... array) {
        int max = array[0];
        for (int j = 1; j < array.length; ++j) {
            if (array[j] > max) {
                max = array[j];
            }
        }

        return max;
    }


    public static int min(int... array) {
        validateArray(array);
        int min = array[0];

        for (int j = 1; j < array.length; ++j) {
            if (array[j] < min) {
                min = array[j];
            }
        }
        return min;
    }

    private static void validateArray(int... array) {
        AssertUtils.assertTrue(Objects.nonNull(array), "[CustomerNumberUtils.validateArray] The Array must not be null");
        AssertUtils.assertTrue(Array.getLength(array) != 0, "[CustomerNumberUtils.validateArray] Array cannot be empty.");

    }
}
