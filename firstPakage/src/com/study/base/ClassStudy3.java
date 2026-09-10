package com.study.base;

public class ClassStudy3 {
    public static void main(String[] args) {
        Animals animal = new Cat("cat",1);
        animal.sayName();
        Cat cat = (Cat) animal;
        cat.catchMouse();
    }
}


class Animals{
    String name;
    int age;
    public Animals(String name, int age) {
        this.name = name;
        this.age = age;
    }
    public void sayName(){
        System.out.println("我是："+this.name+",年龄："+this.age);
    }
}

class Cat extends Animals{
    public Cat(String name, int age) {
        super(name, age);
    }
    void catchMouse(){
        System.out.println("catching!");
    }
}