package com.clouddrive.common.study_demo;


import java.util.*;

public class ObjectArray{
    public static void main(String[] args){
        Test oa = new Test("song",18);
        System.out.println(oa);
        String a = oa.name;
        int b = oa.age;

        System.out.println(a);
        System.out.println(b);

        System.out.println("-------------");


        Map<String,Object> map = new HashMap<>();

        map.put("name","song");
        map.put("age",18);
        map.put("hobby", Arrays.asList("eat","sleep","coding"));
        List<String> list = new ArrayList<>();
        list.add("zhuangbi");
        map.put("other",list);

        System.out.println("----------for keySet()-------");
        for(String key : map.keySet()){
            System.out.println("key是：" + key);
        }

        System.out.println("---------for values---------");
        for (Object value : map.values()){
            System.out.println("value is:" + value);
        }

        System.out.println("---------for entry---------");

        for(Map.Entry<String,Object> entry: map.entrySet()){
            System.out.print(entry.getKey());
            System.out.print(':');
            System.out.print(entry.getValue());
            System.out.println(',');
        }

        System.out.println("---------for forEach---------");

        map.forEach((key,value)->{
            System.out.println(String.format("key:%s,value:%s",key,value));
        });

    }
}


class Test {
    int age;
    String name;
    public Test(String name,int age){
        this.name = name;
        this.age = age;
        System.out.println("constructor");
    }

    @Override
    public String toString() {
        return String.format("{name:%s,age:%d}",name,age);
    }
}

