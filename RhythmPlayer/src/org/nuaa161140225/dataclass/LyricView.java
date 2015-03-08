package org.nuaa161140225.dataclass;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.View.OnLongClickListener;

/**
 * 定义一个歌词显示空间
 * @author 吴香礼
 * @see LyricView
 */
public class LyricView  extends View{
	
	/**
	 *  歌词对象
	 *  @see {@link LyricView#lrc}
	 */
	private SongLyric lrc = null;
	/**
	 *  当前播放时间
	 *  @see {@link LyricView#time}
	 */
	private long time = 0l;
	/**
	 *  字体画笔
	 *  @see {@link LyricView#fontPaint}
	 */
	private Paint fontPaint = null;
	/**
	 * 当前歌词字体画笔
	 * @see {@link LyricView#lrcPaint}
	 */
	private Paint lrcPaint = null;
	/**
	 * 字体颜色
	 * @see {@link LyricView#fontColor}
	 */
	private int fontColor = Color.WHITE;
	/**
	 * 当前歌词字体颜色
	 * @see {@link LyricView#lrcColor}
	 */
	private int lrcColor = Color.RED;
	/**
	 * 字体大小
	 * @see {@link LyricView#fontSize}
	 */
	private int fontSize = 20;
	/**
	 * 歌词开始的X坐标
	 * @see {@link LyricView#mX}
	 */
	private float mX = 0;
	/**
	 * 正在播放歌词的Y坐标
	 * @see {@link LyricView#mMiddleY}
	 */
	private float mMiddleY = 0;
	/**
	 * 歌词显示的Y坐标
	 * @see {@link LyricView#mY}
	 */
	private float mY = 0;
	/**
	 * 歌词Y方向的间距
	 * @see {@link LyricView#DY}
	 */
	private static final int DY = 50;
	/**
	 * 歌词的索引
	 * @see {@link LyricView#mIndex}
	 */
	private int mIndex = 0;
	/**
	 * 歌词链表
	 * @see {@link LyricView#mWordsList}
	 */
	private List<String> mWordsList = new ArrayList<String>();
	
	/**
	 * 默认构造函数
	 * @author 吴香礼
	 * @param context 上下文
	 * @see {@link LyricView#LyricView(Context)}
	 */
	public LyricView(Context context) {
		super(context);
	}
	
	/**
	 * 控件重设函数
	 * @author 吴香礼
	 * @see {@link LyricView#reset()}
	 */
	public void reset()
	{
		if(mWordsList.isEmpty())
		{}
		else
			{
				mWordsList.clear();
				Long[] ts = lrc.getAllTimes();
				if(ts.length>0)
					time = ts[0];
				mIndex = 0;
				// 循环绘制每一行歌词，当前播放歌词特殊绘制
				Log.i("Time", ""+time);
				for (Long l : ts) {
					mWordsList.add(lrc.get(l));	
				}
			}
	}
	/**
	 * 重绘视图
	 * @author 吴香礼
	 * @param canvas 画布
	 * @see {@link LyricView#onDraw(Canvas)}
	 */
	@Override
	protected void onDraw(Canvas canvas) 
	{
		super.onDraw(canvas);
		if(lrc != null)
		{
			try
			{
				if(fontPaint == null)
					fontPaint = new Paint();
				if(lrcPaint == null)
					lrcPaint = new Paint();
				lrcPaint.setAntiAlias(true);
				lrcPaint.setTextSize(fontSize);
				lrcPaint.setColor(fontColor);
				lrcPaint.setTypeface(Typeface.SANS_SERIF);
				lrcPaint.setTextAlign(Paint.Align.CENTER);
				fontPaint.setAntiAlias(true);
				fontPaint.setTextSize(fontSize+4);
				fontPaint.setColor(lrcColor);
				fontPaint.setTypeface(Typeface.SANS_SERIF);
				fontPaint.setTextAlign(Paint.Align.CENTER);
				Long[] ts = lrc.getAllTimes();
				for (Long l : ts) {
					mWordsList.add(lrc.get(l));
				}
				if(mWordsList.isEmpty())
				{
					canvas.drawText("该歌曲歌词不存在！", mX, mMiddleY, lrcPaint);
					canvas.drawText("长按搜索本地歌词。", mX, mMiddleY+2*DY, lrcPaint);
				}
				else
				{
					canvas.drawText(mWordsList.get(mIndex), mX, mMiddleY, fontPaint);
					int alphaValue = 25;
					float tempY = mMiddleY;
					for (int i = mIndex - 1; i >= 0; i--) 
					{
						//画出唱过部分，即上部分
						tempY -= DY;
						if (tempY < 0)
						{
							break;
							}
						lrcPaint.setColor(Color.argb(255 - alphaValue, 245, 245, 245));
						canvas.drawText( mWordsList.get(i), mX, tempY, lrcPaint);
						alphaValue += 25;
						}
					alphaValue = 25;
					tempY = mMiddleY;
					for (int i = mIndex + 1, len = mWordsList.size(); i < len; i++) 
					{
						//绘制下半部分歌词，从当前播放的歌词开始往下绘制
						tempY += DY;
						if (tempY > mY||mIndex == ts.length-1) 
						{
							//当绘制到底下时,或已绘制到最后一句。
							break;
							}
						lrcPaint.setColor(Color.argb(255 - alphaValue, 245, 245, 245));
						canvas.drawText(mWordsList.get(i), mX, tempY, lrcPaint);
						alphaValue += 25;
						}
					mIndex = lrc.getIndex(time);				
					}
				}catch(Exception e)
				{
				
				}
		}
		
	}
	/**
	 * 字体大小改变
	 * @author 吴香礼
	 * @param w
	 * @param h
	 * @param ow
	 * @param oh
	 * @see {@link LyricView#onSizeChanged(int, int, int, int)}
	 */
	@Override
	protected void onSizeChanged(int w, int h, int ow, int oh)
	{
		super.onSizeChanged(w, h, ow, oh);
		mX = w * 0.5f;
		mY = h;
		mMiddleY = h * 0.3f;
		}
	

	/**
	 * 设置歌词对象
	 * @author 吴香礼
	 * @param lrc 新的歌词对象
	 * @see {@link LyricView#setLyric(SongLyric)}
	 */
	public void setLyric(SongLyric lrc) {
		this.lrc = lrc;
	}

	/**
	 * 设置当前时间
	 * @author 吴香礼
	 * @param ms 时间
	 * @see {@link LyricView#setTime(long)}
	 */
	public void setTime(long ms) {
		this.time = ms;
	}
	/**
	 * 设置歌词字体颜色
	 * @author 吴香礼
	 * @param color 新的颜色
	 * @see {@link LyricView#setFontColor(int)}
	 */
	public void setFontColor(int color) {
		this.fontColor = color;
	}
	/**
	 * 设置当前歌词字体颜色
	 * @author 吴香礼
	 * @param color 新的颜色
	 * @see {@link LyricView#setLyricColor(int)}
	 */
	public void setLyricColor(int color) {
		this.lrcColor = color;
	}
	/**
	 * 设置字体大小
	 * @author 吴香礼
	 * @param size 新的字体大小
	 * @see {@link LyricView#setFontSize(int)}
	 */
	public void setFontSize(int size) {
		this.fontSize = size;
	}

}
