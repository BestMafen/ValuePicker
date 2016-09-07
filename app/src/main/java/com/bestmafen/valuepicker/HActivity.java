package com.bestmafen.valuepicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import ValuePicker.OnValuePickedListener;
import ValuePicker.ValuePickerH;

/**
 * Created by xiaokai on 2016/9/7.
 */
public class HActivity extends AppCompatActivity {
    private ValuePickerH h1, h2, h3, h4;
    private TextView tv1, tv2, tv3, tv4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);
        tv4 = (TextView) findViewById(R.id.tv4);

        h1 = (ValuePickerH) findViewById(R.id.h1);
        h2 = (ValuePickerH) findViewById(R.id.h2);
        h3 = (ValuePickerH) findViewById(R.id.h3);
        h4 = (ValuePickerH) findViewById(R.id.h4);

        h1.setParams(ValuePickerH.Gravity.TOP, ValuePickerH.Orientation.UP, 20, 100, 0.5f, 50);
        h2.setParams(ValuePickerH.Gravity.TOP, ValuePickerH.Orientation.DOWN, 20, 100, 0.5f, 50);
        h3.setParams(ValuePickerH.Gravity.BOTTOM, ValuePickerH.Orientation.UP, 20, 100, 0.5f, 50);
        h4.setParams(ValuePickerH.Gravity.BOTTOM, ValuePickerH.Orientation.DOWN, 20, 100, 0.5f, 50);

        h1.setOnValuePickedListener(new OnValuePickedListener() {
            @Override
            public void onValueSelected(float value) {
                tv1.setText(String.valueOf(value));
            }
        });
        h2.setOnValuePickedListener(new OnValuePickedListener() {
            @Override
            public void onValueSelected(float value) {
                tv2.setText(String.valueOf(value));
            }
        });
        h3.setOnValuePickedListener(new OnValuePickedListener() {
            @Override
            public void onValueSelected(float value) {
                tv3.setText(String.valueOf(value));
            }
        });
        h4.setOnValuePickedListener(new OnValuePickedListener() {
            @Override
            public void onValueSelected(float value) {
                tv4.setText(String.valueOf(value));
            }
        });
    }
}
