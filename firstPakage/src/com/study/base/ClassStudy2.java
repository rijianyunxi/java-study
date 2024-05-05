package com.study.base;

/**
 * p无论传给谁了，也仅仅是将值赋予给了参数p ，p再赋值不影响原来空间的p，除非直接用p修改
 *
 *
 * */
public class ClassStudy2 {
    public static void main(String[] args){
        Person p1 = new Person("sjt",11,21.1);
        Person p2 = new Person("p2",11,21.1);
        p1.calc(1);
        // 访问Person的静态属性 共享
        System.out.println(p2.count);
        System.out.println(Person.count);
        // 访问Person的静态方法 静态方法里不能使用class的属性和方法
        System.out.println(p2.staticMethods());
        System.out.println(Person.staticMethods());
//        int num = p1.eatAllApple(1);
//        System.out.println(num);
//        p1 = p2;
//        p1.info();
//        p1.changeInfo(p1);
//        System.out.println("-----");
//        p1.info();
//        Change change = new Change();
//        change.c(p1);
//        System.out.println("-----");
//        p1.info();

    }

    public void mainOther(){
        System.out.println("main other methods");
    }

}

class Person{
    String name;
    int age;
    double source;
    protected static int count = 999;

    public Person(String name, int age, double source){
        this.name = name;
        this.age = age;
        this.source = source;
    }

    public int eatAllApple(int day){
        if(day == 10) return 1;
        return (eatAllApple(day+1) + 1)*2;
    }
    public void info(){
        System.out.println("info:"+this.name);
    }
    public void changeInfo(Person p){
        p.name = "changeInfoP";
    }
    public void calc(int num){
        while(true){
            System.out.println(num);
            num ++;
            if(num > 2) return;

        }
    }
//    静态方法，不实例也可使用
    public static String staticMethods(){
//        eatAllApple(1); 不能放问非静态属性和方法
        staticMethods2();//静态的方法和属性可以
        T t = new T();

        t.say(); // 访问其他class的静态属性可以
        return "staticMethods";
    }
    public static String staticMethods2(){
        return "staticMethods2";
    }
}
class T{
    public void say(){
        System.out.println("T say");
    }
}
class Change{
    public void c(Person p){
//        p.name = "change by c";
        p = null;
        log();
    }

    public void log(){
        System.out.println("+++66+++");
    }
}
