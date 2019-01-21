package com.scraapp;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scraapp.greendao.Categories;
import com.scraapp.greendao.CategoriesDao;
import com.scraapp.greendao.DaoSession;
import com.scraapp.network.request.OrderItems;
import com.scraapp.network.request.PlaceOrderRequestParam;
import com.scraapp.utility.ActionRequest;
import com.scraapp.utility.Constant;

import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductsActivity extends ScrAppActivity {

    Toolbar toolbar;

    private CategoriesDao categoriesDao;
    private Query<Categories> categoriesQuery;
    private DaoSession daoSession;

    private LinearLayout productsLayout;
    private Button confirmCta;

    private Map<String, EditText> productList;
    String lat, lan;

    @Override
    public int getlayout() {
        return R.layout.products_layout;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        productsLayout = findViewById(R.id.products_list_layout);
        confirmCta = findViewById(R.id.confirm_cta);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.app_name);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitleTextColor(Color.WHITE);

        productList = new HashMap<String, EditText>();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            lat = bundle.getString("lat");
            lan = bundle.getString("lan");
        }


        initDAO();
        handleOnclick();

    }

    private void initDAO() {
        // get the note DAO
        daoSession = ((ScrApp) getApplication()).getDaoSession();
        categoriesDao = daoSession.getCategoriesDao();

        // query all Categories, sorted a-z by their text
        categoriesQuery = categoriesDao.queryBuilder().orderAsc(CategoriesDao.Properties.Id).build();
        updateCategories();

    }

    private void handleOnclick() {
        confirmCta.setOnClickListener(view -> {

            List<OrderItems> orderItemsList = new ArrayList<>();

            for(Map.Entry<String, EditText> entry: productList.entrySet()) {
                OrderItems orderItem = new OrderItems();
                orderItem.setUnit("kg");
                orderItem.setWeight(entry.getValue().getText().toString());
                orderItem.setCategory_id(entry.getKey());
                orderItemsList.add(orderItem);
            }


            if (!mApiClient.isRequestRunning(Constant.PLACE_ORDER_REQUEST_TAG)) {
                showProgress();
                PlaceOrderRequestParam placeOrderRequestParam = new PlaceOrderRequestParam(null, Constant.PLACE_ORDER_REQUEST_TAG, orderItemsList);
                placeOrderRequestParam.setAction(ActionRequest.NEW_ORDER);
                placeOrderRequestParam.setUser_id(CommonUtils.getSharedPref(Constant.SP_FILE_LOGIN, Constant.SP_USERID));
                placeOrderRequestParam.setLat(lat);
                placeOrderRequestParam.setLon(lan);
                placeOrderRequestParam.setOrder_placed_date("1");
                mApiClient.placeOrderRequest(placeOrderRequestParam);
            }

        });
    }

    private void updateCategories() {
        List<Categories> categories = categoriesQuery.list();

        String name = categories.get(0).getName();

        for(Categories category: categories) {
            addView(category);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    private void addView(Categories categories) {
        LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.product_dynamic_view, null);

        TextView productName = v.findViewById(R.id.product_name);
        EditText quantityEt = v.findViewById(R.id.quantity);

        productName.setText(categories.getName());
        productList.put(categories.getId(), quantityEt);

        // insert into main view
        LinearLayout insertPoint = findViewById(R.id.products_list_layout);
        insertPoint.addView(v);
    }


}
