package com.scraapp.network.request;

import com.scraapp.R;
import com.scraapp.network.ApiService;
import com.scraapp.network.response.AbstractApiResponse;
import com.scraapp.network.response.ApiCallback;
import com.scraapp.network.response.SignInResponse;

import retrofit2.Call;

public class SignUpRequest extends AbstractApiRequest {

    private SignupRequestParam signupRequestParam;

    /**
     * + The callback used for this request. Declared globally for cancellation. See {@link
     * #cancel()}.
     */
    private ApiCallback<AbstractApiResponse> callback;
    /**
     * To cancel REST API call from Retrofit. See {@link #cancel()}.
     */
    private Call<AbstractApiResponse> call;

    /**
     * Initialize the request with the passed values.
     *
     * @param apiService The {@link ApiService} used for executing the calls.
     * @param signupRequestParam        Identifies the request.
     */
    public SignUpRequest(ApiService apiService, SignupRequestParam signupRequestParam) {
        super(apiService, signupRequestParam.getmRequestTag());
        this.signupRequestParam = signupRequestParam;
    }

    @Override
    public void cancel() {
        callback.invalidate();
        call.cancel();
    }

    @Override
    public void execute() {
        callback = new ApiCallback<>(tag);
        if (!isInternetActive()) {
            callback.postUnexpectedError(context.getString(R.string.error_no_internet));
            return;
        }

        call = apiService.signUpCall(signupRequestParam);
        call.enqueue(callback);
    }
}
