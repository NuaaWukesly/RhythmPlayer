/**
 * 
 */
package org.nuaa161140225.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.nuaa161140225.app.RhythmApp;
import org.nuaa161140225.dataclass.MediaList;
import org.nuaa161140225.dataclass.SongInfo;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * ���ֲ��ŷ����࣬���ں�̨���������ļ����Լ�����
 * 
 * @author ������
 * 
 */
public class PlayService extends Service implements OnCompletionListener {

	private String myAction = "android.intent.rhythmplayer.update";
	/**
	 * �󶨳ɹ������ڻ�ȡ�������
	 */
	private PlayBinder playBinder = new PlayBinder();
	/**
	 * ���ڲ������ֵ�ý�岥����
	 */
	private MediaPlayer player = new MediaPlayer();
	/**
	 * ��ǰ���ŵĸ�������
	 */
	private SongInfo curSong = new SongInfo();
	/**
	 * ��ǰ����ʱ��
	 */
	private int curDuration = -1;
	private NotificationManager nm;

	/**
	 * ���ֲ��ŵ�״̬
	 */
	private boolean isPlaying = false;
	private boolean isStop = true;

	private RhythmApp rap;

	private List<SongInfo> list;

	/**
	 * ��ȡ��������״̬��trueΪplaying,����Ϊfalse
	 * 
	 * @return the isPlaying
	 */
	public boolean getPlayerState() {
		return isPlaying;
	}

	/**
	 * ���ò�������״̬λ
	 * 
	 * @param isPlaying
	 *            the isPlaying to set
	 */
	public void setPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
	}

	/**
	 * �󶨳ɹ����û���ȡ�÷���
	 * 
	 * @author 123
	 * 
	 */
	public class PlayBinder extends Binder {
		/**
		 * ��ȡ����󶨳ɹ��ķ������
		 * 
		 * @return ���ŷ������
		 */
		public PlayService getPlayService() {
			return PlayService.this;
		}
	}

	/**
	 * �������ַ����ź���
	 * 
	 * @author ������
	 */
	public PlayService() {
		// TODO Auto-generated constructor stub
		player = new MediaPlayer();
		this.curSong = new SongInfo();
		this.curDuration = -1;

	}

	/**
	 * ��������ʱ����
	 * 
	 * @see android.app.Service#onCreate()
	 */
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		if (player != null) {
			player.reset();
			player.release();
			player = null;
		}
		Log.i("OnCreate", "����");
		player = new MediaPlayer();
		rap = (RhythmApp) getApplication();
		list = rap.getAllMusicList().get(1).getList();
		if (rap.getLastSongIndex() < list.size() && rap.getLastSongIndex() >= 0) {
			curSong = list.get(rap.getLastSongIndex());
			if (curSong != null) {
				File file = new File(curSong.getmFilePath());
				if (file.exists() && player != null) {
					// �ļ�����
					player.reset();
					try {
						player.setDataSource(curSong.getmFilePath());
						player.prepare();
						setStop(false); // ���Ź���
						this.curDuration = player.getDuration();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

		/* ���������Ƿ���� */
		player.setOnCompletionListener(this);
		player.setOnErrorListener(new OnErrorListener() {

			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				// TODO Auto-generated method stub
				setPlaying(false);// ����ֹͣ����
				return false;
			}
		});
	}

	/**
	 * �˳���������
	 * 
	 * @see android.app.Service#onDestroy()
	 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (player != null) {
			player.stop();
			player.release();
			player = null;
		}
	}

	/**
	 * ���°󶨺����
	 * 
	 * @see android.app.Service#onRebind(android.content.Intent)
	 */
	@Override
	public void onRebind(Intent intent) {
		// TODO Auto-generated method stub
		super.onRebind(intent);
	}

	/**
	 * ����󶨺����
	 * 
	 * @see android.app.Service#onUnbind(android.content.Intent)
	 */
	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		this.onDestroy(); // ����
		return super.onUnbind(intent);
	}

	/**
	 * �ɰ󶨺����
	 * 
	 * @author ������
	 */
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		// ��Binder���أ��Ա��ȡ������󣬶Է�����в���
		Log.i("OnBind", "����");
		return playBinder;
	}

	/**
	 * ���õ�ǰ���Ÿ���
	 * 
	 * @author ������
	 * @param song
	 *            ����
	 * @see PlayService#setCurSong(SongInfo)
	 */
	public void setCurSong(SongInfo song) {
		this.curSong = song;
	}

	/**
	 * ��������
	 * 
	 * @param song
	 *            �����ŵĸ���
	 * @return ״̬�����ųɹ�����true�����򷵻�false
	 */
	public boolean play(SongInfo song) {
		boolean flag = false;
		Log.i("PlayMusic", curSong.getmFileName());
		if (song != null) {
			this.curSong = song;
			File file = new File(song.getmFilePath());
			if (file.exists() && player != null) {
				// �ļ�����
				player.reset();
				Log.i("PlayMusic", "����++++" + song.getmFilePath());
				try {
					player.setDataSource(curSong.getmFilePath());
					player.prepare();
					player.start();
					setStop(false); // ���Ź���
					this.curDuration = player.getDuration();
					setPlaying(true);
					Intent myIntent = new Intent(myAction);
					myIntent.putExtra("playstate", 0);
					Log.i("���Ź㲥	", "6");
					getApplicationContext().sendBroadcast(myIntent);
					flag = true;
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return flag;
	}

	/**
	 * ��ͣ/���²���
	 * 
	 * @return �ɹ�����true��ʧ�ܷ���false
	 */
	public boolean pause() {
		boolean flag = false;
		if (player != null) {
			if (player.isPlaying()) {
				player.pause();
				setPlaying(false);// ��ͣ
			} else {
				player.start();
				setPlaying(true);// ����
				Intent myIntent = new Intent(myAction);
				myIntent.putExtra("playstate", 0);
				getApplicationContext().sendBroadcast(myIntent);
			}
			flag = true;
		}
		return flag;
	}

	/**
	 * ��������
	 * 
	 * @param toTime
	 *            �������ʱ��
	 * @return ����ִ�б�־�ɹ�����true�����򷵻�false
	 */
	public boolean gotoTime(int toTime) {
		boolean flag = false;
		if (player.isPlaying()) {
			if (toTime >= 0 && toTime < getDuration()) {
				player.seekTo(toTime);
				flag = true;
			}
		}

		return flag;
	}

	/**
	 * ��ȡ����λ��
	 * 
	 * @return �ɹ�ʱ����λ�ã�ʧ�ܷ���-1
	 */
	public int getCurPosition() {
		int curposition = -1;
		if (player != null) {
			curposition = player.getCurrentPosition();
		}
		return curposition;
	}

	/**
	 * ��ȡ��ǰ���ֵ�ʱ��
	 * 
	 * @return �ɹ��Ƿ�������ʱ����ʧ��ʱ����-1
	 */
	public int getDuration() {
		return curDuration;
	}

	@Override
	public void onCompletion(MediaPlayer arg0) {
		// TODO Auto-generated method stub
		this.curDuration = -1;
		this.curSong = new SongInfo();
		setPlaying(false);
		Intent myIntent = new Intent(myAction);
		myIntent.putExtra("playstate", 1);
		getApplicationContext().sendBroadcast(myIntent);
	}

	/**
	 * @return the isStop
	 */
	public boolean isStop() {
		return isStop;
	}

	/**
	 * @param isStop
	 *            the isStop to set
	 */
	public void setStop(boolean state) {
		this.isStop = state;
	}

	/* (non-Javadoc)
	 * @see android.app.Service#onTaskRemoved(android.content.Intent)
	 */
	@Override
	public void onTaskRemoved(Intent rootIntent) {
		// TODO Auto-generated method stub
		super.onTaskRemoved(rootIntent);
		
	}
	
	
}
