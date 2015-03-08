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
 * 音乐播放服务类，用于后台播放音乐文件，以及更新
 * 
 * @author 吴香礼
 * 
 */
public class PlayService extends Service implements OnCompletionListener {

	private String myAction = "android.intent.rhythmplayer.update";
	/**
	 * 绑定成功后，用于获取服务对象
	 */
	private PlayBinder playBinder = new PlayBinder();
	/**
	 * 用于播放音乐的媒体播放类
	 */
	private MediaPlayer player = new MediaPlayer();
	/**
	 * 当前播放的歌曲对象
	 */
	private SongInfo curSong = new SongInfo();
	/**
	 * 当前音乐时长
	 */
	private int curDuration = -1;
	private NotificationManager nm;

	/**
	 * 音乐播放的状态
	 */
	private boolean isPlaying = false;
	private boolean isStop = true;

	private RhythmApp rap;

	private List<SongInfo> list;

	/**
	 * 获取播放器的状态，true为playing,否则为false
	 * 
	 * @return the isPlaying
	 */
	public boolean getPlayerState() {
		return isPlaying;
	}

	/**
	 * 设置播放器的状态位
	 * 
	 * @param isPlaying
	 *            the isPlaying to set
	 */
	public void setPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
	}

	/**
	 * 绑定成功后，用户获取该服务
	 * 
	 * @author 123
	 * 
	 */
	public class PlayBinder extends Binder {
		/**
		 * 获取服务绑定成功的服务对象
		 * 
		 * @return 播放服务对象
		 */
		public PlayService getPlayService() {
			return PlayService.this;
		}
	}

	/**
	 * 播放音乐服务够着函数
	 * 
	 * @author 吴香礼
	 */
	public PlayService() {
		// TODO Auto-generated constructor stub
		player = new MediaPlayer();
		this.curSong = new SongInfo();
		this.curDuration = -1;

	}

	/**
	 * 创建服务时调用
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
		Log.i("OnCreate", "到此");
		player = new MediaPlayer();
		rap = (RhythmApp) getApplication();
		list = rap.getAllMusicList().get(1).getList();
		if (rap.getLastSongIndex() < list.size() && rap.getLastSongIndex() >= 0) {
			curSong = list.get(rap.getLastSongIndex());
			if (curSong != null) {
				File file = new File(curSong.getmFilePath());
				if (file.exists() && player != null) {
					// 文件存在
					player.reset();
					try {
						player.setDataSource(curSong.getmFilePath());
						player.prepare();
						setStop(false); // 播放过了
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

		/* 监听播放是否完成 */
		player.setOnCompletionListener(this);
		player.setOnErrorListener(new OnErrorListener() {

			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				// TODO Auto-generated method stub
				setPlaying(false);// 错误，停止播放
				return false;
			}
		});
	}

	/**
	 * 退出服务后调用
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
	 * 重新绑定后调用
	 * 
	 * @see android.app.Service#onRebind(android.content.Intent)
	 */
	@Override
	public void onRebind(Intent intent) {
		// TODO Auto-generated method stub
		super.onRebind(intent);
	}

	/**
	 * 解除绑定后调用
	 * 
	 * @see android.app.Service#onUnbind(android.content.Intent)
	 */
	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		this.onDestroy(); // 销毁
		return super.onUnbind(intent);
	}

	/**
	 * 成绑定后调用
	 * 
	 * @author 吴香礼
	 */
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		// 将Binder返回，以便获取服务对象，对服务进行操作
		Log.i("OnBind", "到此");
		return playBinder;
	}

	/**
	 * 设置当前播放歌曲
	 * 
	 * @author 吴香礼
	 * @param song
	 *            歌曲
	 * @see PlayService#setCurSong(SongInfo)
	 */
	public void setCurSong(SongInfo song) {
		this.curSong = song;
	}

	/**
	 * 播放音乐
	 * 
	 * @param song
	 *            欲播放的歌曲
	 * @return 状态，播放成功返回true，否则返回false
	 */
	public boolean play(SongInfo song) {
		boolean flag = false;
		Log.i("PlayMusic", curSong.getmFileName());
		if (song != null) {
			this.curSong = song;
			File file = new File(song.getmFilePath());
			if (file.exists() && player != null) {
				// 文件存在
				player.reset();
				Log.i("PlayMusic", "到此++++" + song.getmFilePath());
				try {
					player.setDataSource(curSong.getmFilePath());
					player.prepare();
					player.start();
					setStop(false); // 播放过了
					this.curDuration = player.getDuration();
					setPlaying(true);
					Intent myIntent = new Intent(myAction);
					myIntent.putExtra("playstate", 0);
					Log.i("播放广播	", "6");
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
	 * 暂停/重新播放
	 * 
	 * @return 成功返回true，失败返回false
	 */
	public boolean pause() {
		boolean flag = false;
		if (player != null) {
			if (player.isPlaying()) {
				player.pause();
				setPlaying(false);// 暂停
			} else {
				player.start();
				setPlaying(true);// 播放
				Intent myIntent = new Intent(myAction);
				myIntent.putExtra("playstate", 0);
				getApplicationContext().sendBroadcast(myIntent);
			}
			flag = true;
		}
		return flag;
	}

	/**
	 * 快进或快退
	 * 
	 * @param toTime
	 *            快进到的时间
	 * @return 返回执行标志成功返回true，否则返回false
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
	 * 获取播放位置
	 * 
	 * @return 成功时返回位置，失败返回-1
	 */
	public int getCurPosition() {
		int curposition = -1;
		if (player != null) {
			curposition = player.getCurrentPosition();
		}
		return curposition;
	}

	/**
	 * 获取当前音乐的时长
	 * 
	 * @return 成功是返回音乐时长，失败时返回-1
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
