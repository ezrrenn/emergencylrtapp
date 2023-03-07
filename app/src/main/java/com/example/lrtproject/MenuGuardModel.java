package com.example.lrtproject;

public class MenuGuardModel {
    private int imageG;
    private String textG;

    public MenuGuardModel(int imageG, String textG) {
        this.imageG = imageG;
        this.textG = textG;
    }

    public MenuGuardModel(String e, int n){

    }

    public int getImageG() {
        return imageG;
    }

    public String getTextG() {
        return textG;
    }
}
