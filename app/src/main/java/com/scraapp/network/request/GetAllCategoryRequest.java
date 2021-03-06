package com.scraapp.network.request;

import com.scraapp.R;
import com.scraapp.network.ApiService;
import com.scraapp.network.response.ApiCallback;
import com.scraapp.network.response.CategoriesResponse;

import retrofit2.Call;

public class GetAllCategoryRequest extends AbstractApiRequest {

    private GetCategoriesRequestParam getCategoriesRequestParam;

    /**
     * + The callback used for this request. Declared globally for cancellation. See {@link
     * #cancel()}.
     */
    private ApiCallback<CategoriesResponse> callback;
    /**
     * To cancel REST API call from Retrofit. See {@link #cancel()}.
     */
    private Call<CategoriesResponse> call;

    /**
     * Initialize the request with the passed values.
     *
     * @param apiService The {@link ApiService} used for executing the calls.
     */
    public GetAllCategoryRequest(ApiService apiService, GetCategoriesRequestParam getCategoriesRequestParam) {
        super(apiService, getCategoriesRequestParam.getmRequestTag());

        this.getCategoriesRequestParam = getCategoriesRequestParam;
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

        call = apiService.getAllCategories(getCategoriesRequestParam);
        call.enqueue(callback);
    }
}
