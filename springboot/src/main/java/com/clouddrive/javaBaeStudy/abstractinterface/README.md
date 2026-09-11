# Java 抽象类与接口笔记

配套可运行代码：AbstractAndInterfaceDemo.java。

## 先纠正一个容易混的点

~~~java
abstract class Animal {
    public void eat() { // 普通方法：有实现
        System.out.println("吃东西");
    }

    public abstract void makeSound(); // 抽象方法：没有实现
}
~~~

- 抽象类可以写字段、构造器、普通方法和抽象方法。
- 抽象方法才不能写方法体。
- 抽象类不能直接 new，例如 new Animal(...) 是错误的。

## 抽象类：提取“共同的数据和代码”

在示例中，Animal 是抽象类：

- 所有动物都有 name、age；
- 所有动物都能 eat()，所以方法直接在父类实现；
- 每种动物的叫声不同，因此 makeSound() 留成抽象方法，让 Dog、Bird 自己实现。

~~~text
Dog 是 Animal  -> extends Animal
Bird 是 Animal -> extends Animal
~~~

选择抽象类的口诀：**它们本来就是一类东西，并且确实共享状态或实现。**

## 接口：描述“能做什么”

~~~java
interface Flyable {
    void fly();
}
~~~

Flyable 不关心对象是不是动物，只关心它有没有“飞行”能力：

~~~text
Bird 能飞       -> implements Flyable
飞机也能飞      -> implements Flyable
超人也能飞      -> implements Flyable
~~~

一个类只能 extends 一个父类，但能 implements 多个接口：

~~~java
class SmartDog extends Animal implements Watchable, Swimmable {
    // ...
}
~~~

## Java 8 中的接口不只可写抽象方法

你的项目是 Java 8，接口还支持：

~~~java
interface Flyable {
    void fly();                 // 抽象方法

    default void land() {       // 有方法体：默认方法
        System.out.println("降落");
    }

    static void printTip() {    // 有方法体：静态方法
        System.out.println("提示");
    }
}
~~~

但接口依然：

- 没有构造器；
- 不能保存每个对象自己的普通实例字段；
- 主要职责仍是定义能力和规则，而不是堆放大量状态或业务逻辑。

## 和 TypeScript 的区别

### 抽象类

Java 抽象类和 TS 抽象类非常接近：都能放字段、构造器、普通方法和抽象方法，子类用 extends 继承。

### 接口：最重要的区别

TS 是**结构类型**：只要对象“长得像”接口规定的样子，就能传进去，甚至不用写 implements。

~~~ts
interface Flyable {
  fly(): void;
}

const bird = { fly() {} };
const letItFly = (item: Flyable) => item.fly();
letItFly(bird); // 可以
~~~

Java 是**名义类型**：类必须明确声明 implements Flyable。

~~~java
interface Flyable {
    void fly();
}

class Bird implements Flyable {
    @Override
    public void fly() { }
}
~~~

即使一个 Java 类刚好也写了 fly()，但没有 implements Flyable，它也不能当作 Flyable 使用。

另外：TS 的 interface 编译成 JavaScript 后会消失，只用于编译期检查；Java 的接口会编译成真实的 .class 类型，运行时可以用 instanceof Flyable 判断。

## 怎么选

| 你的目的 | 优先使用 |
| --- | --- |
| 多个子类共享字段、构造过程、普通方法 | 抽象类 |
| 约定一种可组合能力，例如可飞、可支付、可缓存 | 接口 |
| 只是在 TS 中描述对象结构 | interface 或 type |

一句话：**抽象类解决“它是什么，并且共用什么”；接口解决“它会什么”。**
