package com.obdread.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.github.pires.obd.commands.control.TroubleCodesCommand;
import com.github.pires.obd.commands.protocol.EchoOffCommand;
import com.github.pires.obd.commands.protocol.LineFeedOffCommand;
import com.github.pires.obd.commands.protocol.ObdResetCommand;
import com.github.pires.obd.commands.protocol.ResetTroubleCodesCommand;
import com.github.pires.obd.commands.protocol.SelectProtocolCommand;
import com.github.pires.obd.enums.ObdProtocols;
import com.github.pires.obd.exceptions.MisunderstoodCommandException;
import com.github.pires.obd.exceptions.NoDataException;
import com.github.pires.obd.exceptions.UnableToConnectException;
import obdread.com.obdreadmobile.R;

import com.obdread.dao.ErrosECUDao;
import com.obdread.dao.UsuarioDao;
import com.obdread.dao.VeiculoDao;
import com.obdread.ed.ErrosECU;
import com.obdread.io.BluetoothManager;
import com.google.inject.Inject;
import com.obdread.service.EnviaDadosWeb;
import com.obdread.util.ClassUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ErrosEcuActivity extends Activity {

    private static final String TAG = ErrosEcuActivity.class.getName();
    private static final int NO_BLUETOOTH_DEVICE_SELECTED = 0;
    private static final int CANNOT_CONNECT_TO_DEVICE = 1;
    private static final int NO_DATA = 3;
    private static final int DATA_OK = 4;
    private static final int CLEAR_DTC = 5;
    private static final int OBD_COMMAND_FAILURE = 10;
    private static final int OBD_COMMAND_FAILURE_IO = 11;
    private static final int OBD_COMMAND_FAILURE_UTC = 12;
    private static final int OBD_COMMAND_FAILURE_IE = 13;
    private static final int OBD_COMMAND_FAILURE_MIS = 14;
    private static final int OBD_COMMAND_FAILURE_NODATA = 15;
    @Inject
    SharedPreferences prefs;
    private ProgressDialog progressDialog;
    private String remoteDevice;
    private GetTroubleCodesTask gtct;
    private BluetoothDevice dev = null;
    private BluetoothSocket sock = null;

    private ErrosECU errosEcu;
    private ErrosECUDao errosECUDao;
    private UsuarioDao userDao;
    private VeiculoDao veiculoDao;

    private Handler mHandler = new Handler(new Handler.Callback() {


        public boolean handleMessage(Message msg) {
             Log.d(TAG, "Mensagem recebida");
            switch (msg.what) {
                case NO_BLUETOOTH_DEVICE_SELECTED:
                    makeToast("Sem interface OBD-II selecionado");
                    finish();
                    break;
                case CANNOT_CONNECT_TO_DEVICE:
                    makeToast("Favor selecionar a Interface OBD-II nas preferências!");
                    finish();
                    break;

                case OBD_COMMAND_FAILURE:
                    makeToast("Falha ao conectar na interface OBD-II");
                    finish();
                    break;
                case OBD_COMMAND_FAILURE_IO:
                    makeToast("Comando OBD-II falhou: IO");
                    finish();
                    break;
                case OBD_COMMAND_FAILURE_IE:
                    makeToast("Comando OBD-II falhou: IE");
                    finish();
                    break;
                case OBD_COMMAND_FAILURE_MIS:
                    makeToast(getString(R.string.text_obd_command_failure) + " MIS");
                    finish();
                    break;
                case OBD_COMMAND_FAILURE_UTC:
                    makeToast("Comando OBD-II falhou: UTC");
                    finish();
                    break;
                case OBD_COMMAND_FAILURE_NODATA:
                    makeToastLong("Sem Erros na ECU");
                    //finish();
                    break;

                case NO_DATA:
                    makeToast("Sem Erros na ECU");
                    ///finish();
                    break;
                case DATA_OK:
                    dataOk((String) msg.obj);
                    break;

            }
            return false;
        }
    });
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        errosECUDao = new ErrosECUDao(this);
        userDao = new UsuarioDao(this);
        veiculoDao = new VeiculoDao(this);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        // Identifica se a interface OBD foi pareada ao dispositivo
        remoteDevice = prefs.getString(ConfigActivity.BLUETOOTH_LIST_KEY, null);
        if (remoteDevice == null || "".equals(remoteDevice)) {
            Log.e(TAG, "Favor selecionar a Interface OBD-II nas preferências");
            mHandler.obtainMessage(NO_BLUETOOTH_DEVICE_SELECTED).sendToTarget();
        } else {
            gtct = new GetTroubleCodesTask();
            gtct.execute(remoteDevice);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.trouble_codes, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_clear_codes:
                try {
                    sock = BluetoothManager.connect(dev);
                } catch (Exception e) {
                    Log.e(TAG,"Ocorreu um erro ao estabelecer a conexão. -> " + e.getMessage());
                    mHandler.obtainMessage(CANNOT_CONNECT_TO_DEVICE).sendToTarget();
                    return true;
                }
                try {
                    ResetTroubleCodesCommand clear = new ResetTroubleCodesCommand();
                    clear.run(sock.getInputStream(), sock.getOutputStream());
                    String result = clear.getFormattedResult();
                    Log.d("TESTRESET", "Trying reset result: " + result);
                } catch (Exception e) {
                    Log.e(TAG, "Ocorreu um erro ao estabelecer a conexão. -> " + e.getMessage());
                }
                gtct.closeSocket(sock);
                // Atualiza a activity principal
                Intent refresh = new Intent(this, ErrosEcuActivity.class);
                startActivity(refresh);
                this.finish(); //
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    Map<String, String> getDict(int keyId, int valId) {
        String[] keys = getResources().getStringArray(keyId);
        String[] vals = getResources().getStringArray(valId);

        Map<String, String> dict = new HashMap<String, String>();
        for (int i = 0, l = keys.length; i < l; i++) {
            dict.put(keys[i], vals[i]);
        }

        return dict;
    }

    public void makeToast(String text) {
        Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
        toast.show();
    }
    public void makeToastLong(String text) {
        Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
        toast.show();
    }
    private void dataOk(String res) {
        ListView lv = (ListView) findViewById(R.id.listView);

        /// Monta uma lista com os códigos de erro + Descrição
        Map<String, String> dtcVals = getDict(R.array.dtc_keys, R.array.dtc_values);

        //String tmpVal = dtcVals.get(res.split("\n"));
        //String[] dtcCodes = new String[]{};
        ArrayList<String> dtcCodes = new ArrayList<String>();
        //int i =1;
        if (res != null && res != "") {
            for (String dtcCode : res.split("\n")) {
                dtcCodes.add(dtcCode + " : " + dtcVals.get(dtcCode));
                Log.d("TEST", dtcCode + " : " + dtcVals.get(dtcCode));

                /// Insere o registro na base
                insereRegistroNaBase(dtcVals, dtcCode);
            }
        } else {
            dtcCodes.add("Não foram encontrados erros na ECU do veículo!");
        }
        ArrayAdapter<String> myarrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dtcCodes);
        lv.setAdapter(myarrayAdapter);
        lv.setTextFilterEnabled(true);
    }

    private void insereRegistroNaBase(Map<String, String> dtcVals, String dtcCode) {
        //// ADICIONA NA TABELA DE ERROS DA ECU
        errosEcu = new ErrosECU();
        errosEcu.setCodigo(dtcCode);
        errosEcu.setDescricao(dtcVals.get(dtcCode));
        errosEcu.setData(ClassUtil.dataAtual());
        errosEcu.setHashUser(userDao.getUsuario().getTicket());
        errosEcu.setIdVeiculo(veiculoDao.buscaVeiculoUsuario().getId());
        errosEcu.setLevel(1);

        errosECUDao.inserir(errosEcu);
    }

    public class ModifiedTroubleCodesObdCommand extends TroubleCodesCommand {
        @Override
        public String getResult() {
            // remove unwanted response from output since this results in erroneous error codes
            return rawData.replace("SEARCHING...", "").replace("NODATA", "");
        }
    }

    public class ClearDTC extends ResetTroubleCodesCommand {
        @Override
        public String getResult() {
            return rawData;
        }
    }


    private class GetTroubleCodesTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            //Create a new progress dialog
            progressDialog = new ProgressDialog(ErrosEcuActivity.this);
            //Set the progress dialog to display a horizontal progress bar
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            //Set the dialog title to 'Loading...'
            progressDialog.setTitle(getString(R.string.dialog_loading_title));
            //Set the dialog message to 'Loading application View, please wait...'
            progressDialog.setMessage(getString(R.string.dialog_loading_body));
            //This dialog can't be canceled by pressing the back key
            progressDialog.setCancelable(false);
            //This dialog isn't indeterminate
            progressDialog.setIndeterminate(false);
            //The maximum number of items is 100
            progressDialog.setMax(5);
            //Set the current progress to zero
            progressDialog.setProgress(0);
            //Display the progress dialog
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String result = "";

            //Get the current thread's token
            synchronized (this) {
                Log.d(TAG, "Iniciando serviço..");
                // get the remote Bluetooth device

                final BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
                dev = btAdapter.getRemoteDevice(params[0]);

                Log.d(TAG, "Parando o serviço Bluetooth.");
                btAdapter.cancelDiscovery();

                Log.d(TAG, "Iniciando conexão com a interface OBD-II..");

                // Instantiate a BluetoothSocket for the remote device and connect it.
                try {
                    sock = BluetoothManager.connect(dev);
                } catch (Exception e) {
                    Log.e(
                            TAG,
                            "Ocorreu um erro ao estabelecer a conexão. -> "
                                    + e.getMessage()
                    );
                    Log.d(TAG, "Conectado a interface OBD-II");
                    mHandler.obtainMessage(CANNOT_CONNECT_TO_DEVICE).sendToTarget();
                    return null;
                }

                try {
                    // Let's configure the connection.
                    Log.d(TAG, "Queueing jobs for connection configuration..");

                    onProgressUpdate(1);

                    new ObdResetCommand().run(sock.getInputStream(), sock.getOutputStream());


                    onProgressUpdate(2);

                    new EchoOffCommand().run(sock.getInputStream(), sock.getOutputStream());

                    onProgressUpdate(3);

                    new LineFeedOffCommand().run(sock.getInputStream(), sock.getOutputStream());

                    onProgressUpdate(4);

                    new SelectProtocolCommand(ObdProtocols.AUTO).run(sock.getInputStream(), sock.getOutputStream());

                    onProgressUpdate(5);

                    ModifiedTroubleCodesObdCommand tcoc = new ModifiedTroubleCodesObdCommand();
                    tcoc.run(sock.getInputStream(), sock.getOutputStream());
                    result = tcoc.getFormattedResult();

                    onProgressUpdate(6);

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("DTC-ERRO", e.getMessage());
                    mHandler.obtainMessage(OBD_COMMAND_FAILURE_IO).sendToTarget();
                    return null;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.e("DTC-ERRO", e.getMessage());
                    mHandler.obtainMessage(OBD_COMMAND_FAILURE_IE).sendToTarget();
                    return null;
                } catch (UnableToConnectException e) {
                    e.printStackTrace();
                    Log.e("DTC-ERRO", e.getMessage());
                    mHandler.obtainMessage(OBD_COMMAND_FAILURE_UTC).sendToTarget();
                    return null;
                } catch (MisunderstoodCommandException e) {
                    e.printStackTrace();
                    Log.e("DTC-ERRO", e.getMessage());
                    mHandler.obtainMessage(OBD_COMMAND_FAILURE_MIS).sendToTarget();
                    return null;
                } catch (NoDataException e) {
                    Log.e("DTC-ERRO", e.getMessage());
                    mHandler.obtainMessage(OBD_COMMAND_FAILURE_NODATA).sendToTarget();
                    return null;
                } catch (Exception e) {
                    Log.e("DTC-ERRO", e.getMessage());
                    mHandler.obtainMessage(OBD_COMMAND_FAILURE).sendToTarget();
                } finally {

                    // close socket
                    closeSocket(sock);
                }

            }

            return result;
        }

        public void closeSocket(BluetoothSocket sock) {
            if (sock != null)
                // close socket
                try {
                    sock.close();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();


            mHandler.obtainMessage(DATA_OK, result).sendToTarget();
            setContentView(R.layout.trouble_codes);

        }
    }

}
