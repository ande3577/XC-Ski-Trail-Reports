package org.dsanderson.android.util;

import org.dsanderson.util.IImageItem;

import android.view.View;
import android.widget.ImageView;

public class FixedImageItem implements IImageItem {
	final ImageView imageView;
	int hiddenMode = View.INVISIBLE;

	public FixedImageItem(ImageView imageView) {
		this.imageView = imageView;
	}
	
	public void setGoneWhenHidden(Boolean gone) {
		hiddenMode = gone ? View.GONE : View.INVISIBLE;
	}

	public void setImage(String imageName) {
		if ((imageName == null) || (imageName.length() == 0)) {
			imageView.setVisibility(hiddenMode);
		} else {
			imageView.setVisibility(View.VISIBLE);		}
	}

	public void draw() {
	}

}
