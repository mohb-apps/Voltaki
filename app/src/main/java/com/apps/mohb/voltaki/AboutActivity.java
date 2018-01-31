/*
 *  Copyright (c) 2016 mohb apps - All Rights Reserved
 *
 *  Project       : Voltaki
 *  Developer     : Haraldo Albergaria Filho, a.k.a. mohb apps
 *
 *  File          : AboutActivity.java
 *  Last modified : 8/10/16 11:35 PM
 *
 *  -----------------------------------------------------------
 */

package com.apps.mohb.voltaki;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.apps.mohb.voltaki.fragments.dialogs.LegalNoticesDialogFragment;
import com.apps.mohb.voltaki.fragments.dialogs.MaterialIconsDialogFragment;
import com.apps.mohb.voltaki.fragments.dialogs.PrivacyPolicyDialogFragment;
import com.apps.mohb.voltaki.fragments.dialogs.TermsOfUseDialogFragment;
import com.apps.mohb.voltaki.messaging.Toasts;
import com.google.android.gms.common.GoogleApiAvailability;


public class AboutActivity extends AppCompatActivity {

    // Class to load Legal Notices text from internet
    private class GetLegalNotices extends AsyncTask {

        private DialogFragment dialog;
        private String legalNotices;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // show toast before start loading legal notices text
            Toasts.createLegalNotices();
            Toasts.setLegalNoticesText(R.string.toast_get_legal_notices);
            Toasts.showLegalNotices();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            // get legal notices text from internet
            GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
            legalNotices = googleApiAvailability.getOpenSourceSoftwareLicenseInfo(getApplicationContext());
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if ((legalNotices != null) && (!legalNotices.isEmpty())) {
                // if successfully got legal notices text, show it on a dialog fragment
                dialog = new LegalNoticesDialogFragment().newInstance(legalNotices);
                dialog.show(getSupportFragmentManager(), "LegalNoticesDialogFragment");
            } else {
                // else show a toast informing that couldn't get
                Toasts.setLegalNoticesText(R.string.toast_no_legal_notices);
                Toasts.showLegalNotices();
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        // displays app version number
        TextView version = (TextView) findViewById(R.id.textAppVersion);
        version.setText(getString(R.string.version_name) + " " + getString(R.string.version_number));
    }


    // OPTIONS MENU

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        DialogFragment dialog;
        Intent intent;
        Bundle bundle;

        switch (id) {

            // Feedback
            case R.id.action_feedback:
                intent = new Intent(this, FeedbackActivity.class);
                bundle = new Bundle();
                bundle.putString("url", getString(R.string.url_contact));
                intent.putExtras(bundle);
                startActivity(intent);
                break;

            // Bug report
            case R.id.action_bug_report:
                intent = new Intent(this, FeedbackActivity.class);
                bundle = new Bundle();
                bundle.putString("url", getString(R.string.url_bug_report));
                intent.putExtras(bundle);
                startActivity(intent);
                break;

            // Terms of use
            case R.id.action_terms_of_use:
                dialog = new TermsOfUseDialogFragment();
                dialog.show(getSupportFragmentManager(), "TermsOfUseDialogFragment");
                break;

            // Privacy policy
            case R.id.action_privacy_policy:
                dialog = new PrivacyPolicyDialogFragment();
                dialog.show(getSupportFragmentManager(), "PrivacyPolicyDialogFragment");
                break;

            // Legal notices
            case R.id.action_legal_notices:
                new GetLegalNotices().execute();
                break;

            // Icons attribution
            case R.id.action_material_icons:
                dialog = new MaterialIconsDialogFragment();
                dialog.show(getSupportFragmentManager(), "MaterialIconsDialogFragment");
                break;

        }

        return super.onOptionsItemSelected(item);
    }

}
