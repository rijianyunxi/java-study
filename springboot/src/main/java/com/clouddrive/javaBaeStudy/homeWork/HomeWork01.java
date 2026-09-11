package com.clouddrive.javaBaeStudy.homeWork;





public class HomeWork01 {

    public static void main(String[] args) {
        // 求double数组的最大值
//        double[] arr = {1.0,2.3,7,-93,-3,888};
//        A01 a01 = new A01(arr);
//        double d = a01.max();
//        System.out.println(d);

        // 找出字符串位置
//        int index = new A02().find("fsdhjfhd3s223",'m');
//        System.out.println(index);


//        改书本的价格，超出150元为150元
//        A03 book = new A03("三体", 199);
//        System.out.println(book.price);
//        book.updatePrice();
//        System.out.println(book.price);


//        看下权限修饰符的权限
//        Sub sub = new Sub("song");
//        sub.sayHi();
//        PublicPrivateProtectedTest publicPrivateProtectedTest = new PublicPrivateProtectedTest();
//        publicPrivateProtectedTest.sayHi("song");
//        publicPrivateProtectedTest.sayHi2("song2");




    }
}

// 求double数组的最大值
class A01 {
    double[] arr;

    public A01(double[] arr){
        this.arr = arr;
    }
    public double max(){
        if(this.arr == null) throw new Error("arr null 无最大值");
        if(this.arr.length == 0) throw new Error("无最大值");

        double number = this.arr[0];

        for(double d : this.arr){
            if(d > number) {
                number = d;
            }
        }

        return number;

    }
}

// 找出字符串位置
class A02{
    public int find(String str,char inputStr){
        int index = -1;
        if(str == null) return  index;
        for(int i = 0;i<str.length();i++){
            if(str.charAt(i) == inputStr) return i;
        }
        return index;
    }
}


// 改书的价格
class A03{
    String name;
    double price;

    public A03(String name,double price){
        this.name = name;
        this.price = price;
    }

    public void updatePrice(){
        if(this.price > 150){
            this.price = 150;
        }
    }
}


class Sub{
    String s;
    public Sub(String s){
        this.s = s;
    }

    protected void sayHi(){
        System.out.println("hi "+ this.s);
    }
}