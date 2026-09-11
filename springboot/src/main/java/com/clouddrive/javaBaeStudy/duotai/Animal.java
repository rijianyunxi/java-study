package com.clouddrive.javaBaeStudy.duotai;

public class Animal {

    String name;
    int age;
    public Animal(String name,int age){
        this.name = name;
        this.age = age;
    }

    public void feed(Food food){
        System.out.println("Animal class common feed...");
    }
}
