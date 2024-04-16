package org.example.dto;

import java.io.Serializable;

public class RegistrationDTO implements Serializable {
    private String childId;
    private String sampleId;

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

    private String sampleCategory;
    private String ageCategory;

    public RegistrationDTO(String childId, String sampleId, String sampleCategory, String ageCategory) {
        this.childId = childId;
        this.sampleId = sampleId;
        this.sampleCategory = sampleCategory;
        this.ageCategory = ageCategory;
    }

    public RegistrationDTO(String childId, String sampleId) {
        this.childId = childId;
        this.sampleId = sampleId;
    }

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

    public String getSampleId() {
        return sampleId;
    }

    public void setSampleId(String sampleId) {
        this.sampleId = sampleId;
    }
}
