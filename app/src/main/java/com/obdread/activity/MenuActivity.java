package com.obdread.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import obdread.com.obdreadmobile.R;

public class MenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    // Activity das preferÃªncias
    public void preferences(View v){
        //Carrega o rastreamento
        Intent it = new Intent (getBaseContext(), Preferencias.class);
        startActivity(it);
    }


    //// INICIA A TELA DE LEITURA DO OBD
    // Activity das preferÃªncias
    public void readObd(View v){
        //Carrega o rastreamento
        Intent it = new Intent (getBaseContext(), MainActivity.class);
        startActivity(it);
    }
}
