public class SClass {
    public static void main(String[] args) {
        Animal cat = new Animal();
        cat.name="hua~";
        cat.say("miao miao");
    }
}


class Animal{
    String name;
    double age;

    public void say(String word){
        System.out.println(this.name+"说"+word);
    }
}