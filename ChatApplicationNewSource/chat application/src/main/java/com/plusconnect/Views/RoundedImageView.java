package com.plusconnect.Views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.plusconnect.chat.R;

public class RoundedImageView extends ImageView {


    private  int radius;
    @Override
    public void setScaleType(ScaleType scaleType) {
        super.setScaleType(ScaleType.CENTER_CROP);
    }

    public RoundedImageView(Context ctx, AttributeSet attrs) {

        super(ctx, attrs);

        radius=ctx.getResources().getDimensionPixelSize(R.dimen.metrics_48);
    }


    @Override
    protected void onDraw(Canvas canvas) {

        Drawable drawable = getDrawable();

        if (drawable == null) {
            return;
        }

        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }
        Bitmap b = ((BitmapDrawable) drawable).getBitmap();
        Bitmap bitmap = b.copy(Config.ARGB_8888, true);

        int w = getWidth(), h = getHeight();

        Bitmap roundBitmap = getRoundedCroppedBitmap(bitmap);
        canvas.drawBitmap(roundBitmap, 0, 0, null);

    }

    public  Bitmap getRoundedCroppedBitmap(Bitmap bitmap) {
        Bitmap finalBitmap;
        if (bitmap.getWidth() != radius || bitmap.getHeight() != radius)
            finalBitmap = Bitmap.createScaledBitmap(bitmap, radius, radius, false);
        else
            finalBitmap = bitmap;
        Bitmap output = Bitmap.createBitmap(finalBitmap.getWidth(),
                finalBitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, finalBitmap.getWidth(), finalBitmap.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(finalBitmap.getWidth() / 2 + 0.7f, finalBitmap.getHeight() / 2 + 0.7f,
                finalBitmap.getWidth() / 2 + 0.1f, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(finalBitmap, rect, rect, paint);

            return output;

//        Bitmap targetBitmap = Bitmap.createBitmap(radius,
//                radius,Bitmap.Config.ARGB_8888);
//
//        Canvas canvas = new Canvas(targetBitmap);
//        Path path = new Path();
//        path.addCircle(((float) radius - 1) / 2,
//                ((float) radius - 1) / 2,
//                (Math.min(((float) radius),
//                        ((float) radius)) / 2),
//                Path.Direction.CCW);
//
//        canvas.clipPath(path);
//        Bitmap sourceBitmap = bitmap;
//        canvas.drawBitmap(sourceBitmap,
//                new Rect(0, 0, sourceBitmap.getWidth(),
//                        sourceBitmap.getHeight()),
//                new Rect(0, 0, radius, radius), null);
//        return targetBitmap;

    }



}