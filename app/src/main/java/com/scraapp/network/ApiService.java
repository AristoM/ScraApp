package com.scraapp.network;

import com.google.android.gms.maps.MapView;
import com.google.gson.JsonObject;
import com.scraapp.network.request.LoginRequestParam;
import com.scraapp.network.response.AbstractApiResponse;
import com.scraapp.network.response.SignInResponse;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
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
    Call<SignInResponse> signInCall(
            @Body LoginRequestParam body);


}