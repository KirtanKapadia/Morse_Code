package com.example.android.morsecode;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private CameraManager mCameraManager;
    private String mCameraID;
    private Button Translate;
    private Button sos;
    private Boolean isTorchOn;
    private MediaPlayer mp;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Translate = (Button) findViewById(R.id.translate);
        sos = (Button) findViewById(R.id.sos);
        isTorchOn = false;

        Translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                turnOffFlashLight();
            }
        });

        Boolean isFlashAvailable = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (!isFlashAvailable) {

            AlertDialog alert = new AlertDialog.Builder(MainActivity.this)
                    .create();
            alert.setTitle("Error !!");
            alert.setMessage("Your device doesn't support flash light!");
            alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // closing the application
                    finish();
                    System.exit(0);
                }
            });
            alert.show();
            return;
        }

        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            mCameraID = mCameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }

    Map<String, String> encoding_map = new HashMap<String, String>() {
        {
            put("a", ".-");
            put("b", "-...");
            put("c", "-.-.");
            put("d", "-..");
            put("e", ".");
            put("f", "..-.");
            put("g", "--.");
            put("h", "....");
            put("i", "..");
            put("j", ".---");
            put("k", "-.-");
            put("l", ".-..");
            put("m", "--");
            put("n", "-.");
            put("o", "---");
            put("p", ".--.");
            put("q", "--.-");
            put("r", ".-.");
            put("s", "...");
            put("t", "-");
            put("u", "..-");
            put("v", "...-");
            put("w", ".--");
            put("x", "-..-");
            put("y", "-.--");
            put("z", "--..");
            put(" ", "/");
            put("1", ".----");
            put("2", "..---");
            put("3", "...--");
            put("4", "....-");
            put("5", ".....");
            put("6", "-....");
            put("7", "--...");
            put("8", "---..");
            put("9", "----.");
            put("0", "-----");
        }
    };

    public String encode(String text) {
        String answer = "";
        int index = 0;
        while (index < text.length()) {
            answer = answer + encoding_map.get(String.valueOf(text.charAt(index)));
            answer += " ";
            index++;
        }
        return answer;
    }

    public String decode(String morse) {

        List<String> morseList = new ArrayList<String>(Arrays.asList(morse.split(" ")));
        String answer = "";
        int index;
        List<String> keys = new ArrayList<String>(encoding_map.keySet());
        List<String> values = new ArrayList<String>(encoding_map.values());
        for (String temp : morseList) {
            index = values.indexOf(temp);
            answer = answer + keys.get(index);
        }
        return answer;
    }

    public void transmit(View button) {
        EditText SimpleText = (EditText) findViewById(R.id.simpletext);
        EditText MorseCode = (EditText) findViewById(R.id.morsecode);

        String simpletext = SimpleText.getText().toString().toLowerCase();

        String morseCode = MorseCode.getText().toString();

        if (simpletext.isEmpty() && morseCode.isEmpty()) {
            Toast.makeText(this, "Please Enter Something!!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (morseCode.isEmpty() && !simpletext.isEmpty()) {
            if (Pattern.matches("^[a-z0-9]+$]", simpletext)) {
                String encoded_msg = encode(simpletext);
                MorseCode.setText(encoded_msg, TextView.BufferType.EDITABLE);
            } else {
                Toast.makeText(this, "Message shouldn't contain special characters", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (!simpletext.isEmpty() && !morseCode.isEmpty()) {
            if (Pattern.matches("^[a-z0-9]+$]", simpletext)) {
                String encoded_msg = encode(simpletext);
                MorseCode.setText(encoded_msg, TextView.BufferType.EDITABLE);
            } else {
                Toast.makeText(this, "Message shouldn't contain special characters", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (simpletext.isEmpty() && !morseCode.isEmpty()) {
            if (Pattern.matches("^[\\.\\s/-]+$", morseCode)) {
                String decoded_msg = decode(morseCode);
                SimpleText.setText(decoded_msg);
            } else {
                Toast.makeText(this, "Invalid Input", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    public void translate(View button) {
        EditText SimpleText = (EditText) findViewById(R.id.simpletext);
        EditText MorseCode = (EditText) findViewById(R.id.morsecode);

        String simpletext = SimpleText.getText().toString().toLowerCase();

        String morseCode = MorseCode.getText().toString();

        if (simpletext.isEmpty() && morseCode.isEmpty()) {
            Toast.makeText(this, "Please Enter Something!!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (simpletext.isEmpty() && !morseCode.isEmpty()) {
            if (Pattern.matches("^[\\.\\s/-]+$", morseCode)) {
                String decoded_msg = decode(morseCode);
                SimpleText.setText(decoded_msg);

            } else {
                Toast.makeText(this, "Invalid Input", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (morseCode.isEmpty() && !simpletext.isEmpty()) {
            if (Pattern.matches("^[a-z0-9]+$", simpletext)) {
                String encoded_msg = encode(simpletext);
                MorseCode.setText(encoded_msg, TextView.BufferType.EDITABLE);
            } else {
                Toast.makeText(this, "Message shouldn't contain special characters", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (!simpletext.isEmpty() && !morseCode.isEmpty()) {
            if (Pattern.matches("^[a-z0-9]+$", simpletext)) {
                String encoded_msg = encode(simpletext);
                MorseCode.setText(encoded_msg, TextView.BufferType.EDITABLE);
            } else {
                Toast.makeText(this, "Message shouldn't contain special characters", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    public void turnOnFlashLight() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mCameraManager.setTorchMode(mCameraID, true);
                //playOnOffSound();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void turnOffFlashLight() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mCameraManager.setTorchMode(mCameraID, false);
                //playOnOffSound();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sos(View view) {
        String sos = "... --- ...";
        Switch transmit = (Switch) findViewById(R.id.switch1);
        if (!transmit.isChecked()) {
            transmit.setChecked(true);
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < sos.length(); i++) {
            char c = sos.charAt(i);
            Toast.makeText(this, String.valueOf(i), Toast.LENGTH_SHORT).show();
            if (String.valueOf(c).equals(".")) {
                turnOnFlashLight();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                turnOffFlashLight();
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else if (String.valueOf(c).equals("-")) {
                turnOnFlashLight();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                turnOffFlashLight();
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else if (String.valueOf(c).equals(" ")) {
                turnOnFlashLight();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                turnOffFlashLight();
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else if (String.valueOf(c).equals("/")) {
                turnOnFlashLight();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                turnOffFlashLight();
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}


//        int index = 0;
//        while(index < sos.length()){
//            if(String.valueOf(sos.charAt(index)).equals(".")){
//                turnOnFlashLight();
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        //Do something after 100ms
//                        turnOffFlashLight();
//                    }
//                }, 2000);
//            }
//            if(String.valueOf(sos.charAt(index)).equals("-")){
//                turnOnFlashLight();
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        //Do something after 100ms
//                        turnOffFlashLight();
//                    }
//                }, 2000);
//            }
//            if(String.valueOf(sos.charAt(index)).equals(" ")){
//                turnOnFlashLight();
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        //Do something after 100ms
//                        turnOffFlashLight();
//                    }
//                }, 3000);
//            }
//            if(String.valueOf(sos.charAt(index)).equals("/")){
//                turnOnFlashLight();
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        //Do something after 100ms
//                        turnOffFlashLight();
//                    }
//                }, 2000);
//            }
//            index++;



