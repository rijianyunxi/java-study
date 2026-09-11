package com.clouddrive.javaBaeStudy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * ArrayList、HashMap、HashSet 的基础练习。
 *
 * HashMap 和 HashSet 都不保证遍历顺序；如果需要保持添加顺序，
 * 可以分别使用 LinkedHashMap 和 LinkedHashSet。
 */
public class HashMapAndList {

    public static void main(String[] args) {
        demonstrateList();
        demonstrateMap();
        demonstrateSet();
    }

    private static void demonstrateList() {
        System.out.println("\n========== ArrayList ==========");

        // List 是接口；ArrayList 是常用实现类。
        List<String> hobbies = new ArrayList<>(Arrays.asList("吃饭", "睡觉", "写代码"));
        hobbies.add("打球");                 // 增加
        hobbies.set(0, "学习 Java");         // 修改下标为 0 的元素
        hobbies.remove("睡觉");              // 按值删除

        System.out.println("爱好：" + hobbies);
        System.out.println("第一个爱好：" + hobbies.get(0));

        for (String hobby : hobbies) {
            System.out.println("- " + hobby);
        }
    }

    private static void demonstrateMap() {
        System.out.println("\n========== HashMap ==========");

        // 这里的 Map 明确表示：学号 -> 学生。
        // 不建议一上来就写 Map<String, Object>，那样取值时经常要强制转型。
        Map<String, Student> studentsById = new HashMap<>();
        studentsById.put("1001", new Student("1001", "Song", 18));
        studentsById.put("1002", new Student("1002", "Alice", 20));
        studentsById.put("1003", new Student("1003", "Bob", 19));

        // key 重复时，后 put 的值会覆盖前一个值。
        studentsById.put("1002", new Student("1002", "Alice", 21));

        Student song = studentsById.get("1001");
        System.out.println("查到的学生：" + song);
        System.out.println("不存在的学生：" + studentsById.getOrDefault("9999", Student.UNKNOWN));
        System.out.println("是否有学号 1003：" + studentsById.containsKey("1003"));

        // 遍历键和值时，entrySet() 通常最直接，不需要再额外 get 一次。
        System.out.println("\n遍历学号和学生：");
        for (Map.Entry<String, Student> entry : studentsById.entrySet()) {
            System.out.printf("学号：%s，学生：%s%n", entry.getKey(), entry.getValue());
        }

        studentsById.remove("1003");
        System.out.println("删除 1003 后，Map 大小：" + studentsById.size());
    }

    private static void demonstrateSet() {
        System.out.println("\n========== HashSet ==========");

        Set<String> tags = new HashSet<>();
        System.out.println("第一次添加 Java：" + tags.add("Java"));
        System.out.println("重复添加 Java：" + tags.add("Java")); // false：重复元素不会加入
        tags.add("Spring Boot");
        tags.add("MySQL");

        System.out.println("标签：" + tags);
        System.out.println("是否包含 MySQL：" + tags.contains("MySQL"));
        tags.remove("MySQL");
        System.out.println("删除 MySQL 后：" + tags);

        // HashSet 判断“重复”的依据是 equals() 和 hashCode()，不是对象是否 new 了两次。
        Set<Student> classStudents = new HashSet<>();
        classStudents.add(new Student("1001", "Song", 18));
        classStudents.add(new Student("1001", "Song", 99)); // 学号相同，视为同一个学生
        classStudents.add(new Student("1002", "Alice", 21));

        System.out.println("\n班级学生数：" + classStudents.size()); // 2
        System.out.println("班级学生：" + classStudents);
    }

    private static final class Student {
        private static final Student UNKNOWN = new Student("-", "未知学生", 0);

        private final String id;
        private final String name;
        private final int age;

        private Student(String id, String name, int age) {
            this.id = id;
            this.name = name;
            this.age = age;
        }

        /**
         * 这里规定“学号相同就是同一个学生”。
         * HashSet / HashMap 的 key 使用自定义对象时，通常要同时重写这两个方法。
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Student)) {
                return false;
            }
            Student other = (Student) obj;
            return Objects.equals(id, other.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }

        @Override
        public String toString() {
            return String.format("Student{id='%s', name='%s', age=%d}", id, name, age);
        }
    }
}
