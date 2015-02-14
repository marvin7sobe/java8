package main.java.com.java8;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class CompletableFuturesTest {

    public void executeTests() {
        testComposing();

    }

    private void testComposing() {
        System.out.print("\nCompletableFutures tests");

        List<String> strings = Arrays.asList("word", "tinki house", "test2", "empty string", "siple word", "hello world", "my word");
        System.out.print("\nInitial words: ");
        strings.forEach(x -> System.out.print(x + ", "));

        System.out.println("\nFirstly words were filtered in first thread and in second thread they were uppercased:");
        List<String> res = CompletableFuture.supplyAsync(() -> strings)
                .thenCompose(words -> CompletableFuture.supplyAsync(() -> {
                    List<String> a1 = words.stream().filter(w -> w.length() > 6).collect(Collectors.toList());
                    a1.forEach(x -> System.out.print(x + ", "));
                    return a1;
                }))
                .thenCompose(longWords -> CompletableFuture.supplyAsync(() -> longWords.stream().map(w -> w.toUpperCase()).collect(Collectors.toList())))
                .join();
        System.out.println();
        res.forEach(x -> System.out.print(x + ", "));
    }

}
