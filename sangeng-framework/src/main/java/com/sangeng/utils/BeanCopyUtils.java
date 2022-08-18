package com.sangeng.utils;


import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BeanCopyUtils {

    private BeanCopyUtils(){};

    public static<T> T copyBean(Object source,Class<T> clazz){
        T t=null;

        try {
             t = clazz.newInstance();
            BeanUtils.copyProperties(source,t);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return t;

    }
    public static <O,V> List<V> copyBeanList(List<O> list,Class<V> clazz){
        return list.stream()
                .map(o -> copyBean(o, clazz))
                .collect(Collectors.toList());
    }
}
