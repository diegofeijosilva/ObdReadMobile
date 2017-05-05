package com.obdread.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import obdread.com.obdreadmobile.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // Activity das preferÃªncias
    public void preferences(View v){
        //Carrega o rastreamento
        Intent it = new Intent (getBaseContext(), Preferencias.class);
        startActivity(it);
    }
}
