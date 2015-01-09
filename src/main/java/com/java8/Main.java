package main.java.com.java8;


import java.util.Arrays;
import java.util.Comparator;

public class Main {
    public static void main(String[] args){
        Main main = new Main();

        String[] words = {"xyzwq", "aaa", "ab", "aabc34rer"};
        main.doSortByLabmda(words);

        main.testDefaultMethods();
    }

    private void doSortByLabmda(String[] words) {
        System.out.println("Original array: " + Arrays.toString(words));

        String[] wordsCopy = new String[words.length];
        System.arraycopy(words, 0, wordsCopy, 0, words.length);

        Comparator<String> cmp = (first, second) -> Integer.compare(first.length(), second.length());
        Arrays.sort(wordsCopy, cmp);
        System.out.println("Sorted array by word length v1: " + Arrays.toString(wordsCopy));

        Arrays.sort(wordsCopy, (first, second) -> Integer.compare(first.length(), second.length()));
        System.out.println("Sorted array by word length v2: " + Arrays.toString(wordsCopy));

        Arrays.sort(wordsCopy, String::compareToIgnoreCase);
        System.out.println("Sorted array alphabetically: " + Arrays.toString(wordsCopy));

        Arrays.sort(wordsCopy, Comparator.comparing(String::length));
        System.out.println("Sorted array by word length v3: " + Arrays.toString(wordsCopy));
    }

    private void testDefaultMethods() {
        Student s1 = new Student(23);
        System.out.println("\nExecuting default method from Person:");
        s1.printName();

        System.out.println("\nExecuting static method from Person");
        System.out.println(Person.getNamePrefix());
    }
}
