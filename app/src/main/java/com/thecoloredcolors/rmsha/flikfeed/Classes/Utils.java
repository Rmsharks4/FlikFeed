package com.thecoloredcolors.rmsha.flikfeed.Classes;

/**
 * Created by User on 11/19/2017.
 */

import android.os.Build;

public class Utils {

    public static boolean isAndroid5() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }
}
