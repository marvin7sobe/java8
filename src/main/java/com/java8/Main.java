package main.java.com.java8;


import com.sun.deploy.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        String[] words = {"xyzwq", "aaa", "ab", "aabc34rer", "123", "defvbn"};

        testSortingByLabmda(words);
        testDefaultMethods();
        testStreamsFiltering(words);
        //todo add example to work with flatMap
        testStreamsTransformations(words);
        testWorkWithOptional(words);
        testReduce();
        testCollectingDataFromStream(words);
    }

    private static void testSortingByLabmda(String[] words) {
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

    private static void testReduce() {
        Stream<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5).stream();
        Optional<Integer> sumOptional = numbers.reduce((x, y) -> x + y);
        sumOptional.ifPresent(w -> printMessage("\nSum using reduce is: " + w));

        numbers = Stream.empty();
        Integer sum = numbers.reduce(0, (x, y) -> x + y);
        printMessage("\nSum using reduce with identity is: " + sum);
    }

    private static void testCollectingDataFromStream(String[] words) {
        Set<String> result = getStream(words).filter(w -> w.contains("x")).collect(HashSet::new, HashSet::add, HashSet::addAll);
        printMessage("\nRaw collecting items from stream to HashSet: \n" + result.toString());

        List<String> resultList = getStream(words).filter(w -> w.contains("a")).collect(Collectors.toList());
        printMessage("\nCollecting items from stream to default List using Collectors: \n" + resultList.toString());

        Set<String> resultSet = getStream(words).filter(w -> w.contains("b")).collect(Collectors.toSet());
        printMessage("\nCollecting items from stream to default Set using Collectors: \n" + resultSet.toString());

        TreeSet<String> resultTreeSet = getStream(words).collect(Collectors.toCollection(TreeSet::new));
        printMessage("\nCollecting items from stream to TreeSet using Collectors: \n" + resultTreeSet.toString());

        String joined = getStream(words).filter(w -> w.contains("b")).collect(Collectors.joining());
        printMessage("\nCollecting items from stream to String(joined): \n" + joined);

        String joinedWithDelimiter = getStream(words).filter(w -> w.contains("b")).collect(Collectors.joining(", "));
        printMessage("\nCollecting items from stream to String(joined) with delimiter: \n" + joinedWithDelimiter);

        String joined2 = getStream(words).map(Object::toString).collect(Collectors.joining("; "));
        printMessage("\nCollecting items from stream (that can be not just strings) to String(joined) with delimiter: \n" + joined2);

        printMessage("\nExecuting function on each element from stream(executed in parallel)");
        getStream(words).sorted(Comparator.comparing(String::length)).forEach(w -> System.out.print(" " + w + " "));

        printMessage("\n\nExecuting function on each element from stream (executed not in parallel)");
        getStream(words).forEachOrdered(w -> System.out.print(" " + w + " "));

        testCollectingDataFromStreamToMap();
    }

    private static void testCollectingDataFromStreamToMap() {
        List<Student> students = Arrays.asList(new Student(1, "John"), new Student(2, "Bob"), new Student(3, "Arni"));
        printMessage("\n\nCollected students(id per name) to map:");
        Map<Long, String> studentsIdToName = students.stream()
                .filter(s -> s.getId() > 1)
                .collect(Collectors.toMap(Student::getId, Student::getName));
        printMessage(studentsIdToName.toString());

        printMessage("\nCollected students(id per Student) to map:");
        //this throws exception on duplicate key
        Map<Long, Student> studentsIdToEntity = students.stream().collect(Collectors.toMap(s -> s.getId(), Function.identity()));
        printMessage(studentsIdToEntity.toString());

        printMessage("\nCollected locales(country per set of languages) to map:");
        //on duplicate key values are collected to set for this key
        Stream<Locale> locales = Stream.of(Locale.getAvailableLocales());
        Map<String, Set<String>> countryToLanguage = locales.filter(l -> l.getDisplayCountry().length() > 0).collect(
                Collectors.toMap(
                        l -> l.getDisplayCountry(),
                        l -> Collections.singleton(l.getDisplayLanguage(l)),
                        (existingValues, newValues) -> {
                            Set res = new HashSet<>(existingValues);
                            res.addAll(newValues);
                            return res;
                        })
        );
        printMessage(countryToLanguage.toString());


        printMessage("\nCollected locales(country per set of languages) to TreeMap:");
        locales = Stream.of(Locale.getAvailableLocales());
        Map<String, Set<String>> countryToLocale = locales.filter(l -> l.getDisplayCountry().length() > 0).collect(
                Collectors.<Locale, String, Set<String>, TreeMap<String, Set<String>>>toMap(
                        l -> l.getDisplayCountry(),
                        l -> Collections.singleton(l.getDisplayLanguage(l)),
                        (existingValues, newValues) -> {
                            Set res = new HashSet<>(existingValues);
                            res.addAll(newValues);
                            return res;
                        },
                        TreeMap::new));
        printMessage(countryToLocale.toString());

        printMessage("\nCollected locales(country per set of Locales) to Map using groupingBy:");
        locales = Stream.of(Locale.getAvailableLocales());
        Map<String, List<Locale>> countryToLocale2 = locales.filter(l -> l.getCountry().length() > 0)
                .collect(Collectors.groupingBy(l -> l.getDisplayCountry()));
//        Map<String, Set<Locale>> countryToLocale2 = locales.filter(l->l.getCountry().length()>0)
//                .collect(Collectors.groupingBy(l->l.getDisplayCountry(), Collectors.toSet()));

//        Map<String, Long> countryToLocaleCounts = locales.collect(
//                groupingBy(Locale::getCountry, counting()));

//        Map<String, Integer> stateToCityPopulation = cities.collect(
//                groupingBy(City::getState, summingInt(City::getPopulation)));

//        max city per state
//        Map<String, City> stateToLargestCity = cities.collect(
//                groupingBy(City::getState, maxBy(Comparator.comparing(City::getPopulation))));

//        Map<String, Set<String>> countryToLanguages = locales.collect(
//                groupingBy(l -> l.getDisplayCountry(), mapping(l -> l.getDisplayLanguage(), toSet())));

//        Map<String, IntSummaryStatistics> stateToCityPopulationSummary = cities.collect(
//                groupingBy(City::getState, summarizingInt(City::getPopulation)));

//        Map<String, String> stateToCityNames = cities.collect(
//                groupingBy(City::getState, mapping(City::getName, joining(", "))));

        printMessage(countryToLocale2.toString());
    }

    private static Stream<String> getStream(String[] words) {
        return Arrays.stream(words);
    }

    private static void printMessage(String m) {
        System.out.println(m);
    }
}
