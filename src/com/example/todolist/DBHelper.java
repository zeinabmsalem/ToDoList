package com.example.todolist;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	
	public static final String DATABASE_NAME = "SQLiteListView.db";
	private static final int DATABASE_VERSION = 1;
	//define table name
	public static final String List_TABLE_NAME = "listtable";
	//define column names
	public static final String List_COLUMN_ID = "_id";
	public static final String List_COLUMN_NAME = "name";
	
	public DBHelper(Context context) {
		super(context,DATABASE_NAME , null, DATABASE_VERSION);
		
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
//		String sql = "CREATE TABLE "+ List_TABLE_NAME + "(" +List_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
//                + List_COLUMN_NAME + " TEXT )";
		
		String sql = "CREATE TABLE "+ List_TABLE_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT );";

		db.execSQL(sql);	
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + List_TABLE_NAME);
	    onCreate(db);
		
	}
	
	public void addListItems(String item){
		
		SQLiteDatabase db =this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(List_COLUMN_NAME, item);
        
        long result = db.insert(List_TABLE_NAME, null, values);
        // -1 means no data is saved (error)
        
        db.close(); 
        
	}
	
	
	public Cursor getListItem() {

		SQLiteDatabase db = this.getReadableDatabase();
        
        String[] projection = {List_COLUMN_ID, List_COLUMN_NAME};
        
        Cursor cursor = db.query(List_TABLE_NAME, projection, null, null, null, null, null, null);      
        
        return cursor;
    }
	
	public Integer updatedatabase(String editdata, Integer rowId) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		List<Integer> database_ids = new ArrayList<Integer>();
		 String[] projection = {List_COLUMN_ID};
		
		 Cursor cursor = db.query(List_TABLE_NAME, projection, null, null, null, null, null, null);      
	        while(cursor.moveToNext()){
	            database_ids.add(Integer.parseInt(cursor.getString(0)));
	            }
	        
	     ContentValues values = new ContentValues();
		 values.put(List_COLUMN_NAME, editdata);
		
		String selection = List_COLUMN_ID+ " LIKE ?";
		String[] selectionArgs = {String.valueOf(database_ids.get(rowId))};
		
		int count = db.update(List_TABLE_NAME, values, selection, selectionArgs);
		
		db.close();

		return count;
		
	}
	
	public Integer deleteItem (Integer id) {
	    SQLiteDatabase db = this.getWritableDatabase();
	    List<Integer> database_ids = new ArrayList<Integer>();
	    
        String[] projection = {List_COLUMN_ID};
        
        Cursor cursor = db.query(List_TABLE_NAME, projection, null, null, null, null, null, null);      
        while(cursor.moveToNext()){
            database_ids.add(Integer.parseInt(cursor.getString(0)));
            }
	    
		int result = db.delete(List_TABLE_NAME, List_COLUMN_ID + " = ? ", new String[] {String.valueOf(database_ids.get(id))});
		
		db.close();
		return result;
	    
	}


}
