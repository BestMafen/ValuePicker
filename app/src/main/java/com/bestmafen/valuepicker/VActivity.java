package com.bestmafen.valuepicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import ValuePicker.OnValuePickedListener;
import ValuePicker.ValuePickerV;

/**
 * Created by xiaokai on 2016/9/7.
 */
public class VActivity extends AppCompatActivity {
    private ValuePickerV v1, v2, v3, v4;
    private TextView tv1, tv2, tv3, tv4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);
        tv4 = (TextView) findViewById(R.id.tv4);

        v1 = (ValuePickerV) findViewById(R.id.v1);
        v2 = (ValuePickerV) findViewById(R.id.v2);
        v3 = (ValuePickerV) findViewById(R.id.v3);
        v4 = (ValuePickerV) findViewById(R.id.v4);

        v1.setParams(ValuePickerV.Gravity.START, ValuePickerV.Orientation.LEFT, 20, 100, 0.5f, 50);
        v2.setParams(ValuePickerV.Gravity.START, ValuePickerV.Orientation.RIGHT, 20, 100, 0.5f, 50);
        v3.setParams(ValuePickerV.Gravity.END, ValuePickerV.Orientation.LEFT, 20, 100, 0.5f, 50);
        v4.setParams(ValuePickerV.Gravity.END, ValuePickerV.Orientation.RIGHT, 20, 100, 0.5f, 50);

        v1.setOnValuePickedListener(new OnValuePickedListener() {
            @Override
            public void onValueSelected(float value) {
                tv1.setText(String.valueOf(value));
            }
        });
        v2.setOnValuePickedListener(new OnValuePickedListener() {
            @Override
            public void onValueSelected(float value) {
                tv2.setText(String.valueOf(value));
            }
        });
        v3.setOnValuePickedListener(new OnValuePickedListener() {
            @Override
            public void onValueSelected(float value) {
                tv3.setText(String.valueOf(value));
            }
        });
        v4.setOnValuePickedListener(new OnValuePickedListener() {
            @Override
            public void onValueSelected(float value) {
                tv4.setText(String.valueOf(value));
            }
        });
    }
}
