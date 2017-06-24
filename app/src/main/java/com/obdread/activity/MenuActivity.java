package com.obdread.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.obdread.service.EnviaDadosWeb;

import obdread.com.obdreadmobile.R;

public class MenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        /// Verifica se possui dados para subir ao webservice
        /// Sobe os dados através de uma thread separada da aplicação
        EnviaDadosWeb task = new EnviaDadosWeb(this);
        task.execute("TESTE");
    }

    // Activity das preferÃªncias
    public void preferences(View v){
        //Carrega o rastreamento
        Intent it = new Intent (getBaseContext(), ConfigActivity.class);
        startActivity(it);
    }


    //// INICIA A TELA DE LEITURA DO OBD
    // Activity das preferÃªncias
    public void readObd(View v){
        //Carrega o rastreamento
        Intent it = new Intent (getBaseContext(), MainActivity.class);
        startActivity(it);
    }

    //// INICIA A TELA DE LEITURA DO OBD
    // Activity das preferÃªncias
    public void readErrosECU(View v){
        //Carrega o rastreamento
        Intent it = new Intent (getBaseContext(), ErrosEcuActivity.class);
        startActivity(it);
    }

    // Faz o logoff no sistema
    public void logoff(View v){

        AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);
        builder.setMessage("Deseja Finalizar?")
                .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                        finish();

                    }
                })
                .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        AlertDialog dialog = builder.show();

    }
}
