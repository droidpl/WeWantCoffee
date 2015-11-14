package com.github.droidpl.android.wewantcoffee.activities;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.droidpl.android.wewantcoffee.R;

import java.util.ArrayList;

public class WalkieTalkieActivity extends AppCompatActivity {

    private String phone;
    private static final int REQUEST_SPEECH = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walkie_talkie);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        startActivityForResult(i, REQUEST_SPEECH);

        phone = getIntent().getStringExtra("phone");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case REQUEST_SPEECH:
                ArrayList results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                final String speech = (String) results.get(0);
                TextView tvSpeech = (TextView) findViewById(R.id.tvSpeech);
                tvSpeech.setText(speech);

                Button btnSendSpeech = (Button) findViewById(R.id.btnSendSpeech);
                btnSendSpeech.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (phone != null) {
                            SmsManager.getDefault().sendTextMessage(phone, null, speech, null, null);
                        }
                    }
                });

                break;
        }
    }

}
