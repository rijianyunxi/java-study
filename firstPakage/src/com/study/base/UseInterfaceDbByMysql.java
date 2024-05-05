package com.study.base;
//import com.study.base.InterfaceDb;


public class UseInterfaceDbByMysql {
    public static void main(String[] args) {
        Mysql m  =new Mysql();
        m.connect();
        m.close();
    }
}

// interface 可以被class implements多个，抽象类只能被class继承一个
// 接口可以继承别的接口
class Mysql implements InterfaceDb,AUrl,Bi{
    @Override
    public void connect() {
        System.out.println("mysql connect");
    }

    @Override
    public void close() {
        System.out.println("mysql close");
    }

    @Override
    public void configUrl() {
        System.out.println("config url");
    }

    @Override
    public void b() {

    }

    @Override
    public void a() {

    }
}

interface AUrl{
    void configUrl();
}

interface Ai{
    void a();
}

interface  Bi extends Ai{
    void b();
}