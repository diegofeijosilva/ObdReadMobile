package com.obdread.util;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.Toast;

import com.obdread.dao.DaoGenerico;

public class MontaEstruturaBanco extends DaoGenerico {
	
	private SQLiteDatabase db;
	
	private Context context;
	
	// Tabela de Usuarios
    private static final String USUARIO =
    		"CREATE TABLE IF NOT EXISTS USUARIO ( " +
            " id integer PRIMARY KEY, " +
            " ticket varchar(100), " +
			" email varchar(100) " +
            " );";

	// Tabela de Usuarios
	private static final String VEICULO =
			"CREATE TABLE IF NOT EXISTS VEICULO ( " +
					" id integer, " +
					" nome varchar(100) " +
					" );";

	// Tabela de Erros da ECU
	private static final String ERROSECU =
			"CREATE TABLE IF NOT EXISTS ERROSECU ( " +
					" hashuser varchar(100), " +
					" idveiculo integer, " +
					" data integer, " +
					" codigo varchar(5), " +
					" descricao varchar(100), " +
					" level integer" +
					" );";

//    // Tabela de usu�rios
//    private static final String USUARIO =
//    		"CREATE TABLE IF NOT EXISTS usuario ( " +
//            " idWebservice varchar(50), " +
//            " nome varchar(100), " +
//            " senha varchar(100), " +
//            " logado integer, "+
//            " DTLOGIN VARCHAR(10), "+ // Adicionado na vers�o 1.0.015
//            " HRLOGIN VARCHAR(5)" +
//            " ); ";
    
//    // Tabela de usu�rios
//    private static final String OS =
//    		"CREATE TABLE IF NOT EXISTS ordemServico ( " +
//
//            " idWebservice INTEGER PRIMARY KEY, " +
//            " idUsuario varchar(50), " +
//            " idMotivo INTEGER, " +
//
//            " codigoOS varchar(100), " +
//            " descricaoOS varchar(100), " +
//            " statusOs integer, " +
//            " obsOS varchar(400), " +
//
//			" gpsLatCliente varchar(25), " +
//			" gpsLongCliente varchar(25), " +
//
//			" cliente varchar(100), " +
//			" rua varchar(100), " +
//			" bairro varchar(100), " +
//			" cidade varchar(100), " +
//			" telefone varchar(20), " +
//
//			" gpsLatTec varchar(25), " +
//			" gpsLongTec varchar(25), " +
//            " sucessoAtendimento integer, " +
//            " obsAtendimento varchar(200), " +
//            " idservico integer, " + 				// inserido na vers�o 1.0.2
//            " dataServico integer, " + 				// inserido na vers�o 1.0.2
//            " horaServico varchar(6), " + 			// inserido na vers�o 1.0.3
//            " flagMobile INTEGER DEFAULT 0, " +		// inserido na vers�o 1.0.011
//
//			" NUMCASA VARCHAR(20), " +				// inserido na vers�o 1.0.014
//			" COMPLEMENTO VARCHAR(100), " +
//			" CELULAR VARCHAR(10), " +
//			" OUTROFONE VARCHAR(10) " +
//            " ); ";
//
//    // Tabela de Motivos
//    private static final String LOG = 				// inserido na vers�o 1.0.009
//    		"CREATE TABLE IF NOT EXISTS log ( " +
//            " id integer PRIMARY KEY, " +
//            " usuario varchar(50), " +
//            " dispositivo varchar(20), " +
//            " operacao varchar(100) " +
//            " );";

 	public MontaEstruturaBanco(Context context) {
 		super(context);
 		this.context = context;
 		Log.i("BASE DE DADOS", "CRIANDO ESTRUTURA");
		dbOpen();
		Log.d("VERSAO BASE ATUAL", Integer.toString(db.getVersion()));
		
		// Cria as tabelas
		db.execSQL(USUARIO);
		db.execSQL(VEICULO);
		db.execSQL(ERROSECU);

		dbClose();


	}	
 	
 	// Abre o banco
 	private void dbOpen(){
 		this.db = this.getWritableDatabase();
 	}
 	
 	// Fecha o banco
 	private void dbClose(){
 		this.db.close();
 	}

}
