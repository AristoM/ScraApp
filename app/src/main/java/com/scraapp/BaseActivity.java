package com.scraapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scraapp.dialog.SignOutDialog;
import com.scraapp.dialog.YesAndNoDialog;
import com.scraapp.frgments.BaseFragment;
import com.scraapp.greendao.Categories;
import com.scraapp.mediators.BaseMediator;
import com.scraapp.network.event.ApiErrorEvent;
import com.scraapp.network.event.ApiErrorWithMessageEvent;
import com.scraapp.network.request.GetCategoriesRequestParam;
import com.scraapp.network.response.CategoriesResponse;
import com.scraapp.network.response.Products;
import com.scraapp.utility.ActionRequest;
import com.scraapp.utility.Constant;
import com.scraapp.utility.CustomTypefaceSpan;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

public class BaseActivity extends ClickAwareActivity implements BaseMediator {

//    private static Context instance;
    LinearLayout parentLayout;

    protected NavigationView navView;
    private DrawerLayout mDrawerLayout;
    private TextView userName, userMailId;


//    public static Context getInstance() {
//        if(instance == null) {
//            instance = new BaseActivity();
//        }
//        return instance;
//    }

    @Override
    public int getlayout() {
        return R.layout.base_activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.base_activity);

        parentLayout = findViewById(R.id.parent_layout);

        baseMediator = this;

        HomeFragment homeFragment = new HomeFragment();
        commitFragment(homeFragment);

        navView = findViewById(R.id.nav_view);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        View headerLayout = navView.getHeaderView(0);
        userName = headerLayout.findViewById(R.id.user_name);
        userMailId = headerLayout.findViewById(R.id.user_mail_id);


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

        String sUname = CommonUtils.getSharedPref(Constant.SP_FILE_LOGIN, Constant.SP_USER_MAIL_ID);
        userMailId.setText(sUname);

        initApi();

    }

    private void initApi() {
        GetCategoriesRequestParam getCategoriesRequestParam = new GetCategoriesRequestParam(null,
                Constant.GET_CATEGORIES_REQUEST_TAG, ActionRequest.GET_CATEGORIES);
        mApiClient.getAllCategoriesRequest(getCategoriesRequestParam);
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
        } /*else if (id == R.id.nav_settings) {
            Intent settings = new Intent(this, SettingsActivity.class);
            startActivity(settings);
        }*/ else if (id == R.id.nav_signout) {
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
        preferences.edit().remove(Constant.SP_USER_MAIL_ID).apply();
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

    @Override
    public void finishActivity() {

    }

    /**
     * Response of Uploaded File
     *
     * @param apiResponse UploadFileResponse
     */
    @Subscribe
    public void onEventMainThread(CategoriesResponse apiResponse) {
        switch (apiResponse.getRequestTag()) {
            case Constant.GET_CATEGORIES_REQUEST_TAG:
                dismissProgress();
//                CommonUtils.displayToast(getContext(), apiResponse.getStatus());

                List<Products> categoriesList = apiResponse.getResult().getProducts();
                deleteDB();
                for(Products products: categoriesList) {
                    Categories categories = new Categories(null, products.getId(), products.getName(), products.getDescription());
                    categoriesDao.insert(categories);
                }

                break;
            default:
                break;
        }
    }

    /**
     * EventBus listener. An API call failed. No error message was returned.
     *
     * @param event ApiErrorEvent
     */
    @Subscribe
    public void onEventMainThread(ApiErrorEvent event) {
        switch (event.getRequestTag()) {
            case Constant.GET_CATEGORIES_REQUEST_TAG:
                dismissProgress();
                CommonUtils.displayToast(getContext(), event.getRetrofitError().toString());
                Log.e("okhttp", event.getRetrofitError().toString());
                break;
            default:
                break;
        }
    }

    /**
     * EventBus listener. An API call failed. An error message was returned.
     *
     * @param event ApiErrorWithMessageEvent Contains the error message.
     */
    @Subscribe
    public void onEventMainThread(ApiErrorWithMessageEvent event) {
        switch (event.getRequestTag()) {
            case Constant.GET_CATEGORIES_REQUEST_TAG:
                dismissProgress();
                CommonUtils.displayToast(getContext(), event.getResultMsgUser());
                break;

            default:
                break;
        }
    }

    private void deleteDB() {
        categoriesDao.deleteAll();
    }
}
