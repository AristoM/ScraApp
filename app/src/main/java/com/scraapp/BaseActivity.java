package com.scraapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.LinearLayout;

import com.scraapp.dialog.SignOutDialog;
import com.scraapp.dialog.YesAndNoDialog;
import com.scraapp.frgments.BaseFragment;
import com.scraapp.mediators.BaseMediator;
import com.scraapp.utility.Constant;
import com.scraapp.utility.CustomTypefaceSpan;

public class BaseActivity extends ClickAwareActivity implements BaseMediator {

    private static Context instance;
    LinearLayout parentLayout;

    protected NavigationView navView;
    private DrawerLayout mDrawerLayout;


    public static Context getInstance() {
        if(instance == null) {
            instance = new BaseActivity();
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

        baseMediator = this;

        HomeFragment homeFragment = new HomeFragment();
        commitFragment(homeFragment);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        navView = findViewById(R.id.nav_view);
        Menu m = navView.getMenu();
        for (int i = 0; i < m.size(); i++) {
            MenuItem mi = m.getItem(i);

            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }

            //the method we have create in activity
            applyFontToMenuItem(mi);
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navView.setNavigationItemSelectedListener(this);

    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/" + "Raleway-Bold.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    public void commitFragment(Fragment fr) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.parent_layout, fr);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_my_orders) {
//            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
//            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            SettingsFragment fragment = new SettingsFragment();
//            fragmentTransaction.add(R.id.fragment_layout, fragment);
//            fragmentTransaction.commit();

            Intent myorder = new Intent(this, MyOrdersActivity.class);
            startActivity(myorder);
        } else if (id == R.id.nav_settings) {
            Intent settings = new Intent(this, SettingsActivity.class);
            startActivity(settings);
        } else if (id == R.id.nav_signout) {
            SignOutDialog signOutDialog = new SignOutDialog();
            signOutDialog.setListener(BaseActivity.this);
            signOutDialog(signOutDialog);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void signOutDialog(YesAndNoDialog dialogFragment) {

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

//        ApproximateDialog approximateDialog = new ApproximateDialog();
        dialogFragment.show(ft, "dialog");

    }

    public void signOut() {
        SharedPreferences preferences = mContext.getSharedPreferences(Constant.SP_FILE_LOGIN, 0);
        preferences.edit().remove(Constant.SP_USER_NAME).apply();
        preferences.edit().remove(Constant.SP_PASSWORD).apply();

        Intent intent = new Intent(mContext, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constant.IS_FROM_SIGNOUT, true);
        startActivity(intent);

        CommonUtils.displayToast(mContext, "Signed out");

    }

    @Override
    public void onBackPressed() {

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    public void commitFragment(BaseFragment baseFragment) {

    }

    @Override
    public void handleLogout() {
        signOut();
//        Toast.makeText(mContext, "welcome", Toast.LENGTH_SHORT).show();
    }
}
