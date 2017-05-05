package com.obdread.dao;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DaoGenerico extends SQLiteOpenHelper {
	
	private static int VERSION = 1;
	private static String DB_NAME = "obd_read.db";
	
	private SQLiteDatabase db;

	public DaoGenerico(Context context) {
		super(context, DB_NAME, null, VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		Log.i("BASE", "ONCREATE GENERIC DAO");
	}

	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		Log.d("UPDATE BASE", String.format("Updating database from version %d to versoin %d", oldVersion, newVersion));
		
		  if (newVersion > oldVersion) {
		    db.beginTransaction();
		 
		    boolean success = true;
		    for (int i = oldVersion ; i < newVersion ; ++i) {
		      int nextVersion = i + 1;
		      
		      switch (nextVersion) {
		        case 5:
			        success = upgradeToVersion5(db);
			        break;
		        
		        case 6:
		        	success = upgradeToVersion6(db);
			        break;	
		        
		      }
		 
		    if (!success) {
		      Log.d("UPDATE BASE", "Error updating database, reverting changes!");
		      break;
		    }
		  }
		 
		  if (success) {
		    Log.d("UPDATE BASE", "Database updated successfully!");
		    db.setTransactionSuccessful();
		  }
		   db.endTransaction();
		  }
		
	}
	
	private boolean upgradeToVersion5(SQLiteDatabase db){
		  boolean result = true;
		  try{
			  db.execSQL("ALTER TABLE ordemServico ADD COLUMN flagMobile INTEGER DEFAULT 0");
			  db.setVersion(5);

		  }catch (SQLException e) {
		    result = false;
		  }
		 
		return result;
	}
	
	private boolean upgradeToVersion6(SQLiteDatabase db){
		  boolean result = true;
		  try{
			  db.execSQL("ALTER TABLE ordemServico ADD COLUMN NUMCASA VARCHAR(20)");
			  db.execSQL("ALTER TABLE ordemServico ADD COLUMN COMPLEMENTO VARCHAR(100)");
			  db.execSQL("ALTER TABLE ordemServico ADD COLUMN CELULAR VARCHAR(10)");
			  db.execSQL("ALTER TABLE ordemServico ADD COLUMN OUTROFONE VARCHAR(10)");
			  
			  db.execSQL("ALTER TABLE usuario ADD COLUMN DTLOGIN VARCHAR(10)");
			  db.execSQL("ALTER TABLE usuario ADD COLUMN HRLOGIN VARCHAR(6)");

			  db.setVersion(6);

		  }catch (SQLException e) {
		    result = false;
		  }
		 
		return result;
	}
	
}
