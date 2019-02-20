package com.scraapp;

import android.support.annotation.NonNull;
import android.view.MenuItem;

public class SettingsActivity extends ClickAwareActivity {
    @Override
    public int getlayout() {
        return R.layout.settings_fragment;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }
}
