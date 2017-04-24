package com.abbytech.shoppingapp.account;

import com.google.gson.annotations.SerializedName;

public class RegisterData {
    @SerializedName("Email")
    public String email;
    @SerializedName("Username")
    public String username;
    @SerializedName("Password")
    public String password;
}
