/**
 * 
 */
package org.nuaa161140225.app;

import java.util.ArrayList;
import java.util.List;

import org.nuaa161140225.database.songDB;
import org.nuaa161140225.dataclass.MediaList;
import org.nuaa161140225.dataclass.SongInfo;
import org.nuaa161140225.rhythmplayer.RhythmPlayer;
import org.nuaa161140225.service.PlayService;

import org.nuaa161140225.rhythmplayer.R;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

/**
 * @author 123
 * 
 */
@SuppressLint("WorldWriteableFiles")
public class RhythmApp extends Application {

	private int lastSongIndex;
	private List<SongInfo> local_music_list;
	private int local_music_num;
	private List<MediaList<SongInfo>> music_class_list;
	private int musicplayer_bgId;
	private int musicplayer_mode;
	private int new_num;
	private int old_num;
	private boolean isFirstLaunch = true;
	
	/* (non-Javadoc)
	 * @see android.app.Application#onTerminate()
	 */
	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
	}

	/**
	 * @return the isFirstLaunch
	 */
	public boolean isFirstLaunch() {
		return isFirstLaunch;
	}

	/**
	 * @param isFirstLaunch the isFirstLaunch to set
	 */
	public void setFirstLaunch(boolean isFirstLaunch) {
		this.isFirstLaunch = isFirstLaunch;
	}

	@SuppressWarnings("deprecation")
	public boolean SavaData() {

		SharedPreferences sp = getApplicationContext().getSharedPreferences(
				"playerStateData", Context.MODE_WORLD_WRITEABLE);
		SharedPreferences.Editor edt = sp.edit();
		/*
		 * 保存最后一首歌、视频的索引
		 */
		edt.putInt("lastSongIndex", lastSongIndex);
		edt.putInt("musicplayer_bgId", musicplayer_bgId);
		/*
		 * 保存音乐播放器的模式
		 */
		edt.putInt("musicplayer_mode", musicplayer_mode);
		edt.commit();
		return true;
	}

	/**
	 * 通过音乐的类型名将该音乐列表添加同名的列表中（该类型必须存在，否则存入失败），如添加到我的最爱。
	 * 
	 * @author 吴香礼
	 * @param list
	 *            音乐列表
	 * @return 添加成功返回true，否则返回false
	 * @see {@link MyApplication#addToMusicListByListName(MediaList)}
	 */
	public boolean addToMusicListByListName(MediaList<SongInfo> list) {
		/*
		 * 判断音乐列表是否存在。当音乐列表存在时，将原来列表的内容抽出，存于一个临时变量temp,
		 * 将该音乐列表从列表集合中去除。最后在原位置添加该音乐列表。
		 */
		int index = getMusicListIndexByListName(list.getListName());
		if (index != -1) {
			MediaList<SongInfo> temp = music_class_list.get(index);
			// 将新内容添加到音乐列表
			temp.addAll(list.getList());
			// 移除原内容，可以不需要此语句
			music_class_list.remove(index);
			/*
			 * 你对list执行list.add("thank you !",20);操作，它会这样处理：先判断索引为20的元素是否存在
			 * 1、如果存在的话，它会先把从20开始，直到最后的数据全往后挪，然后用新值代替以前20位置上的值
			 * 2、如果不存在的话，就直接放在目前最后一个元素后面
			 */
			music_class_list.add(index, temp);
			Log.i("插入列表名", list.getListName());
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 将一首音乐加入到列表名为ListName的音乐列表中。该音乐列表不存在时添加失败。
	 * 
	 * @author 吴香礼
	 * @param ListName
	 *            欲加入的音乐列表名
	 * @param song
	 *            欲加入的歌曲
	 * @return 添加成功返回true,否则返回false.
	 * @see {@link MyApplication#addToMusicListByListName(String, SongInfo)}
	 */
	public boolean addToMusicListByListName(String ListName, SongInfo song) {
		int index = getMusicListIndexByListName(ListName);
		if (index != -1) {
			// 取出原音乐列表
			MediaList<SongInfo> temp = music_class_list.get(index);
			// 在列表集合中去除该音乐列表
			music_class_list.remove(index);
			// 将该视频加入音乐列表
			temp.add(song);
			// 将音乐列表重新加入列表集合
			music_class_list.add(index, temp);
			return true;
		} else {
			// 不存在该音乐列表
			return false;
		}
	}

	/**
	 * 获取音乐列表集合
	 * 
	 * @author 吴香礼
	 * @return 所有的音乐列表，即音乐列表集合
	 * @see {@link MyApplication#getAllMusicList()}
	 */
	public List<MediaList<SongInfo>> getAllMusicList() {
		return music_class_list;
	}

	/**
	 * 获取最后播放的音乐的索引
	 * 
	 * @return
	 */
	public int getLastSongIndex() {
		return this.lastSongIndex;
	}

	/**
	 * 获取本地音乐列表
	 * 
	 * @return
	 */
	public List<SongInfo> getLocalMusicInfo() {
		return this.local_music_list;
	}

	/**
	 * 获取音乐列表集合
	 * 
	 * @return
	 */
	public int getLocalMusicNum() {
		return this.local_music_num;
	}

	/**
	 * 通过音乐列表的索引来获取音乐列表，返回第一个匹配的列表，或一个空列表。
	 * 
	 * @author 吴香礼
	 * @param index
	 *            音乐列表的索引值
	 * @return 音乐列表
	 * @see {@link MyApplication#getMusicListByIndex(int)}
	 */
	public MediaList<SongInfo> getMusicListByIndex(int index) {
		/*
		 * 如果索引值存在返回相应列表，否则返回一个空列表。
		 */
		if (index >= music_class_list.size() || index < 0) {
			/*
			 * 索引不存在,返回一个空列表
			 */
			return new MediaList<SongInfo>();
		} else {
			return music_class_list.get(index);
		}
	}

	/**
	 * 通过音乐列表名（如本地音乐）来获取音乐列表，返回第一个匹配的列表或一个空列表。
	 * 
	 * @author 吴香礼
	 * @param ListName
	 *            音乐列表的名称
	 * @return 音乐列表或空列表
	 * @see {@link MyApplication#getMusicListByListName(String)}
	 */
	public MediaList<SongInfo> getMusicListByListName(String ListName) {
		/*
		 * 遍历所有音乐列表，将列表名字为ListName 的列表返回， 否则返回一个空列表。
		 */
		for (int i = 0; i < music_class_list.size(); i++) {
			if (music_class_list.get(i).getListName().equals(ListName)) {
				/*
				 * 返回指定列表
				 */
				return music_class_list.get(i);
			}
		}
		/*
		 * 找不到，则返回一个空列表
		 */
		return new MediaList<SongInfo>();
	}

	/**
	 * 通过音乐列表的名字获取第一个匹配的列表的索引，找到返回相应索引值，否则返回-1。
	 * 
	 * @author 吴香礼
	 * @param ListName
	 *            音乐列表的名字
	 * @return 音乐列表索引值或-1
	 * @see {@link RhythmApp#getMusicClassIndexByListName(String)}
	 */
	public int getMusicListIndexByListName(String ListName) {
		/*
		 * 遍历所有音乐列表，返回第一个名称匹配的音乐列表的索引，否则返回-1
		 */
		for (int i = 0; i < music_class_list.size(); i++) {
			if (music_class_list.get(i).getListName().equals(ListName)) {
				// 找到索引返回索引值
				return i;
			}
		}
		// 未找到，返回-1
		return -1;
	}

	/**
	 * 获取播放器的模式
	 * 
	 * @return 模式的索引
	 */
	public int getMusicPlayerMode() {
		return this.musicplayer_mode;
	}

	/**
	 * 获取播放器的背景
	 * 
	 * @return 背景的id
	 */
	public int getMusicPlayer_bgID() {
		return this.musicplayer_bgId;
	}

	/**
	 * 获取新加入的歌曲数目
	 * 
	 * @return 歌曲数目
	 */
	public int getNewAddNum() {
		return this.new_num;
	}

	/**
	 * 增加新的音乐列表，如若该列表已经存在则添加失败。
	 * 
	 * @author 吴香礼
	 * @param Medialist
	 *            欲新建的音乐列表
	 * @return 添加成功返回true，否则返回false
	 * @see {@link MyApplication#newMusicListByListName(MediaList)}
	 */
	public boolean newMusicListByListName(MediaList<SongInfo> list) {
		/*
		 * 通过获取索引，判断列表是否已存在
		 */
		int index = getMusicListIndexByListName(list.getListName());
		if (index == -1) {
			/*
			 * 未存在该列表,在最后位置加入该列表
			 */
			music_class_list.add(music_class_list.size() - 1, list);
			return true;
		} else {
			/*
			 * 列表已存在，新建失败。
			 */
			return false;
		}

	}

	@SuppressLint({ "WorldReadableFiles", "WorldWriteableFiles" })
	public void onCreate() {
		super.onCreate();
		
		this.music_class_list = new ArrayList<MediaList<SongInfo>>();
		this.local_music_list = new ArrayList<SongInfo>();
		this.local_music_num = 0;
		this.new_num = 0;
		this.old_num = 0;
		SharedPreferences localSharedPreferences = getApplicationContext()
				.getSharedPreferences("playerStateData", 3);
		this.lastSongIndex = localSharedPreferences.getInt("lastSongIndex", 0);
		this.musicplayer_mode = localSharedPreferences.getInt(
				"musicplayer_mode", 0);
		this.musicplayer_bgId = localSharedPreferences.getInt(
				"musicplayer_bgId", R.drawable.ic_bg1);
		songDB db = new songDB();
		this.music_class_list.add(new MediaList<SongInfo>("本地音乐", 0,
				new ArrayList<SongInfo>()));
		this.music_class_list.add(new MediaList<SongInfo>("默认列表", 0,
				new ArrayList<SongInfo>()));
		this.music_class_list.add(new MediaList<SongInfo>("最近播放", 0,
				new ArrayList<SongInfo>()));
		this.music_class_list.add(new MediaList<SongInfo>("我的最爱", 0,
				new ArrayList<SongInfo>()));
		this.music_class_list.add(new MediaList<SongInfo>("新建列表", 0,
				new ArrayList<SongInfo>()));
		db.getMusicData(this);
		if (this.music_class_list.get(0).getList().size() == 0) {
			Log.i("空", "刷新");
			refreshLocalMusicInfo(getApplicationContext());
			addToMusicListByListName(new MediaList<SongInfo>("本地音乐", 0,
					this.local_music_list));		
		}
		if(this.music_class_list.get(1).getList().size() == 0)
		{
			addToMusicListByListName(new MediaList<SongInfo>("默认列表", 0,
					this.local_music_list));
			db.addList(local_music_list, "默认列表");
		}
		Log.i("listnum", "      "+"##"+music_class_list.get(music_class_list.size()-2).getList().size());
	}

	
	/**
	 * 刷新曲库
	 * 
	 * @param context
	 * @return
	 */
	public boolean refreshLocalMusicInfo(Context context) {

		if (Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			Cursor cursor = context.getContentResolver().query(
					MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
					null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

			if (cursor != null) {

				new_num = 0;
				if (!local_music_list.isEmpty()) {
					local_music_list.clear();
				}
				old_num = local_music_num;
				cursor.moveToFirst();
				for (int j = 0; j < cursor.getCount(); j++) {
					String mFileName = cursor.getString(cursor
							.getColumnIndex(MediaStore.Audio.Media._ID));
					String mFileTitle = cursor
							.getString(cursor
									.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
					String mDuration = cursor.getString(cursor
							.getColumnIndex(MediaStore.Audio.Media.DURATION));
					String mSinger = cursor.getString(cursor
							.getColumnIndex(MediaStore.Audio.Media.ARTIST));
					String mAlbum = cursor.getString(cursor
							.getColumnIndex(MediaStore.Audio.Media.ALBUM));
					String mYear = cursor.getString(cursor
							.getColumnIndex(MediaStore.Audio.Media.YEAR));
					String mFileType = cursor.getString(cursor
							.getColumnIndex(MediaStore.Audio.Media.MIME_TYPE));
					String mFileSize = cursor.getString(cursor
							.getColumnIndex(MediaStore.Audio.Media.SIZE));
					String mFilePath = cursor.getString(cursor
							.getColumnIndex(MediaStore.Audio.Media.DATA));
					SongInfo song = new SongInfo(mFileName, mFileTitle,
							mDuration, mSinger, mAlbum, mYear, mFileType,
							mFileSize, mFilePath, mFilePath.substring(0, mFilePath.lastIndexOf('.'))+".lrc");
					local_music_list.add(song);
					Log.i("getLocalMusicInfo", "@@@" + local_music_num);
					cursor.moveToNext();
				}
				local_music_num = cursor.getCount();
				new_num = local_music_num - old_num;
			}
			Log.i("getLocalMusicInfo", "@@@" + local_music_num + "num"
					+ local_music_list.size());
			cursor.close();
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 重设音乐列表集合。不建议使用。
	 * 
	 * @param music_class_list
	 *            新的音乐列表集合
	 * @return 设置成功返回true
	 * @see {@link MyApplication#resetAllMusicList(List)}
	 */
	public boolean resetAllMusicList(List<MediaList<SongInfo>> music_class_list) {
		this.music_class_list = music_class_list;
		return true;
	}

	/**
	 * 设置最后播放的音乐的索引
	 * 
	 * @param paramInt
	 */
	public void setLastSongIndex(int paramInt) {
		this.lastSongIndex = paramInt;
	}

	/**
	 * 设置播放的模式
	 * 
	 * @param paramInt
	 */
	public void setMusicPlayerMode(int paramInt) {
		this.musicplayer_mode = paramInt;
	}

	/**
	 * 设置背景
	 * 
	 * @param paramInt
	 */
	public void setMusicPlayer_bgID(int paramInt) {
		this.musicplayer_bgId = paramInt;
	}

}
