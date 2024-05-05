/**
 * p无论传给谁了，也仅仅是将值赋予给了参数p ，p再赋值不影响原来空间的p，除非直接用p修改
 *
 *
 * */
public class ClassStudy {
    public static void main(String[] args){
//        p1.calc(1);
        Person p1 = new Person("sjt",11,21.1);
        Person p2 = new Person("p2",11,21.1);
        int num = p1.eatAllApple(1);
        System.out.println(num);
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

}

class Person{
    String name;
    int age;
    double source;
    public Person(String name,int age,double source){
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
            if(num > 1000) return;

        }
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