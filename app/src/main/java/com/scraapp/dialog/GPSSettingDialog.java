package com.scraapp.dialog;

import android.Manifest;
import android.content.DialogInterface;
import android.support.v4.app.ActivityCompat;

import com.scraapp.R;

public class GPSSettingDialog extends YesOrNoDialog {

    int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    @Override
    public String setTitle() {
        return getString(R.string.permission);
    }

    @Override
    public String setMessage() {
        return getString(R.string.need_to_access_your_location);
    }

    @Override
    public String positiveText() {
        return getString(R.string.settings);
    }

    @Override
    public String negativeText() {
        return getString(R.string.cancel);
    }

    @Override
    public DialogInterface.OnClickListener positiveButtonClick() {

        return (dialogInterface, i) -> ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
    }

    @Override
    public DialogInterface.OnClickListener negativeButtonClick() {
        return (dialogInterface, i) -> dismiss();
    }


}
