/**
 * 
 */
package org.nuaa161140225.database;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.nuaa161140225.app.RhythmApp;
import org.nuaa161140225.dataclass.MediaList;
import org.nuaa161140225.dataclass.SongInfo;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

/**
 * @author 123
 * 
 */
@SuppressLint("SdCardPath")
public class songDB {

	/**
	 * ���ݿ����
	 * 
	 * @see {@link MyDataBase#mydb}
	 */
	private SQLiteDatabase mydb;
	/**
	 * ���ݿ������ļ���·��
	 * 
	 * @see {@link MyDataBase#path}
	 */
	File path = new File("/sdcard/RhythmPlayer");
	/**
	 * ���ݿ�·��
	 * 
	 * @see {@link MyDataBase#file}
	 */
	File file = new File("/sdcard/RhythmPlayer/data.db");

	/**
	 * ��¼sd����ǰ�Ƿ���ж�дȨ��
	 * 
	 * @see {@link MyDataBase#sdstate}
	 */
	boolean sdstate = Environment.getExternalStorageState().equals(
			android.os.Environment.MEDIA_MOUNTED);

	String tabname = "musiclisttab";
	/**
	 * ���캯��������������ݿ����
	 * 
	 * @author ������
	 * @see {@link MyDataBase}
	 */
	public songDB() {
		if (sdstate) {

			/*
			 * sd���ɶ�дʱ
			 */
			if (!path.exists()) {
				/*
				 * ·�������ڣ��½�·��
				 */
				path.mkdir();
			}
			if (!file.exists()) {
				/*
				 * ���ݿⲻ����,�½����ݿ��ļ�
				 */
				try {
					if (file.createNewFile()) {
						// �½����ݿ�ɹ�,�򿪻��򴴽����ݿ�
						mydb = SQLiteDatabase.openOrCreateDatabase(file, null);
						/*
						 * �����������½���,�б�ţ�������Ϣ���ø������ڵ������б���
						 */
						createMusicTab(tabname);
					}
				} catch (IOException e) {
					e.printStackTrace();
					Log.i("Sdcard", "sd�����ܴ��ڣ�");
					/*
					 * ��Ҫ�ڴ���һ��������
					 */
				}
			} else {
				// �������ݿ��ļ�ʱ���򿪻��򴴽����ݿ⣬���Ѵ���
				mydb = SQLiteDatabase.openOrCreateDatabase(file, null);
				/*
				 * �����������½���,�б�ţ�����/��Ƶ��Ϣ���ø�������Ƶ���ڵ�����/��Ƶ�б���
				 */
				createMusicTab(tabname);
			}
			/*
			 * �ر����ݿ�
			 */
			mydb.close();
		} else {
			/*
			 * sd�����ɶ�д�����ݿⴴ��ʧ�ܣ���ҪһЩ��ʾ
			 */
		}

	}

	/**
	 * ����һ������ΪtabName�Ĵ洢������Ϣ�ı�
	 * 
	 * @author ������
	 * @param tabName
	 *            ����
	 */
	private void createMusicTab(String tabName) {
		mydb.execSQL("CREATE TABLE IF NOT EXISTS " + tabName
				+ " (_id  INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "filename  TEXT," + "filetitle  TEXT," + "duration  TEXT,"
				+ "singer  TEXT," + "album  TEXT," + "year  TEXT,"
				+ "filetype  TEXT," + "filesize  TEXT," + "filepath  TEXT,"
				+ "lrcpath  TEXT," + "listname TEXT);");
	}

	/**
	 * �������б���д�����ݿ�
	 * 
	 * @author ������
	 * @param data
	 *            �����б���
	 * @return sdcard state
	 * @see {@link MyDataBase#SavaData(List)}
	 */
	public boolean savaMusicData(List<MediaList<SongInfo>> data) {
		if (sdstate) {
			// Log.i("info", "���ˣ�datasize="+data.get(0).getList().size());

			/*
			 * sd���ɶ�дʱ
			 */
			/*
			 * �����е������б��棬����˳��
			 */
			/*
			 * 1�� �򿪻򴴽����ݿ�
			 */
			mydb = SQLiteDatabase.openOrCreateDatabase(file, null);
			/*
			 * 2�����ԭ����ڣ�ɾ��ԭ�б�
			 */

			mydb.execSQL("DROP TABLE IF EXISTS musiclisttab;");
			/*
			 * 3���½���
			 */
			createMusicTab(tabname);
			/*
			 * 4��ѭ����������,�Ӻ���ǰ����
			 */
			for (int i = 0; i < data.size()-1; i++) {
				// Log.i("info", "���ˣ�i="+i);
				// iΪ�����б�����

				for (int j = 0; j < data.get(i).getList().size(); j++) {
					// jΪ�����б�i�еĸ�����Ŀ
					// Log.i("info", "���ˣ�j="+j);
					/*
					 * ��������
					 */
					if (mydb.insert(
							"musiclisttab",
							null,
							putSongDataToValue(data.get(i).getList().get(j),
									data.get(i).getListName())) == -1) {
						// Log.i("��"+j+"�����ݲ���ʧ�ܣ�", "");
					}
				}
				Log.i("�б���", data.get(i).getListName() + "��Ŀ"
						+ data.get(i).getList().size());
			}
			/*
			 * 5���ر����ݿ�
			 */
			mydb.close();
		}
		return sdstate;
	}

	private ContentValues putSongDataToValue(SongInfo song, String listname) {
		ContentValues value = new ContentValues();
		value.put("filename", song.getmFileName());
		value.put("filetitle", song.getmFileTitle());
		value.put("duration", song.getmDuration());
		value.put("singer", song.getmSinger());
		value.put("album", song.getmAlbum());
		value.put("year", song.getmYear());
		value.put("filetype", song.getmFileType());
		value.put("filesize", song.getmFileSize());
		value.put("filepath", song.getmFilePath());
		value.put("lrcpath", song.getLrcPath());
		value.put("listname", listname);
		return value;
	}

	/**
	 * �����ݿ��е��������ڳ�ʼ��Ӧ�õ������б���
	 * 
	 * @author ������
	 * @param mp
	 *            Ӧ�ö���
	 * @return sdcard state
	 * @see {@link MyDataBase#getData(MyApplication)}
	 */
	public boolean getMusicData(RhythmApp app) {
		if (sdstate) {
			int count = 0;
			/*
			 * sd���ɶ�дʱ
			 */

			/*
			 * �����б�
			 */
			List<SongInfo> musiclist = new ArrayList<SongInfo>();
			/*
			 * �򿪻��򴴽����ݿ�
			 */
			mydb = SQLiteDatabase.openOrCreateDatabase(file, null);
			/*
			 * ��ȡ��������
			 */
			Cursor cursor = mydb.query("musiclisttab", null, null, null, null,
					null, null);

			if (cursor.getCount() != 0) {

				/*
				 * ���ݴ��ڣ��Ƶ���һ������
				 */
				cursor.moveToFirst();

				/*
				 * ��ȡ��һ�������б�����
				 */
				// Log.i("��־", "����!");
				String listname = cursor.getString(cursor
						.getColumnIndex("listname"));

				/*
				 * ѭ����ȡ�������ݣ��������ݹ���Ӧ�������ּ��ϵ���Ӧ�б�
				 */
				for (int j = 0; j < cursor.getCount(); j++) {

					// ����б����б䣬�������µ��б�
					if (!listname.equals(cursor.getString(cursor
							.getColumnIndex("listname")))) {
						// Log.i("listname", cursor.getString(cursor
						// .getColumnIndex("listname")));
						/*
						 * ����Ӧ�õ��б���
						 */
						if (!app.addToMusicListByListName(new MediaList<SongInfo>(
								listname, 0, musiclist))) {
							/*
							 * ��������б���Ӧ��Ĭ�ϵ��б������ʧ�� ��Ӧ�����½�һ���б�
							 */
							app.newMusicListByListName(new MediaList<SongInfo>(
									listname, 0, musiclist));
						}
						/*
						 * ��¼�µ��б���
						 */
						Log.i("�б���", listname + "��Ŀ" + count);
						listname = cursor.getString(cursor
								.getColumnIndex("listname"));
						count = 0;
						// ��������б�
						musiclist.clear();
					}
					/*
					 * ��¼������Ϣ�������������б�
					 */
					musiclist.add(getSongFromCursor(cursor));
					count++;
					/*
					 * �Ƶ���һ����
					 */
					cursor.moveToNext();
				}
				/*
				 * �����һ�����б����Ӧ�õ����ּ���
				 */
				if (!app.addToMusicListByListName(new MediaList<SongInfo>(
						listname, 0, musiclist))) {
					/*
					 * ��������б���Ӧ��Ĭ�ϵ��б������ʧ�� ��Ӧ�����½�һ���б�
					 */
					app.newMusicListByListName(new MediaList<SongInfo>(
							listname, 0, musiclist));
				}
			}
			/*
			 * �ر����ݿ�
			 */

			cursor.close();
			mydb.close();
		} else {
			/*
			 * sd��������
			 */
		}
		return sdstate;
	}

	/**
	 * ��ȡ������Ϣ�����䷵��
	 * 
	 * @author ������
	 * @param cursor
	 * @see {@link MyDataBase#getSongData(MyApplication)}
	 */
	private SongInfo getSongFromCursor(Cursor cursor) {
		String mFileName = cursor.getString(cursor.getColumnIndex("filename"));
		String mFileTitle = cursor
				.getString(cursor.getColumnIndex("filetitle"));
		String mDuration = cursor.getString(cursor.getColumnIndex("duration"));
		String mSinger = cursor.getString(cursor.getColumnIndex("singer"));
		String mAlbum = cursor.getString(cursor.getColumnIndex("album"));
		String mYear = cursor.getString(cursor.getColumnIndex("year"));
		String mFileType = cursor.getString(cursor.getColumnIndex("filetype"));
		String mFileSize = cursor.getString(cursor.getColumnIndex("filesize"));
		String mFilePath = cursor.getString(cursor.getColumnIndex("filepath"));
		String mLrcPath = cursor.getString(cursor.getColumnIndex("lrcpath"));
		SongInfo song = new SongInfo(mFileName, mFileTitle, mDuration, mSinger,
				mAlbum, mYear, mFileType, mFileSize, mFilePath, mLrcPath);
		return song;
	}

	public void delSong(SongInfo song,String listname)
	{
		if(sdstate)
		{
			/*
			 * �򿪻��򴴽����ݿ�
			 */
			mydb = SQLiteDatabase.openOrCreateDatabase(file, null);
			String del = "DELETE FROM "+tabname +" WHERE filetitle='"+song.getmFileTitle()+ "' AND listname='"+listname+"'";
			Log.i(listname,del);
			mydb.execSQL(del);
			mydb.close();
		}
	}
	
	public void  insertSong(SongInfo song,String listname)
	{
		if(sdstate){
			mydb = SQLiteDatabase.openOrCreateDatabase(file, null);
			mydb.insert(
					tabname,
					null,
					putSongDataToValue(song,listname));
			mydb.close();
		}
	}
	
	public void delList(String listname)
	{
		if(sdstate)
		{
			mydb = SQLiteDatabase.openOrCreateDatabase(file, null);
			//ע�����еĿո�
			String del = "DELETE FROM "+tabname +" WHERE listname='"+listname+"'";
			mydb.execSQL(del);
			mydb.close();
		}
	}

	public void addList(List<SongInfo>list,String listname)
	{
		int count = list.size();
		if(count>0&&listname.length()>0)
		{
			mydb = SQLiteDatabase.openOrCreateDatabase(file, null);
			for(int i =0 ;i<count;i++)
			{
				mydb.insert(
						tabname,
						null,
						putSongDataToValue(list.get(i),
								listname));
			}
			mydb.close();
		}
	}
  
	
}
