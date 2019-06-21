package com.scraapp.network;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.scraapp.network.event.RequestFinishedEvent;
import com.scraapp.network.request.AbstractApiRequest;
import com.scraapp.network.request.GetAllCategoryRequest;
import com.scraapp.network.request.GetCategoriesRequestParam;
import com.scraapp.network.request.GetOrdersRequest;
import com.scraapp.network.request.GetOrdersRequestParam;
import com.scraapp.network.request.LoginRequestParam;
import com.scraapp.network.request.PlaceOrderRequest;
import com.scraapp.network.request.PlaceOrderRequestParam;
import com.scraapp.network.request.SignInApiRequest;
import com.scraapp.network.request.SignUpRequest;
import com.scraapp.network.request.SignUpVendorRequest;
import com.scraapp.network.request.SignupRequestParam;
import com.scraapp.network.request.SignupVendorRequestParam;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static TimeUnit timeUnit = TimeUnit.SECONDS;
    private Retrofit retrofit;

    private static int HTTP_TIMEOUT = 30; // Seconds by default

    private static final String WS_SCHEME = "https://scra.app/api";
    private static final String WS_PREFIX_DOMAIN = "scra.app";
    private static final String WS_HOSTNAME = "/";
    private static final String WS_SUFFIX_FOLDER = "api/";

    private static String API_BASE_URL = "https://scra.app/api/";

    /**
     * Makes the ApiService calls.
     */
    private ApiService mApiService;

    /**
     * The list of running requests. Used to cancel requests.
     */
    private final Map<String, AbstractApiRequest> requests;

    private Context context;

    public ApiClient(Context context) {

        this.context = context;
        requests = new HashMap<>();
        EventBus.getDefault().register(this);

        initAPIClient();
    }

    private void initAPIClient() {

        OkHttpClient.Builder okBuilder = MyOkHttpBuilder.getOkHttpBuilder(context);

//		okBuilder.retryOnConnectionFailure(true);
//      okBuilder.followRedirects(false);

        OkHttpClient httpClient = okBuilder.connectTimeout(HTTP_TIMEOUT, timeUnit)
                .writeTimeout(HTTP_TIMEOUT, timeUnit)
                .readTimeout(HTTP_TIMEOUT, timeUnit)
                .build();

        Retrofit.Builder builder = new Retrofit.Builder();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

//        Gson gson = new GsonBuilder()
//                .serializeNulls()
//                .disableHtmlEscaping()
//                .setPrettyPrinting()
//                .create();


        retrofit = builder
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(API_BASE_URL)
                .client(httpClient)
                .build();

        mApiService = retrofit.create(ApiService.class);
    }

    private void changeApiBaseUrl(String newApiBaseUrl) {
        API_BASE_URL = newApiBaseUrl;
        initAPIClient();
    }

    public static int getHttpTimeout() {
        return HTTP_TIMEOUT;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public ApiService getApiService() {
        return mApiService;
    }

    @NonNull
    public Map<String, AbstractApiRequest> getRequests() {
        return requests;
    }

    // ============================================================================================
    // Request functions
    // ============================================================================================

    public void getAllCategoriesRequest(GetCategoriesRequestParam getCategoriesRequestParam) {
        GetAllCategoryRequest request = new GetAllCategoryRequest(mApiService, getCategoriesRequestParam);
        requests.put(getCategoriesRequestParam.getmRequestTag(), request);
        request.execute();
    }

    public void signInRequest(LoginRequestParam loginRequestParam) {
        SignInApiRequest request = new SignInApiRequest(mApiService, loginRequestParam);
        requests.put(loginRequestParam.getmRequestTag(), request);
        request.execute();
    }

    public void signUpRequest(SignupRequestParam signupRequestParam) {
        SignUpRequest request = new SignUpRequest(mApiService, signupRequestParam);
        requests.put(signupRequestParam.getmRequestTag(), request);
        request.execute();
    }

    public void getOrdersRequest(GetOrdersRequestParam getOrdersRequestParam) {
        GetOrdersRequest request = new GetOrdersRequest(mApiService, getOrdersRequestParam);
        requests.put(getOrdersRequestParam.getmRequestTag(), request);
        request.execute();
    }

    public void signUpVendorRequest(SignupVendorRequestParam signupRequestParam) {
        SignUpVendorRequest request = new SignUpVendorRequest(mApiService, signupRequestParam);
        requests.put(signupRequestParam.getmRequestTag(), request);
        request.execute();
    }

    public void placeOrderRequest(PlaceOrderRequestParam placeOrderRequestParam) {
        PlaceOrderRequest request = new PlaceOrderRequest(mApiService, placeOrderRequestParam);
        requests.put(placeOrderRequestParam.getmRequestTag(), request);
        request.execute();
    }

    // ============================================================================================
    // Public functions
    // ============================================================================================

    /**
     * Look up the event with the passed tag in the event list. If the request is found, cancel it
     * and remove it from the list.
     *
     * @param requestTag Identifies the request.
     * @return True if the request was cancelled, false otherwise.
     */
    public boolean cancelRequest(String requestTag) {
        System.gc();
        AbstractApiRequest request = requests.get(requestTag);

        if (request != null) {
            request.cancel();
            requests.remove(requestTag);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns true if the request with the passed tag is in the list of running requests, false
     * otherwise.
     */
    public boolean isRequestRunning(String requestTag) {
        return requests.containsKey(requestTag);
    }


    /**
     * A request has finished. Remove it from the list of running requests.
     *
     * @param event The event posted on the EventBus.
     */
    @Subscribe
    public void onEvent(RequestFinishedEvent event) {
        System.gc();
        requests.remove(event.getRequestTag());

    }
}
