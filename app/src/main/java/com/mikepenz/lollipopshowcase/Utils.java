package com.mikepenz.lollipopshowcase;

import android.graphics.Outline;
import android.transition.Explode;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.Window;
import android.view.animation.PathInterpolator;

public class Utils {

    public static void configureWindowEnterExitTransition(Window w) {
        Explode ex = new Explode();
        ex.setInterpolator(new PathInterpolator(0.4f, 0, 1, 1));
        w.setExitTransition(ex);
        w.setEnterTransition(ex);
    }

    public static void configureFab(View fabButton) {
        fabButton.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                int fabSize = view.getContext().getResources().getDimensionPixelSize(R.dimen.fab_size);
                outline.setOval(0, 0, fabSize, fabSize);
            }
        });
    }
}
