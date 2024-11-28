package com.example.sweetcupcakes;

public class Cupcake {
    private final String itemName;
    private final String itemPrice;
    private final String imageUrl;

    public Cupcake(String itemName, String itemPrice, String imageUrl) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.imageUrl = imageUrl;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

}

