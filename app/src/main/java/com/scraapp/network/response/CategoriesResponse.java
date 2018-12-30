package com.scraapp.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoriesResponse extends AbstractApiResponse {

    @SerializedName("result")
    @Expose
    private Result result;

    public class Result {

        @SerializedName("categories")
        private List<Products> products;

        public List<Products> getProducts() {
            return products;
        }

        public void setProducts(List<Products> products) {
            this.products = products;
        }
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}
