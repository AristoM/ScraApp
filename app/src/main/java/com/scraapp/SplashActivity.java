package com.scraapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.scraapp.network.event.ApiErrorEvent;
import com.scraapp.network.event.ApiErrorWithMessageEvent;
import com.scraapp.network.request.LoginRequestParam;
import com.scraapp.network.request.SignupRequestParam;
import com.scraapp.network.request.SignupVendorRequestParam;
import com.scraapp.network.response.AbstractApiResponse;
import com.scraapp.network.response.SignInResponse;
import com.scraapp.utility.ActionRequest;
import com.scraapp.utility.Constant;

import org.greenrobot.eventbus.Subscribe;

public class SplashActivity extends ScrAppActivity {

    protected static final int REQUEST_CODE_AUTOCOMPLETE = 1;

    Button loginCta, signupCustomerCta, signupVendorCta;
    TextView newUser;
    LinearLayout signInLayout, signUpLayoutCustomer, signUpLayoutVendor;
    EditText mUserName, mPassword, mUserNameSignup, mPasswordSignup, mConfirmPassword, mMobileSignup, mEmailSignup;
    EditText mShopName, mShopAddress, mEmailSignupVendor, mMobileSignupVendor, mPanNumber, mAdharCard, mPasswordSignupVendor, mConfirmPasswordVendor;
    String sUname, sPwd;
    ImageView mSplashLogo;
    String vendorLat, vendorLon, vendorCity;

    public int getlayout() {
        return R.layout.splash_layout;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);

        loginCta = findViewById(R.id.login_cta);
        signupCustomerCta = findViewById(R.id.signup_cta);
        newUser = findViewById(R.id.new_customer_cta);
        signInLayout = findViewById(R.id.signin_layout);
        signUpLayoutCustomer = findViewById(R.id.signup_layout);
        signUpLayoutVendor = findViewById(R.id.signup_layout_vendor);
        mUserName = findViewById(R.id.user_name);
        mPassword = findViewById(R.id.password);
        mUserNameSignup = findViewById(R.id.user_name_signup);
        mPasswordSignup = findViewById(R.id.password_signup);
        mConfirmPassword = findViewById(R.id.confirm_password_signup);
        mMobileSignup = findViewById(R.id.mobile_signup);
        mEmailSignup = findViewById(R.id.email_signup);
        mSplashLogo = findViewById(R.id.splash_logo);
        signupVendorCta = findViewById(R.id.signup_cta_vendor);

        // Vendor Registration
        mShopName = findViewById(R.id.shop_name);
        mShopAddress = findViewById(R.id.shop_addresss);
        mEmailSignupVendor = findViewById(R.id.email_signup_vendor);
        mMobileSignupVendor = findViewById(R.id.mobile_signup_vendor);
        mPanNumber = findViewById(R.id.pan_number);
        mAdharCard = findViewById(R.id.adhar_number);
        mPasswordSignupVendor = findViewById(R.id.password_signup_vendor);
        mConfirmPasswordVendor = findViewById(R.id.confirm_password_signup_vendor);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            boolean isFromSignout = bundle.getBoolean(Constant.IS_FROM_SIGNOUT);
            if(isFromSignout) {
                signInLayout.setVisibility(View.VISIBLE);
                signUpLayoutCustomer.setVisibility(View.INVISIBLE);
                signUpLayoutVendor.setVisibility(View.INVISIBLE);
            }
        }


        if(!TextUtils.isEmpty(CommonUtils.getSharedPref(Constant.SP_FILE_LOGIN, Constant.SP_USER_NAME))) {
            String type = CommonUtils.getSharedPref(Constant.SP_FILE_LOGIN, Constant.SP_USER_TYPE);
            loginProcess(type);
        } else {
            signInLayout.setVisibility(View.VISIBLE);
            signUpLayoutCustomer.setVisibility(View.GONE);
            signUpLayoutVendor.setVisibility(View.GONE);
        }

        mShopAddress.setFocusable(false);
        mShopAddress.setClickable(true);
        mShopAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAutocompleteActivity();
            }
        });

        loginCta.setOnClickListener(view -> {

            CharSequence options[] = new CharSequence[] {"Are you a Customer?", "Are you a Vendor?"};

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
//            builder.setTitle("Select your option:");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int pos) {
                    // the user clicked on options[which]
                    if(pos == 0) {
                        CommonUtils.saveSharedPref(Constant.SP_FILE_LOGIN, Constant.SP_USER_TYPE, "login");
                        if(validation(ActionRequest.LOGIN)) {
                            sUname = mUserName.getText().toString();
                            sPwd = mPassword.getText().toString();
                            loginProcess(ActionRequest.LOGIN);
                        } else {
                            CommonUtils.displayToast(getContext(), getString(R.string.fill_mandatory_field));
                        }
                    } else {
                        CommonUtils.saveSharedPref(Constant.SP_FILE_LOGIN, Constant.SP_USER_TYPE, "vendor_login");
                        if(validation(ActionRequest.LOGIN)) {
                            sUname = mUserName.getText().toString();
                            sPwd = mPassword.getText().toString();
                            loginProcess(ActionRequest.LOGIN_VENDOR);
                        } else {
                            CommonUtils.displayToast(getContext(), getString(R.string.fill_mandatory_field));
                        }
                    }
                }
            });
//            builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    //the user clicked on Cancel
//                }
//            });
            builder.show();

        });

        newUser.setOnClickListener(view -> {

            CharSequence options[] = new CharSequence[] {"Are you a Customer?", "Are you a Vendor?"};

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
//            builder.setTitle("Select your option:");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int pos) {
                    // the user clicked on options[which]
                    if(pos == 0) {
                        signInLayout.setVisibility(View.INVISIBLE);
                        mSplashLogo.setVisibility(View.GONE);
                        signUpLayoutCustomer.setVisibility(View.VISIBLE);
                    } else {
                        signInLayout.setVisibility(View.INVISIBLE);
                        mSplashLogo.setVisibility(View.GONE);
                        signUpLayoutCustomer.setVisibility(View.GONE);
                        signUpLayoutVendor.setVisibility(View.VISIBLE);
                    }
                }
            });
            builder.show();




        });


        signupCustomerCta.setOnClickListener(view -> {

            if(validation(ActionRequest.REGISTER_CUSTOMER)) {
                if (!mApiClient.isRequestRunning(Constant.SIGNUP_REQUEST_TAG)) {
                    showProgress();
                    SignupRequestParam signupRequestParam = new SignupRequestParam(ActionRequest.REGISTER_CUSTOMER, mUserNameSignup.getText().toString(),
                            mEmailSignup.getText().toString(), mPasswordSignup.getText().toString(),
                            mMobileSignup.getText().toString(), null, Constant.SIGNUP_REQUEST_TAG);
                    mApiClient.signUpRequest(signupRequestParam);
                }
            }else {
                CommonUtils.displayToast(getContext(), getString(R.string.fill_mandatory_field));
            }
        });

        signupVendorCta.setOnClickListener(view -> {

            if(validation(ActionRequest.VENDOR_REGISTER)) {
//                Intent intent = new Intent(SplashActivity.this, BaseActivity.class);
//                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//                startActivity(intent);
//                finish();

                if (!mApiClient.isRequestRunning(Constant.SIGNUP_VENDOR_REQUEST_TAG)) {
                    showProgress();
                    SignupVendorRequestParam signupRequestParam = new SignupVendorRequestParam(ActionRequest.VENDOR_REGISTER, mShopAddress.getText().toString(), vendorCity, mEmailSignupVendor.getText().toString(),
                            vendorLat, vendorLon, vendorCity, mShopName.getText().toString(), mMobileSignupVendor.getText().toString(), "", mPasswordSignupVendor.getText().toString(),
                            mPanNumber.getText().toString(), mAdharCard.getText().toString(), null, Constant.SIGNUP_VENDOR_REQUEST_TAG);
                    mApiClient.signUpVendorRequest(signupRequestParam);
                }
            } else {
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
        } else if(signUpLayoutCustomer.getVisibility() == View.VISIBLE || signUpLayoutVendor.getVisibility() == View.VISIBLE) {
            signInLayout.setVisibility(View.VISIBLE);
            signUpLayoutCustomer.setVisibility(View.INVISIBLE);
            signUpLayoutVendor.setVisibility(View.INVISIBLE);
            mSplashLogo.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
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
        } else if(action.equalsIgnoreCase(ActionRequest.REGISTER_CUSTOMER)) {
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

        } else if(action.equalsIgnoreCase(ActionRequest.VENDOR_REGISTER)) {
            if(CommonUtils.textValidation(mShopName) && CommonUtils.textValidation(mShopAddress) && CommonUtils.textValidation(mEmailSignupVendor)
                    && CommonUtils.textValidation(mMobileSignupVendor) && CommonUtils.textValidation(mPanNumber) && CommonUtils.textValidation(mAdharCard)
                    && CommonUtils.textValidation(mPasswordSignupVendor) && CommonUtils.textValidation(mConfirmPasswordVendor)) {

                if(CommonUtils.textCompare(mPasswordSignupVendor, mConfirmPasswordVendor)) {
                    return true;
                } else {
                    CommonUtils.displayToast(this, getString(R.string.password_mismatch));
                }
            }

            if(!CommonUtils.textValidation(mShopName)) {
                mShopName.setError("");
            }

            if(!CommonUtils.textValidation(mShopAddress)) {
                mShopAddress.setError("");
            }

            if(!CommonUtils.textValidation(mEmailSignupVendor)) {
                mEmailSignupVendor.setError("");
            }

            if(!CommonUtils.textValidation(mMobileSignupVendor)) {
                mMobileSignupVendor.setError("");
            }

            if(!CommonUtils.textValidation(mPanNumber)) {
                mPanNumber.setError("");
            }

            if(!CommonUtils.textValidation(mAdharCard)) {
                mAdharCard.setError("");
            }

            if(!CommonUtils.textValidation(mPasswordSignupVendor)) {
                mPasswordSignupVendor.setError("");
            }

            if(!CommonUtils.textValidation(mConfirmPasswordVendor)) {
                mConfirmPasswordVendor.setError("");
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

                if(!TextUtils.isEmpty(CommonUtils.getSharedPref(Constant.SP_FILE_LOGIN, Constant.SP_USER_NAME))) {
                    sUname = CommonUtils.getSharedPref(Constant.SP_FILE_LOGIN, Constant.SP_USER_NAME);
                    sPwd = CommonUtils.getSharedPref( Constant.SP_FILE_LOGIN, Constant.SP_PASSWORD);
                } else {
                    SignInResponse signInResponse = (SignInResponse) apiResponse;
                    CommonUtils.saveSharedPref(Constant.SP_FILE_LOGIN, Constant.SP_USER_NAME, sUname);
                    CommonUtils.saveSharedPref(Constant.SP_FILE_LOGIN, Constant.SP_PASSWORD, sPwd);
                    CommonUtils.saveSharedPref(Constant.SP_FILE_LOGIN, Constant.SP_USER_TYPE, signInResponse.getResult().getUser().getUserType());
                    CommonUtils.saveSharedPref(Constant.SP_FILE_LOGIN, Constant.SP_USERID, ((SignInResponse) apiResponse).getResult().getUser().getId());
                }

                Intent intent = new Intent(SplashActivity.this, BaseActivity.class);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                startActivity(intent);
                finish();

                break;
            case Constant.SIGNUP_REQUEST_TAG:
                dismissProgress();

                CommonUtils.displayToast(getContext(), apiResponse.getMessage());
                signInLayout.setVisibility(View.VISIBLE);
                signUpLayoutCustomer.setVisibility(View.INVISIBLE);

                break;
            case Constant.SIGNUP_VENDOR_REQUEST_TAG:
                dismissProgress();

                CommonUtils.displayToast(getContext(), apiResponse.getMessage());
                signInLayout.setVisibility(View.VISIBLE);
                signUpLayoutCustomer.setVisibility(View.INVISIBLE);

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

            case Constant.SIGNUP_VENDOR_REQUEST_TAG:
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
                signInLayout.setVisibility(View.VISIBLE);
                signUpLayoutCustomer.setVisibility(View.INVISIBLE);
                break;

            case Constant.SIGNUP_REQUEST_TAG:
                dismissProgress();
                CommonUtils.displayToast(getContext(), event.getResultMsgUser());
                break;

            case Constant.SIGNUP_VENDOR_REQUEST_TAG:
                dismissProgress();
                CommonUtils.displayToast(getContext(), event.getResultMsgUser());
                break;

            default:
                break;
        }
    }

    void loginProcess(String action) {
        if (!mApiClient.isRequestRunning(Constant.SIGNIN_REQUEST_TAG)) {
            showProgress();
            LoginRequestParam loginRequestParam = new LoginRequestParam(action, mUserName.getText().toString(), mPassword.getText().toString()
                    , null, Constant.SIGNIN_REQUEST_TAG);
            mApiClient.signInRequest(loginRequestParam);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    private void openAutocompleteActivity() {
        try {
            // The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .build(this);
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
        } catch (GooglePlayServicesRepairableException e) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check that the result was from the autocomplete widget.
        if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            if (resultCode == RESULT_OK) {
                autoCompleteCallback(data);
            }
        }
    }

//    @Override
    public void autoCompleteCallback(Intent data) {

        // Get the user's selected place from the Intent.
        Place place = PlaceAutocomplete.getPlace(this, data);

        // TODO call location based filter

        LatLng latLong = place.getLatLng();

        mShopAddress.setText(place.getName() + "");

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLong).zoom(19f).tilt(70).build();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
    }
}
