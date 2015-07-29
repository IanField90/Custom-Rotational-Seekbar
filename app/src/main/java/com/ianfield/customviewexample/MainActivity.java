package com.ianfield.customviewexample;

import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.ianfield.customviewexample.views.RotationalSeekView;


public class MainActivity extends ActionBarActivity {

    RotationalSeekView seekView;
    Runnable increaseProgress;
    Handler handler = new Handler();

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        handler.postDelayed(increaseProgress, 1000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        seekView = (RotationalSeekView) findViewById(R.id.rotationalSeek);

        increaseProgress = new Runnable() {
            @Override
            public void run() {
                if (seekView != null) {
                    seekView.setCurrentProgress(seekView.getCurrentProgress() + 1);
                }
                handler.postDelayed(increaseProgress, 1000);
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
