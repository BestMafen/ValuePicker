package com.bestmafen.valuepicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import ValuePicker.OnValuePickedListener;
import ValuePicker.ValuePickerC;

/**
 * Created by xiaokai on 2016/9/7.
 */
public class CActivity extends AppCompatActivity {
    private ValuePickerC c1, c2, c3, c4;
    private TextView tv1, tv2, tv3, tv4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);
        tv4 = (TextView) findViewById(R.id.tv4);

        c1 = (ValuePickerC) findViewById(R.id.c1);
        c2 = (ValuePickerC) findViewById(R.id.c2);
        c3 = (ValuePickerC) findViewById(R.id.c3);
        c4 = (ValuePickerC) findViewById(R.id.c4);

        c1.setParams(ValuePickerC.Gravity.START, 20, 100, 0.5f, 50);
        c2.setParams(ValuePickerC.Gravity.TOP, 20, 100, 0.5f, 50);
        c3.setParams(ValuePickerC.Gravity.END, 20, 100, 0.5f, 50);
        c4.setParams(ValuePickerC.Gravity.BOTTOM, 20, 100, 0.5f, 50);

        c1.setOnValuePickedListener(new OnValuePickedListener() {
            @Override
            public void onValueSelected(float value) {
                tv1.setText(String.valueOf(value));
            }
        });
        c2.setOnValuePickedListener(new OnValuePickedListener() {
            @Override
            public void onValueSelected(float value) {
                tv2.setText(String.valueOf(value));
            }
        });
        c3.setOnValuePickedListener(new OnValuePickedListener() {
            @Override
            public void onValueSelected(float value) {
                tv3.setText(String.valueOf(value));
            }
        });
        c4.setOnValuePickedListener(new OnValuePickedListener() {
            @Override
            public void onValueSelected(float value) {
                tv4.setText(String.valueOf(value));
            }
        });
    }
}
