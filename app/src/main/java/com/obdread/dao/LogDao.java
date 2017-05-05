package com.obdread.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.obdread.ed.LogTriade;

import java.util.ArrayList;
import java.util.List;

public class LogDao extends DaoGenerico {
	
	private static String TABELA = "log";
	private SQLiteDatabase db;

	public LogDao(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	// Abre o banco
	private void dbOpen(){ this.db = this.getWritableDatabase(); }
					
	// Fecha o banco
	private void dbClose(){ this.db.close(); }
		
	
	// Insere novo motivo
	public int inserir(LogTriade log) {
			
		dbOpen();
			
		ContentValues values = new ContentValues();
			
		values.put("usuario", log.getUsuario());
		values.put("dispositivo", log.getDispositivo());
		values.put("operacao", log.getOperacao());
			
		int i = (int) db.insert(TABELA, "", values);

		dbClose();
			
		return i;
	}

	// Deleta o motivo
	public int deletar(int id) {
			
		dbOpen();
			
		String where = "id" + "=?";

		String _id = String.valueOf(id);
		String[] whereArgs = new String[] { _id };
			
		int i = db.delete(TABELA, where, whereArgs);
			
		dbClose();
			
		return i;
	}
		
				
	// Retorna uma lista com todos os carros
	public List<LogTriade> listaLogs() {
			
		dbOpen();
					
		Cursor c = db.rawQuery("SELECT * FROM "+ TABELA +" ORDER BY id", null);

		List<LogTriade> listaLogs = new ArrayList<LogTriade>();

		if (c.moveToFirst()) {

			// Loop at� o final
			do {
				LogTriade log = new LogTriade();
							
				listaLogs.add(log);
					
				log.setId(c.getInt(0));
				log.setUsuario(c.getString(1));
				log.setDispositivo(c.getString(2));
				log.setOperacao(c.getString(3));

					
			} while (c.moveToNext());
		}

		c.close();
		dbClose();
			
	return listaLogs;

			
	}
		
		
}
