package org.example.dto;

import java.io.Serializable;

public class ChildDTO implements Serializable {
    private String id;
    private String name;
    private String age;
    private String numberOfSamples;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ChildDTO(String id, String name, String age, String numberOfSamples) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.numberOfSamples = numberOfSamples;
    }

    public ChildDTO(String name, String age) {
        this.name = name;
        this.age = age;
    }
    public ChildDTO(String name) {
        this.name = name;

    }

    public ChildDTO(String name, String age, String numberOfSamples) {
        this.name = name;
        this.age = age;
        this.numberOfSamples = numberOfSamples;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getNumberOfSamples() {
        return numberOfSamples;
    }

    public void setNumberOfSamples(String numberOfSamples) {
        this.numberOfSamples = numberOfSamples;
    }
}
