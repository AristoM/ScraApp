package com.scraapp;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.scraapp.greendao.Categories;
import com.scraapp.greendao.CategoriesDao;
import com.scraapp.greendao.DaoSession;

import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.List;

public class ProductsActivity extends AppCompatActivity {

    Toolbar toolbar;

    private CategoriesDao categoriesDao;
    private Query<Categories> categoriesQuery;
    private DaoSession daoSession;

    private LinearLayout productsLayout;
    private Button confirmCta;

    private List<EditText> productsEt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.products_layout);

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

        productsEt = new ArrayList<>();

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
        confirmCta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void updateCategories() {
        List<Categories> categories = categoriesQuery.list();

        String name = categories.get(0).getName();

        for(Categories category: categories) {
            addView(category.getName());
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

    private void addView(String name) {
        LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.product_dynamic_view, null);

        TextView productName = v.findViewById(R.id.product_name);
        EditText quantityEt = v.findViewById(R.id.quantity);

        productName.setText(name);
        productsEt.add(quantityEt);

        // insert into main view
        LinearLayout insertPoint = findViewById(R.id.products_list_layout);
        insertPoint.addView(v);
    }


}
