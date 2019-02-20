package com.scraapp;

import android.support.annotation.NonNull;
import android.view.MenuItem;

public class MyOrdersActivity extends ClickAwareActivity {
    @Override
    public int getlayout() {
        return R.layout.myorders_activity;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }
}
