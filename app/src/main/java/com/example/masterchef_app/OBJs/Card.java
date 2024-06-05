package com.example.masterchef_app.OBJs;

import com.example.masterchef_app.OBJs.Food;
import com.example.masterchef_app.OBJs.User;

import java.io.Serializable;

public class Card implements Serializable {
    private User userOwner;
    private Food foodType;
    private String cardKey ;
    public Card() {
    }

    public Card(User userOwner, Food foodType, String cardKey) {
        this.userOwner = userOwner;
        this.foodType = foodType;
        this.cardKey = cardKey;
    }

    public User getUserOwner() {
        return userOwner;
    }

    public void setUserOwner(User userOwner) {
        this.userOwner = userOwner;
    }

    public Food getFoodType() {
        return foodType;
    }

    public void setFoodType(Food foodType) {
        this.foodType = foodType;
    }

    public String getCardKey() {
        return cardKey;
    }

    public void setCardKey(String cardKey) {
        this.cardKey = cardKey;
    }
}
