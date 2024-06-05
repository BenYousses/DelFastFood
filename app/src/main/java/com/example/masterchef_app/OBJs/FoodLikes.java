package com.example.masterchef_app.OBJs;

public class FoodLikes {

    String userKey ;
 String  foodKey ;
    public FoodLikes() {
    }

    public FoodLikes( String userKey , String foodKey) {

        this.userKey = userKey;
        this.foodKey = foodKey;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getFoodKey() {
        return foodKey;
    }

    public void setFoodKey(String foodKey) {
        this.foodKey = foodKey;
    }
}
