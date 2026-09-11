package com.clouddrive.javaBaeStudy.exceptiongeneric;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** 对照 TS 的 Box<T>、泛型函数和 extends 约束。 */
public class GenericsDemo {
    public static void main(String[] args) {
        System.out.println("=== 1. 泛型类：指定盒子中存什么 ===");
        Box<String> nameBox = new Box<>("小明");
        String name = nameBox.get(); // 无须手工转换为 String。
        System.out.println(name);
        // Box<String> wrong = new Box<>(123); // 编译失败：类型不匹配。

        // Java 泛型参数必须是引用类型：用 Integer，不能写 Box<int>。
        Box<Integer> ageBox = new Box<>(18); // int 自动装箱为 Integer。
        int age = ageBox.get(); // 自动拆箱；如果 get() 返回 null，会抛 NullPointerException。
        System.out.println("年龄：" + age);

        System.out.println("=== 2. 泛型方法 ===");
        System.out.println(identity("hello"));
        System.out.println(identity(123));

        System.out.println("=== 3. 上界约束 ===");
        System.out.println("数字转 double：" + asDouble(12));
        // asDouble("12"); // 编译失败：String 不是 Number 的子类。

        System.out.println("=== 4. 集合不变性与通配符 ===");
        List<Integer> integers = Arrays.asList(1, 2, 3);
        // List<Number> numbers = integers; // 编译失败：List<Integer> 不是 List<Number> 的子类。
        System.out.println("整数列表之和：" + sum(integers));
        System.out.println("小数列表之和：" + sum(Arrays.asList(1.5, 2.5)));

        List<Number> destination = new ArrayList<>();
        addInteger(destination);
        System.out.println("写入整数后：" + destination);

        System.out.println("=== 5. 泛型类型擦除 ===");
        System.out.println("Box<String> 与 Box<Integer> 的运行时类相同："
                + (nameBox.getClass() == ageBox.getClass()));
        // 不能写 new T()、T.class 或 instanceof Box<String>。
        // Java 和 TS 的普通泛型都不会自动给外部 JSON 数据做运行时校验。
    }

    private static class Box<T> {
        private final T value;

        Box(T value) {
            this.value = value;
        }

        T get() {
            return value;
        }
    }

    // TS: function identity<T>(value: T): T { return value; }
    // Java 的 <T> 写在返回类型 T 前面。
    private static <T> T identity(T value) {
        return value;
    }

    // TS 中也有 T extends ...；Java 这里约束的是类/接口继承实现关系。
    private static <T extends Number> double asDouble(T value) {
        return value.doubleValue();
    }

    // ? extends Number：可读取为 Number；不能随意 add 一个数，因为具体元素类型未知。
    private static double sum(List<? extends Number> values) {
        double total = 0;
        for (Number value : values) {
            total += value.doubleValue();
        }
        return total;
    }

    // ? super Integer：可写入 Integer；读出时只能保证是 Object。
    // 可传 List<Integer>、List<Number> 或 List<Object>。
    private static void addInteger(List<? super Integer> values) {
        values.add(42);
    }
}
