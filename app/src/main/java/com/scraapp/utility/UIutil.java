package com.scraapp.utility;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class UIutil {

    public static void hideKeyboard(Context context, View rootView) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(rootView.getWindowToken(), 0);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static void resetInputStyle(EditText editText, int defaultTextColor) {
        editText.setTextColor(defaultTextColor);
        TextInputLayout textInputLayout = (TextInputLayout) editText.getParent().getParent();
        textInputLayout.setErrorEnabled(false);
    }

    public static boolean isValidEmail(String email) {
        if(TextUtils.isEmpty(email)) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }

    }

    public static boolean verifyString(String text) {
        return ((text != null) && !TextUtils.isEmpty(text.trim()));
    }

}
