package org.example;

public class Sample extends Entity<Integer> {
    private SampleCategory sampleCategory;
    private AgeCategory ageCategory;

    public Sample(SampleCategory sampleCategory, AgeCategory ageCategory) {
        this.sampleCategory = sampleCategory;
        this.ageCategory = ageCategory;
    }

    public Sample(String sampleId) {
        super();
    }

    public AgeCategory getAgeCategory() {
        return ageCategory;
    }

    public void setAgeCategory(AgeCategory ageCategory) {
        this.ageCategory = ageCategory;
    }
//    public Sample(SampleCategory sampleCategory) {
//        this.sampleCategory = sampleCategory;
//    }

    public SampleCategory getSampleCategory() {
        return sampleCategory;
    }

    public void setSampleCategory(SampleCategory sampleCategory) {
        this.sampleCategory = sampleCategory;
    }
}
