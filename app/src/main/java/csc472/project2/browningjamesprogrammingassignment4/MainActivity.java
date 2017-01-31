package csc472.project2.browningjamesprogrammingassignment4;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements
        CompoundButton.OnCheckedChangeListener {

    private static final int CHANNEL_REQUEST = 100; // request code
    private static final String TAG = "MainActivity";
    private final int MAXCHANNELS = 12;
    private boolean powerOn = false;
    private Switch powerSwitch;
    private HashMap<String, Integer> map = new HashMap();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        powerSwitch = (Switch) findViewById(R.id.powerSwitch);
        powerSwitch.setOnCheckedChangeListener(this);

        final ArrayList<Button> buttons = new ArrayList<Button>();

        for (int idx = 0; idx < MAXCHANNELS; idx++) {
            int resID = getResources().getIdentifier("button" + idx,
                    "id", getPackageName());
            buttons.add((Button) findViewById(resID));
        }

        final TextView powerTv = (TextView) findViewById(R.id.textView);
        final TextView speakerVolume = (TextView) findViewById(R.id.textView2);
        final TextView currentChannel = (TextView) findViewById(R.id.textView6);
        final SeekBar volumeSeekBar = (SeekBar) findViewById(R.id.volumeSeekBar);

        Button toDVR = (Button) findViewById(R.id.buttonDVR);
        toDVR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (powerOn) {
                    Intent intent = new Intent(MainActivity.this, DVRActivity.class);
                    startActivity(intent);
                }
            }
        });

        Button toConfigure = (Button) findViewById(R.id.buttonConfig);
        toConfigure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (powerOn) {
                    Intent data = new Intent(MainActivity.this, ConfigActivity.class);
                    startActivityForResult(data, CHANNEL_REQUEST);
                }
            }
        });

        powerTv.setText("Off");
        speakerVolume.setText("");
        currentChannel.setText("");
        volumeSeekBar.setVisibility(View.INVISIBLE);

        View.OnClickListener listener =
                new View.OnClickListener() {
                    String number = "";
                    int idx = 0;

                    public void onClick(View v) {
                        if (powerOn) {
                            String buttonText = ((Button) v).getText() + "";
                            try {
                                int buttonInt = Integer.parseInt(buttonText);
                                number += buttonText;
                                idx++;
                                if (idx == 3) {
                                    if (number.equals("000")) {
                                        currentChannel.setText("1");
                                    } else {
                                        currentChannel.setText(number);
                                    }
                                    idx = 0;
                                    number = "";
                                }
                            } catch (Exception e) {
                                if ((buttonText.equals("+")
                                        || buttonText.equals("-"))) {
                                    idx = 0;
                                    number = "";

                                    try {
                                        int channel = Integer.parseInt(currentChannel.getText().toString());
                                        if (channel <= 999) {
                                            channel = buttonText.equals("+") ? channel + 1 : channel - 1;
                                            if (channel == 0) {
                                                channel = 999;
                                            }
                                            if (channel == 1000) {
                                                channel = 1;
                                            }
                                            currentChannel.setText(channel + "");
                                        }
                                    } catch (Exception e2) {
                                        currentChannel.setText("001");
                                        idx = 0;
                                    }
                                }
                            }
                        }
                    }
                };

        for (Button button : buttons) {
            button.setOnClickListener(listener);
        }

        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (powerOn) {
                    Log.d(TAG, "onProgressChanged");
                    speakerVolume.setText("" + i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.d(TAG, "onStartTrackingTouch");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d(TAG, "onStopTrackingTouch");
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHANNEL_REQUEST) {
            Button button;
            if (resultCode == RESULT_OK) {
                if (data.getCharSequenceExtra("Position").equals("Left")) {
                    button = (Button) findViewById(R.id.button12);

                } else if (data.getCharSequenceExtra("Position").equals("Right")) {
                    button = (Button) findViewById(R.id.button14);
                } else {
                    button = (Button) findViewById(R.id.button13);
                }
                map.put(data.getCharSequenceExtra("Answer").toString(),
                        Integer.parseInt(data.getCharSequenceExtra("Number").toString()));

                button.setText(data.getCharSequenceExtra("Answer"));

                Log.d(TAG, "Answer is : " + data.getCharSequenceExtra("Answer"));
            } else {
            }
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton button, boolean isChecked) {
        TextView powerTv = (TextView) findViewById(R.id.textView);
        SeekBar volumeSeekBar = (SeekBar) findViewById(R.id.volumeSeekBar);
        TextView volume = (TextView) findViewById(R.id.textView2);
        TextView channel = (TextView) findViewById(R.id.textView6);
        powerOn = !powerOn;
        if (isChecked) {
            powerTv.setText("On");
            volumeSeekBar.setVisibility(View.VISIBLE);
        } else {
            volumeSeekBar.setVisibility(View.INVISIBLE);
            powerTv.setText("Off");
            volume.setText("");
            channel.setText("");
        }

        Log.d(TAG, isChecked ? "isChecked is on" : "isChecked is off");
        Log.d(TAG, powerOn ? "powerOn is on" : "powerOn is off");
    }

    public void onTextButtonClick(View v) {
        if (powerOn) {
            int buttonId = v.getId();
            Button b = (Button) findViewById(buttonId);
            TextView channel = (TextView) findViewById(R.id.textView6);
            String message = map.get(b.getText().toString())!=null && map.get(b.getText().toString()) != 0
                    ? map.get(b.getText().toString())+"" : b.getText().toString();
            channel.setText(message);
        }
    }

    public void onToggleClicked(View view) {
        ToggleButton toggleButton = (ToggleButton) view;
        Log.d(TAG, "onToggleClicked() " + toggleButton.getTag() + " " + (toggleButton.isChecked() ? "on" : "off"));
    }

    public void onSwitchClicked(View view) {
        Switch sw = (Switch) view;
        Log.d(TAG, "onSwitchClicked() " + sw.getTag() + " " + (sw.isChecked() ? "on" : "off"));
    }
}
