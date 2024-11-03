package com.example.ek;

public class ListingItem {
    public ListingItem(String imagePath, String title, String location, String price, int bath, int bed) {
        this.imagePath = imagePath;
        this.title = title;
        this.location = location;
        this.price = price;
        this.bath = bath;
        this.bed = bed;
    }

    String imagePath;
    String title;
    String location;
    String price;
    int bath;
    int bed;

    public String getImage() {
        return imagePath;
    }

    public void setImage(int image) {
        this.imagePath = imagePath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getBath() {
        return bath;
    }

    public void setBath(int bath) {
        this.bath = bath;
    }

    public int getBed() {
        return bed;
    }

    public void setBed(int bed) {
        this.bed = bed;
    }
}



