package com.souvik.myapplication.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Likes implements Serializable {

    @SerializedName("first_name")
    private String firstName;
    private String avatar;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
