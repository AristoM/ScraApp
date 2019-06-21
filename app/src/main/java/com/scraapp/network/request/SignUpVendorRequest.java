package com.scraapp.network.request;

import com.scraapp.R;
import com.scraapp.network.ApiService;
import com.scraapp.network.response.AbstractApiResponse;
import com.scraapp.network.response.ApiCallback;

import retrofit2.Call;

public class SignUpVendorRequest extends AbstractApiRequest {

    private SignupVendorRequestParam SignupVendorRequestParam;

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
     * @param SignupVendorRequestParam        Identifies the request.
     */
    public SignUpVendorRequest(ApiService apiService, SignupVendorRequestParam SignupVendorRequestParam) {
        super(apiService, SignupVendorRequestParam.getmRequestTag());
        this.SignupVendorRequestParam = SignupVendorRequestParam;
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

        call = apiService.signUpVendorCall(SignupVendorRequestParam);
        call.enqueue(callback);
    }

}
