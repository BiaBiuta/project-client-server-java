package org.example;

public class Registration extends Entity<Integer>{
    Child child;
    Sample sample;


    public Registration(Child child, Sample sample) {
        this.child = child;
        this.sample = sample;
    }

    public Child getChild() {
        return child;
    }

    public void setChild(Child child) {
        this.child = child;
    }

    public Sample getSample() {
        return sample;
    }

    public void setSample(Sample sample) {
        this.sample = sample;
    }


}
