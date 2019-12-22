package com.scraapp.utility;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

public class TextWatcherListener implements TextWatcher, View.OnFocusChangeListener {

    private final EditText mEdittext;
    private int defaultTextColor;

    public TextWatcherListener(EditText mEdittext, int defaultTextColor) {
        this.mEdittext = mEdittext;
        this.defaultTextColor = defaultTextColor;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        UIutil.resetInputStyle(mEdittext, defaultTextColor);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if(hasFocus) {
            UIutil.resetInputStyle(mEdittext, defaultTextColor);
        }
    }
}
