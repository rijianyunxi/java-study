import java.util.Scanner;
public class Scan {
    public static void main(String[] args) {
//        System.out.println(2&3);
//        System.out.println(2|3);
        Scanner scan = new Scanner(System.in);
        System.out.println("请输入姓名：");
        String content = scan.next();
        System.out.println("请输入薪资");
        double salary = scan.nextDouble();
         if(salary > 10000){
            System.out.println(content + "," + salary + ",牛逼！");
        }else{
            System.out.println("阿欧...");
        }
    }
}
