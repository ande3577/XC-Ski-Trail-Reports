package org.dsanderson.xctrailreport.test;

import java.util.List;
import java.util.Random;

import org.dsanderson.xctrailreport.test.R;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

public class testActivity extends ListActivity {
	private TestDataBase database;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LinearLayout layout = (LinearLayout) View.inflate(this, R.layout.main,
				null);
		setContentView(layout);

		database = new TestDataBase(this);
		database.open();

		List<TestDatabaseObject> values = database.getAllObjects();
		ArrayAdapter<TestDatabaseObject> adapter = new ArrayAdapter<TestDatabaseObject>(
				this, android.R.layout.simple_list_item_1, values);
		setListAdapter(adapter);
	}
	
	public void onClick(View view) {
		@SuppressWarnings("unchecked")
		ArrayAdapter<TestDatabaseObject> adapter = (ArrayAdapter<TestDatabaseObject>) getListAdapter();
		TestDatabaseObject object = null;
		switch (view.getId()) {
		case R.id.add:
			String[] names = new String[] { "Cool", "Very nice", "Hate it" };
			int nextInt = new Random().nextInt(3);
			// Save the new comment to the database
			object = database.createTestObject(names[nextInt], new Random().nextInt(100));
			adapter.add(object);
			break;
		case R.id.delete:
			if (getListAdapter().getCount() > 0) {
				object = (TestDatabaseObject) getListAdapter().getItem(0);
				database.deleteComment(object);
				adapter.remove(object);
			}
			break;
		}
		adapter.notifyDataSetChanged();
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