package csc472.project2.browningjamesprogrammingassignment4;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

public class DVRActivity extends AppCompatActivity
        implements CompoundButton.OnCheckedChangeListener {

    private static final String TAG = "DVR Activity";
    private boolean powerOn = false;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dvr);

        Switch powerSwitch = (Switch) findViewById(R.id.powerDVR);
        powerSwitch.setOnCheckedChangeListener(this);

        Button button1 = (Button) findViewById(R.id.backToTV);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void buttonClickListener(View v) {
        if (powerOn) {
            int id = v.getId();
            TextView tv = (TextView) findViewById(id);
            TextView state = (TextView) findViewById(R.id.textView10);
            if (tv.getText().toString().equals("Stop")) {
                state.setText(tv.getText().toString() + "ped");
            }
            if (tv.getText().toString().equals("Play")
                    && !state.getText().toString().equals("Recording")) {
                state.setText(tv.getText().toString() + "ing");
            }
            if ((tv.getText().toString().equals("Pause")
                    || tv.getText().toString().equals("Fast Forward")
                    || tv.getText().toString().equals("Fast Rewind"))
                    && state.getText().toString().equals("Playing")) {
                state.setText(tv.getText().toString());
            }
            if (tv.getText().toString().equals("Record")
                    && state.getText().toString().equals("Stopped")) {
                state.setText(tv.getText().toString() + "ing");
            }
        }
    }

    public void onSwitchClicked(View view) {
        Switch sw = (Switch) view;
        Log.d(TAG, "onSwitchClicked() " + sw.getTag() + " " + (sw.isChecked() ? "on" : "off"));
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        TextView powerDVR = (TextView) findViewById(R.id.textView8);
        TextView state = (TextView) findViewById(R.id.textView10);

        powerOn = !powerOn;

        if (powerOn) {
            powerDVR.setText("On");
            state.setText("Ready");
        } else {
            powerDVR.setText("Off");
            state.setText("Stopped");
        }


        Log.d(TAG, isChecked ? "isChecked is on" : "isChecked is off");
        Log.d(TAG, powerOn ? "powerOn is on" : "powerOn is off");
    }


    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("DVR Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
