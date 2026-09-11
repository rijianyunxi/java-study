package com.clouddrive.javaBaeStudy.duotai;

public class Dog extends Animal {

    public Dog(String name, int age) {
        super(name, age);
    }

    @Override
    public void feed(Food food) {
        System.out.printf("姓名：%s，年龄：%d，正在吃%s%n", name, age, food.name);
    }

    // Dog 专有的方法，Animal 中没有声明。
    public void watchHouse() {
        System.out.println(name + "正在看家");
    }
}
