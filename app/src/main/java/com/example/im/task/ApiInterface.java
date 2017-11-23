package com.example.im.task;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Im on 21-11-2017.
 */

public interface ApiInterface {
String BASE_URL="http://54.255.191.100:1337/user/ride/";
   @GET("history")
   Call< List<RideHistory> > getDetail();
}

