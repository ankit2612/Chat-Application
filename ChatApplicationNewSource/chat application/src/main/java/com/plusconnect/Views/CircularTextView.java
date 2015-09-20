package com.plusconnect.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.plusconnect.chat.R;

/**
 * Created by ambesh on 11-09-2015.
 */
public class CircularTextView extends TextView {

private float radiusTextView;
    private int colorCode;
    private Context context;
    public CircularTextView(Context context) {
        super(context);
        style(context);
        this.radiusTextView=context.getResources().getDimensionPixelSize(R.dimen
        .metrics_16);
    this.colorCode=context.getResources().getColor(R.color.button_blue);
    }

    public CircularTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        style(context);
        this.radiusTextView=context.getResources().getDimensionPixelSize(R.dimen
                .metrics_16);

        this.colorCode=context.getResources().getColor(R.color.button_blue);
    }

    public CircularTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        style(context);
        this.radiusTextView=context.getResources().getDimensionPixelSize(R.dimen
                .metrics_16);

        this.colorCode=context.getResources().getColor(R.color.button_blue);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Rect rect = canvas.getClipBounds();
        Paint paint = new Paint();
        paint.setColor(colorCode);
        canvas.drawCircle(rect.centerX(), rect.centerY(), radiusTextView, paint);
        super.onDraw(canvas);
    }

    private void style(Context context) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "Roboto-Regular.ttf");
        setTypeface(tf);
    }
}
