package com.obdread.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.obdread.ed.ErrosECU;
import com.obdread.ed.Usuario;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ErrosECUDao extends DaoGenerico {

	private static String TABELA = "ERROSECU";
	private SQLiteDatabase db;

	SimpleDateFormat setDate = new SimpleDateFormat("yyyy/MM/dd");

	public ErrosECUDao(Context context) {
		super(context);
	}
	
	// Abre o banco
	private void dbOpen(){ this.db = this.getWritableDatabase(); }
					
	// Fecha o banco
	private void dbClose(){ this.db.close(); }

	//	// Insere novo usuario
	public Integer inserir(ErrosECU errosECU) {
		Log.i(TABELA, "INSERINDO NOVO REGISTRO");
		dbOpen();
		ContentValues values = new ContentValues();

		values.put("hashuser", errosECU.getHashUser());
		values.put("idveiculo", errosECU.getIdVeiculo());
		values.put("data", errosECU.getData());
		values.put("codigo", errosECU.getCodigo());
		values.put("descricao", errosECU.getDescricao());
		values.put("level", errosECU.getLevel());

		int i = (int) db.insert(TABELA, "", values);
		dbClose();
		return i;
	}

	public int deletar(int hashuser) {
			dbOpen();
			String where = "hashuser" + "=?";

			String _id = String.valueOf(hashuser);
			String[] whereArgs = new String[] { _id };

			int i = db.delete(TABELA, where, whereArgs);
			dbClose();
		return i;
	}

	public List<ErrosECU> listaErrosEcu() {

		dbOpen();
		Cursor c = db.rawQuery("SELECT * FROM "+ TABELA +" ORDER BY DATA", null);

		List<ErrosECU> listaErrosECU = new ArrayList<ErrosECU>();

		if (c.moveToFirst()) {

				// Loop at� o final
				do {
					ErrosECU errosECU = new ErrosECU();
					errosECU.setHashUser(c.getString(0));
					errosECU.setIdVeiculo(c.getLong(1));
					errosECU.setData(c.getString(2));
					errosECU.setCodigo(c.getString(3));
					errosECU.setDescricao(c.getString(4));
					errosECU.setLevel(c.getInt(5));

					listaErrosECU.add(errosECU);

				} while (c.moveToNext());
			}

			c.close();
			dbClose();

	return listaErrosECU;
	}

	public void deletarTodos(){
		dbOpen();
		db.execSQL("delete from " + TABELA);
		dbClose();
	}

//	// Retorna um usuario conforme id
//	public Usuario buscarUsuario(Integer id){
//
//			dbOpen();
//
//			Cursor c = db.query(TABELA, null, "idWebservice="+id,null,null, null,null);
//
//			if (c.getCount() > 0){
//
//				Usuario usuario = new Usuario();
//
//				c.moveToFirst();
//				usuario.setIdWebservice(c.getString(0));
//				usuario.setNome(c.getString(1));
//				usuario.setSenha(c.getString(2));
//
//				usuario.setDtLogin(c.getString(c.getColumnIndex("DTLOGIN")));
//				usuario.setHrLogin(c.getString(c.getColumnIndex("HRLOGIN")));
//
//				c.close();
//				dbClose();
//
//			return usuario;
//			}
//
//			c.close();
//			dbClose();
//			return null;
//	}
//
//
//	// Atualiza a linha
//	public int atualizar(Usuario usuario) {
//
//			dbOpen();
//
//			ContentValues values = new ContentValues();
//
//			values.put("nome", usuario.getNome());
//			values.put("senha", usuario.getSenha());
//			values.put("logado", usuario.getLogado());
//
//			values.put("DTLOGIN", usuario.getDtLogin());
//			values.put("HRLOGIN", usuario.getHrLogin());
//
//			String idWebservice = String.valueOf(usuario.getIdWebservice());
//
//			String where = "idWebservice" + "=?";
//			String[] whereArgs = new String[] { idWebservice };
//
//			int i = db.update(TABELA, values, where, whereArgs);
//
//			dbClose();
//
//			return i;
//	}
//
//
//	// Insere novo usuario
//	public Integer inserir(Usuario usuario) {
//
//			dbOpen();
//
//			ContentValues values = new ContentValues();
//
//			values.put("idWebservice", usuario.getIdWebservice());
//			values.put("nome", usuario.getNome());
//			values.put("senha", usuario.getSenha());
//			values.put("logado", usuario.getLogado());
//
//			values.put("DTLOGIN", usuario.getDtLogin());
//			values.put("HRLOGIN", usuario.getHrLogin());
//
//			int i = (int) db.insert(TABELA, "", values);
//
//			dbClose();
//
//		return i;
//	}
//
//	// Deleta o usuario
//	public int deletar(int id) {
//
//			dbOpen();
//
//			String where = "idWebservice" + "=?";
//
//			String _id = String.valueOf(id);
//			String[] whereArgs = new String[] { _id };
//
//			int i = db.delete(TABELA, where, whereArgs);
//
//			dbClose();
//
//		return i;
//	}
//
//	// Deleta todo os usuarios cadastrados localmente
//	public void deletarTodos(){
//
//			dbOpen();
//
//			db.execSQL("delete from " + TABELA);
//
//			dbClose();
//
//	}
//
//	// Retorna quantidade de usuarios cadastrados
//	public Integer quantUsuarios(){
//
//			dbOpen();
//
//			Cursor c = db.rawQuery("SELECT * FROM " + TABELA, null);
//
//			int i = c.getCount();
//
//			c.close();
//			dbClose();
//
//			return i;
//	}
//
//
//	// Retorna uma lista com todos os usuarios
//	public List<Usuario> listarUsuarios() {
//
//			dbOpen();
//
//			Cursor c = db.rawQuery("SELECT * FROM "+ TABELA +" ORDER BY nome", null);
//
//			//return c;
//
//			List<Usuario> usuarios = new ArrayList<Usuario>();
//
//			if (c.moveToFirst()) {
//
//				// Loop at� o final
//				do {
//					Usuario usuario = new Usuario();
//
//					usuarios.add(usuario);
//
//					usuario.setIdWebservice(c.getString(0));
//					usuario.setNome(c.getString(1));
//					usuario.setSenha(c.getString(2));
//
//
//					} while (c.moveToNext());
//			}
//
//			c.close();
//			dbClose();
//
//	return usuarios;
//
//
//	}
//
//	// Retorna usu�rio logado
//	public Usuario logado(){
//
//		dbOpen();
//
//		Cursor c = db.query(TABELA, null, "logado = 1",null,null, null,null);
//
//		if (c.getCount() > 0){
//
//			Usuario usuario = new Usuario();
//
//			c.moveToFirst();
//			usuario.setIdWebservice(c.getString(0));
//			usuario.setNome(c.getString(1));
//			usuario.setSenha(c.getString(2));
//			usuario.setLogado(c.getInt(3));
//
//			usuario.setDtLogin(c.getString(c.getColumnIndex("DTLOGIN")));
//			usuario.setHrLogin(c.getString(c.getColumnIndex("HRLOGIN")));
//
//			c.close();
//			dbClose();
//
//		return usuario;
//		}
//
//		c.close();
//		dbClose();
//		return null;
//
//	}
//
//	// Retorna se existe o usuario
//	public Usuario login(Usuario usuario) {
//
//		 // Transforma a senha em md5 para validar no banco local
//		//usuario.setSenha(ClassUtil.md5(usuario.getSenha()));
//
//		dbOpen(); // Abre o banco
//		Cursor c = db.rawQuery("SELECT * FROM "+TABELA+" where nome = '"+usuario.getNome()+"' and senha = '"+usuario.getSenha()+"' ", null);
//
//			if (c.getCount() > 0){
//
//				Usuario user = new Usuario();
//
//				c.moveToFirst();
//
//				user.setIdWebservice(c.getString(0));
//				user.setNome(c.getString(1));
//				user.setSenha(c.getString(2));
//				user.setLogado(c.getInt(3));
//
//				usuario.setDtLogin(c.getString(4));
//				usuario.setHrLogin(c.getString(5));
//
//				c.close();
//				dbClose();
//
//				return user;
//			}
//
//		c.close();
//		dbClose();
//
//	 return null;
//	}
//

}
