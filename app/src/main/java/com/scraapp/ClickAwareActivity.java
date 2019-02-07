package com.scraapp;

import android.content.Intent;
import android.telecom.Call;
import android.util.Log;

import com.scraapp.mediators.CallbackMediator;
import com.scraapp.network.response.AbstractApiResponse;

import org.greenrobot.eventbus.Subscribe;

import static com.scraapp.HomeFragment.REQUEST_CHECK_SETTINGS;

abstract public class ClickAwareActivity extends ScrAppActivity {

    public static CallbackMediator mCallbackMediator;
    protected static final int REQUEST_CODE_AUTOCOMPLETE = 11;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case RESULT_OK:
                        Log.e("Settings", "Result OK");
//                        updateGPSStatus("GPS is Enabled in your device");
                        //startLocationUpdates();

//                        HomeFragment fragment = (HomeFragment) getFragmentManager().findFragmentById(R.id.example_fragment);
//                        fragment.
                        mCallbackMediator.locationOk();

                        break;
                    case RESULT_CANCELED:
                        Log.e("Settings", "Result Cancel");
//                        updateGPSStatus("GPS is Disabled in your device");
                        mCallbackMediator.locationNoThanks();
                        break;
                }
                break;
            case REQUEST_CODE_AUTOCOMPLETE:
                if (resultCode == RESULT_OK) {
                    mCallbackMediator.autoCompleteCallback(data);
                }

                break;
        }
    }

    public static void setUpListeners(CallbackMediator callbackMediator) {
        mCallbackMediator = callbackMediator;
    }

    @Subscribe
    public void onEventMainThread(AbstractApiResponse apiResponse) {
        switch (apiResponse.getRequestTag()) {

        }

    }


}
