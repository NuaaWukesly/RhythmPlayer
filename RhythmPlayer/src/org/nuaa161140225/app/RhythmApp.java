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
		 * �������һ�׸衢��Ƶ������
		 */
		edt.putInt("lastSongIndex", lastSongIndex);
		edt.putInt("musicplayer_bgId", musicplayer_bgId);
		/*
		 * �������ֲ�������ģʽ
		 */
		edt.putInt("musicplayer_mode", musicplayer_mode);
		edt.commit();
		return true;
	}

	/**
	 * ͨ�����ֵ����������������б����ͬ�����б��У������ͱ�����ڣ��������ʧ�ܣ�������ӵ��ҵ����
	 * 
	 * @author ������
	 * @param list
	 *            �����б�
	 * @return ��ӳɹ�����true�����򷵻�false
	 * @see {@link MyApplication#addToMusicListByListName(MediaList)}
	 */
	public boolean addToMusicListByListName(MediaList<SongInfo> list) {
		/*
		 * �ж������б��Ƿ���ڡ��������б����ʱ����ԭ���б�����ݳ��������һ����ʱ����temp,
		 * ���������б���б�����ȥ���������ԭλ����Ӹ������б�
		 */
		int index = getMusicListIndexByListName(list.getListName());
		if (index != -1) {
			MediaList<SongInfo> temp = music_class_list.get(index);
			// ����������ӵ������б�
			temp.addAll(list.getList());
			// �Ƴ�ԭ���ݣ����Բ���Ҫ�����
			music_class_list.remove(index);
			/*
			 * ���listִ��list.add("thank you !",20);���������������������ж�����Ϊ20��Ԫ���Ƿ����
			 * 1��������ڵĻ��������ȰѴ�20��ʼ��ֱ����������ȫ����Ų��Ȼ������ֵ������ǰ20λ���ϵ�ֵ
			 * 2����������ڵĻ�����ֱ�ӷ���Ŀǰ���һ��Ԫ�غ���
			 */
			music_class_list.add(index, temp);
			Log.i("�����б���", list.getListName());
			return true;
		} else {
			return false;
		}
	}

	/**
	 * ��һ�����ּ��뵽�б���ΪListName�������б��С��������б�����ʱ���ʧ�ܡ�
	 * 
	 * @author ������
	 * @param ListName
	 *            ������������б���
	 * @param song
	 *            ������ĸ���
	 * @return ��ӳɹ�����true,���򷵻�false.
	 * @see {@link MyApplication#addToMusicListByListName(String, SongInfo)}
	 */
	public boolean addToMusicListByListName(String ListName, SongInfo song) {
		int index = getMusicListIndexByListName(ListName);
		if (index != -1) {
			// ȡ��ԭ�����б�
			MediaList<SongInfo> temp = music_class_list.get(index);
			// ���б�����ȥ���������б�
			music_class_list.remove(index);
			// ������Ƶ���������б�
			temp.add(song);
			// �������б����¼����б���
			music_class_list.add(index, temp);
			return true;
		} else {
			// �����ڸ������б�
			return false;
		}
	}

	/**
	 * ��ȡ�����б���
	 * 
	 * @author ������
	 * @return ���е������б��������б���
	 * @see {@link MyApplication#getAllMusicList()}
	 */
	public List<MediaList<SongInfo>> getAllMusicList() {
		return music_class_list;
	}

	/**
	 * ��ȡ��󲥷ŵ����ֵ�����
	 * 
	 * @return
	 */
	public int getLastSongIndex() {
		return this.lastSongIndex;
	}

	/**
	 * ��ȡ���������б�
	 * 
	 * @return
	 */
	public List<SongInfo> getLocalMusicInfo() {
		return this.local_music_list;
	}

	/**
	 * ��ȡ�����б���
	 * 
	 * @return
	 */
	public int getLocalMusicNum() {
		return this.local_music_num;
	}

	/**
	 * ͨ�������б����������ȡ�����б����ص�һ��ƥ����б���һ�����б�
	 * 
	 * @author ������
	 * @param index
	 *            �����б������ֵ
	 * @return �����б�
	 * @see {@link MyApplication#getMusicListByIndex(int)}
	 */
	public MediaList<SongInfo> getMusicListByIndex(int index) {
		/*
		 * �������ֵ���ڷ�����Ӧ�б����򷵻�һ�����б�
		 */
		if (index >= music_class_list.size() || index < 0) {
			/*
			 * ����������,����һ�����б�
			 */
			return new MediaList<SongInfo>();
		} else {
			return music_class_list.get(index);
		}
	}

	/**
	 * ͨ�������б������籾�����֣�����ȡ�����б����ص�һ��ƥ����б��һ�����б�
	 * 
	 * @author ������
	 * @param ListName
	 *            �����б������
	 * @return �����б����б�
	 * @see {@link MyApplication#getMusicListByListName(String)}
	 */
	public MediaList<SongInfo> getMusicListByListName(String ListName) {
		/*
		 * �������������б����б�����ΪListName ���б��أ� ���򷵻�һ�����б�
		 */
		for (int i = 0; i < music_class_list.size(); i++) {
			if (music_class_list.get(i).getListName().equals(ListName)) {
				/*
				 * ����ָ���б�
				 */
				return music_class_list.get(i);
			}
		}
		/*
		 * �Ҳ������򷵻�һ�����б�
		 */
		return new MediaList<SongInfo>();
	}

	/**
	 * ͨ�������б�����ֻ�ȡ��һ��ƥ����б���������ҵ�������Ӧ����ֵ�����򷵻�-1��
	 * 
	 * @author ������
	 * @param ListName
	 *            �����б������
	 * @return �����б�����ֵ��-1
	 * @see {@link RhythmApp#getMusicClassIndexByListName(String)}
	 */
	public int getMusicListIndexByListName(String ListName) {
		/*
		 * �������������б����ص�һ������ƥ��������б�����������򷵻�-1
		 */
		for (int i = 0; i < music_class_list.size(); i++) {
			if (music_class_list.get(i).getListName().equals(ListName)) {
				// �ҵ�������������ֵ
				return i;
			}
		}
		// δ�ҵ�������-1
		return -1;
	}

	/**
	 * ��ȡ��������ģʽ
	 * 
	 * @return ģʽ������
	 */
	public int getMusicPlayerMode() {
		return this.musicplayer_mode;
	}

	/**
	 * ��ȡ�������ı���
	 * 
	 * @return ������id
	 */
	public int getMusicPlayer_bgID() {
		return this.musicplayer_bgId;
	}

	/**
	 * ��ȡ�¼���ĸ�����Ŀ
	 * 
	 * @return ������Ŀ
	 */
	public int getNewAddNum() {
		return this.new_num;
	}

	/**
	 * �����µ������б��������б��Ѿ����������ʧ�ܡ�
	 * 
	 * @author ������
	 * @param Medialist
	 *            ���½��������б�
	 * @return ��ӳɹ�����true�����򷵻�false
	 * @see {@link MyApplication#newMusicListByListName(MediaList)}
	 */
	public boolean newMusicListByListName(MediaList<SongInfo> list) {
		/*
		 * ͨ����ȡ�������ж��б��Ƿ��Ѵ���
		 */
		int index = getMusicListIndexByListName(list.getListName());
		if (index == -1) {
			/*
			 * δ���ڸ��б�,�����λ�ü�����б�
			 */
			music_class_list.add(music_class_list.size() - 1, list);
			return true;
		} else {
			/*
			 * �б��Ѵ��ڣ��½�ʧ�ܡ�
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
		this.music_class_list.add(new MediaList<SongInfo>("��������", 0,
				new ArrayList<SongInfo>()));
		this.music_class_list.add(new MediaList<SongInfo>("Ĭ���б�", 0,
				new ArrayList<SongInfo>()));
		this.music_class_list.add(new MediaList<SongInfo>("�������", 0,
				new ArrayList<SongInfo>()));
		this.music_class_list.add(new MediaList<SongInfo>("�ҵ��", 0,
				new ArrayList<SongInfo>()));
		this.music_class_list.add(new MediaList<SongInfo>("�½��б�", 0,
				new ArrayList<SongInfo>()));
		db.getMusicData(this);
		if (this.music_class_list.get(0).getList().size() == 0) {
			Log.i("��", "ˢ��");
			refreshLocalMusicInfo(getApplicationContext());
			addToMusicListByListName(new MediaList<SongInfo>("��������", 0,
					this.local_music_list));		
		}
		if(this.music_class_list.get(1).getList().size() == 0)
		{
			addToMusicListByListName(new MediaList<SongInfo>("Ĭ���б�", 0,
					this.local_music_list));
			db.addList(local_music_list, "Ĭ���б�");
		}
		Log.i("listnum", "      "+"##"+music_class_list.get(music_class_list.size()-2).getList().size());
	}

	
	/**
	 * ˢ������
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
	 * ���������б��ϡ�������ʹ�á�
	 * 
	 * @param music_class_list
	 *            �µ������б���
	 * @return ���óɹ�����true
	 * @see {@link MyApplication#resetAllMusicList(List)}
	 */
	public boolean resetAllMusicList(List<MediaList<SongInfo>> music_class_list) {
		this.music_class_list = music_class_list;
		return true;
	}

	/**
	 * ������󲥷ŵ����ֵ�����
	 * 
	 * @param paramInt
	 */
	public void setLastSongIndex(int paramInt) {
		this.lastSongIndex = paramInt;
	}

	/**
	 * ���ò��ŵ�ģʽ
	 * 
	 * @param paramInt
	 */
	public void setMusicPlayerMode(int paramInt) {
		this.musicplayer_mode = paramInt;
	}

	/**
	 * ���ñ���
	 * 
	 * @param paramInt
	 */
	public void setMusicPlayer_bgID(int paramInt) {
		this.musicplayer_bgId = paramInt;
	}

}
