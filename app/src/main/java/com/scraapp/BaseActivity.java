package com.scraapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.widget.LinearLayout;

public class BaseActivity extends ClickAwareActivity {

    private static Context instance;
    LinearLayout parentLayout;

    private DrawerLayout mDrawerLayout;


    public Context getInstance() {
        if(instance == null) {
            instance = BaseActivity.this;
        }
        return instance;
    }

    @Override
    public int getlayout() {
        return R.layout.home_activity_test;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.home_activity_test);

        parentLayout = findViewById(R.id.parent_layout);

        HomeFragment homeFragment = new HomeFragment();
        commitFragment(homeFragment);

        mDrawerLayout = findViewById(R.id.drawer_layout);

    }

    public void commitFragment(Fragment fr) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.parent_layout, fr);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }
}
