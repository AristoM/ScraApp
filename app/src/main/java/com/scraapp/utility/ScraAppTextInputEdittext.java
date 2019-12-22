package com.scraapp.utility;

import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.TextInputEditText;
import android.util.AttributeSet;

public class ScraAppTextInputEdittext extends TextInputEditText {

    private Context context;
    private AttributeSet attrs;
    private int defStyle;

    public ScraAppTextInputEdittext(Context context) {
        super(context);
        this.context=context;
        init();
    }

    public ScraAppTextInputEdittext(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        this.attrs=attrs;
        init();
    }

    public ScraAppTextInputEdittext(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context=context;
        this.attrs=attrs;
        this.defStyle=defStyle;
        init();
    }

    private void init() {
        Typeface font=Typeface.createFromAsset(getContext().getAssets(), "fonts/Raleway-Bold.ttf");
        this.setTypeface(font);
    }
    @Override
    public void setTypeface(Typeface tf, int style) {
        tf=Typeface.createFromAsset(getContext().getAssets(), "fonts/Raleway-Bold.ttf");
        super.setTypeface(tf, style);
    }

    @Override
    public void setTypeface(Typeface tf) {
        tf=Typeface.createFromAsset(getContext().getAssets(), "fonts/Raleway-Bold.ttf");
        super.setTypeface(tf);
    }
}
