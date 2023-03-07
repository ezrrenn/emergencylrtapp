package com.example.lrtproject;

public class MenuAuthorityModel {

    private int imageA;
    private String textA;

    public MenuAuthorityModel(int imageA, String textA){
        this.imageA = imageA;
        this.textA = textA;
    }

    public MenuAuthorityModel(String k, int l) {
    }

    public int getImage(){
        return imageA;
    }

    public String getText(){
        return textA;
    }
}
