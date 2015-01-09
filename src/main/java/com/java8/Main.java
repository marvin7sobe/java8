package main.java.com.java8;


import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        String[] words = { "xyzwq", "aaa", "ab", "aabc34rer" };

        testSortingsByLabmda(words);
        testDefaultMethods();
        testStreamsFiltering(words);
        testStreamsTransformations(words);
    }

    private static void testSortingsByLabmda(String[] words) {
        printMessage("\nOriginal array: " + Arrays.toString(words));

        String[] wordsCopy = new String[words.length];
        System.arraycopy(words, 0, wordsCopy, 0, words.length);

        Comparator<String> cmp = (first, second) -> Integer.compare(first.length(), second.length());
        Arrays.sort(wordsCopy, cmp);
        printMessage("\nSorted array by word length v1: " + Arrays.toString(wordsCopy));

        Arrays.sort(wordsCopy, (first, second) -> Integer.compare(first.length(), second.length()));
        printMessage("\nSorted array by word length v2: " + Arrays.toString(wordsCopy));

        Arrays.sort(wordsCopy, String::compareToIgnoreCase);
        printMessage("\nSorted array alphabetically: " + Arrays.toString(wordsCopy));

        Arrays.sort(wordsCopy, Comparator.comparing(String::length));
        printMessage("\nSorted array by word length v3: " + Arrays.toString(wordsCopy));
    }

    private static void testDefaultMethods() {
        Student s1 = new Student(23);
        printMessage("\nExecuting default method from Person:");
        s1.printName();

        printMessage("\nExecuting static method from Person");
        printMessage(Person.getNamePrefix());
    }

    private static void testStreamsFiltering(String[] words) {
        Stream<String> wordsStream = getStream(words).filter(w -> containsString(w, "aa"));
        printMessage("\nFiltered by presence 'aa' sequence");
        printStreamContent(wordsStream);

        wordsStream = getStream(words).filter(w -> w.length() > 2);
        printMessage("\nFiltered where length > 2");
        printStreamContent(wordsStream);
    }

    private static void testStreamsTransformations(String[] words) {
        Stream<String> upperCased = getStream(words).map(String::toUpperCase);
        printMessage("\nUppercased by .map() v1");
        printStreamContent(upperCased);

        Stream<String> lowerCasedWords2 = getStream(words).map(w -> w.toUpperCase());
        printMessage("\nUppercased by .map() v2");
        printStreamContent(lowerCasedWords2);

        Stream<Character> firstCharacter = getStream(words).map(w -> w.charAt(0));
        printMessage("\nFirst charracter from word by .map()");
        printStreamContent(firstCharacter);

        Stream<Character> distinctFirstCharacter = getStream(words).map(w -> w.charAt(0)).distinct();
        printMessage("\nDistinct first charracter from word by .map()");
        printStreamContent(distinctFirstCharacter);
    }

    private static void printStreamContent(Stream longWords) {
        printMessage(Arrays.toString(longWords.toArray()));
    }

    private static Stream<String> getStream(String[] words) {
        return Arrays.asList(words).stream();
    }

    private static void printMessage(String m) {
        System.out.println(m);
    }

    private static boolean containsString(String a, CharSequence c) {
        return a.contains(c);
    }
}
