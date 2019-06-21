package com.scraapp.network.request;

import com.scraapp.R;
import com.scraapp.network.ApiService;
import com.scraapp.network.response.AbstractApiResponse;
import com.scraapp.network.response.ApiCallback;

import retrofit2.Call;

public class GetOrdersRequest extends AbstractApiRequest {


    private GetOrdersRequestParam getOrdersRequestParam;

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
     * @param getOrdersRequestParam        Identifies the request.
     */
    public GetOrdersRequest(ApiService apiService, GetOrdersRequestParam getOrdersRequestParam) {
        super(apiService, getOrdersRequestParam.getmRequestTag());
        this.getOrdersRequestParam = getOrdersRequestParam;
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

        call = apiService.getOrders(getOrdersRequestParam);
        call.enqueue(callback);
    }
}
