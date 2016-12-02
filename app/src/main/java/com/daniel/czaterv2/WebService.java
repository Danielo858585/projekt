package com.daniel.czaterv2;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.POST;

public interface WebService {

    @POST("/puszek-1.0.0-SNAPSHOT/register")
    Call<Void> createUser(@Body UserRegistry userRegistry);

    @POST("/login")
    Call<User> login(@Field("name") String name,

                     @Field("password")
                             String password
    );

    @POST("/puszek-1.0.0-SNAPSHOT/anonymousLogin")
    Call<UserAnonymous> userAnonymous (@Body AnonymousClass anonymousClass);

}
