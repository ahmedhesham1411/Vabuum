package com.example.onlineshopping.Model;

public class Home_model {
    String name;
    String discription;
    int image;

    public Home_model(String name, String discription, int image) {
        this.name = name;
        this.discription = discription;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
