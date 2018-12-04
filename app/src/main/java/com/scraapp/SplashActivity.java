package com.scraapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.fido.fido2.api.common.RequestOptions;
import com.scraapp.network.event.ApiErrorEvent;
import com.scraapp.network.event.ApiErrorWithMessageEvent;
import com.scraapp.network.response.AbstractApiResponse;
import com.scraapp.utility.Constant;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class SplashActivity extends ScrApp {

    Button loginCta, signupCta;
    TextView registerCta;
    LinearLayout signInLayout, signUpLayout;
    TextView mUserName, mPassword;

    public int getlayout() {
        return R.layout.splash_layout;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginCta = findViewById(R.id.login_cta);
        signupCta = findViewById(R.id.signup_cta);
        registerCta = findViewById(R.id.not_signed_user_cta);
        signInLayout = findViewById(R.id.signin_layout);
        signUpLayout = findViewById(R.id.signup_layout);
        mUserName = findViewById(R.id.user_name);
        mPassword = findViewById(R.id.password);

        loginCta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
//                startActivity(intent);
//                finish();

                if(validation()) {
                    if (!mApiClient.isRequestRunning(Constant.SIGNIN_REQUEST_TAG)) {
                        showProgress();
                        mApiClient.signInRequest(Constant.SIGNIN_REQUEST_TAG, mUserName.getText().toString(), mPassword.getText().toString());
                    }
                } else {
                    CommonUtils.displayToast(getContext(), getString(R.string.fill_mandatory_field));
                }
            }
        });

        registerCta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInLayout.setVisibility(View.INVISIBLE);
                signUpLayout.setVisibility(View.VISIBLE);

            }
        });

        signupCta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInLayout.setVisibility(View.VISIBLE);
                signUpLayout.setVisibility(View.INVISIBLE);
            }
        });

        initDialog(getContext());

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        if(signInLayout.getVisibility() == View.VISIBLE) {
            finish();
        } else if(signUpLayout.getVisibility() == View.VISIBLE) {
            signInLayout.setVisibility(View.VISIBLE);
            signUpLayout.setVisibility(View.INVISIBLE);
        }

    }


//    @Override
//    protected void onStop() {
//        super.onStop();
//
//        contentView = null;
//    }

    private boolean validation() {

        if(CommonUtils.textValidation(mUserName) && CommonUtils.textValidation(mPassword)) {
            return true;
        }

        if(!CommonUtils.textValidation(mUserName)) {
            mUserName.setError("");
        }

        if(!CommonUtils.textValidation(mPassword)) {
            mPassword.setError("");
        }

        return false;

    }

    // ============================================================================================
    // EventBus callbacks
    // ============================================================================================

//    @Subscribe
//    public void onEventMainThread(CreateTempImagesFinishedEvent event) {
//        dismissProgress();
//        selectedImagePath = event.bitmapPaths.get(0);
//        Glide.with(getActivity())
//                .load(selectedImagePath)
//                .apply(RequestOptions.centerCropTransform().placeholder(R.mipmap.ic_launcher_round))
//                .into(imgUpload);
//
//    }

    /**
     * Response of Uploaded File
     *
     * @param apiResponse UploadFileResponse
     */
    @Subscribe
    public void onEventMainThread(AbstractApiResponse apiResponse) {
        switch (apiResponse.getRequestTag()) {
            case Constant.SIGNIN_REQUEST_TAG:
                dismissProgress();
                CommonUtils.displayToast(getContext(), apiResponse.getMessage());

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
            case Constant.SIGNIN_REQUEST_TAG:
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
            case Constant.SIGNIN_REQUEST_TAG:
                dismissProgress();
                CommonUtils.displayToast(getContext(), event.getResultMsgUser());
                break;

            default:
                break;
        }
    }

}
