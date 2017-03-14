package fr.intech.dylan.android.testlight;

import android.app.Activity;
import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Calendar;
import java.util.StringTokenizer;
import java.util.TimeZone;

/**
 * Created by BlackBear9 on 10/03/2017.
 */

public class MainActivity extends Activity
{

    private int brightness;
    private ContentResolver cResolver;
    TextView TEXT_LIGHT_available, TEXT_LIGHT_reading, TEXT_LIGHT_loading, TEXT_LIGHT_infoLumAmb, TEXT_LIGHT_infoLumEc, TEXT_LIGHT_pourcent, DATE, DATE_year, DATE_hours;
    ProgressBar lightMeter, loading;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\
        //\\//\\//\\ Gestion of lightSensor //\\//\\
        //\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\
        TEXT_LIGHT_available = (TextView)findViewById(R.id.LIGHT_available);
        TEXT_LIGHT_reading = (TextView)findViewById(R.id.LIGHT_reading);
        TEXT_LIGHT_loading = (TextView)findViewById(R.id.loadingText);
        TEXT_LIGHT_infoLumAmb = (TextView)findViewById(R.id.Luminositeam);
        TEXT_LIGHT_infoLumEc = (TextView)findViewById(R.id.LuminositeEc);
        TEXT_LIGHT_pourcent = (TextView)findViewById(R.id.pourcentLumi);
        DATE = (TextView)findViewById(R.id.Date);
        DATE_year = (TextView)findViewById(R.id.year);

        TEXT_LIGHT_loading.setText("Loading...");
        loading = (ProgressBar) findViewById(R.id.loading);
        lightMeter = (ProgressBar)findViewById(R.id.progressBarLight);

        TEXT_LIGHT_pourcent.setVisibility(View.INVISIBLE);
        TEXT_LIGHT_infoLumEc.setVisibility(View.INVISIBLE);
        TEXT_LIGHT_infoLumAmb.setVisibility(View.INVISIBLE);
        lightMeter.setVisibility(View.INVISIBLE);
        lightMeter.getProgressDrawable().setColorFilter(Color.BLUE, android.graphics.PorterDuff.Mode.SRC_IN);


        SensorManager mySensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        Sensor mLightSensor = mySensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        if(mLightSensor != null)
        {
            float max = mLightSensor.getMaximumRange();
            lightMeter.setMax(255);

            mySensorManager.registerListener(LightSensorListener, mLightSensor, SensorManager.SENSOR_DELAY_NORMAL);
            TEXT_LIGHT_available.setText("SmartBrightness");
            //Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        }
        else
        {
            TEXT_LIGHT_available.setText("Vous ne possédez pas de capteur de lumière sur votre mobile.");
        }


        //\\//\\//\\//\\//\\//\\//\\//\\//\\//\\
        //\\//\\//\\ Gestion of brightness //\\
        //\\//\\//\\//\\//\\//\\//\\//\\//\\//\\
        cResolver = getContentResolver();

        try
        {
            brightness = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
            brightness = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        }
        catch(Settings.SettingNotFoundException e)
        {
            Log.e("Error", "Cannot access system brightness");

            e.printStackTrace();
        }


        //\\//\\//\\//\\//\\//\\//\\//\\//\\//\\
        //\\//\\//\\  Date  //\\//\\//\\//\\//\\
        //\\//\\//\\//\\//\\//\\//\\//\\//\\//\\
        Calendar c = Calendar.getInstance(TimeZone.getDefault());

        String day;
        String month;
        switch(c.get(Calendar.DAY_OF_WEEK))
        {
            case Calendar.MONDAY:
                day = "Lundi";
                break;

            case Calendar.TUESDAY:
                day = "Mardi";
                break;

            case Calendar.WEDNESDAY:
                day = "Mercredi";
                break;

            case Calendar.THURSDAY:
                day = "Jeudi";
                break;

            case Calendar.FRIDAY:
                day = "Vendredi";
                break;

            case Calendar.SATURDAY:
                day = "Samedi";
                break;

            case Calendar.SUNDAY:
                day = "Dimanche";
                break;

            default:
                day = "Undefined";
                break;
        }

        switch (c.get(Calendar.MONTH))
        {
            case 0:
                month = "Janvier";
                break;
            case 1:
                month = "Fevrier";
                break;
            case 2:
                month = "Mars";
                break;
            case 3:
                month = "Avril";
                break;
            case 4:
                month = "Mai";
                break;
            case 5:
                month = "Juin";
                break;
            case 6:
                month = "Juillet";
                break;
            case 7:
                month = "Août";
                break;
            case 8:
                month = "Septembre";
                break;
            case 9:
                month = "Octobre";
                break;
            case 10:
                month = "Novembre";
                break;
            case 11:
                month = "Decembre";
                break;
            default:
                month = "Undefined";
                break;
        }


        DATE.setText("" + day + " " + c.get(Calendar.DAY_OF_MONTH) + " " + month);
        DATE_year.setText("" + c.get(Calendar.YEAR));


    }


    private final SensorEventListener LightSensorListener = new SensorEventListener(){

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}

        @Override
        public void onSensorChanged(SensorEvent event) {
            if(event.sensor.getType() == Sensor.TYPE_LIGHT){
                TEXT_LIGHT_loading.setVisibility(View.INVISIBLE);
                TEXT_LIGHT_infoLumAmb.setVisibility(View.VISIBLE);
                TEXT_LIGHT_infoLumEc.setVisibility(View.VISIBLE);
                TEXT_LIGHT_pourcent.setVisibility(View.VISIBLE);
                loading.setVisibility(View.INVISIBLE);
                lightMeter.setVisibility(View.VISIBLE);
                TEXT_LIGHT_reading.setText(""+event.values[0]);

                int i = Math.round((event.values[0] / (float)255)*100);


                if (((event.values[0] / (float)255)*100) > 100)
                {
                    i = 100;
                }

                TEXT_LIGHT_pourcent.setText(i + "%");
                lightMeter.setProgress((int)event.values[0]);
                Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, brightness);
                WindowManager.LayoutParams layoutpars = getWindow().getAttributes();
                layoutpars.screenBrightness = event.values[0] / (float)255;
                getWindow().setAttributes(layoutpars);
            }

        }
    };



}
