package com.obdread.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.obdread.dao.UsuarioDao;
import com.obdread.dao.VeiculoDao;
import com.obdread.ed.Usuario;
import com.obdread.ed.Veiculo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import obdread.com.obdreadmobile.R;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class SelecionaVeiculoActivity extends Activity {
	
	private VeiculoDao daoVeiculo = new VeiculoDao(this);
	private UsuarioDao daoUsuario = new UsuarioDao(this);
	private Usuario usuario;
	private Veiculo veiculoSelecionado;
	
	private List<Veiculo> listaVeiculo;
		
	private Spinner comboLinhas;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seleciona_veiculo);
		
		// Criar o objeto spinner comboLinhas
    	comboLinhas = (Spinner)findViewById(R.id.spUsuarios);

		usuario = daoUsuario.existeUsuario();

		BuscaVeiculoJsonAsyncTask task = new BuscaVeiculoJsonAsyncTask(this);
		task.execute("TESTE");

	}
	
	public void salvarVeiculo(View v){

	if(veiculoSelecionado != null){
		daoVeiculo.deletarTodos();
		daoVeiculo.inserir(veiculoSelecionado);

		Log.i("VEICULO","VEICULO INSERIDO COM SUCESSO");

		Log.i("MENU","Iniciando a tela de MENU do sistema");
		Intent it = new Intent (getApplicationContext(), MenuActivity.class);
		startActivity(it);

		/// Fecha esta tela.
		finish();
	}

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
	}

	public class BuscaVeiculoJsonAsyncTask extends AsyncTask<String, Void, List<Veiculo>> {

		private ProgressDialog progress;
		private Context context;

		private Context mContext;
		private String urlStr = "";
		Gson gson = new Gson();

		public BuscaVeiculoJsonAsyncTask(Context context) {
			this.context = context;
		}

		//        Ã‰ chamado antes de executar o processo (doInBackground), Podemos utilizar para criarmos um ProgressDialogo.
//        Este mÃ©todo roda em Thread de interface.
		@Override
		protected void onPreExecute() {
			//Cria novo um ProgressDialogo e exibe
			progress = new ProgressDialog(context);
			progress.setMessage("Aguarde, carregando os veiculos....");
			progress.show();
		}

		@Override
		protected List<Veiculo> doInBackground(String... params) {
			String urlString = params[0];

			try {
				//constants
				URL url = new URL("http://192.168.14.206:8080/ObdReadWeb/rest/ObdService/listaVeiculos");
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
				postDataParams.put("ticket", usuario.getTicket());
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


					return gson.fromJson(sb.toString(),new TypeToken<List<Veiculo>>(){}.getType());

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

		// Atualiza a thread prinicpal
		@Override
		protected void onPostExecute(List<Veiculo> result) {
			//Cancela progressDialogo
			progress.dismiss();
			Log.i("LISTA VEICULOS", String.valueOf(result.size()));

			listaVeiculo = result;

			carregaComboVeiculos();
		}

	}

	public void carregaComboVeiculos(){

		//Cria um ArrayAdapter usando um padrão de layout da classe R do android, passando o ArrayList nomes
		ArrayAdapter<Veiculo> arrayAdapter = new ArrayAdapter<Veiculo>(this, android.R.layout.simple_spinner_item, listaVeiculo);
		ArrayAdapter<Veiculo> spinnerArrayAdapter = arrayAdapter;
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
		comboLinhas.setAdapter(spinnerArrayAdapter);

		//Método do Spinner para capturar o item selecionado
		comboLinhas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View v, int posicao, long id) {
				//pega nome pela posição
				veiculoSelecionado = (Veiculo) parent.getSelectedItem();
				//imprime um Toast na tela com o nome que foi selecionado
				//Toast.makeText(SelecionaVeiculoActivity.this, "Nome Selecionado: " + veiculoSelecionado.getNome(), Toast.LENGTH_LONG).show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});


	}

	
}
