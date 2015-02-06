package main.java.com.java8.domain;

import main.java.com.java8.domain.Person;

public class Student implements Person {
    private long id;
    private String name;

    public Student(long id) {
        this.id = id;
    }

    public Student(long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Student{id=" + id + ", name=" + name + "}";
    }
}
