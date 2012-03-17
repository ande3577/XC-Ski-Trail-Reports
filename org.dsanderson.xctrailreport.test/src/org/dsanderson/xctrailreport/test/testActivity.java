package org.dsanderson.xctrailreport.test;

import java.util.Random;

import org.dsanderson.android.util.DatabaseObject;
import org.dsanderson.xctrailreport.test.R;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;

public class testActivity extends ListActivity {
	private TestDataBase database;

	private static final String[] names = new String[] { "Cool", "Very nice",
			"Hate it" };

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
		registerForContextMenu(getListView());
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
			database.clearSortOrder();
			database.addSortOrder(TestDatabaseFactory.COLUMN_NAME, true);
			database.addSortOrder(TestDatabaseFactory.COLUMN_VALUE, true);
			break;
		case R.id.sortByValue:
			database.clearSortOrder();
			database.addSortOrder(TestDatabaseFactory.COLUMN_VALUE, true);
			database.addSortOrder(TestDatabaseFactory.COLUMN_NAME, true);
			break;
		case R.id.sortByAdded:
			database.clearSortOrder();
			database.addSortOrder(TestDataBase.COLUMN_ID, true);
			break;
		case R.id.filterDisabled:
			database.clearFilter();
			break;
		case R.id.filterEnabled:
			database.clearFilter();
			database.addFilter(TestDatabaseFactory.COLUMN_VALUE + "<50");
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		fillData();
		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.deleteItem: {
			Long id = getObjectFromMenuItem(item);
			if (id != null) {
				database.remove(id);
			}
			break;
		}
		case R.id.change:
			Long id = getObjectFromMenuItem(item);
			if (id != null) {
				TestDatabaseObject object = (TestDatabaseObject) database
						.getObject(id);
				object.setName(names[new Random().nextInt(3)]);
				database.update(object);
			}
			break;
		default:
			return super.onContextItemSelected(item);
		}
		fillData();
		return true;
	}

	private Long getObjectFromMenuItem(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		if (info == null)
			return null;
		else
			return info.id;
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.add:
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
		String[] from = new String[] { TestDatabaseFactory.COLUMN_NAME,
				TestDatabaseFactory.COLUMN_VALUE };
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