package main.java.com.java8;


import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        String[] words = { "xyzwq", "aaa", "ab", "aabc34rer" };

        testSortingsByLabmda(words);
        testDefaultMethods();
        testStreamsFiltering(words);
        //todo add example to work with flatMap
        testStreamsTransformations(words);
        testWorkWithOptional(words);
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

    private static boolean containsString(String a, CharSequence c) {
        return a.contains(c);
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

    private static void testWorkWithOptional(String[] words) {
        Optional<String> res = getStream(words).filter(w -> w.length() > 10).max((first, second) -> Integer.compare(first.length(), second.length()));
        res.ifPresent(w -> printMessage("\nMaximum using max and ifPresent is(will not be printed if no value): " + w));
        String max = res.orElse("empty");
        printMessage("\nMaximum using max and orElse is: " + max);

        max = res.orElseGet(() -> getMaximum());
        printMessage("\nMaximum using max and orElseGet is: " + max);

        res = getStream(words).filter(w -> w.startsWith("a")).findFirst();
        res.ifPresent(w -> printMessage("\nFirst match than starts with \"a\" is: " + w));
        Optional<String> newRes = Optional.ofNullable(res.orElse(null));
        printMessage("New Optional res is:" + newRes.get());

        boolean anyMatch = getStream(words).parallel().anyMatch(w -> w.startsWith("x"));
        printMessage("\nIs there any words that starts with \"x\": " + anyMatch);
    }

    private static String getMaximum() {
        return "123";
    }

    private static Stream<String> getStream(String[] words) {
        return Arrays.stream(words);
    }

    private static void printMessage(String m) {
        System.out.println(m);
    }
}
