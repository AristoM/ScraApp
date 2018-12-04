package com.scraapp.utility;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

public class ScraAppButton extends android.support.v7.widget.AppCompatButton {

    public ScraAppButton(Context context) {
        super(context);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/" + "Raleway-Bold.ttf");
        this.setTypeface(face);
    }

    public ScraAppButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/" + "Raleway-Bold.ttf");
        this.setTypeface(face);
    }

    public ScraAppButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/" + "Raleway-Bold.ttf");
        this.setTypeface(face);
    }

}
