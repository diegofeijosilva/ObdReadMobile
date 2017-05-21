package com.obdread.activity;

import android.R.integer;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.Toast;
import obdread.com.obdreadmobile.R;

public class Preferencias extends PreferenceActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		
	}
	
	// Retorna o �ltimo usu�rio logado
	public static String getUltimoLogin(Context context){
		SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);
		return SP.getString("ULTIMO_LOGIN", "NA");
	}
	
	public static void setUltimoLogin(Context context, String login){
		SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);
		Editor edit = SP.edit();
		edit.putString("ULTIMO_LOGIN", login);
		edit.apply(); 
	}
	
	// Retorna o par�metro Login Autom�tico
	public static Boolean getLoginAutomatico(Context context){
		SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);
		return SP.getBoolean("loginAutomatico", true);
	}
	
	// Retorna o tempo de vida do login
	public static Long getTempoVidaLogin(Context context){
		SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);
		
		Long i = Long.parseLong(SP.getString("vidaUtilLogin", "1"));
		return i * 3600;
	}

	// Retorna a url do serviço Rest
	public static String getUrlServicoRestWeb(Context context){
		SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);
		return SP.getString("SERVICO_REST_WEB", "NA");
	}

	public static void setUrlServicoRestWeb(Context context, String servicoRestWeb){
		SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);
		Editor edit = SP.edit();
		edit.putString("SERVICO_REST_WEB", servicoRestWeb);
		edit.apply();
	}
	

}
