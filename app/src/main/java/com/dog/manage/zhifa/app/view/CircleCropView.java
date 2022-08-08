package com.dog.manage.zhifa.app.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.dog.manage.zhifa.app.R;

/**
 * Created by MandyLu on 2018/7/14.
 * 圆形裁剪框
 */
public class CircleCropView extends View {
    public final int CIRCLE_MARGIN = 0;

    public CircleCropView(Context context) {
        super(context);
    }

    public CircleCropView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleCropView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CircleCropView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();

        Path path = new Path();
        Rect viewDrawingRect = new Rect();
        getDrawingRect(viewDrawingRect);

        float radius = getWidth() / 4;
        path.addCircle(getWidth() / 2,
                radius + getContext().getResources().getDimension(R.dimen.dp_62), radius, Path.Direction.CW);

        Paint outsidePaint = new Paint();
        outsidePaint.setAntiAlias(true);
        outsidePaint.setARGB(255, 255, 255, 255);

        canvas.clipPath(path, Region.Op.DIFFERENCE);
        canvas.drawRect(viewDrawingRect, outsidePaint);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(getContext().getResources().getDimension(R.dimen.dp_5));
        paint.setColor(getContext().getResources().getColor(R.color.colorAppTheme));
        paint.setAntiAlias(true);
        canvas.drawCircle(getWidth() / 2,
                radius + getContext().getResources().getDimension(R.dimen.dp_62), radius, paint);
        canvas.restore();
    }
}
