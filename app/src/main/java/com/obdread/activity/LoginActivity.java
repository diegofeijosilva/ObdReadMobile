package com.obdread.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.obdread.dao.UsuarioDao;
import com.obdread.ed.Usuario;

import obdread.com.obdreadmobile.R;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends Activity {
	
	private UsuarioDao dao = new UsuarioDao(this);
	private Usuario usuario = new Usuario();
	
	private List<Usuario> listaUsuario;
		
	private Spinner comboLinhas;
	private EditText password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		// Criar o objeto spinner comboLinhas
    	comboLinhas = (Spinner)findViewById(R.id.spUsuarios);
    	
    	password = (EditText) findViewById(R.id.password);

		
	}
	
	public void logar(View v){

//		if(password.getText().toString().length() <= 0){
//			password.setError("Informe sua senha!");
//			password.requestFocus();
//
//		} else {
//			usuario.setSenha(password.getText().toString());
//
//			Usuario user = new Usuario();
//
//			user = dao.login(usuario);
//
//			if( user != null){
//
//				//Log.d("Usuario", user.getNome());
//
//				// Seta no campo logado o valor 1 que identifica o usu�rio
//				user.setLogado(1);
//
//				user.setDtLogin(ClassUtil.dataAtual());
//
//				user.setHrLogin(ClassUtil.horaAtual());
//
//				dao.atualizar(user);
//
//				// Armazena nas prefer�ncias o usu�rio logado.
//				Preferencias.setUltimoLogin(this, user.getNome());
//
				Intent it = new Intent (getBaseContext(), MainActivity.class);
				startActivity(it);
				
				finish();
				
////			} else {
////
////				// Loga erro de login
////				daoLog.inserir(new LogTriade(usuario.getNome(), "MOBILE", "ERRO LOGIN: SENHA INV�LIDA"));
////
////				Toast.makeText(getBaseContext(),"Usu�rio ou senha inv�lidos. ",
////        				Toast.LENGTH_LONG).show();
////			}
//
//		}

	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
//		listaUsuario = dao.listarUsuarios();
//
//    	// Instancie um ArrayAdapter <seu objeto> passando o context (FormEquipamento.this) , o tipo do spinner
//    	//(android.R.layout.simple_spinner_item) e a lista com os objetos que voc� queira q apare�a no spinner, no caso listaCombo
//    	ArrayAdapter<Usuario> adaptador = new ArrayAdapter<Usuario>(LoginActivity.this , android.R.layout.simple_spinner_item , listaUsuario );
//
//    	// Com ArrayAdapter chame o m�todo setDropDownViewResource, para definir o tipo de lista no spinner, no caso apenas com 1 linha
//    	adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//    	// Com o ArrayAdapter j� com a lista de objetos, com o contexto , tipo de spinner e tipo de lista, chame o setAdapter do objeto
//    	// comboMarca, o nosso Spinner, onde o mesmo carregar� os objetos e exibir� na lista o toString() de cada objeto
//    	//MarcaEquipamento, no caso o getDescricao() - descri��o de cada objeto; .
//    	comboLinhas.setAdapter(adaptador);
//
//    	comboLinhas.setOnItemSelectedListener(new mySpinnerListener());
//    	comboLinhas.setPrompt("Selecione o Usu�rio");
//
//    	// Seta o ultimo usu�rio logado no sistema
//    	for (int i = 0; i < comboLinhas.getCount(); i++) {
//            if (comboLinhas.getItemAtPosition(i).toString().equals(Preferencias.getUltimoLogin(this))) {
//            	comboLinhas.setSelection(i);
//            }
//        }
		
	}
	
	// Serve para pegar a linha selecionada no Spinner
	class mySpinnerListener implements Spinner.OnItemSelectedListener
		    {
		        @Override
		        public void onItemSelected(AdapterView parent, View v, int position, long id) {
		          
		        	usuario = (Usuario) parent.getSelectedItem();
		        }
		 
		        @Override
		        public void onNothingSelected(AdapterView parent) {
		            // TODO Auto-generated method stub
		            // Do nothing.
		        }
	}
	
}
