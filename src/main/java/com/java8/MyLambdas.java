package main.java.com.java8;

import java.util.function.IntConsumer;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

import static main.java.com.java8.Utils.printMessage;

public class MyLambdas {

    public void executeTests() {
        testBasicSupplier();
        testIntConsumer();
    }

    public void testBasicSupplier() {
        printMessage("\nTest Basic supplier:");
        infoSupplier(Logger.getLogger("My Logger"), () -> "\nMy lambda message from Supplier");
    }

    private void infoSupplier(Logger logger, Supplier<String> message) {
        if (logger.isLoggable(Level.INFO)) {
            printMessage(message.get());
        }
    }

    public void testIntConsumer() {
        printMessage("\nTest basic Int Consumer:");
        repeat(3, i -> printMessage("Count down: " + (3 - i)));

        printMessage("\nTest Runnable Consumer:");
        repeat(2, () -> printMessage("Runnable.run() executed"));
    }

    private void repeat(int times, IntConsumer action) {
        for (int i = 0; i < times; i++) {
            action.accept(i);
        }
    }

    private void repeat(int times, Runnable action) {
        for (int i = 0; i < times; i++) {
            action.run();
        }
    }
}
