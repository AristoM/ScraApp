package com.scraapp.network.request;

import com.scraapp.R;
import com.scraapp.network.ApiService;
import com.scraapp.network.response.ApiCallback;
import com.scraapp.network.response.ApiCallbackDummy;
import com.scraapp.network.response.SignInResponse;

import retrofit2.Call;

public class SignInApiRequest extends AbstractApiRequest {

    private LoginRequestParam loginRequestParam;

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
     * @param loginRequestParam The {@link ApiService} used for executing the calls.
     */
    public SignInApiRequest(LoginRequestParam loginRequestParam) {
        super(loginRequestParam.getmApiService(), loginRequestParam.getmRequestTag());
        this.loginRequestParam = loginRequestParam;
    }

    @Override
    public void execute() {
        callback = new ApiCallbackDummy<>(tag);
        if (!isInternetActive()) {
            callback.postUnexpectedError(context.getString(R.string.error_no_internet));
            return;
        }

        call = apiService.signInCall(loginRequestParam);
        call.enqueue(callback);

    }

    @Override
    public void cancel() {
        callback.invalidate();
        call.cancel();
    }
}
