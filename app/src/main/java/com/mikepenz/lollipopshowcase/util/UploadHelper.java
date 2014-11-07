package com.mikepenz.lollipopshowcase.util;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;

import com.mikepenz.lollipopshowcase.R;
import com.mikepenz.lollipopshowcase.entity.AppInfo;
import com.nispok.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mikepenz on 18.10.14.
 */
public class UploadHelper {
    private static UploadHelper instance = null;


    private UploadHelper(ActionBarActivity act, List<AppInfo> applicationList) {
        this.act = act;

        if (applicationList != null) {
            this.applicationList = applicationList;
        } else {
            this.applicationList = new ArrayList<AppInfo>();
        }
    }

    private ActionBarActivity act;
    private List<AppInfo> applicationList = new ArrayList<AppInfo>();

    public static UploadHelper getInstance(ActionBarActivity act, List<AppInfo> applicationList) {
        if (instance == null) {
            instance = new UploadHelper(act, applicationList);
        } else if (act != null) {
            instance.act = act;
        }
        return instance;
    }

    public void upload(AppInfo appInfo) {
        new UploadComponentInfoTask().execute(appInfo);
    }

    public void uploadAll() {
        new UploadComponentInfoTask().execute();
    }

    private class UploadComponentInfoTask extends AsyncTask<AppInfo, Integer, Boolean> {
        ProgressDialog mProgressDialog = new ProgressDialog(act);

        @Override
        protected void onPreExecute() {
            if (!Network.isAvailiable(act)) {
                this.cancel(true);
                Snackbar.with(act).text(act.getString(R.string.dialog_nointernet)).show(act);
            } else {
                mProgressDialog.setTitle(R.string.dialog_uploading);
                mProgressDialog.setMessage(act.getString(R.string.dialog_processinganduploading));
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.setCancelable(false);
                mProgressDialog.setMax(applicationList.size());
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mProgressDialog.show();
            }

            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(AppInfo... params) {
            boolean updateRequired = false;
            if (params == null || params.length == 0) {
                mProgressDialog.setMax(applicationList.size());

                for (AppInfo ai : applicationList) {
                    updateRequired = postData();
                    publishProgress(mProgressDialog.getProgress() + 1);
                    if (updateRequired) {
                        break;
                    }
                }
            } else if (params.length == 1) {
                updateRequired = postData();
                publishProgress(applicationList.size());
            }

            return updateRequired;
        }

        @Override
        protected void onPostExecute(Boolean updateRequired) {
            mProgressDialog.dismiss();
            super.onPostExecute(updateRequired);
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            if (values.length > 0) {
                mProgressDialog.setProgress(values[0]);
            }
            super.onProgressUpdate(values);
        }

    }

    public boolean postData() {

        try {
            Thread.sleep(150);
        } catch (Exception ex) {

        }

        return false;
    }
}
