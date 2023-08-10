public class SClass {
    public static void main(String[] args) {
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