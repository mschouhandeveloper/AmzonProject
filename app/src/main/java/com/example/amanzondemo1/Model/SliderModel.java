package com.example.amanzondemo1.Model;

public class SliderModel {

    String image;
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SliderModel(String imageurl,String name) {
        this.image = imageurl;
        this.name = name;
    }

    public SliderModel() {

    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
