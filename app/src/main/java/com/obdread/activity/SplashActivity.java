
package com.obdread.activity;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.obdread.dao.UsuarioDao;
import com.obdread.ed.Usuario;
import com.obdread.util.ClassUtil;
import com.obdread.util.MontaEstruturaBanco;

import obdread.com.obdreadmobile.R;
public class SplashActivity extends Activity implements Runnable {


	private UsuarioDao usuarioDao = new UsuarioDao(this);
	private TextView txtVersion;
	private TextView txtBase;


	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

		// Cria toda a estrutura do banco
		MontaEstruturaBanco mb = new MontaEstruturaBanco(this);

		// Se possuir rede tenta sincronizar os usuários com o WS
		if(ClassUtil.testaRede(this)){

		} else { // Sem acesso a internet
			Toast.makeText(this,ClassUtil.MSG_PADRAO_SEM_REDE, Toast.LENGTH_LONG).show();
		}

        Handler h = new Handler();
        h.postDelayed(this, 6000);   //5000
	}



	@Override
	public void run() {

		new DownloadJsonAsyncTask().execute("TESTE");

		// Verifica se já tem um usuário setado no sistema
		// Na instalação do aplicativo deve exigir o login no sistema web
		// através da serviço rest.
		Usuario user = usuarioDao.existeUsuario();

		if(user == null){

			//Carrega a tela de login
			startLogin();

		} else {
			//Se tiver um usuário cadastrado
			startMenu();
		}

	// Finaliza o splahs
	SplashActivity.this.finish();
		
	}

	private void startLogin(){
		Log.i("LOGIN","Iniciando a tela de LOGIN do sistema");
		Intent it = new Intent (getApplicationContext(), LoginActivity.class);
		startActivity(it);
	}

	private void startMenu(){
		Log.i("MENU","Iniciando a tela de MENU do sistema");
		Intent it = new Intent (getApplicationContext(), MainActivity.class);
		startActivity(it);
	}
	
	

}