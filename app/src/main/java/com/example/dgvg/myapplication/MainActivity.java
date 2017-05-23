package com.example.dgvg.myapplication;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.app.Activity;
import android.media.MediaPlayer;
import android.speech.tts.TextToSpeech;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Locale;


public class MainActivity extends Activity implements OnClickListener {
    private TextView logview;
    public EditText et;
    private Button connect, deconnect, reset;
    private BluetoothAdapter mBluetoothAdapter = null;
    TextView tadd;
    Integer count=0;
    Button b;
    //   private TextToSpeech tts;

    String longi, lati, data, disp, speak;
    private String[] logArray = null;

    private BtInterface bt = null;

    static final String TAG = "Chihuahua";
    static final int REQUEST_ENABLE_BT = 3;

    //This handler listens to messages from the bluetooth interface and adds them to the log
    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            data = msg.getData().getString("receivedData");
            addToLog(data);
            if (data.contains("a"))
                step();
        /*    else if (data.contains("b"))
                disp= "b";
            else if (data.contains("c"))
                disp= "c";
            else if (data.contains("d"))
                disp= "d";
            else if (data.contains("e"))
                disp= "e";
            else if (data.contains("f"))
                disp= "f";
            else if (data.contains("g"))
                disp= "g";
            else if (data.contains("h"))
                disp= "h";
            else if (data.contains("i"))
                disp= "i";
            else if (data.contains("j"))
                disp= "j";
            else if (data.contains("k"))
                disp= "k";
            else if (data.contains("l"))
                disp= "l";
            else if (data.contains("m"))
                disp= "m";
            else if (data.contains("n"))
                disp= "n";
            else if (data.contains("o"))
                disp= "o";
            else if (data.contains("p"))
                disp= "p";*/
            tadda();
        }
    };

    //this handler is dedicated to the status of the bluetooth connection
    final Handler handlerStatus = new Handler() {
        public void handleMessage(Message msg) {
            int status = msg.arg1;
            if (status == BtInterface.CONNECTED) {
                addToLog("Connected");
            } else if (status == BtInterface.DISCONNECTED) {
                addToLog("Disconnected");
            }
        }
    };

    //handles the log view modification
    //only the most recent messages are shown
    private void addToLog(String message) {
        for (int i = 1; i < logArray.length; i++) {
            logArray[i - 1] = logArray[i];
        }
        logArray[logArray.length - 1] = message;

        logview.setText("");
        for (int i = 0; i < logArray.length; i++) {
            if (logArray[i] != null) {
                logview.append(logArray[i] + "\n");
            }
        }
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Bundle gotBasket = getIntent().getExtras();
        MobNumberAll = gotBasket.getString("bread");
        locationgenerator();*/


        //msg = "I'm in trouble! HELP! \nLatitude: " + lati + "\nLongitude: " + longi;

        //first, inflate all layout objects, and set click listeners
        //tts = new TextToSpeech(this, this);
        logview = (TextView) findViewById(R.id.logview);
        //I chose to display only the last 3 messages
        logArray = new String[3];



        connect = (Button) findViewById(R.id.connect);
        connect.setOnClickListener(this);

        deconnect = (Button) findViewById(R.id.deconnect);
        deconnect.setOnClickListener(this);

        reset = (Button) findViewById(R.id.reset);
        reset.setOnClickListener(this);
        tadd= (TextView) findViewById(R.id.textView2);
        tadd.setText("0");
        et=(EditText)findViewById(R.id.et);
        b=(Button)findViewById(R.id.send);
        b.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String s=et.getText().toString();
                if(s.length()!=0) {
                    for (int i = 0; i < s.length(); i++) {
                        bt.sendData(s.charAt(i) + "");
                    }
                }
            }
        });

       /* tester = (Button) findViewById(R.id.bTest);
        tester.setOnClickListener(this);*/

    }

    private void tadda() {
        tadd= (TextView) findViewById(R.id.textView2);
        tadd.setText(count.toString());
        /*else if(disp.contains("b"))
            tadd.setText("How are you?");
        else if(disp.contains("c"))
            tadd.setText("Sorry!");
        else if(disp.contains("d"))
            tadd.setText("Thank You!");
        else if(disp.contains("e"))
            tadd.setText("Good to see you!");
        else if(disp.contains("f"))
            tadd.setText("Good Morning");
        else if(disp.contains("g"))
            tadd.setText("Good Afternoon");
        else if(disp.contains("h"))
            tadd.setText("Hello!");
        else if(disp.contains("i"))
            tadd.setText("Good Night");
        else if(disp.contains("j"))
            tadd.setText("My name is");
        else if(disp.contains("k"))
            tadd.setText("What's your name?");
        else if(disp.contains("l"))
            tadd.setText("I'm hungry");
        else if(disp.contains("m"))
            tadd.setText("I'm sick");
        else if(disp.contains("n"))
            tadd.setText("What is the time?");
        else if(disp.contains("o"))
            tadd.setText("I'm fine thank you.");
        else if(disp.contains("p"))
            tadd.setText("Sample");
        if(!disp.contains("p"))
            tts.speak(tadd.getText().toString(), TextToSpeech.QUEUE_ADD, null);
        else if(disp.contains("p"))
        {
            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.sample);
            mp.start();
        }*/
    }


 /*   private void locationgenerator() {
        lm= (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        Criteria c= new Criteria();
        provider = lm.getBestProvider(c,false);
        l=lm.getLastKnownLocation(provider);
        if(l!=null)
        {
            double lng=l.getLongitude();
            double lat=l.getLatitude();
            longi = String.valueOf(lng);
            lati= String.valueOf(lat);

        }
        else{
            longi= "Location not available";
            lati= "Location not available";
        }
    }*/


    //it is better to handle bluetooth connection in onResume (ie able to reset when changing screens)
    @Override
    public void onResume() {
        super.onResume();
        //first of all, we check if there is bluetooth on the phone
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            Log.v(TAG, "Device does not support Bluetooth");
        } else {
            //Device supports BT
            if (!mBluetoothAdapter.isEnabled()) {
                //if Bluetooth not activated, then request it
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            } else {
                //BT activated, then initiate the BtInterface object to handle all BT communication
                bt = new BtInterface(handlerStatus, handler);
            }
        }
    }

    //called only if the BT is not already activated, in order to activate it
    protected void onActivityResult(int requestCode, int resultCode, Intent moreData) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                //BT activated, then initiate the BtInterface object to handle all BT communication
                bt = new BtInterface(handlerStatus, handler);
            } else if (resultCode == Activity.RESULT_CANCELED)
                Log.v(TAG, "BT not activated");
            else
                Log.v(TAG, "result code not known");
        } else {
            Log.v(TAG, "request code not known");
        }
    }

    //handles the clicks on various parts of the screen
    //all buttons launch a function from the BtInterface object
    @Override
    public void onClick(View v) {
        if (v == connect) {
            addToLog("Trying to connect");
            bt.connect();
        } else if (v == deconnect) {
            addToLog("closing connection");
            bt.close();
        }
        else if(v == reset)
        {
            count=0;
            tadda();
        }/*else if (v == tester) {
            emergency(MobNumberAll);
        }*/

    }
/*
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }

    }

*/

    /*private void emergency(String phone_no) {

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phone_no));
        startActivity(callIntent);
        try{
            Log.v("phoneNumber",phone_no);
            Log.v("Message",msg);
            Intent intent=new Intent(getApplicationContext(),AndroidRemoteActivity.class);
            PendingIntent pi=PendingIntent.getActivity(getApplicationContext(), 0, intent,0);
            SmsManager smsManager= SmsManager.getDefault();
            smsManager.sendTextMessage(phone_no, null, msg, pi, null);
            Toast.makeText(getApplicationContext(), "Your SMS has been sent successfully!", Toast.LENGTH_LONG).show();
        } catch (Exception ex){
            Toast.makeText(getApplicationContext(),"Your SMS has failed...", Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }*/
/*
    @Override
    public void onDestroy() {
// Don't forget to shutdown tts!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

*/

    void step(){
        count++;
    }
}
