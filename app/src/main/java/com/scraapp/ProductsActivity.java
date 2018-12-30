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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scraapp.greendao.Categories;
import com.scraapp.greendao.CategoriesDao;
import com.scraapp.greendao.DaoSession;

import org.greenrobot.greendao.query.Query;

import java.util.List;

public class ProductsActivity extends AppCompatActivity {

    Toolbar toolbar;

    private CategoriesDao categoriesDao;
    private Query<Categories> categoriesQuery;
    private DaoSession daoSession;

    private LinearLayout productsLayout;
    private ImageView plusCta;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.products_layout);

        productsLayout = findViewById(R.id.products_list_layout);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.app_name);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitleTextColor(Color.WHITE);

        initDAO();

    }

    private void initDAO() {
        // get the note DAO
        daoSession = ((ScrApp) getApplication()).getDaoSession();
        categoriesDao = daoSession.getCategoriesDao();

        // query all Categories, sorted a-z by their text
        categoriesQuery = categoriesDao.queryBuilder().orderAsc(CategoriesDao.Properties.Id).build();
        updateCategories();

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

        // fill in any details dynamically here
        ImageView plusCta = v.findViewById(R.id.plus_cta);
        TextView productName = v.findViewById(R.id.product_name);
        EditText editText = v.findViewById(R.id.quantity);

        editText.setEnabled(true);
        editText.setClickable(true);
        editText.setFocusable(true);

        productName.setText(name);

        // insert into main view
        ViewGroup insertPoint = (ViewGroup) findViewById(R.id.products_list_layout);
        insertPoint.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
    }


}
