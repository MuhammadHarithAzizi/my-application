package com.example.lab_rest.remote;

import com.example.lab_rest.model.User;
import com.example.lab_rest.model.Users;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UserService {

    @FormUrlEncoded
    @POST("users/login")
    Call<Users> login(@Field("username") String username, @Field("password") String password);

}
