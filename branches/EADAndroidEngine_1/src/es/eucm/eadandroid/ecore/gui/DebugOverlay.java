package es.eucm.eadandroid.ecore.gui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Debug;

public class DebugOverlay {
	
	
	private static final int REFRESH_RATIO = 50; // in ms 
	
	private int DEBUG_SCREEN_WIDTH = 0;
	private int DEBUG_SCREEN_HEIGHT = 0;
	
	private int DEBUG_INFO_SCREEN_X = 0;
	
	private long totalElapsedTime = 0;
	
	private int MAX_HEAP_INFO ;
	private static final int MAX_HEAP_SIZE = 24000000;
	
	private float nativeFree[];
	private float nativeAlloc[];
	private float nativeSize[];
	
	private String[] memInfo = null;
	
	private int i = 0;
	
	private Paint p = new Paint();
	
	public void init() {
		
		DEBUG_SCREEN_WIDTH = GUI.FINAL_WINDOW_WIDTH;
		DEBUG_SCREEN_HEIGHT = GUI.FINAL_WINDOW_HEIGHT;		
		DEBUG_INFO_SCREEN_X = (int) (GUI.FINAL_WINDOW_WIDTH * 0.85f);
		MAX_HEAP_INFO = DEBUG_SCREEN_WIDTH * 2;
		
		nativeFree = new float[MAX_HEAP_INFO];
		nativeAlloc = new float[MAX_HEAP_INFO];
		nativeSize = new float[MAX_HEAP_INFO];
		
	}

	public void draw(Canvas canvas) {

		canvas.clipRect(0, 0, DEBUG_SCREEN_WIDTH, DEBUG_SCREEN_HEIGHT);
		canvas.drawARGB(50, 0, 0, 0);

		p.setStrokeWidth(3f);

		p.setColor(Color.RED);
		canvas.drawPoints(nativeSize, p);

		int xp = 0;
		int yp = 0;

		if (nativeSize.length > 0) {

			xp = (int) nativeSize[i - 2];
			yp = (int) nativeSize[i - 1];
		}

		p.setStrokeWidth(10f);
		canvas.drawPoint(xp, yp, p);

		p.setStrokeWidth(3f);
		p.setColor(Color.GREEN);
		canvas.drawPoints(nativeAlloc, p);

		xp = 0;
		yp = 0;

		if (nativeAlloc.length > 0) {

			xp = (int) nativeAlloc[i - 2];
			yp = (int) nativeAlloc[i - 1];
		}

		p.setStrokeWidth(10f);
		canvas.drawPoint(xp, yp, p);

		long heapSize = (MAX_HEAP_SIZE / 1048576)
				- (yp * (MAX_HEAP_SIZE / 1048576)) / DEBUG_SCREEN_HEIGHT;

		canvas.drawText(String.valueOf(heapSize) + "MB", xp + 10f, yp, p);

		canvas.clipRect(DEBUG_INFO_SCREEN_X, 0, DEBUG_SCREEN_WIDTH,
				DEBUG_SCREEN_HEIGHT);

		canvas.drawARGB(200, 0, 0, 0);
		p.setTextSize(15f);
		p.setColor(Color.WHITE);

		float y = 0;

		if (memInfo != null) {

			for (int i = 0; i < memInfo.length; i++) {
				canvas.drawText(memInfo[i], (float) DEBUG_INFO_SCREEN_X + 15,
						y, p);
				y += 30;
			}

		}

	}

	public void updateMemAlloc(long elapsedTime) {

		totalElapsedTime += elapsedTime;

		if (totalElapsedTime > REFRESH_RATIO) {

			totalElapsedTime = 0;
			
			if (i>=DEBUG_INFO_SCREEN_X) 
				i = 0;
			
			nativeFree[i] = i;
			nativeFree[i+1] = (float) DEBUG_SCREEN_HEIGHT - (Debug.getNativeHeapFreeSize() * DEBUG_SCREEN_HEIGHT) / MAX_HEAP_SIZE;
			
			nativeAlloc[i] = i;
			nativeAlloc[i+1] = (float) DEBUG_SCREEN_HEIGHT - (Debug.getNativeHeapAllocatedSize() * DEBUG_SCREEN_HEIGHT) / MAX_HEAP_SIZE;
			
			nativeSize[i] = i;
			nativeSize[i+1] = (float) DEBUG_SCREEN_HEIGHT - (Debug.getNativeHeapSize() * DEBUG_SCREEN_HEIGHT) / MAX_HEAP_SIZE;
			
			i+=2;

		}
	}

	public void setMemAllocInfo(String[] memInfo) {
		
		this.memInfo = memInfo;

	}



}
