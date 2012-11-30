package org.dsanderson.android.util;

import android.view.View;
import android.widget.TextView;

public class HiddenTextField extends TextItem {

	public HiddenTextField(TextView view) {
		super(view);
	}

	@Override
	public void draw() {
		textView.setVisibility(View.GONE);
		textView.setText(textString);
	}

}
