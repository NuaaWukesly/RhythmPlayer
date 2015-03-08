/**
 * 
 */
package org.nuaa161140225.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.nuaa161140225.app.RhythmApp;
import org.nuaa161140225.database.songDB;
import org.nuaa161140225.dataclass.SongInfo;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;

import android.os.IBinder;

import android.util.Log;

/**
 * @author 123
 * 
 */
public class saveDataService extends Service {

	private RhythmApp app;
	private static String Lrc;
	private static String myAction = "android.intent.rhythmplayer.update";
	private Thread lrcT;
	private Thread updT;
	private Thread delsT;
	private Thread insetsT;
	private Thread clrT;
	private Thread updSong;
	private static songDB db;
	private static String keyword = "";
	private static SongInfo song = new SongInfo();
	private static SongInfo song1 = new SongInfo();
	private static SongInfo song2 = new SongInfo();
	private static SongInfo song3 = new SongInfo();
	private static String listname = "";
	private static String listname1 = "";
	private static String listname2 = "";
	private static String listname3 = "";
	private static String listname4 = "";
	private static boolean dbusing = false;
	private static List<SongInfo> list = new ArrayList<SongInfo>();

	public class SDSBinder extends Binder {
		public saveDataService getSDS() {
			return saveDataService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return new SDSBinder();
	}

	public void onCreate() {
		Log.i("Savadata", "开始");
		db = new songDB();
		Lrc = "";
	}

	public void onDestroy() {
	}

	public void onStart(Intent paramIntent, int paramInt) {
	}

	/**
	 * @return the lrc
	 */
	public String getLrc() {
		return Lrc;
	}

	/**
	 * @param lrc
	 *            the lrc to set
	 */
	public void setLrc(String lrc) {
		Lrc = lrc;
	}

	public void SearchLrc(String kw) {
		// final String kw = keyword;
		this.keyword = kw;
		this.Lrc = "";
		if (!keyword.isEmpty()) {
			Log.i("LrcT", "开始");
			lrcT = new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					String[] ext = { "lrc", "lrcx" };
					Log.i("LrcT00", "开始");
					searchAll("/storage/", ext, keyword);
					Log.i("LrcT02", "开始");
					Intent myIntent = new Intent(myAction);
					myIntent.putExtra("playstate", 3);
					Log.i("搜索广播	", "3");
					getApplicationContext().sendBroadcast(myIntent);
				}
			});
			lrcT.start();
		}
	}

	public void UpdateList(List<SongInfo> lst, String ln) {

		this.list = lst;
		this.listname = ln;
		Log.i("updT", "开始");
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (true) {
					if (!dbusing) {
						Log.i("更新列表线程", "开始");
						dbusing = true;
						db.delList(listname);
						Log.i("更新列表线程1", "开始");
						db.addList(list, listname);
						Log.i("更新列表线程2", "开始");
						dbusing = false;
						Log.i("更新列表线程", "开始");
						break;
					}
				}
			}
		}).start();
		// updT.start();
	}

	public void DeleteSong(SongInfo s, String ln) {
		this.song1 = s;
		this.listname1 = ln;
		Log.i("DelsT", "开始");
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (true) {
					if (!dbusing) {
						dbusing = true;
						db.delSong(song1, listname1);
						dbusing = false;
						break;
					}
				}
			}
		}).start();
		// delsT.start();
	}

	public void InsertSong(SongInfo s, String ln) {
		song2 = s;
		listname2 = ln;
		Log.i("insets", "开始");
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (true) {
					if (!dbusing) {
						dbusing = true;
						db.insertSong(song2, listname2);
						dbusing = false;
						break;
					}
				}
			}
		}).start();
		// insetsT.start();
	}

	public void clearList(String ln) {
		listname4 = ln;
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (true) {
					if (!dbusing) {
						Log.i("清空线程", "开始");
						dbusing = true;
						db.delList(listname4);
						Log.i("清空线程", "结束");
						dbusing = false;
						break;
					}
				}
			}
		}).start();
		// clrT.start();
	}

	public void updateSong(SongInfo s, String ln) {
		this.song3 = s;
		this.listname3 = ln;
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (true) {
					if (!dbusing) {
						dbusing = true;
						db.delSong(song3, listname3);
						db.insertSong(song3, listname3);
						dbusing = false;
					}
				}
			}
		}).start();
		// updSong.start();
	}

	// 遍历路径下的指定的后缀名
	@SuppressLint("DefaultLocale")
	private static void searchAll(String dir, final String[] suffixs,
			String keyword) {
		File file = new File(dir);
		// 遍历该目录中的所有文件
		File[] files = file.listFiles();
		if ((files != null) && (files.length > 0)) {
			// 遍历当前目录
			for (File tempfile : files) {
				if (tempfile.isDirectory() && (!file.isHidden())) {
					// 如果为文件夹，则递归调用
					searchAll(tempfile.getPath(), suffixs, keyword);
				} else {
					for (int i = 0; i < suffixs.length; i++) {
						if (tempfile.getPath().indexOf(keyword) >= 0) {
							if (tempfile.getPath().endsWith(
									suffixs[i].toUpperCase())
									|| tempfile.getPath().endsWith(
											suffixs[i].toLowerCase())) {
								// 如果为指定后最名，则添加到列表里
								Lrc = tempfile.getPath();
								Log.e(dir, tempfile.getPath().toString());
								// 找到一个立即返回
								return;
							}
						}
					}
				}
			}
		}
	}

}
