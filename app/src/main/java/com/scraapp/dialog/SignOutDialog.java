package com.scraapp.dialog;

import android.content.DialogInterface;

import com.scraapp.R;
import com.scraapp.mediators.BaseMediator;

public class SignOutDialog extends YesAndNoDialog {

    public static int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 99;
    BaseMediator baseMediator;

    public SignOutDialog() {
//        baseMediator = (BaseMediator) BaseActivity.getInstance();
    }

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

    public void setListener(BaseMediator listener) {
        baseMediator = listener;
    }

    @Override
    public DialogInterface.OnClickListener positiveButtonClick() {

        return (dialogInterface, i) -> {
            dismiss();
            baseMediator.handleLogout();
        };
    }

    @Override
    public DialogInterface.OnClickListener negativeButtonClick() {
        return (dialogInterface, i) -> dismiss();
    }

}
