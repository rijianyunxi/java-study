package com.study.base;

public class Interface1 {
    public static void main(String[] args) {
        A a = new A();
        a.hi();
        System.out.println(a.count);
        System.out.println(a.name);
    }
}
interface AInterface{
    public int count = 0;
    public String name = "sjt";

    public void hi();
    public void gun();

    // jdk8以后接口中可以有实现的方法，但是需要用default修饰
    default public void ok(){
        System.out.println("ok");
    }
    // jdk8以后接口中可以有实现的静态方法，
    public static void cry(){
        System.out.println("cry");
    }
}

// 继承接口和抽象类一样，需要重写所有抽象方法
class A implements AInterface{
    @Override
    public void hi() {
        System.out.println("hi");
    }

    @Override
    public void gun() {
        System.out.println("gun");
    }
}