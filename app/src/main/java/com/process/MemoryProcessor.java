/**
 * 
 */
package com.process;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;


public class MemoryProcessor {

	private static MemoryProcessor memoryProcessor;

	public static MemoryProcessor getInstance() {
		if (memoryProcessor == null) {
			memoryProcessor = new MemoryProcessor();
		}
		return memoryProcessor;
	}

	public void unbindDrawables(View view) {
		try {
			
			if (view.getBackground() != null)
				view.getBackground().setCallback(null);

			if (view instanceof ImageView) {
				ImageView imageView = (ImageView) view;
				imageView.setImageBitmap(null);
			} else if (view instanceof ViewGroup) {
				ViewGroup viewGroup = (ViewGroup) view;
				for (int i = 0; i < viewGroup.getChildCount(); i++)
					unbindDrawables(viewGroup.getChildAt(i));
				if (!(view instanceof AdapterView))
					viewGroup.removeAllViews();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		view = null;
		System.gc();

	}
}
