package es.eucm.eadandroid.ecore.gui;

import android.graphics.Bitmap;

public class GraphicsConfiguration {

	public static Bitmap createCompatibleImage(int width, int height,
			boolean transparency) {
		Bitmap bmp = null;
		if (transparency)
			bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
		else
			bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		return bmp;
	}

}
