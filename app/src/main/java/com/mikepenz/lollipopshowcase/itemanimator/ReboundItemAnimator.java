package com.mikepenz.lollipopshowcase.itemanimator;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;

import java.util.ArrayList;
import java.util.List;


public class ReboundItemAnimator extends SimpleItemAnimator {
    //hold the views to animate in runPendingAnimations
    private List<RecyclerView.ViewHolder> mViewHolders = new ArrayList<RecyclerView.ViewHolder>();


    @Override
    public void runPendingAnimations() {
        if (!mViewHolders.isEmpty()) {
            for (final RecyclerView.ViewHolder viewHolder : mViewHolders) {
                SpringSystem springSystem = SpringSystem.create();
                SpringConfig springConfig = new SpringConfig(70, 10);

                final View target = viewHolder.itemView;

                // Add a spring to the system.
                Spring spring = springSystem.createSpring();
                spring.setSpringConfig(springConfig);
                spring.setCurrentValue(0.0f);

                // Add a listener to observe the motion of the spring.
                spring.addListener(new SimpleSpringListener() {

                    @Override
                    public void onSpringUpdate(Spring spring) {
                        // You can observe the updates in the spring
                        // state by asking its current value in onSpringUpdate.
                        float value = (float) spring.getCurrentValue();

                        target.setScaleX(value);
                        target.setScaleY(value);
                    }
                });

                // Set the spring in motion; moving from 0 to 1
                spring.setEndValue(1.0f);
            }
        }
    }

    @Override
    public boolean animateRemove(RecyclerView.ViewHolder viewHolder) {
        viewHolder.itemView.animate().alpha(0).scaleX(0).scaleY(0).setDuration(300).start();
        return false;
    }

    @Override
    public boolean animateAdd(RecyclerView.ViewHolder viewHolder) {
        //viewHolder.itemView.setAlpha(0.0f);
        viewHolder.itemView.setScaleX(0);
        viewHolder.itemView.setScaleY(0);
        return mViewHolders.add(viewHolder);
    }

    @Override
    public boolean animateMove(RecyclerView.ViewHolder viewHolder, int i, int i2, int i3, int i4) {
        return false;
    }

    @Override
    public boolean animateChange(RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder2, int i, int i2, int i3, int i4) {
        return false;
    }

    @Override
    public void endAnimation(RecyclerView.ViewHolder viewHolder) {
    }

    @Override
    public void endAnimations() {
    }

    @Override
    public boolean isRunning() {
        return !mViewHolders.isEmpty();
    }

}