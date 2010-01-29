package es.eucm.eadventure.prototypes.gui.hud;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;

public class Magnifier {
	
	
	/*
	 * Tama�o de dedo
	 */
	
	/**
	 * MAGNIFIER SATIC DESC
	 */

	private Bitmap magBmp;
	private int bmpEdgeSize;
	private Canvas canvasMag;
	private int radius ;
	private int magFrameWidth;
	
	/**
	 * MAGNIFIER DINAMIC DESC
	 */
	
	private int centerX;
	private int centerY;
	
	//Bounds
	private int left;
	private int top;
	private int right;
	
	/**
	 * FINGER REGION
	 */
	//Bounds
	private int frleft;
	private int frtop;
	private int frright;
	private int frradius;
	
	private boolean shown;
	
	/**
	 * MAGNIFIED BITMAP
	 */
	private Bitmap bmpsrc;
	
	
	public Magnifier(int radius , int magFrameWidth , Bitmap bmp) {
		
		this.magFrameWidth = magFrameWidth;
		this.radius = radius - magFrameWidth;
		this.frradius = radius - magFrameWidth;
		this.bmpEdgeSize = radius*2;
		this.shown = false;
		this.bmpsrc = bmp;
		centerX=0;
		centerY=0;
		left=0;
		top=0;
		right=0;
		createMagnifier();
		
	}
	
	private void createMagnifier() {

		magBmp = Bitmap.createBitmap(bmpEdgeSize, bmpEdgeSize, Bitmap.Config.ARGB_4444);
		canvasMag = new Canvas(magBmp);
		Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

		p.setColor(0xFF000000);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(magFrameWidth);
		canvasMag.drawCircle(bmpEdgeSize/2,bmpEdgeSize/2, this.radius, p);
				
	}
	
	public void updateMagPos(int xfocus,int yfocus) {
			
		calculateCenter(xfocus,yfocus);
		calculateFingerSlope(xfocus,yfocus);
		
	}
	
	private void updateMagPicture() {
		
		Bitmap bmpmagaux = Bitmap.createBitmap(bmpsrc, frleft, frtop, frradius*2, frradius*2);
		Bitmap bmpmag = Bitmap.createScaledBitmap(bmpmagaux, bmpEdgeSize, bmpEdgeSize,false);
	
		Path mPath = new Path();
        canvasMag.save();
        mPath.reset();
        mPath.addCircle(bmpEdgeSize/2, bmpEdgeSize/2, radius, Path.Direction.CCW);
        canvasMag.clipPath(mPath, Region.Op.REPLACE);
        canvasMag.drawBitmap(bmpmag, 0, 0, null);
        canvasMag.restore();
		
	}
	
	private void calculateFingerSlope(int xfocus, int yfocus) {
		
		frtop = yfocus - frradius;
		frleft = xfocus - frradius;
		frright = xfocus + frradius;
	}

	private void calculateCenter(int xfocus, int yfocus) {
		
		this.centerX = xfocus;		
		this.centerY = yfocus-radius;
		top = centerY - bmpEdgeSize/2;
		left = centerX - bmpEdgeSize/2;
		right = centerX + bmpEdgeSize/2;
			
	}

	public void doDraw(Canvas c ) {
		
		if (shown) {
			updateMagPicture();
			c.drawBitmap(magBmp, left, top, null);
		}
	}

	public void toggle() {
		
		shown = !shown ;
		
	}

	public void show() {
		
		shown = true;
		
	}

	public void hide() {
		shown = false;
	}

}
