package com.nolankuza.theultimatealliance.util;

import android.app.Activity;
import android.content.Context;

import com.nolankuza.theultimatealliance.R;

public class Resources {

    public static int getDimen(Context context, int resourceId) {
        android.content.res.Resources resources = context.getResources();
        return (int) (resources.getDimension(resourceId));
    }

    public static int getDp(Activity activity, int resourceId) {
        android.content.res.Resources resources = activity.getResources();
        return (int) (resources.getDimension(resourceId) / resources.getDisplayMetrics().density);
    }
}
