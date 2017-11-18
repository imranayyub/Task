package com.example.im.task;

/**
 * Created by Im on 04-11-2017.
 */

public class Contact {
    String email, name, date, app, fname, address, pic, galleryImage;

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public void  setPic(String pic){ this.pic=pic;}

    public void setAddress(String address) {
        this.address = address;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public void setGalleryImage(String galleryImage) {
        this.galleryImage = galleryImage;
    }


    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public String getDate() {
        return this.date;
    }

    public String getApp() {
        return this.app;
    }

    public String getFname() {
        return this.fname;
    }

    public String getAddress() {
        return this.address;
    }

    public String  getPic(){ return this.pic;}

    public String getGalleryImage(){ return  this.galleryImage;}
}
