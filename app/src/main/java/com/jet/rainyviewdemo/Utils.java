package com.jet.rainyviewdemo;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.TypedValue;

/**
 * @Author：Jet啟思
 * @Date:2018/7/24 15:07
 */
public class Utils {

    public static int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }

    /**
     * 调整图片大小
     *
     * @param resWidth
     * @return
     */
    public static Bitmap getBitmap(Resources res, int resWidth, int resourceId) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resourceId, options);
        options.inJustDecodeBounds = false;
        options.inDensity = options.outWidth;
        options.inTargetDensity = resWidth;
        return BitmapFactory.decodeResource(res, resourceId, options);
    }

}
