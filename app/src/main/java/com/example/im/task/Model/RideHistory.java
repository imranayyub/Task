package com.example.im.task.Model;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;

/**
 * Created by Im on 21-11-2017.
 */

public class RideHistory {
    // Serializing Json.
    @SerializedName("order_id")
    private String booking_id;
    @SerializedName("image")
    private String image;
    @SerializedName("driver_name")
    private String driver_name;
    @SerializedName("createdAt")
    private String time;

    //constructor
    public RideHistory(String booking_id, String image, String driver_name, String time) throws JSONException {
        this.booking_id = booking_id;
        this.image = image;
        this.driver_name = driver_name;
        this.time = time;
    }

    public String getBooking_id() {
        return booking_id;
    }

    public String getImage() {
        return image;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public String getTime() {
        return time;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public void setTime(String time) {

        this.time = time;
    }

}