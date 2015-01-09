package main.java.com.java8;

public class Student implements Person {
    private long id;

    public Student(long id) {
        this.id = id;
    }

    @Override
    public long getId() {
        return id;
    }
}
