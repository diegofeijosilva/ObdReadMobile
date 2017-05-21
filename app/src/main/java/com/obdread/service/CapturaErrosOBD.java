package com.obdread.service;

import android.app.IntentService;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.util.Log;

import com.obdread.activity.ConfigActivity;
import com.obdread.activity.TroubleCodesActivity;
import com.obdread.dao.ErrosECUDao;
import com.obdread.dao.UsuarioDao;
import com.obdread.dao.VeiculoDao;
import com.obdread.ed.ErrosECU;
import com.obdread.ed.Usuario;
import com.obdread.ed.Veiculo;
import com.obdread.util.ClassUtil;

/**
 * Created by Diego Feijó on 21/05/2017.
 *
 * Serviço de captura dos erros da ECU
 */

public class CapturaErrosOBD extends IntentService {

    private static final String TAG = CapturaErrosOBD.class.getName();
    private BluetoothDevice dev = null;
    private String remoteDevice;

    private Usuario usuario;
    private UsuarioDao usuarioDao;

    private Veiculo veiculo;
    private VeiculoDao veiculoDao;

    private ErrosECU errosEcu;
    private ErrosECUDao errosEcuDao;

    public CapturaErrosOBD() {
        super("Serviço de Captura de dados da porta OBD-II");
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

        /// Pega o usuário
        usuario = usuarioDao.getUsuario();
        /// Pega o veiculo
        veiculo = veiculoDao.buscaVeiculoUsuario();

        if(usuario == null || veiculo == null){
            Log.i("CapturaErrosOBD", "Sem usuario ou veiculo cadastrado");
        }

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        iniciaCaptura();
        enviaErrosWeb();
    }

    private void iniciaCaptura() {

        Log.d(TAG, "Iniciando o serviço..");
        // get the remote Bluetooth device

//        remoteDevice = prefs.getString(ConfigActivity.BLUETOOTH_LIST_KEY, null);
//        if (remoteDevice == null || "".equals(remoteDevice)) {
//            Log.e(TAG, "No Bluetooth device has been selected.");
//            mHandler.obtainMessage(NO_BLUETOOTH_DEVICE_SELECTED).sendToTarget();
//        } else {
//            gtct = new TroubleCodesActivity.GetTroubleCodesTask();
//            gtct.execute(remoteDevice);
//        }
//
//        final BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
//        dev = btAdapter.getRemoteDevice(params[0]);
    }

    private void enviaErrosWeb() {
        // Se possuir rede tenta sincronizar os usuários com o WS
        if(ClassUtil.testaRede(this)){

        } else { // Sem acesso a internet
            Log.i("CapturaErrosOBD", "Sem acesso a internet.");
        }
    }
}
