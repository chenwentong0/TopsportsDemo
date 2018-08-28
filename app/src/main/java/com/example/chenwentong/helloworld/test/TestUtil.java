package com.example.chenwentong.helloworld.test;

import java.util.ArrayList;
import java.util.List;

/**
 * Date 2018/8/27
 * Time 11:59
 *
 * @author wentong.chen
 */
public class TestUtil {

    public static List<String> getStringList(int length) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            list.add("test data item = " + i);
        }
        return list;
    }
}
