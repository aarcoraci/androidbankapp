package com.aarcoraci.bankapp.utils;

import android.util.DisplayMetrics;

/**
 * Created by angel on 3/22/2018.
 */

public abstract class Utils {
    public static float convertDpToPixel(DisplayMetrics metrics, float dp) {
        return dp * metrics.density;
    }

    public static float convertPixelsToDp(DisplayMetrics metrics, float px) {
        return px / metrics.density;
    }
}
