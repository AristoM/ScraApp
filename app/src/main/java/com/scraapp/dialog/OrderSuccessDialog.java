package com.scraapp.dialog;

import android.content.DialogInterface;

import com.scraapp.R;

public class OrderSuccessDialog extends YesAndNoDialog {

    @Override
    public String setTitle() {
        return getString(R.string.successful);
    }

    @Override
    public String setMessage() {
        return getString(R.string.your_order_has_been_placed);
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
        return null;
    }

}
