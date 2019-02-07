package com.scraapp.network.request;

import com.scraapp.network.ApiService;

import java.util.List;

public class PlaceOrderRequestParam extends RequestParam {

    private String action, user_id, lat, lon, order_placed_date;

    private List<OrderItems> order_items;

    public PlaceOrderRequestParam(ApiService apiService, String requestTag) {
        super(apiService, requestTag);
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getOrder_placed_date() {
        return order_placed_date;
    }

    public void setOrder_placed_date(String order_placed_date) {
        this.order_placed_date = order_placed_date;
    }

    public List<OrderItems> getOrder_items() {
        return order_items;
    }

    public void setOrder_items(List<OrderItems> order_items) {
        this.order_items = order_items;
    }
}
