package com.scraapp.network;

import com.scraapp.network.request.GetCategoriesRequestParam;
import com.scraapp.network.request.GetOrdersRequestParam;
import com.scraapp.network.request.LoginRequestParam;
import com.scraapp.network.request.PlaceOrderRequestParam;
import com.scraapp.network.request.SignupRequestParam;
import com.scraapp.network.request.SignupVendorRequestParam;
import com.scraapp.network.response.AbstractApiResponse;
import com.scraapp.network.response.CategoriesResponse;
import com.scraapp.network.response.PlaceOrderResponse;
import com.scraapp.network.response.SignInResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

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

    @POST("users/")
    Call<AbstractApiResponse> getOrders(@Body GetOrdersRequestParam body);

    @POST("vendors/")
    Call<AbstractApiResponse> signUpVendorCall(@Body SignupVendorRequestParam body);

    @POST("categories/")
    Call<CategoriesResponse> getAllCategories(@Body GetCategoriesRequestParam body);

    @POST("orders/")
    Call<PlaceOrderResponse> placeOrder(@Body PlaceOrderRequestParam body);

}
