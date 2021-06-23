package com.souvik.myapplication;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;
import com.souvik.myapplication.network.ApiClient;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends ViewModel {

    private MutableLiveData<Boolean> loginStatus;
    private MutableLiveData<String> otpData;
    private MutableLiveData<JsonObject> profileData;
    private String TAG = MainViewModel.class.getSimpleName();

    public LiveData<Boolean> getLoginStatus(String phoneNumber){
        loginStatus = new MutableLiveData<>();
        HashMap<String,String> data = new HashMap<String, String>();
        data.put("number",phoneNumber);
        ApiClient.getApiInterface().loginApi(data).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d(TAG, "onResponse: "+response.body());
                if(response.body() !=null && response.body().has("status") && response.body().get("status").getAsBoolean()){
                    loginStatus.setValue(response.body().get("status").getAsBoolean());
                }else {
                    loginStatus.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                loginStatus.setValue(false);
            }
        });
        return loginStatus;
    }

    public LiveData<String> getOTPData(String phoneNumber, String otp){
        otpData = new MutableLiveData<>();
        HashMap<String,String> data = new HashMap<String, String>();
        data.put("number",phoneNumber);
        data.put("otp",otp);
        ApiClient.getApiInterface().verifyOTPApi(data).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d(TAG, "onResponse: "+response.body());
                if(response.body() !=null && response.body().has("token") && !response.body().get("token").isJsonNull()){
                    otpData.setValue(response.body().get("token").getAsString());
                }else {
                    otpData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d(TAG, "onError: "+t.getMessage());
                otpData.setValue(null);
            }
        });
        return otpData;
    }

    public LiveData<JsonObject> getProfileData(String token){
        profileData = new MutableLiveData<>();
        ApiClient.getApiInterface().getProfileList(token).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d(TAG, "onResponse: "+response.body());
                profileData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d(TAG, "onError: "+t.getMessage());
            }
        });
        return profileData;
    }



}
