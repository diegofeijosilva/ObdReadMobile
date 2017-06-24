package com.obdread.service;

import android.app.IntentService;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.obdread.dao.ErrosECUDao;
import com.obdread.dao.UsuarioDao;
import com.obdread.dao.VeiculoDao;
import com.obdread.ed.ErrosECU;
import com.obdread.ed.Usuario;
import com.obdread.ed.Veiculo;
import com.obdread.util.ClassUtil;

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
import java.util.List;

/**
 * Created by Diego Feijó on 21/05/2017.
 *
 * Trhead que envia os dados para o sistema web
 */

public class EnviaDadosWeb extends AsyncTask<String, Void, Usuario> {

    private Context context;
    private String URL_SISTEMA_WEB = "http://192.168.14.210:8080/ObdReadWeb/rest/ObdService/";
    Gson gson = new Gson();

    private Usuario usuario;
    private Veiculo veiculo;
    private List<ErrosECU> listaErrosECU;

    public EnviaDadosWeb(Context context) {
        this.context = context;
    }

    @Override
    protected Usuario doInBackground(String... params) {

        UsuarioDao usuarioDao = new UsuarioDao(context);
        VeiculoDao veiculoDao = new VeiculoDao(context);
        ErrosECUDao errosECUDao = new ErrosECUDao(context);

        usuario = usuarioDao.getUsuario();
        veiculo = veiculoDao.buscaVeiculoUsuario();

        if(usuario != null && veiculo != null){
            listaErrosECU = errosECUDao.listaErrosEcu();

            if (listaErrosECU.size() > 0){
                for (ErrosECU ed: listaErrosECU) {
                    this.enviaErrosEcu(ed);
                }
                errosECUDao.deletarTodos();
            }

        }

        return null;
    }

    private void enviaErrosEcu(ErrosECU errosEcu){
        try {
            //URL url = new URL(Preferencias.getUrlServicoRestWeb(context) + "/rest/ObdService/login");
            URL url = new URL(URL_SISTEMA_WEB + "recebeErrosEcu");
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
            postDataParams.put("codigo", errosEcu.getCodigo());
            postDataParams.put("data", errosEcu.getData());
            postDataParams.put("level", errosEcu.getLevel());
            postDataParams.put("idVeiculo", errosEcu.getIdVeiculo());
            postDataParams.put("hashUser", errosEcu.getHashUser());
            postDataParams.put("descricao", errosEcu.getDescricao());
            Log.e("params",postDataParams.toString());

            DataOutputStream wr = new DataOutputStream(conn.getOutputStream ());
            wr.writeBytes(postDataParams.toString());

            int responseCode = conn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                Log.i("ENVIO ERROS ECU", "ENVIO COM SUCESSO");
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
    }
}
