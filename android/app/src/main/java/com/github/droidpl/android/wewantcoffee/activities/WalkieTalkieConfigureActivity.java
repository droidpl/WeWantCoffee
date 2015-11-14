package com.github.droidpl.android.wewantcoffee.activities;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.github.droidpl.android.wewantcoffee.R;
import com.github.droidpl.android.wewantcoffee.widgets.WalkieTalkie;

/**
 * The configuration screen for the {@link WalkieTalkie WalkieTalkie} AppWidget.
 */
public class WalkieTalkieConfigureActivity extends Activity {

    public static final String TAG = "WalkieTalkie";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    EditText mAppWidgetText;
    TextView mPhoneNumberTextView;
    private static final String PREFS_NAME = "com.github.droidpl.android.wewantcoffee.widgets.WalkieTalkie";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    private static final String PREF_PREFIX_KEY_PHONE = "appwidget_phone_";

    public static final int CONTACT_PICKER_RESULT = 1124;

    public WalkieTalkieConfigureActivity() {
        super();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.walkie_talkie_configure);

        findViewById(R.id.select_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLaunchContactPicker(v);
            }
        });

        mPhoneNumberTextView = (TextView) findViewById(R.id.phone_nr_textview);

        mAppWidgetText = (EditText) findViewById(R.id.appwidget_text);
        findViewById(R.id.add_button).setOnClickListener(mOnClickListener);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        mAppWidgetText.setText(loadTitlePref(WalkieTalkieConfigureActivity.this, mAppWidgetId));
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = WalkieTalkieConfigureActivity.this;

            // When the button is clicked, store the string locally
            String widgetText = mAppWidgetText.getText().toString();
            saveTitlePref(context, mAppWidgetId, widgetText, mPhoneNumberTextView.getText().toString());

            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            WalkieTalkie.updateAppWidget(context, appWidgetManager, mAppWidgetId);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    // Write the prefix to the SharedPreferences object for this widget
    static void saveTitlePref(Context context, int appWidgetId, String text, String phone) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.putString(PREF_PREFIX_KEY_PHONE + appWidgetId, phone);
        prefs.commit();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    public static String loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }

    public static String loadPhonePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY_PHONE + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }

    public static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.commit();
    }


    public void doLaunchContactPicker(View view) {
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CONTACT_PICKER_RESULT:
                    // handle contact results

                    Uri contactData = data.getData();

                    String name = "";

                    Cursor cursor =  managedQuery(contactData, null, null, null, null);
                    if (cursor.moveToFirst()) {
                        name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                        Log.d(TAG, "Name: " + name);
                        mAppWidgetText.setText(name);
                    }


                    ContentResolver cr = getContentResolver();
                    Cursor c = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
                            "DISPLAY_NAME = '" + name + "'", null, null);
                    if (c.moveToFirst()) {
                        String contactId =
                                c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                        //
                        //  Get all phone numbers.
                        //
                        Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                        while (phones.moveToNext()) {
                            String number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            int type = phones.getInt(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));

                            if(mPhoneNumberTextView.getText().toString().isEmpty()){
                                mPhoneNumberTextView.setText(number);
                            }

                            switch (type) {
                                case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                                    // do something with the Home number here...
                                    break;
                                case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                                    // do something with the Mobile number here...
                                    Log.d(TAG, "Number: " + number);
                                    mPhoneNumberTextView.setText(number);
                                    break;
                                case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                                    // do something with the Work number here...
                                    break;
                            }
                        }
                        phones.close();
                    }
                    cursor.close();
                    c.close();



                    cursor.close();

                    break;
            }
        } else {
            // gracefully handle failure
            Log.w(TAG, "Warning: activity result not ok");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

