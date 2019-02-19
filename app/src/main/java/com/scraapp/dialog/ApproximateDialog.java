package com.scraapp.dialog;

import android.content.DialogInterface;

import com.scraapp.R;

public class ApproximateDialog extends YesAndNoDialog {

    @Override
    public String setTitle() {
        return null;
    }

    @Override
    public String setMessage() {
        return getString(R.string.give_your_approximate_value);
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
