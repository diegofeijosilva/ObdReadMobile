package com.obdread.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.obdread.ed.Usuario;
import com.obdread.ed.Veiculo;

import java.text.SimpleDateFormat;

public class VeiculoDao extends DaoGenerico {

	private static String TABELA = "VEICULO";
	private SQLiteDatabase db;


	public VeiculoDao(Context context) {
		super(context);
	}
	
	// Abre o banco
	private void dbOpen(){ this.db = this.getWritableDatabase(); }
					
	// Fecha o banco
	private void dbClose(){ this.db.close(); }

	/**
	 * VERIFICA SE EXISTE UM USUÁRIO NO BANCO LOCAL
	 * NA INSTALAÇÃO DO APLICATIVO VAI EXIGIR O LOGIN ATRAVÉS DO
	 * SERVIÇO REST DO SISTEMA WEB
	 * @return
     */
	public Veiculo buscaVeiculoUsuario (){
		dbOpen();

		Cursor c = db.query(TABELA, null, null,null,null, null,null);

		if (c.getCount() > 0){

			Veiculo vei = new Veiculo();

			c.moveToFirst();
			vei.setId(c.getLong(0));
			vei.setNome(c.getString(1));

			c.close();
			dbClose();

			return vei;
		}

		c.close();
		dbClose();
		return null;
	}

	//	// Insere novo usuario
	public Integer inserir(Veiculo veiculo) {

			dbOpen();

			ContentValues values = new ContentValues();

			values.put("id", veiculo.getId());
			values.put("nome", veiculo.getNome());

			int i = (int) db.insert(TABELA, "", values);

			dbClose();

		return i;
	}

	// deleta o veiculo selecionado
	public void deletarTodos(){
		dbOpen();
		db.execSQL("delete from " + TABELA);
		dbClose();

		Log.i("VEICULO_DAO","TODOS OS REGISTROS FORAM DELETADOS");
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
