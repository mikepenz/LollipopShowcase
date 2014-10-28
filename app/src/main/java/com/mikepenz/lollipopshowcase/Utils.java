package com.mikepenz.lollipopshowcase;

import android.annotation.TargetApi;
import android.graphics.Outline;
import android.os.Build;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageButton;
import android.widget.ImageView;

public class Utils {

    /*
    public static void configureWindowEnterExitTransition(Window w) {
        Explode ex = new Explode();
        ex.setInterpolator(new PathInterpolator(0.4f, 0, 1, 1));
        w.setExitTransition(ex);
        w.setEnterTransition(ex);
    }
    */

    public static void configureFab(View fabButton) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fabButton.setOutlineProvider(new ViewOutlineProvider() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void getOutline(View view, Outline outline) {
                    int fabSize = view.getContext().getResources().getDimensionPixelSize(R.dimen.fab_size);
                    outline.setOval(0, 0, fabSize, fabSize);
                }
            });
        } else {
            ((ImageButton) fabButton).setScaleType(ImageView.ScaleType.FIT_CENTER);
        }
    }
}
