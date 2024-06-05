package com.example.masterchef_app.OBJs;

import com.example.masterchef_app.OBJs.Food;
import com.example.masterchef_app.OBJs.MessageModel;

public class BunnerModel {
   private String bunnerImg ;

private String bunnerKey ;

private Food food  ;

    public BunnerModel(String bunnerImg ,String bunnerKey , Food food) {
        this.bunnerImg = bunnerImg;
        this.bunnerKey = bunnerKey;
        this.food = food;
    }
    public BunnerModel() {
    }

    public String getBunnerImg() {
        return bunnerImg;
    }

    public void setBunnerImg(String bunnerImg) {
        this.bunnerImg = bunnerImg;
    }

    public String getBunnerKey() {
        return bunnerKey;
    }

    public void setBunnerKey(String bunnerKey) {
        this.bunnerKey = bunnerKey;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }
}
