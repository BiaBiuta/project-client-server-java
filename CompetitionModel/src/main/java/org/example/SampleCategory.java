package org.example;

public enum SampleCategory {
    DESEN("Desen"),
    CAUTARE_COMOARA("Cautare comoara"),
    POEZIE("Poezie");

    private final String categoryName;

    SampleCategory(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }
    public static SampleCategory fromString(String categoryName) {
        for (SampleCategory category : SampleCategory.values()) {
            if (category.getCategoryName().equalsIgnoreCase(categoryName)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Nu există o categorie de vârstă pentru: " + categoryName);
    }
}
