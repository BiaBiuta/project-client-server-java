package org.example.dto;

import java.io.Serializable;

public class SamplesDTO implements Serializable {
    private String id;
    private String sampleCategory;
    private String ageCategory;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SamplesDTO(String id) {
        this.id = id;
    }

    public SamplesDTO(String id, String sampleCategory, String ageCategory) {
        this.id = id;
        this.sampleCategory = sampleCategory;
        this.ageCategory = ageCategory;
    }

    public SamplesDTO(String sampleCategory, String ageCategory) {
        this.sampleCategory = sampleCategory;
        this.ageCategory = ageCategory;
    }

    public String getSampleCategory() {
        return sampleCategory;
    }

    public void setSampleCategory(String sampleCategory) {
        this.sampleCategory = sampleCategory;
    }

    public String getAgeCategory() {
        return ageCategory;
    }

    public void setAgeCategory(String ageCategory) {
        this.ageCategory = ageCategory;
    }
}
