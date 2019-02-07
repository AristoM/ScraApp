package com.scraapp.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

abstract public class YesOrNoDialog extends DialogFragment {

    AlertDialog.Builder builder;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        if (getArguments() != null) {
            if (getArguments().getBoolean("notAlertDialog")) {
                return super.onCreateDialog(savedInstanceState);
            }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(setTitle());
        builder.setMessage(setMessage());
        builder.setPositiveButton(positiveText(), positiveButtonClick());
        builder.setNegativeButton(negativeText(), negativeButtonClick());

        return builder.create();

    }

    public abstract String setTitle();

    public abstract String setMessage();

    public abstract String positiveText();

    public abstract String negativeText();

    public abstract DialogInterface.OnClickListener positiveButtonClick();

    public abstract DialogInterface.OnClickListener negativeButtonClick();

}
