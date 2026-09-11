package com.clouddrive.javaBaeStudy.abstractinterface;

/**
 * 可直接运行的抽象类与接口示例。
 *
 * 重点：抽象类可以同时包含字段、构造器、普通方法和抽象方法；
 * 接口用来描述可组合的“能力”，一个类可以实现多个接口。
 */
public class AbstractAndInterfaceDemo {

    public static void main(String[] args) {
        System.out.println("========== 1. 抽象类 ==========");
        Animal dog = new Dog("旺财", 3);
        introduceAnimal(dog);

        System.out.println("\n========== 2. 接口能力 ==========");
        Bird bird = new Bird("小蓝", 1);
        introduceAnimal(bird);
        letItFly(bird); // Bird 向上转型为 Flyable。

        System.out.println("\n========== 3. 多个接口 ==========");
        SmartDog smartDog = new SmartDog("小白", 2);
        smartDog.watchHouse();
        smartDog.swim();

        System.out.println("\n========== 4. 接口不等于父类 ==========");
        // RobotDog 不是 Animal，但它也能实现 Watchable。
        Watchable robotDog = new RobotDog();
        robotDog.watchHouse();

        System.out.println("\n========== 5. 接口静态方法 ==========");
        Flyable.printTip();
    }

    /** 参数写 Animal，Dog 和 Bird 都能传进来：面向父类编程。 */
    private static void introduceAnimal(Animal animal) {
        System.out.println("名字：" + animal.getName() + "，年龄：" + animal.getAge());
        animal.eat();       // 调用抽象类中已经实现的普通方法。
        animal.makeSound(); // 实际调用哪个版本，取决于真实对象。
    }

    /** 参数写接口，只要对象实现了 Flyable 就能传入。 */
    private static void letItFly(Flyable flyable) {
        flyable.fly();
        flyable.land(); // 直接使用接口提供的 default 方法。
    }
}

/**
 * 抽象类：提取同类对象共同拥有的状态和代码。
 *
 * 不能 new Animal(...)，但它可以有：
 * 1. 实例字段；2. 构造器；3. 普通方法；4. 抽象方法。
 */
abstract class Animal {
    private final String name;
    private final int age;

    protected Animal(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // 普通方法：有方法体，子类可以直接继承使用。
    public void eat() {
        System.out.println(name + " 正在吃东西");
    }

    // 抽象方法：没有方法体，具体子类必须实现。
    public abstract void makeSound();

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}

class Dog extends Animal {
    Dog(String name, int age) {
        super(name, age);
    }

    @Override
    public void makeSound() {
        System.out.println(getName() + "：汪汪！");
    }
}

class Bird extends Animal implements Flyable {
    Bird(String name, int age) {
        super(name, age);
    }

    @Override
    public void makeSound() {
        System.out.println(getName() + "：啾啾！");
    }

    @Override
    public void fly() {
        System.out.println(getName() + " 正在飞");
    }
}

/**
 * 接口：描述一种“能做什么”的能力。
 *
 * Java 8 中：接口可声明抽象方法，也可包含 default 方法和 static 方法。
 * 接口没有构造器，也没有每个对象各自拥有的普通实例字段。
 */
interface Flyable {
    // 默认是 public abstract；实现类需要提供具体实现。
    void fly();

    // Java 8 的接口默认方法：实现类可直接用，也可以重写。
    default void land() {
        System.out.println("安全降落");
    }

    // Java 8 的接口静态方法：必须通过接口名调用。
    static void printTip() {
        System.out.println("飞行前请检查天气");
    }
}

interface Watchable {
    void watchHouse();
}

interface Swimmable {
    void swim();
}

/** Java 只能继承一个类，但可以实现多个接口。 */
class SmartDog extends Animal implements Watchable, Swimmable {
    SmartDog(String name, int age) {
        super(name, age);
    }

    @Override
    public void makeSound() {
        System.out.println(getName() + "：汪汪！");
    }

    @Override
    public void watchHouse() {
        System.out.println(getName() + " 正在看门");
    }

    @Override
    public void swim() {
        System.out.println(getName() + " 正在游泳");
    }
}

/** 它不属于 Animal，但仍然可以拥有 Watchable 能力。 */
class RobotDog implements Watchable {
    @Override
    public void watchHouse() {
        System.out.println("机器人狗正在巡逻");
    }
}
