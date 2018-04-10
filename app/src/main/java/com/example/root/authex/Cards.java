package com.example.root.authex;

/**
 * Created by root on 3/19/18.
 */

public class Cards {
    private int image_id;
    private String cardtype;
    private String card_name;
    private String email;
    private String mno;
    private String address;
    private String price;
    private String socketindex;

    public Cards(){

    }

    public Cards(int image_id, String card_name, String email, String mno, String address, String price, String socketindex, String cardtype) {
        this.image_id = image_id;
        this.card_name = card_name;
        this.email = email;
        this.mno = mno;
        this.address = address;
        this.price = price;
        this.socketindex = socketindex;
        this.cardtype = cardtype;
    }

    public String getCardtype() {
        return cardtype;
    }

    public void setCardtype(String cardtype) {
        this.cardtype = cardtype;
    }

    public String getSocketindex() {
        return socketindex;
    }

    public void setSocketindex(String socketindex) {
        this.socketindex = socketindex;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    public String getcard_name() {
        return card_name;
    }

    public void setcard_name(String card_name) {
        this.card_name = card_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMno() {
        return mno;
    }

    public void setMno(String mno) {
        this.mno = mno;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
