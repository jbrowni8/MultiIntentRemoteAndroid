package csc472.project2.browningjamesprogrammingassignment4;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class ConfigActivity extends AppCompatActivity {

    private static final String KEY = "key";
    private static boolean powerOn = false;
    private int sum = 0;
    private String message = "";

    private int radioButtonPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        final EditText label = (EditText) findViewById(R.id.editText);
        label.requestFocus();

        final RadioButton button = (RadioButton) findViewById(R.id.radioButton5);
        button.setChecked(true);
        final Button save = (Button) findViewById(R.id.button15);
        System.out.println(button.getText().toString());
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (label.getText().toString().length() < 2
                        || label.getText().toString().length() > 4) {
                    Toast.makeText(ConfigActivity.this,
                            "Favorite channel must be between 2 and 4 characters.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    TextView number = (TextView) findViewById(R.id.editText2);
                    RadioButton radioButton = (RadioButton) findViewById(radioButtonPos);
                    if (radioButton == null) {
                        radioButton = button;
                    }
                    // System.out.println("Position is : " + radioButton.getText().toString());
                    // System.out.println("Label is : " + label.getText().toString());

                    Intent data = new Intent(ConfigActivity.this, MainActivity.class);
                    data.putExtra("Answer", label.getText());
                    data.putExtra("Number", number.getText());
                    data.putExtra("Position", radioButton.getText());
                    setResult(RESULT_OK, data);
                    finish();
                }
            }
        });

        Button cancel = (Button) findViewById(R.id.button);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    public int onRadioButtonClick(View v) {
        RadioButton radioButton;
        radioButtonPos = v.getId();
        radioButton = (RadioButton) findViewById(radioButtonPos);
        System.out.println(radioButton.getText().toString());
        return radioButtonPos;
    }

    public void onButtonClick(View v) {
        TextView et = (TextView) findViewById(R.id.editText2);
        int buttonId = v.getId();
        Button b = (Button) findViewById(buttonId);
        message += b.getText().toString();
        if (message.length() <= 3) {
            et.setText(message);
        }
    }

    public void upOrDownClick(View v) {
        TextView et = (TextView) findViewById(R.id.editText2);
        sum = Integer.parseInt(et.getText().toString());
        int buttonId = v.getId();
        Button b = (Button) findViewById(buttonId);
        if (b.getText().toString().equals("+")) {
            sum++;
        } else {
            if (sum > 0) {
                sum--;
            }
        }
        et.setText(sum + "");
    }
}
