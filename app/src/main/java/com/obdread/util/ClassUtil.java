package com.obdread.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

public class ClassUtil {
	
	public static final String MSG_PADRAO_SEM_REDE = "Sem Conexao com a internet, favor verificar.";
	public static final String MSG_SINCRONIZADO_SUCESSO = "Dados sincronizados com sucesso!";
	
	public static Boolean testaRede(Context context){
		
		//recupera contexto de servicos do dispositivo
    	ConnectivityManager conn = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
	
    	if(conn.getNetworkInfo(0).isConnected()){
    		// Conexao 3G ou 4G
    		return isWebservice();

    	} else if(conn.getNetworkInfo(1).isConnected()){
    		//com conexao Wifi
    		return isWebservice();
    		
    		
    	}
    	
	return false;
	}
	
	//Testa se o Webservice est� no ar
	static public boolean isWebservice(){

		//String url = ConexaoWebservice.getSoapAction();
		
		return true;
		
		/*
		
		// Busca atrav�s do link a atualiza��o dispon�vel
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);

		try {
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();

			Log.d("RETORNO HTTP", Integer.toString(statusCode));

			if (statusCode == 200) {
				return true;
			}

		} catch (ClientProtocolException e) {
					e.printStackTrace();
		} catch (IOException e) {
					e.printStackTrace();
		}

		return false;
		
		*/
		    
	}
	//Fun��o para criar hash MD5 da senha informada  
    public static String md5(String senha){  
    	
        String sen = "";  
        MessageDigest md = null;  
        
        try {  
            md = MessageDigest.getInstance("MD5");  
        } catch (NoSuchAlgorithmException e) {  
            e.printStackTrace();  
        }  
        
        BigInteger hash = new BigInteger(1, md.digest(senha.getBytes()));  
        sen = hash.toString(16);              
        
        return sen;  
        
    }  
    
    // Retorna a data e hora atual do sistema
    public static String dataHoraAtual(){
    	
    	SimpleDateFormat simpleFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
   
    return simpleFormat.format( new Date( System.currentTimeMillis() ) );	
    }
    
 // Retorna a data e hora atual do sistema
    public static String horaAtual(){
    	
    	SimpleDateFormat simpleFormat = new SimpleDateFormat("HH:mm");
   
    return simpleFormat.format( new Date( System.currentTimeMillis() ) );	
    }
    
    //Retorna a data atual do sistema
    public static String dataAtual(){
    	
    	SimpleDateFormat simpleFormat = new SimpleDateFormat("dd/MM/yyyy");
   
    return simpleFormat.format(new Date());	
    }
    
    // Enviar a hora no padr�o 00:00 em milisegundos
    public static Long getHoraForMilisegundo(String horaOrigem){
    	  
    	Long hora = new Long(horaOrigem.substring(0, 2)) * 3600; //1 hora = 3.600.000 milisegundos  
    	//int minuto = Integer.parseInt(horaOrigem.substring(3)) * 60000; //1 minuto = 60.000 milisegundos   
    	  
    	return hora;
    }
    
    // Recebe a data em string e formata para date
    public static Date stringToDate(String data){
    	
    	DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");  
    	Date date;
		try {
			date = (Date)formatter.parse(data);
			
			return date;
		} catch (ParseException e) {

			e.printStackTrace();
		}  
    	
    return null;
    }
    
    public static String getVersionAPK(Context context) {

		try {
			return context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}
