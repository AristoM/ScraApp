package com.scraapp.network.request;

import com.google.android.gms.awareness.snapshot.PlacesResponse;
import com.scraapp.R;
import com.scraapp.network.ApiService;
import com.scraapp.network.response.AbstractApiResponse;
import com.scraapp.network.response.ApiCallback;
import com.scraapp.network.response.PlaceOrderResponse;

import retrofit2.Call;

public class PlaceOrderRequest extends AbstractApiRequest {

    private PlaceOrderRequestParam placeOrderRequestParam;

    /**
     * + The callback used for this request. Declared globally for cancellation. See {@link
     * #cancel()}.
     */
    private ApiCallback<PlaceOrderResponse> callback;
    /**
     * To cancel REST API call from Retrofit. See {@link #cancel()}.
     */
    private Call<PlaceOrderResponse> call;

    /**
     * Initialize the request with the passed values.
     *
     * @param apiService The {@link ApiService} used for executing the calls.
     * @param placeOrderRequestParam        Identifies the request.
     */
    public PlaceOrderRequest(ApiService apiService, PlaceOrderRequestParam placeOrderRequestParam) {
        super(apiService, placeOrderRequestParam.getmRequestTag());
        this.placeOrderRequestParam = placeOrderRequestParam;
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

        call = apiService.placeOrder(placeOrderRequestParam);
        call.enqueue(callback);
    }
}
