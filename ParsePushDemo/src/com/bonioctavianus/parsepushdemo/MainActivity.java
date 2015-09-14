package com.bonioctavianus.parsepushdemo;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.bonioctavianus.parsepushdemo.database.DBAdapter;

public class MainActivity extends AppCompatActivity {
	private ListView list;
	private DBAdapter db;
	public android.support.v7.widget.Toolbar mToolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(mToolbar);
		
		initView();
		setNotif();
	}
	
	private void initView() {
		this.db = new DBAdapter(this);
		this.list = (ListView) findViewById(R.id.list);
	}
	
	// menampilkan notifikasi dengan simple cursor adapter
	private void setNotif() {
		Cursor cNotif = db.getAllNotif();
		if (cNotif.getCount() > 0) {
			
			String[] data = new String[] { DBAdapter.KEY_ROWID, DBAdapter.KEY_DATE, DBAdapter.KEY_TITLE, DBAdapter.KEY_MESSAGE };
			int[] view = new int[] { R.id._id, R.id.date, R.id.title, R.id.message };
			SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.row_list, cNotif, 
					data, view, SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
			this.list.setAdapter(adapter);
		}
	}
	
	@Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setNotif();
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}