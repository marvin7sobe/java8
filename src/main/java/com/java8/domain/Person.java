package main.java.com.java8.domain;

public interface Person {

    String NAME_PREFIX = "user_";

    long getId();

    default void printName() {
        System.out.println(NAME_PREFIX + getId());
    }

    static String getNamePrefix(){
        return NAME_PREFIX;
    }
}
