package com.daniel.czaterv2;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface WebService {

    @POST("/login")
    Call<Void> login(@Body User user);

}
