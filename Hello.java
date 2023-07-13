/**
 * java输出hello word
 *public static void main(String[] args) 固定写法
 * */
public class Hello {
    public static void main(String[] args){
        System.out.println("hello world,song");
    }
}

/**
 * 制表符
 * \ 转义
 * \t \n \/ \r
 */

class ZhuanYi{
    public static void main(String[] args) {
        System.out.println("1\t2\t3");
        System.out.println("13\rj");
        System.out.println("1\n2\n3");
        System.out.println("1\"3\"");
    }
}

/**
 * byte short int long
 * float double
 * char char的本质是整数！！！！
 *
 * */
class Var{
    public static void main(String[] args) {
        int num = 1;
        double num1 = 9.9;
        double num2 = 1.00;
        char str = '男';
        String str1 = "song";
        char str2 = 999;
        System.out.println("str2为"+str2);
        System.out.println(num+num1+str+str1);
        System.out.println(num+str);
        System.out.println(num2==num); //
        System.out.println(8.1 / 3 == 2.7); // 应当计算两数的差值绝对值在一定范围内即可
        boolean res = Math.abs(8.1 / 3 - 2.7) < 0.00001;//true 相等
//        System.out.println(res+num);  // 布尔类型不参与类型的自动转换
        double i9 = 8.9;
        System.out.println((byte)i9); //强制转换
        byte bb = 'a';
        System.out.println(bb);
    }
}

class Parse{
    public static void main(String[] args) {
        String str = "127";
        int i1 = Integer.parseInt(str);
        long l1 = Long.parseLong(str);
        short s1 = Short.parseShort(str);
        float f1 = Float.parseFloat(str);
        double d1 = Double.parseDouble(str);
        boolean b1 = Boolean.parseBoolean("true");
        byte b = Byte.parseByte(str);
        System.out.println(i1+" "+l1+" "+s1+" "+f1+" "+d1+" "+b1+" "+b);
    }
}