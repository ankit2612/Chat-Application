package com.plusconnect.Views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.rockerhieu.emojicon.EmojiconTextView;


/**
 * Created by Dexter on 08-06-2015.
 */
public class RobotoRegularEmojiconTextView extends EmojiconTextView {

    public RobotoRegularEmojiconTextView(Context context) {
        super(context);
        style(context);
    }

    public RobotoRegularEmojiconTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        style(context);
    }

    public RobotoRegularEmojiconTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        style(context);
    }

    private void style(Context context) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "fonts/Roboto-Regular.ttf");
        setTypeface(tf);
    }

}