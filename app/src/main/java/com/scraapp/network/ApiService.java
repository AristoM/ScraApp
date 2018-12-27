package com.scraapp.network;

import com.scraapp.network.request.GetCategoriesRequestParam;
import com.scraapp.network.request.LoginRequestParam;
import com.scraapp.network.request.SignupRequestParam;
import com.scraapp.network.response.AbstractApiResponse;
import com.scraapp.network.response.SignInResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

//    /**
//     * Upload file to server
//     *
//     * @param username
//     * @param password
//     * @return
//     */
//    @POST("{endPoint}")
//    Call<AbstractApiResponse> signInCall(
//            @Path("endPoint") String endPoint,
//            @Query("action") String action,
//            @Query("email") String username,
//            @Query("password") String


    @POST("users/")
    Call<SignInResponse> signInCall(@Body LoginRequestParam body);

    @POST("users/")
    Call<AbstractApiResponse> signUpCall(@Body SignupRequestParam body);

    @POST("categories/")
    Call<AbstractApiResponse> getAllCategories(@Body GetCategoriesRequestParam body);


}
