package main.java.com.java8;


import main.java.com.java8.domain.Album;
import main.java.com.java8.domain.Artist;
import main.java.com.java8.domain.Person;
import main.java.com.java8.domain.Student;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.*;

import static main.java.com.java8.Utils.printMessage;

public class Main {
    public static void main(String[] args) throws ScriptException {
        String[] words = {"xyzwq", "aaa", "ab", "aabc34rer", "123", "defvbn"};

        testSortingByLabmda(words);
        testDefaultMethods();
        testStreamsFiltering(words);
        //todo add example to work with flatMap
        testNewMapFeatures();
        testStreamsTransformations(words);
        testWorkWithOptional(words);
        testReduce();
        testCollectingDataFromStream(words);
        testWorkWithPrimitiveStreams(words);
        testParallelStream(words);
        testMayLambdas();
        testNashornScriptEngine();
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

    private static void testNewMapFeatures() {
        printMessage("\nNew forEach method in map");
        Map<Artist, List<Album>> map = new HashMap<>();
        map.put(new Artist("John"), Arrays.asList(new Album("Title 1"), new Album("Title 2")));
        map.put(new Artist("Dino"), Arrays.asList(new Album("Title 2"), new Album("Title 3")));
        map.put(new Artist("Sam"), Arrays.asList(new Album("Title 4"), new Album("Title 5"), new Album("Title 6")));
        Map<Artist, Integer> countsOfAlbums = new HashMap<>();
        map.forEach((artist, albums) -> countsOfAlbums.put(artist, albums.size()));

        printMessage("\nCache implementation");
        Map<String, Artist> cacheMap = new HashMap<>();
        map.forEach((artist, albums) -> cacheMap.put(artist.getName(), artist));
        Artist fromCache = cacheMap.computeIfAbsent("John", s -> new Artist(s));
        printMessage("Artist from cache: " + fromCache.getName());

        Artist fromDBAndPutToCache = cacheMap.computeIfAbsent("Arny", getArtistFromDatabase());
        printMessage("Artist from DB: " + fromDBAndPutToCache.getName());
    }

    private static Function<? super String, ? extends Artist> getArtistFromDatabase() {
        return s -> {
            //dbManager.getByName(s)
            return new Artist(s);
        };
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

        Stream<String> uppercasedFirstCharacter = getStream(words)
                .map(w -> {
                    String first = Character.toString(w.charAt(0)).toLowerCase();
                    return first + w.substring(1);});
        printMessage("\nUppercasing first character of word");
        printStreamContent(uppercasedFirstCharacter);

        Stream<Character> distinctFirstCharacter = getStream(words).map(w -> w.charAt(0)).distinct();
        printMessage("\nDistinct first charracter from word by .map()");
        printStreamContent(distinctFirstCharacter);

        printMessage("\nNew item was added to list but stream is not broken and will display this word:");
        List<String> wordsList = new ArrayList<>();
        Collections.addAll(wordsList, words);
        Stream<String> withNewWord = wordsList.stream();
        wordsList.add("new_item_was_added_to_list");
        printStreamContent(withNewWord);
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

    private static void testWorkWithPrimitiveStreams(String[] words) {
        printMessage("\nWork with primitive streams:");
        IntStream intStream = IntStream.of(1, 2, 3, 4);
        printMessage("\nOriginal int stream values:");
        intStream.forEach(i -> System.out.print(" " + i + " "));

        printMessage("\nBoxing from primitive to Objects");
        Stream<Integer> boxedInts = IntStream.of(1, 2, 3, 4).boxed();
        boxedInts.forEach(i -> System.out.print(" " + i + " "));
        printMessage("\nUnboxing from Objects to primitive");
        intStream = Stream.of(1, 2, 4).mapToInt(Integer::intValue);
        intStream.forEach(i -> System.out.print(" " + i + " "));

        IntStream zeroToNine = IntStream.range(0, 10);
        IntStream zeroToTen = IntStream.rangeClosed(0, 10);

        printMessage("\nPrimitive long stream");
        LongStream longStream = LongStream.of(1l, 2l, 3l);
        longStream.forEach(l -> System.out.print(" " + l + " "));
        LongStream zeroToFive = LongStream.range(0, 6l);

        printMessage("\nPrimitive double stream");
        DoubleStream doubleStream = DoubleStream.of(2.2, 3.3, 4.4);
        doubleStream.forEach(l -> System.out.print(" " + l + " "));

        printMessage("\nGetting words length from words stream to intStream");
        IntStream wordsLength = getStream(words).mapToInt(String::length);
        wordsLength.forEach(i -> System.out.print(" " + i + " "));
    }

    private static void testParallelStream(String[] words) {
        printMessage("\n\nDisplay words in few threads using Parallel stream:");
        Stream stream = getStream(words).parallel();
        printStreamContent(stream);

        printMessage("\nGrouping data to ConcurentMap:");
        Stream<Locale> locales = Stream.of(Locale.getAvailableLocales());
        Map<String, List<Locale>> countryToLocale = locales.filter(l -> l.getCountry().length() > 0)
                .collect(Collectors.groupingByConcurrent(l -> l.getDisplayCountry()));
        printMessage(countryToLocale.toString());
    }

    public static void testMayLambdas() {
        MyLambdas myLambdas = new MyLambdas();
        myLambdas.executeTests();
    }

    private static void testNashornScriptEngine() throws ScriptException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("nashorn");
        Object length = engine.eval("'Hello World'.length");
        System.out.println("\nNashorn JS: Length of 'Hello World' evaluated by engine is : " + length);

        Bindings scope = engine.createBindings();
        scope.put("stage", 23);
        Object stage = engine.eval("stage", scope);
        System.out.println("Nashorn JS: simple stage variable: " + stage);

        scope.put("student1", new Student(11, "Don"));
        Object firstName = engine.eval("student1.name", scope);
        System.out.println("Nashorn JS: firstName from Student object: " + firstName);

        engine.eval("var JDate = java.util.Date");
        engine.eval("var d1 = new JDate()");
        Object jDate = engine.eval("d1");
        System.out.println("Nashorn JS: using java.util.Date class to create new date: " + jDate);

        engine.eval("var StringArray = Java.type('java.lang.String[]')");
        engine.eval("var strings = new StringArray(10)");
        engine.eval("strings[2]='z34'");
        engine.eval("var item = strings[2]");
        Object stringItem = engine.eval("item");
        System.out.println("Nashorn JS: item from strings array: " + stringItem);
    }

    private static Stream<String> getStream(String[] words) {
        return Arrays.stream(words);
    }
}
