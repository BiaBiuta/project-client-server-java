package org.example.dto;

import java.io.Serializable;

public class RegistrationDTO implements Serializable {
    private String childId;
    private String sampleId;

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
