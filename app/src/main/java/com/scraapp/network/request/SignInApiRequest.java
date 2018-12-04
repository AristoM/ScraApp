package com.scraapp.network.request;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.scraapp.R;
import com.scraapp.network.ApiService;
import com.scraapp.network.response.AbstractApiResponse;
import com.scraapp.network.response.ApiCallback;
import com.scraapp.network.response.SignInResponse;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

public class SignInApiRequest extends AbstractApiRequest {

    String mUserName, mPassword;

    /**
     * + The callback used for this request. Declared globally for cancellation. See {@link
     * #cancel()}.
     */
    ApiCallback<SignInResponse> callback;
    /**
     * To cancel REST API call from Retrofit. See {@link #cancel()}.
     */
    private Call<SignInResponse> call;

    /**
     * Initialize the request with the passed values.
     *
     * @param apiService The {@link ApiService} used for executing the calls.
     * @param tag        Identifies the request.
     */
    public SignInApiRequest(ApiService apiService, String tag, String mUserName, String mPassword) {
        super(apiService, tag);
        this.mUserName = mUserName;
        this.mPassword = mPassword;
    }

    @Override
    public void execute() {
        callback = new ApiCallback<>(tag);
        if (!isInternetActive()) {
            callback.postUnexpectedError(context.getString(R.string.error_no_internet));
            return;
        }

//        Gson gson = new Gson();
//        Map<String, String> params = new HashMap<>();
//        params.put("email", mUserName);
//        params.put("password", mPassword);
//        params.put("action", "test");

//        call = apiService.signInCall(gson.toJsonTree(params).getAsJsonObject());
        call = apiService.signInCall(new LoginRequestParam("login", mUserName, mPassword));
//        call = apiService.signInCall(params);
        call.enqueue(callback);

    }

    @Override
    public void cancel() {
        callback.invalidate();
        call.cancel();
    }
}
