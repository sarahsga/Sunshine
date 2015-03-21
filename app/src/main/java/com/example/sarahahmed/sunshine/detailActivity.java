package com.example.sarahahmed.sunshine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class detailActivity extends ActionBarActivity {

    static String weatherInfo;
    TextView textView = new TextView(this);
    Bundle bundle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);

            setContentView(textView);

            Intent intent = getIntent();
            bundle = intent.getExtras();
            weatherInfo = bundle.getString("weatherInfo");
            textView.setText(weatherInfo);
        } catch (Exception e) {
            Log.e("detailActivity - onCreate", "Something's wrong here");
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

    /**
     * A placeholder fragment containing a simple view.
     */
