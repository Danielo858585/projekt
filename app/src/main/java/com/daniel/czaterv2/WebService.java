package com.daniel.czaterv2;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface WebService {

    @POST("/puszek/register")
    Call<Void> createUser(@Body UserRegistry userRegistry);

    @POST("/puszek/loginAnonymous")
    Call<UserAnonymous> userAnonymous (@Body AnonymousSend anonymousSend);

    @POST("/puszek/login")
    Call<UserLoginResponse> userLogin (@Body UserLoginRequest userLoginRequest);

    @Headers("token:lingeringField-102eb2d51e27431ea9f8e695f85a3573")
    @POST("/puszek/addChat")
    Call<AddCzatResponse> addCzat (@Body AddCzatRequest AddCzatRequest);

    @POST("/puszek/getChatList")
    Call <List<ChatId>> getChatList (@Body GetChatList getChatList);

}

/*
TOKEN w headerze REQUESTA

* REQ /login
* {
*   "login or email" : "viader"
*   "password" : "1234"
* }
* RES
* {
*   "name" : "viader"
*   "token" : "viader-token123"
* }
*-----------------------------------------------
* REQ /loginAnonymous
* {
*   "uid" : "MAC karty"
* }
* RES
* {
*   jak w login
* }
*----------------------------------------------
* REQ /addChat
* {
*   "latitude" : 12.22
*   "longitude" : 12.12312
*   "range" : 1000
*   "name" : "nazwa"
*   "naxUsersNumber : 10
* }
* RES
* {
*   "id" : "unikalneIdChatu"
* }
* ---------------------------------------------
* REQ /getChatList
* REQ
* {
*   "latitude" : 123.234
*   "longitude" : 123.321
* }
* RES
* {
*   "chats" : [{unikalneIDChatu},
*              {unikalneIDChatu},
*               ...
*               ]
* }
*
*
*
* */