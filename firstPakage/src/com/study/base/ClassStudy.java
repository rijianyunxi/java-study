package com.study.base;

public class ClassStudy {
    public static void main(String[] args) {
        J k = new J("宋金涛",25);
        k.say();

        Animal cat = new Animal();
        cat.name="hua~";
        cat.say("miao miao");
        int res = cat.calc(10,100);
        System.out.println(res);
    }
}



class Animal{
    String name;
    double age;

    public void say(String word){
        System.out.println(this.name+"说"+word);
    }
    public int calc(int m , int n){
        return m + n;
    }
}

class J{
    String name;
    int age;
    //    public J(){}
    public  J(String name,int age){
        this.name  = name;
        this.age = age;
    }

    public void say(){
        System.out.println(this.name+"====say");
    }
}