package com.souvik.myapplication.network;

import com.google.gson.JsonObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiInterface {

    @Headers({"Accept: application/json", "Content-Type: application/json"})
    @POST("/V1/users/phone_number_login")
    Call<JsonObject> loginApi(@Body HashMap<String,String> data);

    @Headers({"Accept: application/json", "Content-Type: application/json"})
    @POST("/V1/users/verify_otp")
    Call<JsonObject> verifyOTPApi(@Body HashMap<String,String> data);

    @Headers({"Accept: application/json", "Content-Type: application/json"})
    @GET("/V1/users/test_profile_list")
    Call<JsonObject> getProfileList(@Header("Authorization") String auth);
}
