
package com.obdread.activity;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import obdread.com.obdreadmobile.R;
public class SplashActivity extends Activity implements Runnable {
	

	private TextView txtVersion;
	private TextView txtBase;


	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

      	

        


        Handler h = new Handler();
        h.postDelayed(this, 6000);   //5000
	}

	private void startMenu(){
		Intent it = new Intent (getApplicationContext(), MainActivity.class);
		startActivity(it);
	}

	@Override
	public void run() {


//Se tiver em sessão vai direto para o menu
		startMenu();
	
	// Finaliza o splahs
	SplashActivity.this.finish();
		
	}

	
	

}