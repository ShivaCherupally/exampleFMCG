package com.fmcg.util;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;

/**
 * Created by Shiva on 9/22/2017.
 */

public class TextDrawable extends Drawable
{

	private final String text;
	private final Paint paint;

	public TextDrawable(String text)
	{
		this.text = text;
		this.paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setTextSize(25);
		paint.setAntiAlias(true);
		paint.setTextAlign(Paint.Align.LEFT);
	}

	@Override
	public void draw(Canvas canvas)
	{
		canvas.drawText(text, 0, 6, paint);
	}

	@Override
	public void setAlpha(int alpha)
	{
		paint.setAlpha(alpha);
	}

	@Override
	public void setColorFilter(@Nullable final ColorFilter colorFilter)
	{
		paint.setColorFilter(colorFilter);
	}


	@Override
	public int getOpacity()
	{
		return PixelFormat.TRANSLUCENT;
	}

}
