package com.scraapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;

import com.scraapp.mediators.BaseMediator;
import com.scraapp.utility.Constant;

public class HomeActivity extends BaseApp implements BaseMediator {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mConfirmPickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent product = new Intent(HomeActivity.this, ProductsActivity.class);
                startActivity(product);

            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_my_orders) {
            CommonUtils.displayToast(mContext, "myorder");
        } else if (id == R.id.nav_settings) {
            CommonUtils.displayToast(mContext, "settings");
        } else if (id == R.id.nav_signout) {
            signOut();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void commitFragment() {


    }

    public void signOut() {
        SharedPreferences preferences = getSharedPreferences(Constant.SP_FILE_LOGIN, 0);
        preferences.edit().remove(Constant.SP_USER_NAME).apply();
        preferences.edit().remove(Constant.SP_PASSWORD).apply();

        Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        CommonUtils.displayToast(mContext, "Signed out");

    }
}
