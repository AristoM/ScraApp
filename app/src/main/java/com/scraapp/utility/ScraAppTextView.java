package com.scraapp.utility;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class ScraAppTextView extends android.support.v7.widget.AppCompatTextView {

    public ScraAppTextView(Context context) {
        super(context);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/" + "Raleway-Bold.ttf");
        this.setTypeface(face);
    }

    public ScraAppTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/" + "Raleway-Bold.ttf");
        this.setTypeface(face);
    }

    public ScraAppTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/" + "Raleway-Bold.ttf");
        this.setTypeface(face);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


    }

}
