package com.scraapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.scraapp.network.event.ApiErrorEvent;
import com.scraapp.network.event.ApiErrorWithMessageEvent;
import com.scraapp.network.request.LoginRequestParam;
import com.scraapp.network.request.SignupRequestParam;
import com.scraapp.network.response.AbstractApiResponse;
import com.scraapp.network.response.SignInResponse;
import com.scraapp.utility.ActionRequest;
import com.scraapp.utility.Constant;

import org.greenrobot.eventbus.Subscribe;

public class SplashActivity extends ScrAppActivity {

    Button loginCta, signupCta;
    TextView registerCta;
    LinearLayout signInLayout, signUpLayout;
    EditText mUserName, mPassword, mUserNameSignup, mPasswordSignup, mConfirmPassword, mMobileSignup, mEmailSignup;
    String sUname, sPwd;

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
        mUserNameSignup = findViewById(R.id.user_name_signup);
        mPasswordSignup = findViewById(R.id.password_signup);
        mConfirmPassword = findViewById(R.id.confirm_password_signup);
        mMobileSignup = findViewById(R.id.mobile_signup);
        mEmailSignup = findViewById(R.id.email_signup);

        if(!TextUtils.isEmpty(CommonUtils.getSharedPref(this, Constant.SP_FILE_LOGIN, Constant.SP_USER_NAME))) {
            loginProcess();
        } else {
            signInLayout.setVisibility(View.VISIBLE);
            signUpLayout.setVisibility(View.INVISIBLE);
        }

        loginCta.setOnClickListener(view -> {

            if(validation(ActionRequest.LOGIN)) {
                sUname = mUserName.getText().toString();
                sPwd = mPassword.getText().toString();
                loginProcess();
            } else {
                CommonUtils.displayToast(getContext(), getString(R.string.fill_mandatory_field));
            }
        });

        registerCta.setOnClickListener(view -> {
                signInLayout.setVisibility(View.INVISIBLE);
                signUpLayout.setVisibility(View.VISIBLE);

        });

        signupCta.setOnClickListener(view -> {

            if(validation(ActionRequest.REGISTER)) {
                if (!mApiClient.isRequestRunning(Constant.SIGNIN_REQUEST_TAG)) {
                    showProgress();
                    SignupRequestParam signupRequestParam = new SignupRequestParam(ActionRequest.REGISTER, mUserNameSignup.getText().toString(),
                            mEmailSignup.getText().toString(), mPasswordSignup.getText().toString(),
                            mMobileSignup.getText().toString(), null, Constant.SIGNUP_REQUEST_TAG);
                    mApiClient.signUpRequest(signupRequestParam);
                }
            }else {
                CommonUtils.displayToast(getContext(), getString(R.string.fill_mandatory_field));
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

    private boolean validation(String action) {

        if(action.equalsIgnoreCase(ActionRequest.LOGIN)) {
            if (CommonUtils.textValidation(mUserName) && CommonUtils.textValidation(mPassword)) {
                return true;
            }

            if (!CommonUtils.textValidation(mUserName)) {
                mUserName.setError(null);
            }

            if (!CommonUtils.textValidation(mPassword)) {
                mPassword.setError(null);
            }
        } else if(action.equalsIgnoreCase(ActionRequest.REGISTER)) {
            if(CommonUtils.textValidation(mUserNameSignup) && CommonUtils.textValidation(mPasswordSignup) && CommonUtils.textValidation(mConfirmPassword)
                    && CommonUtils.textValidation(mMobileSignup) && CommonUtils.textValidation(mEmailSignup)) {
                if(CommonUtils.textCompare(mPasswordSignup, mConfirmPassword)) {
                    return true;
                } else {
                    CommonUtils.displayToast(this, getString(R.string.password_mismatch));
                }
            }

            if(!CommonUtils.textValidation(mUserNameSignup)) {
                mUserNameSignup.setError(null);
            }

            if(!CommonUtils.textValidation(mPasswordSignup)) {
                mPasswordSignup.setError(null);
            }

            if(!CommonUtils.textValidation(mConfirmPassword)) {
                mConfirmPassword.setError(null);
            }

            if(!CommonUtils.textValidation(mMobileSignup)) {
                mMobileSignup.setError(null);
            }

            if(!CommonUtils.textValidation(mEmailSignup)) {
                mEmailSignup.setError(null);
            }

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

                if(!TextUtils.isEmpty(CommonUtils.getSharedPref(this, Constant.SP_FILE_LOGIN, Constant.SP_USER_NAME))) {
                    sUname = CommonUtils.getSharedPref(this, Constant.SP_FILE_LOGIN, Constant.SP_USER_NAME);
                    sPwd = CommonUtils.getSharedPref(this, Constant.SP_FILE_LOGIN, Constant.SP_PASSWORD);
                } else {
                    SignInResponse signInResponse = (SignInResponse) apiResponse;
                    CommonUtils.saveSharedPref(this, Constant.SP_FILE_LOGIN, Constant.SP_USER_NAME, sUname);
                    CommonUtils.saveSharedPref(this, Constant.SP_FILE_LOGIN, Constant.SP_USER_NAME, sPwd);
                }

                Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                startActivity(intent);
                finish();

                break;
            case Constant.SIGNUP_REQUEST_TAG:
                dismissProgress();

                CommonUtils.displayToast(getContext(), apiResponse.getMessage());
                signInLayout.setVisibility(View.VISIBLE);
                signUpLayout.setVisibility(View.INVISIBLE);

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

            case Constant.SIGNUP_REQUEST_TAG:
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

            case Constant.SIGNUP_REQUEST_TAG:
                dismissProgress();
                CommonUtils.displayToast(getContext(), event.getResultMsgUser());
                break;

            default:
                break;
        }
    }

    void loginProcess() {
        if (!mApiClient.isRequestRunning(Constant.SIGNIN_REQUEST_TAG)) {
            showProgress();
            LoginRequestParam loginRequestParam = new LoginRequestParam(ActionRequest.LOGIN, mUserName.getText().toString(), mPassword.getText().toString()
                    , null, Constant.SIGNIN_REQUEST_TAG);
            mApiClient.signInRequest(loginRequestParam);
        }
    }

}
