package com.mikepenz.lollipopshowcase;

import android.animation.Animator;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mikepenz.lollipopshowcase.entity.AppInfo;
import com.mikepenz.lollipopshowcase.util.UploadHelper;
import com.nispok.snackbar.Snackbar;

import java.util.Date;

public class DetailActivity extends ActionBarActivity {

    private static final int SCALE_DELAY = 30;

    private Toolbar toolbar;
    private LinearLayout rowContainer;

    private AppInfo appInfo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //Utils.configureWindowEnterExitTransition(getWindow());

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Handle Back Navigation :D
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailActivity.this.onBackPressed();
            }
        });

        // Row Container
        rowContainer = (LinearLayout) findViewById(R.id.row_container);

        // Fab Button
        View fabButton = findViewById(R.id.fab_button);
        fabButton.setOnClickListener(fabClickListener);
        Utils.configureFab(fabButton);

        //getWindow().getEnterTransition().removeListener(this);

        for (int i = 1; i < rowContainer.getChildCount(); i++) {
            View rowView = rowContainer.getChildAt(i);
            rowView.animate().setStartDelay(100 + i * SCALE_DELAY).scaleX(1).scaleY(1);
        }

        ComponentName componentName = null;

        if (savedInstanceState != null) {
            componentName = savedInstanceState.getParcelable("appInfo");
        } else if (getIntent() != null && getIntent().getExtras() != null) {
            componentName = (ComponentName) getIntent().getExtras().get("appInfo");
        }

        if (componentName != null) {
            Intent intent = new Intent();
            intent.setComponent(componentName);
            ResolveInfo app = getPackageManager().resolveActivity(intent, 0);
            appInfo = new AppInfo(this, app);
        }

        if (appInfo != null) {
            //toolbar.setLogo(appInfo.getIcon());
            toolbar.setTitle(appInfo.getName());

            View view = rowContainer.findViewById(R.id.row_name);
            fillRow(view, "Application Name", appInfo.getName());
            ((ImageView) view.findViewById(R.id.appIcon)).setImageDrawable(appInfo.getIcon());

            view = rowContainer.findViewById(R.id.row_package_name);
            fillRow(view, "Package Name", appInfo.getPackageName());

            view = rowContainer.findViewById(R.id.row_activity);
            fillRow(view, "Activity", appInfo.getActivityName());

            view = rowContainer.findViewById(R.id.row_component_info);
            fillRow(view, "ComponentInfo", appInfo.getComponentInfo());

            view = rowContainer.findViewById(R.id.row_version);
            fillRow(view, "Version", appInfo.getVersionName() + " (" + appInfo.getVersionCode() + ")");

            view = rowContainer.findViewById(R.id.row_moments);
            fillRow(view, "Moments", "First installed: " + new Date(appInfo.getFirstInstallTime()) + "\nLast updated: " + new Date(appInfo.getLastUpdateTime()));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("appInfo", appInfo.getComponentName());
        super.onSaveInstanceState(outState);
    }

    View.OnClickListener fabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            UploadHelper.getInstance(DetailActivity.this, null).upload(appInfo);
        }
    };

    public void fillRow(View view, final String title, final String description) {
        TextView titleView = (TextView) view.findViewById(R.id.title);
        titleView.setText(title);

        TextView descriptionView = (TextView) view.findViewById(R.id.description);
        descriptionView.setText(description);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("AppInfo", description);
                clipboard.setPrimaryClip(clip);

                Snackbar.with(getApplicationContext()).dismiss();
                Snackbar.with(getApplicationContext()) // context
                        .text("Copied " + title) // text to display
                        .show(DetailActivity.this);
            }
        });
    }

    @Override
    public void onBackPressed() {

        for (int i = rowContainer.getChildCount() - 1; i > 0; i--) {

            View rowView = rowContainer.getChildAt(i);
            ViewPropertyAnimator propertyAnimator = rowView.animate().setStartDelay((rowContainer.getChildCount() - 1 - i) * SCALE_DELAY)
                    .scaleX(0).scaleY(0);

            propertyAnimator.setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        finishAfterTransition();
                    } else {
                        finish();
                    }
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                }

                @Override
                public void onAnimationRepeat(Animator animator) {
                }
            });
        }
    }
}
