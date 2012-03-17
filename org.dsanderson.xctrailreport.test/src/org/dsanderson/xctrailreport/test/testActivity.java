package org.dsanderson.xctrailreport.test;

import java.util.Random;

import org.dsanderson.xctrailreport.test.R;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;

public class testActivity extends ListActivity {
	private TestDataBase database;
	String sortString = null;
	String filterString = null;

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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.clear:
			database.clear();
			break;
		case R.id.sortByName:
			sortString = TestDataBase.COLUMN_NAME + " ASC, " + TestDataBase.COLUMN_VALUE + " ASC";
			break;
		case R.id.sortByValue:
			sortString = TestDataBase.COLUMN_VALUE + " ASC";
			break;
		case R.id.sortByAdded:
			sortString = TestDataBase.COLUMN_ID + " ASC";
			break;
		case R.id.filterDisabled:
			filterString = null;
			break;
		case R.id.filterEnabled:
			filterString = TestDataBase.COLUMN_VALUE + "<50";
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		fillData();
		return true;
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.add:
			String[] names = new String[] { "Cool", "Very nice", "Hate it" };
			int nextInt = new Random().nextInt(3);
			// Save the new comment to the database
			database.insert(new TestDatabaseObject(names[nextInt], new Random()
					.nextInt(100)));
			break;
		case R.id.delete:
			if (getListAdapter().getCount() > 0) {
				Cursor cursor = database.getCursor();
				if (cursor.moveToFirst())
					database.remove(cursor);
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

		database.setSortOrder(sortString);
		database.setFilter(filterString);
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