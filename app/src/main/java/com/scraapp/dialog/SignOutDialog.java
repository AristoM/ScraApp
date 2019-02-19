package com.scraapp.dialog;

import android.Manifest;
import android.content.DialogInterface;
import android.support.v4.app.ActivityCompat;

import com.scraapp.R;

public class SignOutDialog extends YesAndNoDialog {

    public static int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 99;

    @Override
    public String setTitle() {
        return getString(R.string.signout);
    }

    @Override
    public String setMessage() {
        return getString(R.string.do_you_wish_to_logout);
    }

    @Override
    public String positiveText() {
        return getString(R.string.ok);
    }

    @Override
    public String negativeText() {
        return getString(R.string.cancel);
    }

    @Override
    public DialogInterface.OnClickListener positiveButtonClick() {

        return (dialogInterface, i) -> dismiss();
    }

    @Override
    public DialogInterface.OnClickListener negativeButtonClick() {
        return (dialogInterface, i) -> dismiss();
    }

}
