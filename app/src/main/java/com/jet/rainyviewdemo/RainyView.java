package com.jet.rainyviewdemo;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Description：
 * Author：Jet啟思
 * Time：2018/8/18 17:10
 */
public class RainyView extends View {

    private int imageSize = Utils.dpToPx(40);
    private Paint paint;
    private Bitmap bitmap;
    private float rainyVelocity;
    private Matrix matrix = new Matrix();
    private Context context;

    int height;
    int width;
    float maxHeight;

    private List<Float> widthArr = new ArrayList<>();
    private List<Float> heightArr = new ArrayList<>();
    private List<Integer> angels = new ArrayList<>();

    public RainyView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public RainyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public RainyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bitmap = Utils.getBitmap(getResources(), imageSize, R.drawable.dog);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        for (int i = 0; i < widthArr.size(); i++) {
            matrix.reset();
            float dy = heightArr.get(i) + (maxHeight + height + imageSize) * rainyVelocity;
            matrix.postTranslate(widthArr.get(i), dy);
            matrix.postRotate(angels.get(i) + 180 * rainyVelocity, imageSize / 2 + widthArr.get(i), imageSize / 2 + dy);
            canvas.drawBitmap(bitmap, matrix, paint);
        }
        canvas.restore();
    }

    public void addItemRain() {
        widthArr.clear();
        heightArr.clear();
        for (int i = 0; i < 20; i++) {
            widthArr.add((float) (width * Math.random()));
            float tempHeight = (float) (height * Math.random());
            heightArr.add(-(imageSize + tempHeight));
            maxHeight = Math.max(tempHeight, maxHeight);
            angels.add((int) (Math.random() * 360));
        }
    }

    private ObjectAnimator objectAnimator;

    private ObjectAnimator getObjectAnimator() {
        if (objectAnimator == null) {
            objectAnimator = ObjectAnimator.ofFloat(this, "rainyVelocity", 0, getHeight());
            objectAnimator.setDuration(3000);
            objectAnimator.setStartDelay(300);
        }
        return objectAnimator;
    }

    public int getRotateAngel() {
        return rotateAngel;
    }

    public void setRotateAngel(int rotateAngel) {
        this.rotateAngel = rotateAngel;
        invalidate();
    }

    private int rotateAngel;

    public float getRainyVelocity() {
        return rainyVelocity;
    }

    public void setRainyVelocity(float rainyVelocity) {
        this.rainyVelocity = rainyVelocity;
        invalidate();
    }

}
