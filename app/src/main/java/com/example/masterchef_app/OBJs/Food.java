package com.example.masterchef_app.OBJs;

import java.io.Serializable;

public class Food implements Serializable {
    private String title;
    private String pic;


    private String Price;
    private String tax;
    private String calories;
    private String delSer;
    private String description;
    private String foodKey;
    private int NumberCards ;
    private String CategoryKey ;
    private String time;
    private int likesNumbers ;
    public Food() {
    }

    public Food(String title, String pic, String price, String tax, String calories, String delSer, String description, int NumberCards,String foodKey,String CategoryKey, String time , int likesNumbers) {
        this.title = title;
        this.pic = pic;
        Price = price;
        this.tax = tax;
        this.calories = calories;
        this.delSer = delSer;
        this.description = description;
        this.foodKey = foodKey;
        this.NumberCards = NumberCards;
        this.CategoryKey = CategoryKey;
        this.time = time;
        this.likesNumbers = likesNumbers;
    }
    public Food(String title, String pic, String price, String tax, String calories, String delSer, String description, int NumberCards, String time ) {
        this.title = title;
        this.pic = pic;
        Price = price;
        this.tax = tax;
        this.calories = calories;
        this.delSer = delSer;
        this.description = description;
        this.NumberCards = NumberCards;
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getNumberCards() {
        return NumberCards;
    }

    public void setNumberCards(int numberCards) {
        NumberCards = numberCards;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getDelSer() {
        return delSer;
    }

    public void setDelSer(String delSer) {
        this.delSer = delSer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFoodKey() {
        return foodKey;
    }

    public void setFoodKey(String foodKey) {
        this.foodKey = foodKey;
    }

    public String getCategoryKey() {
        return CategoryKey;
    }

    public void setCategoryKey(String categoryKey) {
        CategoryKey = categoryKey;
    }

    public int getLikesNumbers() {
        return likesNumbers;
    }

    public void setLikesNumbers(int likesNumbers) {
        this.likesNumbers = likesNumbers;
    }
}
