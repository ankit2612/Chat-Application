package com.plusconnect.Views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Dexter on 08-06-2015.
 */
public class FontAwesomeTextView extends TextView {

    public FontAwesomeTextView(Context context) {
        super(context);
        style(context);
    }

    public FontAwesomeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        style(context);
    }

    public FontAwesomeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        style(context);
    }

    private void style(Context context) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "fontawesome-webfont.ttf");
        setTypeface(tf);
    }

}