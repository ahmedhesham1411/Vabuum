package com.example.onlineshopping.Model;

public class Item {
    String userid;
    String imageURL;
    String item_desc;
    String item_id;
    String item_name;
    String item_price;
    String cat;

    public Item(String userid, String imageURL, String item_desc, String item_id, String item_name, String item_price,String cat) {
        this.userid = userid;
        this.imageURL = imageURL;
        this.item_desc = item_desc;
        this.item_id = item_id;
        this.item_name = item_name;
        this.item_price = item_price;
        this.cat=cat;
    }

    public Item() {
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getItem_desc() {
        return item_desc;
    }

    public void setItem_desc(String uitem_desc) {
        this.item_desc = uitem_desc;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_price() {
        return item_price;
    }

    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }
}
