package com.bhullnatik.gonioviewsample;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.bhullnatik.gonioview.GonioView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final GonioView gonioView = new GonioView(this);
        setContentView(gonioView);

        gonioView.setColor(Color.RED);
        // In Pixels
        gonioView.setHandleSize(64);
        gonioView.setOnAngleSelectedListener(new GonioView.OnAngleSelectedListener() {
            @Override
            public void onAngleSelected() {
                Log.d("GonioViewSample", "Angle selected: " + gonioView.getCurrentAngle());
            }
        });
    }
}
