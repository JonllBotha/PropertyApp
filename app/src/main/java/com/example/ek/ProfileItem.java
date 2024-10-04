package com.example.ek;

public class ProfileItem {
    public ProfileItem(int image, String item, int arrow) {
        this.image = image;
        this.item = item;
        this.arrow = arrow;
    }

    String item;
    int image;
    int arrow;

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getArrow() {
        return arrow;
    }

    public void setArrow(int image) {
        this.arrow = arrow;
    }
}
