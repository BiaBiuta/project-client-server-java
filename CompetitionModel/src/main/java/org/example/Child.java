package org.example;

import java.io.Serializable;

public class Child extends Entity<Integer> implements Serializable {
    private Integer age;
    private String name;
    private int numberOfSamples;
    public Child(String name, Integer age ) {
        this.name = name;
        this.age = age;
        this.numberOfSamples = 0;
    }

    public Child(String childId) {
        super();
    }

    public int getNumberOfSamples() {
        return numberOfSamples;
    }

    public void setNumberOfSamples(int numberOfSamples) {
        this.numberOfSamples = numberOfSamples;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
