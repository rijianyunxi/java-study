package com.clouddrive.javaBaeStudy.duotai;
// 多态 统一调度，各自实现。
public class Feed {
    public static void main(String[] args) {
        Food food = new Food("狗粮");

        System.out.println("=== 1. 向上转型与多态 ===");
        // 左边 Animal 是引用的编译时类型，右边 Dog 是对象的实际运行时类型。
        // new Dog 创建对象；赋给 Animal 引用是向上转型，不会把狗变成普通动物。
        Animal animal = new Dog("大黄", 3);
        animal.feed(food); // 实际对象是 Dog，因此执行 Dog 重写的 feed。
        // animal.watchHouse(); // 编译错误：Animal 没有声明 watchHouse。

        System.out.println("=== 2. 向下转型：使用子类专有方法 ===");
        Dog dog = (Dog) animal;
        dog.watchHouse();
        // 转型没有创建新对象，两个引用仍指向同一只狗。
        System.out.println("是否为同一个对象：" + (animal == dog));

        System.out.println("=== 3. 同一个父类参数，接收不同实际类型 ===");
        feedAnimal(new Animal("普通动物", 1), food); // Animal 的 feed。
        feedAnimal(dog, food); // 参数传递时隐式向上转型，执行 Dog 的 feed。

        System.out.println("=== 4. 安全的向下转型 ===");
        tryWatchHouse(animal); // 实际是 Dog，可以转型。
        Animal ordinaryAnimal = new Animal("普通动物", 2);
        tryWatchHouse(ordinaryAnimal); // 实际不是 Dog，不能转型。

        System.out.println("=== 5. 强制转换不等于改变对象类型 ===");
        try {
            // 语法允许，但实际对象是 Animal，不是 Dog，运行时转换失败。
            Dog wrongDog = (Dog) ordinaryAnimal;
            wrongDog.watchHouse();
        } catch (ClassCastException ex) {
            // 这里只为演示而捕获；实际使用优先像上面一样先判断 instanceof。
            System.out.println("转换失败：真正的 Animal 对象不能强行变成 Dog。");
        }
    }

    private static void feedAnimal(Animal animal, Food food) {
        animal.feed(food); // 方法由实际对象决定，这就是运行时多态。
    }

    private static void tryWatchHouse(Animal animal) {
        // Java 8 写法：先判断，再强制转换。null instanceof Dog 也为 false。
        if (animal instanceof Dog) {
            Dog dog = (Dog) animal;
            dog.watchHouse();
        } else {
            System.out.println("不是 Dog，不能调用看家方法。");
        }
    }
}
