package com.teamramrod.solarpanelmanager;

/*
    Copyright (c) 2013 Darshan-Josiah Barber

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
*/



import com.example.solarpanelmanager.R;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.DisplayMetrics;

/**
 * This class is used to create an image of the battery displaying the current charge by rendering the image onto
 * a canvas and displaying it onto the screen.
 */


class BatteryImageCreator {
	private int width, top_h, body_h, bottom_h;
	private Canvas canvas;
	private Paint fill_paint, bitmap_paint;
	private Bitmap battery_top, battery_body, battery_bottom, battery;

	public static final int SIZE_LARGE = 1;
	public static final int SIZE_NOTIFICATION = 4;

	public BatteryImageCreator(Context context) {
		this(context, SIZE_LARGE);
	}

	public BatteryImageCreator(Context context, int inSampleSize) {
		//Initialize the battery body and then call setlevel() which actually draws the battery on to the canvas.
		Resources res = context.getResources();
  
		BitmapFactory bf = new BitmapFactory();
		BitmapFactory.Options bfo = new BitmapFactory.Options();
		
		// Change attributes of the bitmap
		bfo.inDensity = DisplayMetrics.DENSITY_DEFAULT; 
		bfo.inScaled = false;
		bfo.inTargetDensity = DisplayMetrics.DENSITY_DEFAULT; 
		bfo.inSampleSize = inSampleSize;

		//Initialize the bitmap to the empty pictures of top, body and bottom.s
		battery_top = BitmapFactory.decodeResource(res, R.drawable.empty_battery_top, bfo);
		battery_body = BitmapFactory.decodeResource(res, R.drawable.empty_battery_body, bfo);
		battery_bottom = BitmapFactory.decodeResource(res, R.drawable.empty_battery_bottom, bfo);
		

		width = battery_top.getWidth();
		top_h = battery_top.getHeight();
		body_h = battery_body.getHeight();
		bottom_h = battery_bottom.getHeight();

		canvas = new Canvas();

		battery = Bitmap.createBitmap(width, top_h + body_h + bottom_h, Bitmap.Config.ARGB_8888);
		battery.setDensity(DisplayMetrics.DENSITY_DEFAULT);
		canvas.setBitmap(battery);

		// The Paint class holds the style and color information about how to draw the bitmap.

		fill_paint = new Paint();
		fill_paint.setColor(0xaa33b5e5);
		fill_paint.setAntiAlias(true);
		fill_paint.setStrokeCap(Paint.Cap.ROUND);
		fill_paint.setStrokeJoin(Paint.Join.ROUND);
		fill_paint.setStyle(Paint.Style.FILL);
		fill_paint.setDither(true);

		bitmap_paint = new Paint();
		bitmap_paint.setAntiAlias(true);
		bitmap_paint.setDither(true);

		setLevel(40); 
	}

	/**
	 * Get the current battery level and draw the body of the battery after having set the different components 
	 * of the battery such as the top, the main body and the bottom.
	 */
	
	public void setLevel(int level) {
		if (level < 0) {
			level = 0; 
		}

		int rect_top = top_h + (body_h * (100 - level) / 100);
		int rect_bottom = top_h + body_h;

		RectF body_rect = new RectF(0, rect_top, width, rect_bottom);

		canvas.drawColor(Color.TRANSPARENT, android.graphics.PorterDuff.Mode.CLEAR);

		canvas.drawRoundRect(body_rect, 7.5f, 7.5f, fill_paint);

		canvas.drawBitmap(battery_top, 0, 0, bitmap_paint);
		canvas.drawBitmap(battery_body, 0, top_h, bitmap_paint);
		canvas.drawBitmap(battery_bottom, 0, top_h + body_h, bitmap_paint);
	}

	public Bitmap getBitmap() {
		return battery;
	}
   
	/**
	 * Manually recycle the bitmap as the image of the battery is constantly changing.
	 */
	
	public void recycle() {
		battery_top.recycle();
		battery_body.recycle();
		battery_bottom.recycle();
		battery.recycle();
	}
}
