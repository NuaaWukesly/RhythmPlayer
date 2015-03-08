package org.nuaa161140225.dataclass;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

/**
 * 歌词对象
 * @author 吴香礼
 * @see SongLyric
 */
public final class SongLyric {
		
	/**
	 * 歌名
	 * @see {@link SongLyric#title}
	 */
	private String title = "";
	/**
	 * 歌手名
	 * @see {@link SongLyric#artist}
	 */
	private String artist = "";
	/**
	 * 专辑名
	 * @see {@link SongLyric#album}
	 */
	private String album = "";
	/**
	 * 歌词的偏移时间
	 * @see {@link SongLyric#offset}
	 */
	private long offset = 0;
	/**
	 * 最大时间
	 * @see {@link SongLyric#maxTime}
	 */
	private long maxTime = 0;
	/**
	 * 歌词内容
	 * @see {@link SongLyric#lrcs}
	 */
	private Map<Long, String> lrcs = new HashMap<Long, String>();
	/**
	 * 验证是否通过
	 * @see {@link SongLyric#valid}
	 */
	private boolean valid = false;
	
	private String path = "";
	private String Code = "GBK";

	/**
	 * 构造函数，使用歌词的路径够造歌词对象
	 * @author 吴香礼
	 * @param url 歌词路径
	 * @see {@link SongLyric#SongLyric(String)}
	 */
	public SongLyric(String url) {
		File file = new File(url);
		if (file.exists()) {
			try {
				/*
				 * 构建读取器，注意文件的编码方式有gbk和utf8
				 */
				setPath(url);
				BufferedReader br = new BufferedReader(new InputStreamReader(
						new FileInputStream(file), Code));
				String line = null;
				while ((line = br.readLine()) != null) {
					dealLine(line);
					//Log.i(file.toString(), line+valid);
				}
				valid = true;
				/*
				 * 关闭读取器
				 */
				br.close();
			} catch (Exception e) {
				System.out.println("Exception");
			}
		}
		else
			Log.i(file.toString(), "不存在"+valid);
	}
	
	/**
	 * 重设歌词对象
	 * @author 吴香礼
	 * @param path 新的歌词的路径
	 * @see {@link SongLyric#setLrc(String)}
	 */
	public void setLrc(String path,String Code)
	{
		/*
		 * 将歌词对象变回初始状态
		 */
		this.lrcs.clear();
		this.valid = false;
		this.album = "";
		this.artist ="";
		this.maxTime = 0;
		this.offset = 0;
		this.title = "";
		File file = new File(path);
		if (file.exists()) {
			try {
				/*
				 * 构建读取器，注意文件的编码方式有gbk和utf8
				 */
				setPath(path);
				if(Code.isEmpty())
				{
					Code = "GBK";
				}
				BufferedReader br = new BufferedReader(new InputStreamReader(
						new FileInputStream(file), Code));
				String line = null;
				while ((line = br.readLine()) != null) {
					dealLine(line);
				}
				valid = true;
				/*
				 * 关闭读取器
				 */
				br.close();
			} catch (Exception e) {
				System.out.println("Exception");
			}
		}
		else
			{
				Log.i(file.toString(), "不存在"+valid);
				valid = false;
			}
	}
	
	/**
	 * 获取歌词标题
	 * @author 吴香礼
	 * @return 歌词标题
	 * @see {@link SongLyric#getTitle()}
	 */
	public String getTitle() 
	{
		return this.title;
	}

	/**
	 * 获取艺术家
	 * @author 吴香礼
	 * @return 艺术家
	 * @see {@link SongLyric#getArtist()}
	 */
	public String getArtist() {
		return this.artist;
	}

	/**
	 * 获取专辑名
	 * @author 吴香礼
	 * @return 专辑名
	 * @see {@link SongLyric#getAlbum()}
	 */
	public String getAlbum() {
		return this.album;
	}

	/**
	 * 获取验证状态
	 * @author 吴香礼
	 * @return 验证状态
	 * @see {@link SongLyric#isValid()}
	 */
	public boolean isValid() {
		return this.valid;
	}

	/**
	 * 获取最大时间
	 * @author 吴香礼
	 * @return 最大时间
	 * @see {@link SongLyric#getMaxTime()}
	 */
	public long getMaxTime() {
		return this.maxTime;
	}

	/**
	 * 设置最大时间
	 * @author 吴香礼
	 * @param time 时间
	 * @see {@link SongLyric#setMaxTime(long)}
	 */
	public void setMaxTime(long time) {
		this.maxTime = time;
	}

	/**
	 * 获取该时间应当显示的歌词
	 * @author 吴香礼
	 * @param ls 歌曲时间
	 * @return 对应歌词
	 * @see {@link SongLyric#get(long)}
	 */
	public String get(long ls) {
		long time = ls + offset;
		Long curr = -1l;
		for (Long l : lrcs.keySet()) {
			curr = l > time ? curr : l < curr ? curr : l;
		}
		//Log.i(lrcs.get(curr).toString(), "存在"+valid);
		return lrcs.get(curr);
	}
	
	/**
	 * 获取该时间所要显示的歌词初始时间的索引
	 * @author 吴香礼
	 * @param ls 歌曲时间
	 * @return 歌词的索引
	 * @see {@link SongLyric#getIndex(long)}
	 */
	public int getIndex(long ls) {
		Long[] ts = getAllTimes();
		for (int i = 0; i < ts.length - 1; i++) {
			if (ls + offset >= ts[i] && ls + offset < ts[i + 1]) {
				return i;
			}
		}
		if(ts.length>0)
		{
			if(ls+offset<ts[0])
			{
				//时间小于第一个歌词时间是返回0
				return 0;
			}
			else if(ls+offset>ts[ts.length-1])
			{
				//时间大于最后一句歌词时，返回最后一句歌词
				return ts.length-1;
			}
		}
		return 0;
	}
	
	/**
	 * @return the code
	 */
	public String getCode() {
		return Code;
	}

	/**
	 * 获取该时间与歌词初始时间差值
	 * @author 吴香礼
	 * @param ls 时间
	 * @return	 时间差
	 * @see {@link SongLyric#getOffset(long)}
	 */	
	public int getOffset(long ls) {
		Long[] ts = getAllTimes();
		int index = getIndex(ls);
		if (index < ts.length && index >= 0) {
			return (int) (ls + offset - ts[index]);
		}
		return 0;
	}

	/**
	 * 获取该时间段播放的歌词共播放时间
	 * @author 吴香礼
	 * @param ls 时间
	 * @return 下一时间
	 * @see {@link SongLyric#getNextTime(long)}
	 */
	public int getNextTime(long ls) {
		Long[] ts = getAllTimes();
		int index = getIndex(ls);
		if (index < ts.length - 1) {
			return (int) (ts[index + 1] - ts[index]);
		}
		return 0;
	}
	
	/**
	 * 处理歌词行
	 * @author 吴香礼
	 * @param line 歌词行
	 * @see {@link SongLyric#dealLine(String)}
	 */
	private void dealLine(String line)
	{
		if (line != null && !line.equals(""))
		{
			if (line.startsWith("[ti:"))
			{
				//标题
				title = line.substring(4, line.length() - 1);
			} 
			else if (line.startsWith("[ar:")) 
			{
				//歌手
				artist = line.substring(4, line.length() - 1);
			}
			else if (line.startsWith("[al:")) 
			{
				//专辑
				album = line.substring(4, line.length() - 1);
			} 
			else if (line.startsWith("[offset:")) 
			{
				//专辑
				offset = Long.parseLong(line.substring(8, line.length() - 1));
			}
			else 
			{
				//该行歌词内容
				Pattern ptn = Pattern.compile("\\[(\\d{2}:\\d{2}\\.\\d{2})\\]");
				Matcher mth = ptn.matcher(line);
				while (mth.find())
				{
					//得到时间点
					long time = strToLong(mth.group(1));
					//得到时间点后的内容
					String[] str = ptn.split(line);
					String lrc = str.length > 0 ? str[str.length - 1] : "";
					lrcs.put(time, lrc);
					maxTime = maxTime > time ? maxTime : time;
				}
			}
		}
	}
	
	/**
	 * 将00:00.00格式的歌词时间转换为long
	 * @author 吴香礼
	 * @param timeStr 时间的字符串格式
	 * @return 时间的long格式
	 * @see {@link SongLyric#strToLong(String)}
	 */
	public static long strToLong(String timeStr) {
		String[] s = timeStr.split(":");
		int min = Integer.parseInt(s[0]);
		String[] ss = s[1].split("\\.");
		int sec = Integer.parseInt(ss[0]);
		int mill = Integer.parseInt(ss[1]);
		return min * 60 * 1000 + sec * 1000 + mill * 10;
	}
	
	/**
	 * 处理毫秒数，以00:00的方式返回
	 * @author 吴香礼
	 * @param ts 毫秒数
	 * @return 指定时间格式
	 * @see {@link SongLyric#longToString(long)}
	 */
	public static String longToString(long ts) {
		int time = (int) ts / 1000;
		int ms = time % 60;
		int ss = time / 60;
		ss = ss > 99 ? 99 : ss;
		StringBuffer str = new StringBuffer();
		str.append(ss < 10 ? "0" + ss + ":" : ss + ":");
		str.append(ms < 10 ? "0" + ms : ms + "");
		return str.toString();
	}
	
	/**
	 * 获取顺序时间数组对象
	 * @author 吴香礼
	 * @return 时间数组
	 * @see {@link SongLyric#getAllTimes()}
	 */
	public Long[] getAllTimes() {
		Long[] ts = new Long[lrcs.size()];
		int index = 0;
		for (Long l : lrcs.keySet()) {
			ts[index++] = l;
		}
		for (int i = 0; i < ts.length - 1; i++) {
			for (int j = i; j < ts.length; j++) {
				if (ts[i] > ts[j]) {
					Long tmp = ts[i];
					ts[i] = ts[j];
					ts[j] = tmp;
				}
			}
		}
		return ts;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

}