package com.obdread.activity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

import com.google.gson.Gson;
import com.obdread.dao.UsuarioDao;
import com.obdread.ed.Usuario;

import org.json.JSONException;
import org.json.JSONObject;

import obdread.com.obdreadmobile.R;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends Activity {
	
	private UsuarioDao dao = new UsuarioDao(this);
	private Usuario usuarioEnvio = new Usuario();
	private Usuario usuario;
	
	private List<Usuario> listaUsuario;
		
	private Spinner comboLinhas;
	private EditText senha;
	private EditText email;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		// Criar o objeto spinner comboLinhas
    //	comboLinhas = (Spinner)findViewById(R.id.spUsuarios);
    	
    	senha = (EditText) findViewById(R.id.password);
		email =  (EditText) findViewById(R.id.email);

	}
	
	public void logar(View v){

		if (email.getText().toString().length() == 0) {
			email.setError("Digite o email!");
			return;
		}

		if (senha.getText().toString().length() == 0) {
			senha.setError("Digite a senha!");
			return;
		}

		LoginJsonAsyncTask task = new LoginJsonAsyncTask(this);
		task.execute("TESTE");

		usuarioEnvio.setEmail(email.getText().toString());
		usuarioEnvio.setSenha(senha.getText().toString());

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
//				Intent it = new Intent (getBaseContext(), MainActivity.class);
//				startActivity(it);
//
//				finish();
				
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


	public class LoginJsonAsyncTask extends AsyncTask<String, Void, Usuario> {

		private ProgressDialog progress;
		private Context context;

		private Context mContext;
		private String urlStr = "";
		Gson gson = new Gson();

		public LoginJsonAsyncTask(Context context) {
			this.context = context;
		}

		@Override
		protected void onPreExecute() {
			//Cria novo um ProgressDialogo e exibe
			progress = new ProgressDialog(context);
			progress.setMessage("Aguarde, realizando o login....");
			progress.show();
		}

		@Override
		protected Usuario doInBackground(String... params) {
			String urlString = params[0];

			try {
				//URL url = new URL(Preferencias.getUrlServicoRestWeb(context) + "/rest/ObdService/login");
				URL url = new URL("http://192.168.14.206:8080/ObdReadWeb/rest/ObdService/login");
				//URL url = new URL("http://192.168.14.206:8080/ObdReadWeb/rest/ObdService");
				String message = new JSONObject().toString();

				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setReadTimeout(15000 /*milliseconds*/);
				conn.setConnectTimeout(15000 /* milliseconds */);
				conn.setRequestMethod("POST"); // or POST
				conn.setDoInput(true);
				conn.setDoOutput(true);

				// Don't use a cached copy.
				conn.setUseCaches(false);
				// conn.setFixedLengthStreamingMode(message.getBytes().length);

				//conn.setRequestProperty("Connection", "Keep-Alive");
				// conn.setRequestProperty("http.keepAlive", "false");

				//make some HTTP header nicety
				conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

				//  conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
				//   conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");

				// Abre a conexão
				conn.connect();

				//// Monta os parâmetros
				JSONObject postDataParams = new JSONObject();
				postDataParams.put("email", usuarioEnvio.getEmail());
				postDataParams.put("senha", usuarioEnvio.getSenha());
				Log.e("params",postDataParams.toString());

				DataOutputStream wr = new DataOutputStream(conn.getOutputStream ());
				wr.writeBytes(postDataParams.toString());

				int responseCode = conn.getResponseCode();

				if (responseCode == HttpURLConnection.HTTP_OK) {

					InputStream in = conn.getInputStream();

					BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					StringBuilder sb = new StringBuilder();
					String line;
					while ((line = br.readLine()) != null) {
						sb.append(line + "\n");
					}
					br.close();

					Log.i("JSON", sb.toString());


					return gson.fromJson(sb.toString(),Usuario.class);

				} else {
					Toast.makeText(context, "Sistema Web não disponível",
							Toast.LENGTH_LONG).show();
				}

			} catch (ProtocolException e) {
				e.printStackTrace();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;

		}

		public String getPostDataString(JSONObject params) throws Exception {

			StringBuilder result = new StringBuilder();
			boolean first = true;

			Iterator<String> itr = params.keys();

			while(itr.hasNext()){

				String key= itr.next();
				Object value = params.get(key);

				if (first)
					first = false;
				else
					result.append("&");

				result.append(URLEncoder.encode(key, "UTF-8"));
				result.append("=");
				result.append(URLEncoder.encode(value.toString(), "UTF-8"));

			}
			return result.toString();
		}

		// Atualiza a thread prinicpal
		@Override
		protected void onPostExecute(Usuario result) {
			//Cancela progressDialogo
			progress.dismiss();


			Log.i("JSON RETORNO",gson.toJson(usuario));

			usuario = result;

			/// Se conseguiu logar vai inserir o usuário no banco
			if(usuario != null && usuario.getTicket() != null){
				dao.inserir(usuario);
				startSelecionaVeiculo();
			} else {
				Toast.makeText(context,"Servidor indisponível no momento.",
						Toast.LENGTH_LONG).show();
			}
		}

	}

	private void startSelecionaVeiculo(){
		Log.i("MENU","Iniciando a tela de MENU do sistema");
		Intent it = new Intent (getApplicationContext(), SelecionaVeiculoActivity.class);
		startActivity(it);

		// Fecha a tela de login
		finish();
	}
	
}
