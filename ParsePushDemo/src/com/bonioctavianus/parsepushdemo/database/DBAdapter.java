package com.bonioctavianus.parsepushdemo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// Statement untuk membuat class DBAdapter.
public class DBAdapter {

	// Membuat kolom tabel. Untuk kolom pertama selalu
	// direkomendasikan menggunakan nama "_id".
	public static final String KEY_ROWID = "_id";
	public static final String KEY_DATE = "date";
	public static final String KEY_TITLE = "title";
	public static final String KEY_MESSAGE = "message";

	// Membuat array String yg berisi semua KEY
	public static final String[] ALL_KEYS = new String[] { KEY_ROWID,
			KEY_DATE, KEY_TITLE, KEY_MESSAGE };

	// Membuat informasi database seperti nama database, nama tabel dan versi
	// database.
	public static final String DATABASE_NAME = "dbNotif";
	public static final String TABLE_NAME = "notif";
	public static final int DATABASE_VERSION = 1; // nomor versi harus lebih
													// besar untuk setiap
													// perubahan pada database

	// perintah SQL untuk membuat database
	private static final String DATABASE_CREATE_SQL = "CREATE TABLE "
			+ TABLE_NAME + " (" + KEY_ROWID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_DATE
			+ " TEXT NOT NULL, " + KEY_TITLE + " TEXT NOT NULL, " + KEY_MESSAGE
			+ " TEXT NOT NULL" + ");";

	// Membuat context.
	private final Context context;

	// Membuat objek myDBHelper dari class DatabaseHelper yg meng'extends class
	// SQLiteOpenHelper
	private DatabaseHelper myDBHelper;

	// Membuat objek db dari class SQLiteDatabase
	private SQLiteDatabase db;

	// Mengatur context DatabaseHelper.
	public DBAdapter(Context ctx) {
		this.context = ctx;
		myDBHelper = new DatabaseHelper(context);
	}

	// membuka database
	public DBAdapter open() {
		db = myDBHelper.getWritableDatabase();
		return this;
	}

	// menutup database
	public void close() {
		myDBHelper.close();
	}

	// insert data ke dalam tabel
	public long insertNotif(String date, String title, String message) {
		long result = 0;
		
		open();
		ContentValues cv = new ContentValues();
		cv.put(KEY_DATE, date);
		cv.put(KEY_TITLE, title);
		cv.put(KEY_MESSAGE, message);

		result = db.insert(TABLE_NAME, null, cv);
		close();
		
		return result;
	}

	// mengambil semua data pada tabel
	public Cursor getAllNotif() {
		open();
		Cursor c = db.query(true, TABLE_NAME, ALL_KEYS, null, null, null,
				null, KEY_DATE + " DESC", null, null);
		if (c != null) {
			c.moveToFirst();
		}
		close();
		
		return c;
	}

	// membuat class DatabaseHelper yg meng'extends class SQLiteOpenHelper
	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		// method onCreate() untuk membuat database dengan parameter String
		// "DATABASE_CREATE_SQL"
		// yang telah didefinisikan sebelumnya
		@Override
		public void onCreate(SQLiteDatabase _db) {
			_db.execSQL(DATABASE_CREATE_SQL);
		}

		// method onUpgrade() untuk meng'update perubahan pada database
		// pertama menghapus database lama, kemudian membuat database baru
		// dengan
		// parameter oldVersion dan newVersion
		@Override
		public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {

			// menghapus database lama
			_db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

			// membuat database baru
			onCreate(_db);
		}
	}
}