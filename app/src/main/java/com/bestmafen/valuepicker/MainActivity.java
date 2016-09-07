package com.bestmafen.valuepicker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.bt_h).setOnClickListener(this);
        findViewById(R.id.bt_v).setOnClickListener(this);
        findViewById(R.id.bt_c).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_h:
                startActivity(new Intent(this, HActivity.class));
                break;
            case R.id.bt_v:
                startActivity(new Intent(this, VActivity.class));
                break;
            case R.id.bt_c:
                startActivity(new Intent(this, CActivity.class));
                break;
        }
    }
}
