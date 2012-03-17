package org.dsanderson.xctrailreport.test;

import java.util.List;
import java.util.Random;

import org.dsanderson.xctrailreport.test.R;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;

public class testActivity extends ListActivity {
	private TestDataBase database;

	// private Cursor cursor;
	private SimpleCursorAdapter adapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LinearLayout layout = (LinearLayout) View.inflate(this, R.layout.main,
				null);
		setContentView(layout);

		database = new TestDataBase(this);
		database.open();

		database.getAllObjects();

		fillData();
	}

	public void onClick(View view) {
		TestDatabaseObject object = null;
		switch (view.getId()) {
		case R.id.add:
			String[] names = new String[] { "Cool", "Very nice", "Hate it" };
			int nextInt = new Random().nextInt(3);
			// Save the new comment to the database
			object = database.createTestObject(names[nextInt],
					new Random().nextInt(100));
			break;
		case R.id.delete:
			if (getListAdapter().getCount() > 0) {
				Cursor cursor = database.getCursor();
				if (cursor.moveToFirst()) {
					object = database.cursorToTestObject(cursor);
					database.deleteComment(object);
				}
			}
			break;
		}
		fillData();
	}

	private void fillData() {

		// Fields from the database (projection)
		// Must include the _id column for the adapter to work
		String[] from = new String[] { TestDataBase.COLUMN_NAME,
				TestDataBase.COLUMN_VALUE };
		// Fields on the UI to which we map
		int[] to = new int[] { R.id.name, R.id.value };

		Cursor cursor = database.getCursor();
		adapter = new SimpleCursorAdapter(this, R.layout.row, cursor, from, to);

		setListAdapter(adapter);
	}

	@Override
	protected void onResume() {
		database.open();
		super.onResume();
	}

	@Override
	protected void onPause() {
		database.close();
		super.onPause();
	}

}