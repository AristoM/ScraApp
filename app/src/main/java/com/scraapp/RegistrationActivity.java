package com.scraapp;

import android.support.annotation.NonNull;
import android.view.MenuItem;

public class RegistrationActivity extends ScrAppActivity {

    @Override
    public int getlayout() {
        return R.layout.register_layout;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }
}
