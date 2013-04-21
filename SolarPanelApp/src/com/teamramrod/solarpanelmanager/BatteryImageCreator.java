package com.teamramrod.solarpanelmanager;

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
		Resources res = context.getResources();
		// ?context = null;

		BitmapFactory bf = new BitmapFactory();
		BitmapFactory.Options bfo = new BitmapFactory.Options();
		bfo.inDensity = DisplayMetrics.DENSITY_DEFAULT;
		bfo.inScaled = false;
		bfo.inTargetDensity = DisplayMetrics.DENSITY_DEFAULT;
		bfo.inSampleSize = inSampleSize;

		battery_top = BitmapFactory.decodeResource(res, R.drawable.empty_battery_top, bfo);
		battery_body = BitmapFactory.decodeResource(res, R.drawable.empty_battery_body, bfo);
		battery_bottom = BitmapFactory.decodeResource(res, R.drawable.empty_battery_bottom, bfo);
		// ?res = null;

		width = battery_top.getWidth();
		top_h = battery_top.getHeight();
		body_h = battery_body.getHeight();
		bottom_h = battery_bottom.getHeight();

		canvas = new Canvas();

		battery = Bitmap.createBitmap(width, top_h + body_h + bottom_h, Bitmap.Config.ARGB_8888);
		battery.setDensity(DisplayMetrics.DENSITY_DEFAULT);
		canvas.setBitmap(battery);

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

		setLevel(40); // TODO: Does it make sense to show an empty battery at
						// first, in case it take a moment to get level?
	}

	public void setLevel(int level) {
		if (level < 0) {
			level = 0; // I suspect we might get called with -1 in certain
						// circumstances
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

	public void recycle() {
		battery_top.recycle();
		battery_body.recycle();
		battery_bottom.recycle();
		battery.recycle();
	}
}
