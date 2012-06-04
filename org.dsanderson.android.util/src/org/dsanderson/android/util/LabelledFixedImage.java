package org.dsanderson.android.util;

import org.dsanderson.util.IImageItem;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class LabelledFixedImage implements IImageItem {
	final ImageView imageView;
	final TextView labelView;

	public LabelledFixedImage(ImageView imageView, TextView labelView) {
		this.imageView = imageView;
		this.labelView = labelView;
	}

	public void setImage(String imageName) {
		int visibility = View.VISIBLE;
		if (imageName == null || imageName.length() == 0) {
			visibility = View.GONE;
		}
		imageView.setVisibility(visibility);
		labelView.setVisibility(visibility);
	}

	public void draw() {
	}

}
