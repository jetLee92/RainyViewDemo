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
 * Description：单身狗雨
 * Author：Jet啟思
 * Time：2018/8/18 17:10
 */
public class RainyView extends View {

    private int imageSize = Utils.dpToPx(40);
    private Paint paint;
    private Bitmap bitmap;

    private ObjectAnimator objectAnimator;
    // 效果系数
    private float rainyFraction;
    private Matrix matrix = new Matrix();

    // view高
    private int height;
    // view宽
    private int width;
    // 距离顶部最大的高度
    private float maxHeight;
    // 狗头数量
    private final int num = 30;

    private List<Float> widthArr = new ArrayList<>();
    private List<Float> heightArr = new ArrayList<>();
    private List<Integer> angles = new ArrayList<>();

    public RainyView(Context context) {
        super(context);
        init();
    }

    public RainyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RainyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bitmap = Utils.getBitmap(getResources(), imageSize, R.drawable.dog);
//        bitmap = Utils.getBitmap(getResources(), imageSize, R.drawable.ic_favorite_red_a700_36dp);
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
        for (int i = 0; i < num; i++) {
            // 重置matrix，必须重置不然会叠加
            if (num != widthArr.size()) {
                break;
            }
            matrix.reset();
            // 当前的高度 = 随机的初始高度（负值） + （view的高度 + 最高的狗头所在的高度 == 整个动画的高度）*系数
            // 加上2*imageSize是为了保证狗头都落下底部外
            float dy = heightArr.get(i) + (maxHeight + height + 2 * imageSize) * rainyFraction;
            matrix.postTranslate(widthArr.get(i), dy);
            // 以狗头为中心旋转
            matrix.postRotate(angles.get(i) + 180 * rainyFraction, imageSize / 2 + widthArr.get(i), imageSize / 2 + dy);
            canvas.drawBitmap(bitmap, matrix, paint);
        }
        canvas.restore();
    }

    public void addItemRain() {
        widthArr.clear();
        heightArr.clear();
        angles.clear();
        for (int i = 0; i < num; i++) {
            // 随机横向x的位置
            widthArr.add((float) ((width - imageSize) * Math.random()));
            float tempHeight = (float) (height * Math.random());
            // 随机的距离顶部的高度，是负值。加上imageSize是为了保证从顶部开始落下
            heightArr.add(-(imageSize + tempHeight));
            maxHeight = Math.max(tempHeight, maxHeight);
            // 随机初始角度
            angles.add((int) (Math.random() * 360));
        }
    }

    public void startAnimator() {
        if (objectAnimator == null) {
            objectAnimator = ObjectAnimator.ofFloat(this, "rainyFraction", 0, 1);
            objectAnimator.setDuration(6000);
        }
        objectAnimator.start();
    }

    public float getRainyFraction() {
        return rainyFraction;
    }

    public void setRainyFraction(float rainyFraction) {
        this.rainyFraction = rainyFraction;
        invalidate();
    }

}
