package com.example.root.authex;

public class Subscription {
    private int image_id;
    private String userAddress;
    private String email;
    private String subscription;

    public Subscription(){

    }

    public Subscription(int image_id, String userAddress, String email, String subscription) {
        this.image_id = image_id;
        this.userAddress = userAddress;
        this.email = email;
        this.subscription = subscription;
    }

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubscription() {
        return subscription;
    }

    public void setSubscription(String subscription) {
        this.subscription = subscription;
    }
}
