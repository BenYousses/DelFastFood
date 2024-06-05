package com.example.masterchef_app.OBJs;

import java.io.Serializable;

public class Category implements Serializable {
private String categoryName ;
private String categoryImage ;
private String CvategoryKey;

    public Category() {
    }

    public Category(String categoryName, String categoryImage, String cvategoryKey) {
        this.categoryName = categoryName;
        this.categoryImage = categoryImage;
        CvategoryKey = cvategoryKey;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public String getCvategoryKey() {
        return CvategoryKey;
    }

    public void setCvategoryKey(String cvategoryKey) {
        CvategoryKey = cvategoryKey;
    }
}
