package org.example;

public enum AgeCategory {
    ANI_6_8("6-8 ani"),
    ANI_9_11("9-11 ani"),
    ANI_12_15("12-15 ani");

    private final String categoryName;

    AgeCategory(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }
    public static AgeCategory fromString(String categoryName) {
        for (AgeCategory category : AgeCategory.values()) {
            if (category.getCategoryName().equalsIgnoreCase(categoryName)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Nu există o categorie de vârstă pentru: " + categoryName);
    }
}
