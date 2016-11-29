package com.daniel.czaterv2;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.POST;

public interface WebService {

    @POST("/register")
    Call<UserRegistry> createUser(@Body UserRegistry userRegistry);

    @POST("/login")
    Call<User> login(@Field("name") String name,

                     @Field("password")
                             String password
    );

    @POST("/loginAnonymous")
    Call<User> loginAnonymous(@Field("uid") String macAdress
    );

}
