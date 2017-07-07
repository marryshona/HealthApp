package com.example.zohai.healthapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ActivitySignin extends AppCompatActivity {


    ImageView signinback;
    TextView forgot;
    TextView sign;
    EditText datasource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);

        signinback = (ImageView)findViewById(R.id.signinback);
        sign = (TextView)findViewById(R.id.signin);
        forgot = (TextView)findViewById(R.id.forgt);
        datasource = (EditText) findViewById(R.id.username);

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(ActivitySignin.this,Dashboard2.class);
                it.putExtra("Datasource",datasource.getText().toString());
                startActivity(it);
            }
        });
        signinback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ActivitySignin.this,MainActivity.class);
                startActivity(it);
            }
        });
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivitySignin.this,ForgotPassword.class);
                startActivity(intent);
            }
        });
    }
}
