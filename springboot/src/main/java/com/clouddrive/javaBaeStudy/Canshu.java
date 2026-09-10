package com.clouddrive.javaBaeStudy;

//不定长参数，js es6剩余参数

public class Canshu {
    public static void main(String[] args) {
        F f = new F();
        f.f1(1,2,3,4,5);
    }
}



class F{
    public void f1(int a, int ...args){
        System.out.println(a);
        for (int arg : args) {
            System.out.println(arg);
        }
    }
}