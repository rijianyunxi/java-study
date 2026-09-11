package com.clouddrive.javaBaeStudy.homeWork;

/**
 * Java 访问控制修饰符：
 * 访问级别    修饰符          同类    同包    子类    不同包
 * --------------------------------------------------------
 * 公开        public          √       √       √       √
 * 受保护      protected       √       √       √       ✕
 * 默认        无修饰符         √       √       ✕       ✕
 * 私有        private         √       ✕       ✕       ✕
 */



public class PublicPrivateProtectedTest {

    protected void sayHi(String str){
        System.out.println("PublicPrivateProtectedTest " + str);
    }
    void sayHi2(String str){
        System.out.println("PublicPrivateProtectedTest " + str);
    }
    public void sayHi3(String str){
        System.out.println("PublicPrivateProtectedTest " + str);
    }

    private void sayHi4(String str){
        System.out.println("PublicPrivateProtectedTest " + str);
    }
}
