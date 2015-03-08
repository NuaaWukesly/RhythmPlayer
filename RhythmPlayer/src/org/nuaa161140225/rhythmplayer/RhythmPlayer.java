package org.nuaa161140225.rhythmplayer;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.nuaa161140225.adapter.exListAdapter;
import org.nuaa161140225.adapter.fileAdapter;
import org.nuaa161140225.adapter.songListAdapter;
import org.nuaa161140225.app.RhythmApp;
import org.nuaa161140225.dataclass.LyricView;
import org.nuaa161140225.dataclass.MediaList;
import org.nuaa161140225.dataclass.SongInfo;
import org.nuaa161140225.dataclass.SongLyric;
import org.nuaa161140225.dataclass.myFile;
import org.nuaa161140225.service.PlayService;
import org.nuaa161140225.service.PlayService.PlayBinder;
import org.nuaa161140225.service.saveDataService.SDSBinder;
import org.nuaa161140225.service.saveDataService;

import org.nuaa161140225.rhythmplayer.R;

import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

@SuppressLint("ShowToast")
public class RhythmPlayer extends Activity implements
		GestureDetector.OnGestureListener {

	private View showThemeView;
	// //////////////////////////////////////////////////
	// 控件定义部分
	// //////////////////////////////////////////////////
	/*
	 * 标题栏控件
	 */
	/**
	 * 标题栏，音乐列表按钮
	 */
	private ImageButton titlebar_list;
	/**
	 * 标题栏，主页面即歌词显示页面切换按钮
	 */
	private ImageButton titlebar_music;
	/**
	 * 标题栏，打开文件浏览器按钮
	 */
	private ImageButton titlebar_openDir;
	/**
	 * 标题栏，声音调节按钮
	 */
	private ImageButton titlebar_voice;
	/**
	 * 标题栏，更多功能按钮
	 */
	private ImageButton title_more;
	/**
	 * 更多功能弹出菜单
	 */
	private PopupWindow more_Form;
	private View more_Layout;
	private TextView more_going;
	private TextView more_aboutus;

	// 工具栏控件
	/**
	 * 工具栏，播放模式选择按钮
	 */
	private ImageButton toolbar_mode;
	/**
	 * 工具栏，播放前一首按钮
	 */
	private ImageButton toolbar_pre;
	/**
	 * 工具栏，播放按钮
	 */
	private ImageButton toolbar_play;
	/**
	 * 工具栏，播放下一首按钮
	 */
	private ImageButton toolbar_next;
	/**
	 * 工具栏，菜单显示按钮
	 */
	private ImageButton toolbar_menu;
	/*
	 * 中间部分
	 */
	/**
	 * 中间部分滑动布局
	 */
	private ViewFlipper music_Flipper;
	// page1
	/**
	 * 歌词显示页面，线性布局
	 */
	private LinearLayout page_LRC;
	/**
	 * 歌词显示控件
	 */
	private LyricView LrcView;
	private Dialog searLrcD;
	/**
	 * 当前播放歌曲名字显示控件
	 */
	private TextView song_Name_tv;
	/**
	 * 当前播放位置显示控件
	 */
	private TextView song_curposition_tv;
	/**
	 * 歌曲总时长
	 */
	private TextView sond_duration_tv;
	/**
	 * 歌曲播放进度条
	 */
	private SeekBar playTime_sb;
	// page2
	/**
	 * 播放器主列表显示页面，可扩展列表
	 */
	private ExpandableListView mainlistview;
	// /////////////////////////////////////////////////////////////////
	// 组列表长按弹出菜单布局
	// /////////////////////////////////////////////////////////////////
	/**
	 * 组列表长按弹出菜单布局view
	 */
	private View groupLongPress_Layout;
	/**
	 * 组列表长按弹出菜单，增加到列表按钮
	 */
	private Button listgroup_add2list;
	/**
	 * 组列表长按弹出菜单，清空列表按钮
	 */
	private Button listgroup_clear;
	/**
	 * 组列表长按弹出菜单，删除列表按钮
	 */
	private Button listgroup_del;
	/**
	 * 组列表长按弹出菜单，更改列表名字按钮
	 */
	private Button listgroup_altername;
	/**
	 * 组列表长按弹出菜单，取消按钮
	 */
	private Button listgroup_cancel;
	/**
	 * 组列表长按弹出菜单窗口
	 */
	private PopupWindow group_LongPress_Form;

	// /////////////////////////////////////////////////////////////////
	// 子列表长按弹出菜单布局
	// /////////////////////////////////////////////////////////////////
	/**
	 * 子列表长按弹出菜单布局view
	 */
	private View childLongPress_Layout;
	/**
	 * 子列表长按弹出菜单窗口
	 */
	private PopupWindow chileLongPress_Form;
	/**
	 * 子列表长按弹出菜单，增加到列表按钮
	 */
	private Button listchild_add2list;
	/**
	 * 子列表长按弹出菜单，歌曲信息按钮
	 */
	private Button listchild_songinfo;
	/**
	 * 子列表长按弹出菜单，移除列表按钮
	 */
	private Button listchild_remove;
	/**
	 * 子列表长按弹出菜单，取消按钮
	 */
	private Button listchild_cancel;
	// page3
	/**
	 * 当前播放列表显示页面，线性布局
	 */
	private LinearLayout curPlayListPage;
	/**
	 * 当前播放列表布局view
	 */
	private View curList_Layout;
	/**
	 * 当前播放列表显示控件
	 */
	private ListView curListview;
	// page4
	/**
	 * 打开文件显示页面，线性布局
	 */
	private LinearLayout openFilePage;
	private View scanFileView;
	private ListView scanFile_lv;
	private TextView scanfile_filePathTv;
	private Button scanFile_retBnt;
	private Button scanFile_playBnt;
	private Button scanFile_cancelBnt;
	// /////////////////////////////////////////////////////////////////
	// 菜单栏布局相关控件
	// /////////////////////////////////////////////////////////////////
	private PopupWindow myMenu;
	private View menu_Layout;
	private ImageButton menu_musicLrc;
	private ImageButton menu_theme;
	private ImageButton menu_exit;
	private ImageButton menu_refresh;
	private ImageButton menu_timing;
	private ImageButton menu_opinion;
	// /////////////////////////////////////////////////////////////////
	// 歌词修正菜单相关空间
	// /////////////////////////////////////////////////////////////////
	private View LrcCode_Layout;
	private PopupWindow LrcCode_form;
	private Button GBK_bnt;
	private Button UTF8_bnt;
	private Button UTF16_bnt;
	private Button Unicode_bnt;
	private Button LrcCode_cancelbnt;
	// /////////////////////////////////////////////////////////////////
	// 主题选择相关控件
	// /////////////////////////////////////////////////////////////////
	private View theme_Layout;
	private PopupWindow theme_Form;
	private ImageView theme02;
	private ImageView theme03;
	private ImageView theme04;
	private ImageView theme06;
	private ImageView theme08;
	private ImageView theme09;
	private ImageView theme10;
	private ImageView theme11;
	private ImageView theme12;
	// /////////////////////////////////////////////////////////////////
	// 状态栏布局
	// /////////////////////////////////////////////////////////////////
	private RemoteViews statusView;// = new RemoteViews(this.getPackageName(),
	// R.layout.notification_layout);
	// /////////////////////////////////////////////////////////////////
	// 相关值的定义
	// /////////////////////////////////////////////////////////////////
	// 适配器类
	/**
	 * 播放列表集合适配器
	 */
	private exListAdapter mainListAdapter;
	/**
	 * 文件列表适配器
	 */
	private fileAdapter fileListAdapter;
	/**
	 * 当前播放列表播放器
	 */
	private songListAdapter curListAdapter;
	// 列表数据类
	/**
	 * 音乐列表集合
	 */
	List<MediaList<SongInfo>> main_music_list;
	/**
	 * 当前播放列表
	 */
	private List<SongInfo> curPlayList;
	/**
	 * 文件列表
	 */
	private List<myFile> fileList;
	// 相关类
	/**
	 * 当前播放歌曲
	 */
	private SongInfo curSong;
	/**
	 * 声音管理器
	 */
	private AudioManager audioManager;
	/**
	 * 手势识别类
	 */
	private GestureDetector detector;
	/**
	 * 歌词处理类
	 */
	private SongLyric lrc;
	/**
	 * 应用类
	 */
	private RhythmApp app;
	/**
	 * 播放服务类
	 */
	private PlayService playService = null;
	/**
	 * 播放服务连接类
	 */
	private ServiceConnection serviceConnection;
	/**
	 * 数据保存类
	 */
	private saveDataService SDSevice = null;
	/**
	 * 数据保存服务连接类
	 */
	private ServiceConnection SDSconnt;

	protected PlayBinder playBinder;
	protected SDSBinder sdsBinder;
	/**
	 * 通知类
	 */
	private Notification notification;
	/**
	 * 通知管理类
	 */
	private NotificationManager notificationManager;
	private MyReceiver myReceiver;
	// 常量
	private String myAction = "android.intent.rhythmplayer.update";
	private static final int MODE_LOOP = 3;
	private static final int MODE_LOOPALL = 2;
	private static final int MODE_ORDER = 0;
	private static final int MODE_RANDOM = 1;
	private static final int STATE_PAUSE = 0;
	private static final int STATE_PLAY = 1;
	private static final int TIMING = 4;
	private static final int SEARCHLRC = 5;
	private static final int CLOSEDIALOG = 6;
	// 变量&数组
	private int CURR_STATE = 0;
	private int MODE_CURR_STATE;
	private int groupid;
	private int hour1 = 25;
	private int minute1;
	/**
	 * 根据当前模式的索引选择对应图标，数组存储图片的id
	 */
	int[] music_toolbar_mode_state = { R.drawable.ic_mode_order,
			R.drawable.ic_mode_random, R.drawable.ic_mode_loopall,
			R.drawable.ic_mode_loop };
	/**
	 * CURR_STATE=0，暂停是显示三角，CURR_STATE=1，播放时显示两竖
	 */
	int[] music_toolbar_play_state = { R.drawable.ic_bnt_play,
			R.drawable.ic_bnt_pause };
	private boolean menu_isShow = false;
	boolean next_isEnable = false;
	boolean play_isEnable = false;
	boolean pre_isEnable = false;
	boolean ispause = false;
	private boolean isT = false;
	private boolean insearch = false;

	@SuppressWarnings("deprecation")
	private void showNotification(SongInfo song, boolean isplay) {
		Log.i("showNotifi10", "到此");
		Log.i("showNotifi11", "到此");
		int icon = R.drawable.notif;// 图标
		CharSequence name = song.getmFileTitle().subSequence(0,
				song.getmFileTitle().lastIndexOf('.'));// 名字
		notification = new Notification(icon, name, System.currentTimeMillis());
		notification.flags = notification.flags | Notification.FLAG_AUTO_CANCEL;
		statusView = new RemoteViews(RhythmPlayer.this.getPackageName(),
				R.layout.notif_layout);
		statusView.setTextViewText(R.id.notifi_songname, name);
		statusView.setTextViewText(R.id.notifi_singer, song.getmSinger());
		if (isplay) {
			statusView.setImageViewResource(R.id.notifi_play,
					R.drawable.ic_pause);
		} else {
			statusView.setImageViewResource(R.id.notifi_play,
					R.drawable.ic_play);
		}
		notification.contentView = statusView;
		notification.contentIntent = null;
		// /////////////////////////////////////////////////////////////
		// 点击播放
		// /////////////////////////////////////////////////////////////
		Intent i_play = new Intent(myAction);
		i_play.putExtra("playstate", 2); // 暂停
		PendingIntent pi_play = PendingIntent.getBroadcast(RhythmPlayer.this,
				0, i_play, PendingIntent.FLAG_UPDATE_CURRENT);
		notification.contentView.setOnClickPendingIntent(R.id.notifi_play,
				pi_play);
		notification.contentIntent = PendingIntent.getActivity(
				RhythmPlayer.this, 0, new Intent(RhythmPlayer.this,
						RhythmPlayer.class), PendingIntent.FLAG_UPDATE_CURRENT);
		notificationManager.notify(1, notification);
		Log.i("showNotifi12", "到此");
	}

	public class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals(myAction)) {
				switch (intent.getIntExtra("playstate", 0)) {
				case 0: {// 更新播放进度条
					if (playService.getPlayerState()) {// 当初在播放状态时才可以，不然error
						int i = playService.getCurPosition();
						i /= 1000;
						int minute = i / 60;
						int hour = minute / 60;
						int second = i % 60;
						minute %= 60;
						song_curposition_tv.setText(String.format(
								"%02d:%02d:%02d", hour, minute, second));
						playTime_sb.setProgress(playService.getCurPosition());
						// 更新歌词
						LrcView.setTime(playService.getCurPosition());
						LrcView.postInvalidate();
						// Log.i("广播", "1");
						Intent myIntent = new Intent(myAction);
						myIntent.putExtra("playstate", 0);
						sendBroadcast(myIntent);
						// Log.i("广播", "2");
					}
					break;
				}
				case 1: {// 歌曲播放完毕
					playTime_sb.setProgress(0);
					song_curposition_tv.setText("00:00:00");
					curSong = getNextSong(curSong);
					Log.i("Notifi", "下一首");
					play(curSong);// 播放下一首

					break;
				}
				case 2: {
					// 暂停
					playService.pause();
					curSong.setEnable(true);
					Log.i("广播2", "repalay");
					Log.i("notif", "暂停");
					showNotification(curSong, playService.getPlayerState());
					changePlayState();
					CURR_STATE = playService.getPlayerState() ? STATE_PLAY
							: STATE_PAUSE;
					// 改变该按钮的背景
					playTime_sb.setEnabled(true);
					playTime_sb.setMax(playService.getDuration());
					toolbar_play
							.setBackgroundResource(music_toolbar_play_state[CURR_STATE]);
					if (playService.getPlayerState()) {
						Intent myIntent = new Intent(myAction);
						myIntent.putExtra("playstate", 0);
						sendBroadcast(myIntent);
					}
					break;
				}
				case 3: {
					// 歌词搜索完毕
					Log.i("接收广播30", "&&" + SDSevice.getLrc());
					if (!SDSevice.getLrc().isEmpty()) {

						int index = app.getAllMusicList().get(0).getList()
								.indexOf(curSong);
						curSong.setmLrcPath(SDSevice.getLrc());
						Log.i("接收广播34", "&&" + SDSevice.getLrc());
						if (index >= 0
								&& index < app.getAllMusicList().get(0)
										.getList().size()) {
							app.getAllMusicList().get(0).getList()
									.remove(index);
							app.getAllMusicList().get(0).getList()
									.set(index, curSong);
							Log.i("接收广播34", "&&" + SDSevice.getLrc());
							// 显示新歌词
						}
						index = app.getAllMusicList().get(3).getList()
								.indexOf(curSong);
						Log.i("接收广播35", "&&" + SDSevice.getLrc());
						if (index >= 0
								&& index < app.getAllMusicList().get(3)
										.getList().size()) {
							app.getAllMusicList().get(3).getList()
									.remove(index);
							app.getAllMusicList().get(3).getList()
									.set(index, curSong);
							SDSevice.DeleteSong(curSong, app.getAllMusicList()
									.get(3).getListName());
							SDSevice.InsertSong(curSong, app.getAllMusicList()
									.get(3).getListName());
						}
						showLrc(curSong, lrc.getCode());
						Log.i("接收广播3", "&&" + curSong.getLrcPath());
					} else {
						Toast.makeText(getApplicationContext(), "未找到歌词！",
								Toast.LENGTH_LONG).show();
					}
					insearch = false;
				}
				default: {
					break;
				}
				}
			}
		}
	}

	/**
	 * 消息队列处理方法，用于音乐播放器运行时产生的的消息
	 * 
	 * @see {@link MusicPlayer#myHandler}
	 */
	Handler myHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case TIMING: {
				// 计时退出
				/*
				 * 定时判断，是否到时间，实则关闭应用
				 */
				if (isT) {
					if ((hour1 <= Calendar.getInstance().get(Calendar.HOUR) && minute1 == Calendar
							.getInstance().get(Calendar.MINUTE))
							|| (hour1 == Calendar.getInstance().get(
									Calendar.HOUR) && minute1 <= Calendar
									.getInstance().get(Calendar.MINUTE))) {
						RhythmPlayer.this.finish();
					} else {
						sendEmptyMessage(TIMING);
					}
				}
				break;
			}
			case SEARCHLRC: {
				/*
				 * 开启一个服务用于遍历SDcard查找歌词
				 */
				showInfo("开始搜索歌词");
				insearch = true;
				SDSevice.SearchLrc(curSong.getmFileTitle().substring(0,
						curSong.getmFileTitle().indexOf('.')));
				if (isT) {
					myHandler.sendEmptyMessage(TIMING);
				}
				break;
			}
			case CLOSEDIALOG: {
				if (searLrcD != null) {
					if (searLrcD.isShowing()) {
						searLrcD.dismiss();
					}
				}
				if (isT) {
					myHandler.sendEmptyMessage(TIMING);
				}
			}
			}
			super.handleMessage(msg);
		}
	};

	/**
	 * 初始化值
	 */
	@SuppressWarnings("deprecation")
	private void initializeValue() {
		UpdatePlayList();// 初始化各个列表及Listview
		MODE_CURR_STATE = app.getMusicPlayerMode();// 获取记录
		lrc = new SongLyric("");// 初始化歌词类
		// 初始化状态
		if (playService != null) {
			CURR_STATE = playService.getPlayerState() ? STATE_PLAY
					: STATE_PAUSE;
		} else {
			CURR_STATE = STATE_PAUSE; // 处于暂停状态
		}
		/*
		 * 加载初始资源
		 */
		toolbar_mode
				.setBackgroundResource(music_toolbar_mode_state[MODE_CURR_STATE]);
		toolbar_play
				.setBackgroundResource(music_toolbar_play_state[CURR_STATE]);
		titlebar_music.setBackgroundResource(R.drawable.ic_music_checked);
		mainlistview.setIndicatorBounds(getWindowManager().getDefaultDisplay()
				.getWidth() - 40, getWindowManager().getDefaultDisplay()
				.getWidth() - 9);
		if (app.getLastSongIndex() < curPlayList.size()
				&& app.getLastSongIndex() >= 0) {
			curSong = curPlayList.get(app.getLastSongIndex());
			// 显示歌曲的信息，各个按键均可用
			music_Flipper.setDisplayedChild(0); // 显示第一页
		} else if (curPlayList.size() > 0) {
			curSong = curPlayList.get(0);
			// 显示歌曲的信息，各个按键均可用
			music_Flipper.setDisplayedChild(0); // 显示第一页
		} else {
			toolbar_next.setEnabled(false);
			toolbar_play.setEnabled(false);
			toolbar_pre.setEnabled(false);
			playTime_sb.setEnabled(false);
			music_Flipper.setDisplayedChild(3); // 显示第三页
		}
		// 显示文件浏览
		fileList = getFile("/");
		myReceiver = new MyReceiver();
		this.registerReceiver(myReceiver, new IntentFilter(myAction));
		Log.i("initializeValue", "函数结束");
	}

	private void MenuListener() {

		this.menu_musicLrc.setOnClickListener(new View.OnClickListener() {
			public void onClick(View paramView) {
				myMenu.dismiss();
				toolbar_menu.setBackgroundResource(R.drawable.ic_bnt_menu);
				menu_isShow = false;
				if (LrcCode_form.isShowing()) {
					LrcCode_form.dismiss();
					Log.i("menu_showLRC_bnt", "修正歌词");
				} else {
					LrcCode_form.showAtLocation(LrcCode_Layout, 17, 0, 0);
				}
			}
		});

		/**
		 * 菜单窗口的常用菜单下的更换主题选项按钮点击事件
		 * 
		 * @author Streamer
		 */
		menu_theme.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * 需要作出的响应：更换主题
				 */
				myMenu.dismiss();
				theme_Form.showAtLocation(theme_Layout, Gravity.CENTER, 0, 0);
				toolbar_menu.setBackgroundResource(R.drawable.ic_bnt_menu);
				menu_isShow = false;
				Log.i("menu_change_theme", "更换主题");
			}
		});// End

		/**
		 * 菜单窗口的常用菜单下的刷新曲库选项按钮点击事件
		 * 
		 * @author Streamer
		 */
		menu_refresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * 需要作出的响应：刷新曲库
				 */
				myMenu.dismiss();
				toolbar_menu.setBackgroundResource(R.drawable.ic_bnt_menu);
				menu_isShow = false;
				/*
				 * 刷新应用的数据
				 */
				app.refreshLocalMusicInfo(RhythmPlayer.this);
				app.getAllMusicList().get(0).getList().clear();
				app.getAllMusicList().get(0).getList()
						.addAll(app.getLocalMusicInfo());
				// 更新数据库
				SDSevice.UpdateList(app.getAllMusicList().get(0).getList(), app
						.getAllMusicList().get(0).getListName());
				UpdatePlayList();
				Log.i("menu_refresh_list", "刷新曲库");
			}
		});// End

		/**
		 * 菜单窗口的常用菜单下的退出程序选项按钮点击事件
		 * 
		 * @author Streamer
		 */
		menu_exit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * 需要作出的响应：退出播放器，保存数据
				 */
				// Log.i("menu_exit", "退出播放器");
				showExitInfo();
			}
		});// End

		/**
		 * 菜单窗口的设置菜单下的定时设置选项按钮点击事件
		 * 
		 * @author Streamer
		 */
		menu_timing.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * 需要作出的响应：定时设置
				 */
				myMenu.dismiss();
				final TimePicker timePicker = new TimePicker(RhythmPlayer.this);
				timePicker.setCurrentHour(Calendar.getInstance().get(
						Calendar.HOUR));
				timePicker.setCurrentMinute(Calendar.getInstance().get(
						Calendar.MINUTE));
				timePicker.setIs24HourView(true);
				Dialog dialog = new AlertDialog.Builder(RhythmPlayer.this)
						.setTitle("定时退出")
						.setView(timePicker)
						.setNegativeButton("取消计时",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										isT = false;
										dialog.dismiss();
									}
								})
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										hour1 = timePicker.getCurrentHour();
										minute1 = timePicker.getCurrentMinute();
										showInfo("播放器将于" + hour1 + ":"
												+ minute1 + "退出。");
										dialog.dismiss();
										// 发送消息
										isT = true;
										myHandler.sendEmptyMessage(TIMING);
									}
								}).create();
				dialog.show();
				toolbar_menu.setBackgroundResource(R.drawable.ic_bnt_menu);
				menu_isShow = false;
				Log.i("menu_timing", "定时设置");
			}
		});

		/**
		 * 菜单窗口的常用帮助下的意见反馈选项按钮点击事件
		 * 
		 * @author Streamer
		 */
		menu_opinion.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * 需要作出的响应：弹出回馈窗口，E_mail发送窗口
				 */
				myMenu.dismiss();
				toolbar_menu.setBackgroundResource(R.drawable.ic_bnt_menu);
				menu_isShow = false;

				final EditText editopinion = new EditText(RhythmPlayer.this);
				editopinion.setHeight(200);
				editopinion.setWidth(getWindowManager().getDefaultDisplay()
						.getWidth() - 60);
				editopinion.setGravity(Gravity.LEFT);
				Dialog dialog = new AlertDialog.Builder(RhythmPlayer.this)
						.setTitle("意见反馈")
						.setView(editopinion)
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										dialog.dismiss();
									}
								})
						.setPositiveButton("发送",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										SmsManager
												.getDefault()
												.sendTextMessage(
														"18061608810",
														null,
														"反馈意见："
																+ editopinion
																		.getText()
																		.toString(),
														null, null);
										showInfo("意见反馈成功！您的鼓励，我们的动力。");
										dialog.dismiss();
									}
								}).create();
				dialog.show();
				Log.i("menu_opinion_return", "用户意见回馈");
			}
		});// End

	}

	private void changeBgListener() {
		/**
		 * 划屏切换页面用户触碰监听事件处理,将事件交由手势识别类处理
		 * 
		 * @author Streamer
		 */
		music_Flipper.setOnTouchListener(new OnTouchListener() {

			@SuppressLint("UnlocalizedSms")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				// 交由手势识别类
				return detector.onTouchEvent(event);
			}
		});// End

		/**
		 * 手势识别类的实例化，用于处理用户操作屏幕事件
		 * 
		 * @author Streamer
		 */
		Log.i("main", "到此");
		detector = new GestureDetector(RhythmPlayer.this,
				new OnGestureListener() {

					@Override
					public boolean onSingleTapUp(MotionEvent e) {
						// TODO Auto-generated method stub
						// 用户轻触屏幕后松开。
						return false;
					}

					@Override
					public void onShowPress(MotionEvent e) {
						// TODO Auto-generated method stub
						// 用户轻触屏幕，尚末松开或拖动，注意，强调的是没有松开或者拖动状态
					}

					@Override
					public boolean onScroll(MotionEvent e1, MotionEvent e2,
							float distanceX, float distanceY) {
						// TODO Auto-generated method stub
						// 用户按下屏幕并拖动

						if (music_Flipper.getCurrentView().getId() == R.id.page1) {
							int y = (int) (e1.getY() - e2.getY());
							if (y > 20 && CURR_STATE == STATE_PLAY
									&& playService != null) {
								// 用户往上滑
								Log.i("往上滑", "" + y);

								int d = playService.getCurPosition() + 40 * y;
								if (d < playService.getDuration()) {
									playService.gotoTime(d);
									playTime_sb.setProgress(d);
								} else {
									playTime_sb.setProgress(playService
											.getDuration() - 10);
									playService.gotoTime(playService
											.getDuration() - 10);
								}
							}
							if (y < -20 && CURR_STATE == STATE_PLAY
									&& playService != null) {
								// 用户往下滑
								Log.i("往上滑", "" + y);

								// 此时y为负数
								int d = playService.getCurPosition() + 40 * y;
								if (d > 10) {
									playTime_sb.setProgress(d);
									playService.gotoTime(d);
								} else {
									playTime_sb.setProgress(10);
									playService.gotoTime(10);
								}

							}
						}

						return false;
					}

					@Override
					public void onLongPress(MotionEvent e) {
						// TODO Auto-generated method stub
						// 用户长按屏幕
					}

					@Override
					public boolean onFling(MotionEvent e1, MotionEvent e2,
							float velocityX, float velocityY) {
						// TODO Auto-generated method stub

						// 用户按下屏幕，快速移动后松开（就是在屏幕上滑动）
						// e1:第一个ACTION_DOWN事件（手指按下的那一点）
						// e2:最后一个ACTION_MOVE事件 （手指松开的那一点）
						// velocityX:手指在x轴移动的速度 单位：像素/秒
						// velocityY:手指在y轴移动的速度 单位：像素/秒

						/*
						 * 用户滑动屏幕是发生此事件。 需要做出的响应：当用户滑动屏幕幅度达到一定程度是，根据用户的滑动方
						 * 向切换到相应页面，并改变其他按钮的背景图片；如果菜单处于显示状态同
						 * 时将菜单窗口关闭；当音乐播放器处于播放状态时划屏失效，即不做响应。
						 */

						float x = (int) (e2.getX() - e1.getX());
						// 注意设置判断滑动合适相应：进度条未显示时 即为 musicplayer出入未播放状态时
						// 方法参照 videoplayer的相应方法
						if (x > 200) {
							music_Flipper.setInAnimation(RhythmPlayer.this,
									R.anim.push_right_in);
							music_Flipper.setOutAnimation(RhythmPlayer.this,
									R.anim.push_right_out);
							music_Flipper.showPrevious();
						}
						if (x < -200) {
							music_Flipper.setInAnimation(RhythmPlayer.this,
									R.anim.push_left_in);
							music_Flipper.setOutAnimation(RhythmPlayer.this,
									R.anim.push_left_out);
							music_Flipper.showNext();
						}
						// change titlebar every button image
						change_titlebar_bnt_bg();
						if (myMenu.isShowing()) {
							myMenu.dismiss();
							toolbar_menu
									.setBackgroundResource(R.drawable.ic_bnt_menu);
							menu_isShow = false;
						}

						return false;
					}

					@Override
					public boolean onDown(MotionEvent e) {
						// TODO Auto-generated method stub
						// 用户轻触屏幕，单击

						if (myMenu.isShowing()) {
							myMenu.dismiss();
							toolbar_menu
									.setBackgroundResource(R.drawable.ic_bnt_menu);
						}
						if (LrcCode_form.isShowing())
							LrcCode_form.dismiss();
						if (group_LongPress_Form.isShowing())
							group_LongPress_Form.dismiss();
						if (chileLongPress_Form.isShowing())
							chileLongPress_Form.dismiss();
						if (theme_Form.isShowing())
							theme_Form.dismiss();

						return false;
					}
				});
	}

	private void titleBarListener() {
		/**
		 * 音乐播放器标题栏的音乐播放主界面选择按钮点击事件处理
		 * 
		 * @author Streamer
		 */
		titlebar_music.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * 事件处理：将显示界面切换到音乐播放的主界面，并改变所 有的音乐播放器标题栏的选项按钮的背景图片
				 */
				if (music_Flipper.getCurrentView().getId() == R.id.page1) {
					// 如果是正确的页面，不作处理
				} else {
					// 如果不是处在正确的页面，切换页面
					music_Flipper.setDisplayedChild(0);
				}
				// 改变按钮背景图片
				change_titlebar_bnt_bg();
			}
		});// End

		/**
		 * 音乐播放器标题栏的音乐播放器所有列表显示界面选择按钮点击事件，即 界面二选择按钮点击事件
		 * 
		 * @author Streamer
		 */
		titlebar_list.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * 事件处理：将显示界面切换到音乐播放的所有音乐列表显示界面， 并改变所有的音乐播放器标题栏的选项按钮的背景图片
				 */
				if (music_Flipper.getCurrentView().getId() == R.id.musicpage_mainListview) {
					// 如果是正确的页面，不作处理
					music_Flipper.setDisplayedChild(3);
				} else {
					// 如果不是处在正确的页面，切换页面
					music_Flipper.setDisplayedChild(1);
				}
				// 改变按钮背景图片
				change_titlebar_bnt_bg();
			}
		});// End

		titlebar_list.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (music_Flipper.getCurrentView().getId() == R.id.musicpage_mainListview) {
					music_Flipper.setDisplayedChild(2);// 显示当前播放列表
				} else {
					music_Flipper.setDisplayedChild(1);
				}
				change_titlebar_bnt_bg();
			}
		});
		titlebar_openDir.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (music_Flipper.getCurrentView().getId() != R.id.openFilePage) {
					music_Flipper.setDisplayedChild(3);
					change_titlebar_bnt_bg();
				}
			}
		});
		/**
		 * 音乐播放器标题栏的音乐播放器声音添加选项按钮点击事件
		 * 
		 * @author Streamer
		 */
		titlebar_voice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * 事件处理：当媒体音量可调的时候弹出音乐音量调节窗口，或关闭窗口 以及记录音量调节窗口的状态
				 */
				audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
						AudioManager.ADJUST_SAME, AudioManager.FLAG_PLAY_SOUND
								| AudioManager.FLAG_SHOW_UI);
			}
		});// End

		title_more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (more_Form.isShowing()) {
					more_Form.dismiss();
					title_more.setBackgroundResource(R.drawable.more);
				} else {
					more_Form.showAsDropDown(title_more, 0, 30);
					title_more.setBackgroundResource(R.drawable.more_checked);
				}
			}
		});

		more_aboutus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 显示相关信息
				more_Form.dismiss();
				Toast showus = new Toast(RhythmPlayer.this);
				showus.setView(RhythmPlayer.this.getLayoutInflater().inflate(
						R.layout.aboutus_layout, null));
				showus.setGravity(Gravity.CENTER, 0, 0);
				showus.setDuration(Toast.LENGTH_LONG);
				showus.show();
			}
		});

		more_going.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				more_Form.dismiss();
				Toast showus = new Toast(RhythmPlayer.this);
				showus.setView(RhythmPlayer.this.getLayoutInflater().inflate(
						R.layout.appgoing_ayout, null));
				showus.setGravity(Gravity.CENTER, 0, 0);
				showus.setDuration(Toast.LENGTH_LONG);
				showus.show();
			}
		});

	}

	private void toolBarListener() {
		/**
		 * 音乐播放器工具栏的播放模式选择按钮点击事件
		 * 
		 * @author Streamer
		 */
		toolbar_mode.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * 需要做出的响应：依次改变的前的播放模式并显示对应的模式图标； 改变一些控件的可用状态
				 */
				MODE_CURR_STATE++;
				if (MODE_CURR_STATE == 4)
					MODE_CURR_STATE = MODE_ORDER;
				toolbar_mode
						.setBackgroundResource(music_toolbar_mode_state[MODE_CURR_STATE]);
				if (MODE_CURR_STATE == MODE_RANDOM) {
					toolbar_next.setEnabled(true);
					toolbar_pre.setEnabled(true);
				}
				if (MODE_CURR_STATE == MODE_LOOPALL)
					toolbar_next.setEnabled(true);
			}
		});// End

		/**
		 * 音乐播放器工具栏的上一首选择按钮点击事件
		 * 
		 * @author Streamer
		 */
		toolbar_pre.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * 需要作出的响应：根据当前的模式获取下一首播放曲目，并播放
				 */
				// 获取前一首曲目信息并替换当前曲目
				Log.i("Play", curSong.getmFileTitle() + "+++++++11"
						+ curPlayList.size());
				// 获取写一首曲目信息并替换当前曲目
				int state = MODE_CURR_STATE;
				if (MODE_CURR_STATE == MODE_LOOP) {
					// 当处于循环状态时，getPreSong将获取原来的歌曲，及获取不成功
					MODE_CURR_STATE = MODE_ORDER;
				}
				curSong = getPreSong(curSong);
				MODE_CURR_STATE = state;
				if (playService == null) {
					Log.i("Play", curSong.getmFileTitle() + "+++++++12"
							+ curPlayList.size());
				}
				play(curSong); // 播放歌曲
				Log.i("Play", curSong.getmFileTitle() + "+++++++13"
						+ curPlayList.size());
				changePlayState();
			}
		});// End

		/**
		 * 音乐播放器工具栏的播放/暂停选择按钮点击事件
		 * 
		 * @author Streamer
		 */
		toolbar_play.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * 需要作出的响应：如果处于播放状态，则暂停，否则播放，并记录播放器的播放状态
				 */
				lrc.setMaxTime(playService.getDuration());

				song_Name_tv.setText("当前播放 :"
						+ curSong.getmFileTitle().substring(0,
								curSong.getmFileTitle().indexOf('.')));
				int i = playService.getDuration();
				i /= 1000;
				int minute = i / 60;
				int hour = minute / 60;
				int second = i % 60;
				minute %= 60;
				sond_duration_tv.setText(String.format("%02d:%02d:%02d", hour,
						minute, second));
				playTime_sb.setMax(playService.getDuration());
				curSong.setEnable(true);
				playService.pause();
				// 发送通知
				showNotification(curSong, playService.getPlayerState());
				CURR_STATE = playService.getPlayerState() ? STATE_PLAY
						: STATE_PAUSE;
				// 改变该按钮的背景
				playTime_sb.setEnabled(true);
				toolbar_play
						.setBackgroundResource(music_toolbar_play_state[CURR_STATE]);
			}
		});// End

		/**
		 * 音乐播放器工具栏的下一曲选项按钮点击事件
		 * 
		 * @author Streamer
		 */
		toolbar_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * 需要作出的响应：根据当前的模式获取下一首播放曲目，并播放
				 */
				// 获取写一首曲目信息并替换当前曲目
				int state = MODE_CURR_STATE;
				if (MODE_CURR_STATE == MODE_LOOP) {
					// 当处于循环状态时，getNextSong将获取原来的歌曲，及获取不成功
					MODE_CURR_STATE = MODE_ORDER;
				}
				Log.i("Play", curSong.getmFileTitle() + "+++++++21"
						+ curPlayList.size());

				curSong = getNextSong(curSong);
				MODE_CURR_STATE = state;
				Log.i("Play", curSong.getmFileTitle() + "+++++++22"
						+ curPlayList.size());
				play(curSong);
				Log.i("Play", curSong.getmFileTitle() + "+++++++23"
						+ curPlayList.size());
				changePlayState();
			}
		});// End

		/**
		 * 音乐播放器工具栏的菜单显示选择按钮点击事件
		 * 
		 * @author Streamer
		 */
		toolbar_menu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * 需要作出的响应：如果菜单处于显示状态则将其关闭，否则打开，并记录菜单的显示状态
				 */
				if (menu_isShow) {
					// 菜单处于显示状态,关闭菜单窗口
					myMenu.dismiss();
					// 改变按钮背景
					toolbar_menu.setBackgroundResource(R.drawable.ic_bnt_menu);
					// 记录状态
					menu_isShow = false;
				} else {
					// 菜单处于关闭状态，显示菜单
					myMenu.showAtLocation(toolbar_menu, 80, 0, 80);
					toolbar_menu
							.setBackgroundResource(R.drawable.ic_menu_checked);
					// 记录状态
					menu_isShow = true;
				}
			}
		});// End
	}

	private void LrcCodeListener() {
		GBK_bnt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				lrc.setLrc(
						curSong.getmFilePath().substring(0,
								curSong.getmFilePath().indexOf('.'))
								+ ".lrc", "GBK");
				lrc.setMaxTime(playService.getDuration());
				LrcView.setLyric(lrc);
				LrcView.reset();
				LrcView.setTime(0);
				LrcView.postInvalidate();
				LrcCode_form.dismiss();
			}
		});// End

		UTF8_bnt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				lrc.setLrc(
						curSong.getmFilePath().substring(0,
								curSong.getmFilePath().indexOf('.'))
								+ ".lrc", "UTF-8");
				lrc.setMaxTime(playService.getDuration());
				LrcView.setLyric(lrc);
				LrcView.reset();
				LrcView.setTime(0);
				LrcView.postInvalidate();
				LrcCode_form.dismiss();
			}
		});// End

		UTF16_bnt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				lrc.setLrc(
						curSong.getmFilePath().substring(0,
								curSong.getmFilePath().indexOf('.'))
								+ ".lrc", "UTF-16");
				lrc.setMaxTime(playService.getDuration());
				LrcView.setLyric(lrc);
				LrcView.reset();
				LrcView.setTime(0);
				LrcView.postInvalidate();
				LrcCode_form.dismiss();
			}
		});// End

		Unicode_bnt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				lrc.setLrc(
						curSong.getmFilePath().substring(0,
								curSong.getmFilePath().indexOf('.'))
								+ ".lrc", "Unicode");
				lrc.setMaxTime(playService.getDuration());
				LrcView.setLyric(lrc);
				LrcView.reset();
				LrcView.setTime(0);
				LrcView.postInvalidate();
				LrcCode_form.dismiss();
			}
		});// End

		LrcCode_cancelbnt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * 关闭编码选择窗口
				 */
				LrcCode_form.dismiss();
			}
		});// End
	}

	private void ThemeListener() {
		theme02.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				theme_Form.dismiss();
				app.setMusicPlayer_bgID(R.drawable.bg1);
				showThemeView.setBackgroundResource(R.drawable.bg1);
			}
		});
		theme03.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				theme_Form.dismiss();
				app.setMusicPlayer_bgID(R.drawable.bg2);
				showThemeView.setBackgroundResource(R.drawable.bg2);
			}
		});
		theme04.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				theme_Form.dismiss();
				app.setMusicPlayer_bgID(R.drawable.bg3);
				showThemeView.setBackgroundResource(R.drawable.bg3);
			}
		});
		theme06.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				theme_Form.dismiss();
				app.setMusicPlayer_bgID(R.drawable.ic_bg1);
				showThemeView.setBackgroundResource(R.drawable.ic_bg1);
			}
		});
		theme08.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				theme_Form.dismiss();
				app.setMusicPlayer_bgID(R.drawable.ic_bg3);
				showThemeView.setBackgroundResource(R.drawable.ic_bg3);
			}
		});
		theme09.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				theme_Form.dismiss();
				app.setMusicPlayer_bgID(R.drawable.ic_bg4);
				showThemeView.setBackgroundResource(R.drawable.ic_bg4);
			}
		});
		theme10.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				theme_Form.dismiss();
				app.setMusicPlayer_bgID(R.drawable.ic_bg5);
				showThemeView.setBackgroundResource(R.drawable.ic_bg5);
			}
		});
		theme12.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				theme_Form.dismiss();
				app.setMusicPlayer_bgID(R.drawable.ic_bg7);
				showThemeView.setBackgroundResource(R.drawable.ic_bg7);
			}
		});
	}

	private void ScanFileListener() {
		/**
		 * 文件浏览，点击文件事件
		 * 
		 * @author Streamer
		 */
		scanFile_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (fileList.size() > arg2 && arg2 >= 0) {
					File file = new File(fileList.get(arg2).getFilePath());
					if (file.canRead()) {
						if (file.isDirectory()) {
							/* 如果是文件夹就再进去读取 */
							fileList = getFile(fileList.get(arg2).getFilePath());
						} else {
							/* 如果是文件 */
							scanfile_filePathTv.setText(fileList.get(arg2)
									.getFilePath());
							if (file.getPath().endsWith(".mp3")
									|| file.getPath().endsWith(".MP3")) {
								scanFile_playBnt.setEnabled(true);
							} else {
								scanFile_playBnt.setEnabled(false);
							}
						}
					} else {
						/* 弹出AlertDialog显示权限不足 */
						showInfo("没有权限！");
					}
				}
			}
		});// End

		/**
		 * 文件浏览，返回键点击事件
		 * 
		 * @author Streamer
		 */
		scanFile_retBnt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!scanfile_filePathTv.getText().toString().equals("/")) {
					fileList = getFile(new File(scanfile_filePathTv.getText()
							.toString()).getParent());
				} else {
					music_Flipper.setDisplayedChild(0);
					change_titlebar_bnt_bg();
				}
			}
		});

		/**
		 * 文件浏览，播放键点击事件
		 * 
		 * @author Streamer
		 */
		scanFile_playBnt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				File file = new File(scanfile_filePathTv.getText().toString());
				curSong = new SongInfo();
				curSong.setmSinger("<Unkown>");
				curSong.setmFileTitle(file.getName());
				curSong.setmFilePath(file.getPath());
				curSong.setmFileName(file.getName());
				curSong.setmLrcPath(file.getPath().substring(0,
						file.getPath().lastIndexOf('.'))
						+ ".lrc");
				app.getAllMusicList().get(1).getList().add(curSong);
				// 更新数据库对应的列表
				SDSevice.InsertSong(curSong, app.getAllMusicList().get(1)
						.getListName());
				UpdatePlayList();
				play(curSong);
				music_Flipper.setDisplayedChild(0);
				change_titlebar_bnt_bg();
				toolbar_play.setEnabled(true);
			}
		});

		/**
		 * 文件浏览的取消按钮点击事件
		 * 
		 * @author Streamer
		 */
		scanFile_cancelBnt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				music_Flipper.setDisplayedChild(0);
				change_titlebar_bnt_bg();
			}
		});// End
	}

	private void ListListener() {
		/**
		 * 当前播放列表项目点击事件
		 * 
		 * @author Streamer
		 */
		curListview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				/*
				 * 需要作出的响应：播放选择的曲目，并作图标转换，状态记录
				 */
				if (arg2 < curPlayList.size()) {
					Log.i("播放曲目路径", curPlayList.get(arg2).getmFilePath());
					curSong = curPlayList.get(arg2);
					play(curSong);
					CURR_STATE = STATE_PLAY;
					music_Flipper.setDisplayedChild(0);
					change_titlebar_bnt_bg();
					toolbar_play
							.setBackgroundResource(music_toolbar_play_state[CURR_STATE]);
				} else {
					showInfo("播放失败！");
				}
			}
		});// End

		/**
		 * 监听可扩展列表的长按事件,通过判断view的id可以判断是长按组还是长按子
		 * 
		 * @author Streamer
		 */
		mainlistview.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				final int index = arg2;
				if (arg1.getId() == R.id.music_listgroup) {
					// 长按组
					if (arg2 != main_music_list.size() - 1) {
						/*
						 * 初始化一些功能的可用状态
						 */
						if (arg2 < 4) {
							/*
							 * 系统预设列表不可变更
							 */
							listgroup_altername.setEnabled(false);
							listgroup_del.setEnabled(false);
						} else {
							listgroup_altername.setEnabled(true);
							listgroup_del.setEnabled(true);
						}
						if (arg2 == 1
								|| main_music_list.get(index).getList().size() == 0) {
							// 默认列表不可清除
							listgroup_clear.setEnabled(false);
						} else {
							listgroup_clear.setEnabled(true);
						}
						if (main_music_list.get(index).getList().size() == 0) {
							listgroup_add2list.setEnabled(false);
						}
						if (index != 1) {
							// 非默认列表
							group_LongPress_Form.showAsDropDown(arg1, 20, 10);
						}
					}
					/**
					 * 长按媒体列表集合事件，播放该列表
					 * 
					 * @author Streamer
					 */
					listgroup_add2list
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									group_LongPress_Form.dismiss();
									app.getAllMusicList().get(1).getList()
											.clear();
									// 添加到默认列表
									app.getAllMusicList()
											.get(1)
											.addAll(main_music_list.get(index)
													.getList());
									// 更新数据库
									SDSevice.UpdateList(app.getAllMusicList()
											.get(index).getList(), app
											.getAllMusicList().get(1)
											.getListName());
									UpdatePlayList();
									updatePlayBntEable();
								}
							});
					/**
					 * 长按媒体列表集合事件，更改列表名称
					 * 
					 * @author Streamer
					 */
					listgroup_altername
							.setOnClickListener(new OnClickListener() {

								@SuppressWarnings("deprecation")
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									group_LongPress_Form.dismiss();
									final EditText editnewlist = new EditText(
											RhythmPlayer.this);
									editnewlist.setHeight(70);
									editnewlist
											.setWidth(getWindowManager()
													.getDefaultDisplay()
													.getWidth() - 60);
									editnewlist.setGravity(Gravity.LEFT);
									Dialog dialog = new AlertDialog.Builder(
											RhythmPlayer.this)
											.setTitle("新列表名")
											.setView(editnewlist)
											.setNegativeButton(
													"取消",
													new DialogInterface.OnClickListener() {

														@Override
														public void onClick(
																DialogInterface dialog,
																int which) {
															// TODO
															// Auto-generated
															// method stub
															dialog.dismiss();
														}
													})
											.setPositiveButton(
													"更改",
													new DialogInterface.OnClickListener() {

														@Override
														public void onClick(
																DialogInterface dialog,
																int which) {
															// TODO
															// Auto-generated
															// method stub
															// 删除旧数据
															SDSevice.clearList(app
																	.getAllMusicList()
																	.get(index)
																	.getListName());
															app.getAllMusicList()
																	.get(index)
																	.setListName(
																			editnewlist
																					.getText()
																					.toString());
															// 添加新数据
															SDSevice.UpdateList(
																	app.getAllMusicList()
																			.get(index)
																			.getList(),
																	editnewlist
																			.getText()
																			.toString());
															UpdatePlayList();
															dialog.dismiss();
														}
													}).create();
									dialog.show();
								}
							});
					/**
					 * 长按媒体列表集合事件，删除列表
					 * 
					 * @author Streamer
					 */
					listgroup_del.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							app.getAllMusicList().remove(index);
							// 更新数据库
							SDSevice.clearList(app.getAllMusicList().get(index)
									.getListName());
							group_LongPress_Form.dismiss();
							UpdatePlayList();
						}
					});
					/**
					 * 长按媒体列表集合事件，清除该列表
					 * 
					 * @author Streamer
					 */
					listgroup_clear.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							app.getAllMusicList().get(index).getList().clear();
							// 更新数据库
							SDSevice.clearList(app.getAllMusicList().get(index)
									.getListName());
							UpdatePlayList();
							group_LongPress_Form.dismiss();
						}
					});
					/**
					 * 长按媒体列表集合事件，取消操作
					 * 
					 * @author Streamer
					 */
					listgroup_cancel.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							group_LongPress_Form.dismiss();
						}
					});

				} else if (arg1.getId() == R.id.music_listchild) {
					// 长按子
					chileLongPress_Form.showAsDropDown(arg1, 20, 10);
					/**
					 * 长按列表集合中的子项的添加到列表功能
					 * 
					 * @author Streamer
					 */
					listchild_add2list
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									String[] listsname = new String[main_music_list
											.size() - 1];
									for (int i = 0; i < main_music_list.size() - 1; i++) {
										listsname[i] = main_music_list.get(i)
												.getListName();
									}
									Dialog dialog = new AlertDialog.Builder(
											RhythmPlayer.this)
											.setTitle("添加到")
											.setItems(
													listsname,
													new DialogInterface.OnClickListener() {

														@Override
														public void onClick(
																DialogInterface dialog,
																int which) {
															// TODO
															// Auto-generated
															// method stub
															main_music_list
																	.get(which)
																	.getList()
																	.add((SongInfo) mainlistview
																			.getItemAtPosition(index));

															// 更新数据库
															SDSevice.InsertSong(
																	(SongInfo) mainlistview
																			.getItemAtPosition(index),
																	main_music_list
																			.get(which)
																			.getListName());

														}
													})
											.setNegativeButton(
													"取消",
													new DialogInterface.OnClickListener() {

														@Override
														public void onClick(
																DialogInterface dialog,
																int which) {
															// TODO
															// Auto-generated
															// method stub
															dialog.dismiss();
														}
													}).create();
									chileLongPress_Form.dismiss();
									dialog.show();
								}
							});

					/**
					 * 取消操作
					 * 
					 * @author Streamer
					 */
					listchild_cancel.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							chileLongPress_Form.dismiss();

						}
					});// End
					/**
					 * 长按列表集合中的子项的移除列表功能
					 * 
					 * @author Streamer
					 */
					listchild_remove.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Log.i("remove", "&&" + groupid + "**" + index);
							int dex = index - groupid - 1;
							if ((dex >= 0)
									&& dex < app.getAllMusicList().get(groupid)
											.getList().size()) {
								// 更新数据库
								SDSevice.DeleteSong(
										app.getAllMusicList().get(groupid)
												.getList()
												.get(index - groupid - 1), app
												.getAllMusicList().get(groupid)
												.getListName());
								app.getAllMusicList().get(groupid).getList()
										.remove(index - groupid - 1);
								Log.i("remove", "&&" + groupid + "**" + index
										+ "成功");

								UpdatePlayList();
							}
							chileLongPress_Form.dismiss();
						}
					});// End
					/**
					 * 长按列表集合中的子项显示媒体信息功能
					 * 
					 * @author Streamer
					 */
					listchild_songinfo
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									chileLongPress_Form.dismiss();
									final SongInfo s = (SongInfo) mainlistview
											.getItemAtPosition(index);
									Dialog dialog = new AlertDialog.Builder(
											RhythmPlayer.this)
											.setTitle("歌曲信息")
											.setItems(
													new String[] {
															"文件名："
																	+ s.getmFileTitle(),
															"专辑名："
																	+ s.getmAlbum(),
															"类   型："
																	+ s.getmFileType(),
															"歌   手："
																	+ s.getmSinger(),
															"时   长："
																	+ s.getmDuration()
																	+ "ms",
															"大   小："
																	+ s.getmFileSize()
																	+ "byte",
															"年 份："
																	+ s.getmYear()
																	+ "年",
															"路 径："
																	+ s.getmFilePath() },
													null)
											.setPositiveButton(
													"确定",
													new DialogInterface.OnClickListener() {

														@Override
														public void onClick(
																DialogInterface dialog,
																int which) {
															// TODO
															// Auto-generated
															// method stub
															dialog.dismiss();
														}
													}).create();
									dialog.show();
								}
							});// End
				}
				/*
				 * 返回true，不给系统传递按键信息，防止与click冲突
				 */
				return true;
			}
		});// End

		/**
		 * 之展开一个组，关闭其他展开的组
		 * 
		 * @author Streamer
		 */
		mainlistview.setOnGroupExpandListener(new OnGroupExpandListener() {

			@Override
			public void onGroupExpand(int groupPosition) {
				// TODO Auto-generated method stub
				groupid = groupPosition;
				for (int i = 0; i < main_music_list.size(); i++) {
					if (i != groupPosition) {
						mainlistview.collapseGroup(i);
					}
				}
			}
		});// End
		/**
		 * 音乐列表集合的展开列表中的视频点击事件处理 播放点击的音乐频，将页面切换到播放页面，改变 响应控件图标
		 * 
		 * @author Streamer
		 */
		mainlistview.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				Log.i("播放曲目路径", main_music_list.get(groupPosition).getList()
						.get(childPosition).getmFilePath());
				/*
				 * 获取点击曲目
				 */
				curSong = main_music_list.get(groupPosition).getList()
						.get(childPosition);
				if (groupPosition != 1) {// 不是默认列表时
					// 添加到默认列表
					app.getAllMusicList().get(1).getList().clear();
					// 添加到默认列表
					app.getAllMusicList()
							.get(1)
							.addAll(main_music_list.get(groupPosition)
									.getList());
					// 更新数据库对应的列表
					SDSevice.UpdateList(app.getAllMusicList().get(1).getList(),
							app.getAllMusicList().get(1).getListName());
					UpdatePlayList();
				}
				play(curSong);
				music_Flipper.setDisplayedChild(0);
				change_titlebar_bnt_bg();
				return false;
			}
		});

		/**
		 * 所有音乐列表的组项目点击事件
		 * 
		 * @author Streamer
		 */
		mainlistview.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				// TODO Auto-generated method stub
				/*
				 * 需要作出的响应：展开该音乐列表
				 */

				if (groupPosition == main_music_list.size() - 1) {
					/*
					 * 点击的是新建列表
					 */
					final EditText editnewlist = new EditText(RhythmPlayer.this);
					editnewlist.setHeight(70);
					editnewlist.setWidth(getWindowManager().getDefaultDisplay()
							.getWidth() - 60);
					editnewlist.setGravity(Gravity.LEFT);
					Dialog dialog = new AlertDialog.Builder(RhythmPlayer.this)
							.setTitle("新建列表名")
							.setView(editnewlist)
							.setNegativeButton("取消",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub
											dialog.dismiss();
										}
									})
							.setPositiveButton("新建",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub
											if (editnewlist.getText()
													.toString().isEmpty()) {
												showInfo("列表建立失败！列表名不能为空。");
											} else {
												MediaList<SongInfo> newlist = new MediaList<SongInfo>(
														editnewlist.getText()
																.toString(),
														0,
														new ArrayList<SongInfo>());
												if (app.newMusicListByListName(newlist)) {
													showInfo("列表建立成功！");
													Toast.makeText(
															RhythmPlayer.this,
															"请往"
																	+ editnewlist
																			.getText()
																			.toString()
																	+ "列表添加数据。否则该列表无法保存",
															Toast.LENGTH_LONG);
													// 更新列表,可以去除，因为无法加入
													SDSevice.UpdateList(
															new ArrayList<SongInfo>(),
															editnewlist
																	.getText()
																	.toString());
													UpdatePlayList();
												} else {
													showInfo("列表建立失败！");
												}
											}
											dialog.dismiss();
										}
									}).create();
					dialog.show();
					return true;
				}

				// Log.i("你点击了",main_music_list.get(groupPosition).getListName());
				return false; // 返回true时下级菜单不显示
			}
		});
	}

	private void SeekBarListener() {
		/**
		 * 进度条改变事件
		 * 
		 * @author Streamer
		 */
		playTime_sb
				.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
					public void onProgressChanged(SeekBar seekbar,
							int progress, boolean fromUser) {
						// TODO Auto-generated method stub
						/*
						 * 用户改变进度时 需要作出的响应：定位播放
						 */
						if (fromUser) {
							if (playService.getPlayerState()) {
								/*
								 * 当用户滑动进度条时，只有当前处在播放状态才会响应
								 */
								playService.gotoTime(progress);
								Log.i("seekbar", "进入");
							}
						}
					}

					public void onStartTrackingTouch(SeekBar arg0) {
						// TODO Auto-generated method stub
						// 用户开始触碰
					}

					public void onStopTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub
						// 用户结束触碰
					}
				});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 播放器的主布局View
		/*
		 * 初始化系统服务
		 */
		initialApp();
		showThemeView = getLayoutInflater().inflate(
				R.layout.activity_rhythm_player, null);
		/*
		 * 将窗口设置为无标题
		 */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(showThemeView);
		/*
		 * 设置主布局的背景
		 */
		showThemeView.setBackgroundResource(app.getMusicPlayer_bgID());
		initializeLayout();
		initializeValue();
		showLrc(curSong, lrc.getCode());
		changeBgListener();
		titleBarListener();
		toolBarListener();
		MenuListener();
		LrcCodeListener();
		ThemeListener();
		ScanFileListener();
		ListListener();
		SeekBarListener();
		LrcView.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				if (!lrc.isValid()) {
					if (insearch) {
						showInfo("正在搜索本地歌词......");
					} else {
						searLrcD = new AlertDialog.Builder(RhythmPlayer.this)
								.setIcon(R.drawable.b15)
								.setTitle("搜索本地歌词")
								.setMessage("您确定要搜索本地歌词？")
								.setPositiveButton("确定",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												// TODO Auto-generated method
												// stub
												myHandler
														.sendEmptyMessage(CLOSEDIALOG);
												myHandler
														.sendEmptyMessage(SEARCHLRC);
											}
										})
								.setNegativeButton("取消",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												// TODO Auto-generated method
												// stub
												myHandler
														.sendEmptyMessage(CLOSEDIALOG);
											}
										}).create();
						searLrcD.show();
					}
				}
				return false;
			}
		});

	}

	private void showLrc(SongInfo song, String Code) {
		// TODO Auto-generated method stub
		if (!song.getmFilePath().isEmpty()) {
			lrc.setLrc(song.getLrcPath(), Code);
			LrcView.setLyric(lrc);
			LrcView.reset();
			LrcView.setTime(0);
			LrcView.postInvalidate();
		}
	}

	/**
	 * 初始化应用和相关的系统服务
	 */
	private void initialApp() {
		// 注意要首先初始化app
		app = (RhythmApp) getApplication();
		initializeSDS();// 初始化数据保存服务
		initializePlayer();// 初始化播放器
		notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
	}

	protected boolean initializeSDS() {
		boolean flag = false;
		SDSconnt = new ServiceConnection() {

			@Override
			public void onServiceDisconnected(ComponentName name) {
				// TODO Auto-generated method stub
				Log.i("SDS", "连接失败");
				SDSevice = null;
			}

			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				// TODO Auto-generated method stub
				Log.i("SDS", "连接成功");
				// 获取数据保存服务
				sdsBinder = (SDSBinder) service;
				SDSevice = sdsBinder.getSDS();
			}
		};
		flag = getApplicationContext().bindService(
				new Intent(RhythmPlayer.this, saveDataService.class), SDSconnt,
				Service.BIND_AUTO_CREATE);
		return flag;
	}

	protected boolean initializePlayer() {
		boolean flag = false;
		serviceConnection = new ServiceConnection() {

			@Override
			public void onServiceDisconnected(ComponentName name) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "播放器启动失败！请重新启动。",
						Toast.LENGTH_SHORT).show();
				playService = null;
			}

			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "欢迎使用律动音乐播放器",
						Toast.LENGTH_SHORT).show();
				// 获取播放服务
				playBinder = (PlayBinder) service;
				playService = playBinder.getPlayService();
				if (playService == null) {
					Log.i("PlayService", "NULL");
				} else {
					Log.i("PlayService", "Is a Value");

					updatePlayBntEable();

				}

				if (!curSong.getmFilePath().isEmpty()) {

					showNotification(curSong, false);

					int i = playService.getDuration();
					i /= 1000;
					int minute = i / 60;
					int hour = minute / 60;
					int second = i % 60;
					minute %= 60;
					sond_duration_tv.setText(String.format("%02d:%02d:%02d",
							hour, minute, second));
					song_Name_tv.setText("当前播放 :"
							+ curSong.getmFileTitle().substring(0,
									curSong.getmFileTitle().indexOf('.')));

				}

			}
		};

		// 绑定播放服务
		flag = getApplicationContext().bindService(
				new Intent(RhythmPlayer.this, PlayService.class),
				serviceConnection, Service.BIND_AUTO_CREATE);

		return flag;
	}

	private void play(SongInfo curSong) {
		// TODO Auto-generated method stub

		// 歌曲有效
		showLrc(curSong, lrc.getCode());
		// 播放
		playService.play(curSong);
		if (curSong.isEnable()) {
			showNotification(curSong, true);
			CURR_STATE = STATE_PLAY;
			toolbar_play
					.setBackgroundResource(music_toolbar_play_state[CURR_STATE]);
		} else {
			// 不播放
			playService.pause();
			showNotification(curSong, false);
			CURR_STATE = STATE_PAUSE;
			toolbar_play
					.setBackgroundResource(music_toolbar_play_state[CURR_STATE]);
			playTime_sb.setMax(playService.getDuration());
			playTime_sb.setProgress(0);
			song_curposition_tv.setText("00:00:00");
		}
		curSong.setEnable(true);
		Log.i("notifi", "到此");
		lrc.setMaxTime(playService.getDuration());
		song_Name_tv.setText("当前播放 :"
				+ curSong.getmFileTitle().substring(0,
						curSong.getmFileTitle().indexOf('.')));
		int i = playService.getDuration();
		i /= 1000;
		int minute = i / 60;
		int hour = minute / 60;
		int second = i % 60;
		minute %= 60;
		sond_duration_tv.setText(String.format("%02d:%02d:%02d", hour, minute,
				second));
		playTime_sb.setMax(playService.getDuration());
		changePlayState();
		Log.i("play", "函数结束");
	}

	/**
	 * 初始化控件函数
	 * 
	 * @author 吴香礼
	 */
	@SuppressWarnings("deprecation")
	private void initializeLayout() {
		// /////////////////////////////////////////////////////////////
		// 标题栏空间初始化
		// /////////////////////////////////////////////////////////////
		titlebar_list = (ImageButton) findViewById(R.id.title_list);
		titlebar_music = (ImageButton) findViewById(R.id.title_music);
		titlebar_openDir = (ImageButton) findViewById(R.id.title_openDir);
		titlebar_voice = (ImageButton) findViewById(R.id.title_voice);
		title_more = (ImageButton) findViewById(R.id.title_more);
		more_Layout = getLayoutInflater().inflate(R.layout.more_layout, null);
		more_Form = new PopupWindow(more_Layout, 300, 150);
		more_aboutus = (TextView) more_Layout.findViewById(R.id.more_aboutus);
		more_going = (TextView) more_Layout.findViewById(R.id.more_going);
		// /////////////////////////////////////////////////////////////
		// 工具栏空间初始化
		// /////////////////////////////////////////////////////////////
		toolbar_menu = (ImageButton) findViewById(R.id.toolbar_menu);
		toolbar_mode = (ImageButton) findViewById(R.id.toolbar_mode);
		toolbar_next = (ImageButton) findViewById(R.id.toolbar_next);
		toolbar_play = (ImageButton) findViewById(R.id.toolbar_play);
		toolbar_pre = (ImageButton) findViewById(R.id.toolbar_pre);
		// /////////////////////////////////////////////////////////////
		// 中间部分，播放显示页面相关空间初始化1
		// /////////////////////////////////////////////////////////////
		music_Flipper = (ViewFlipper) findViewById(R.id.viewflipper);
		page_LRC = (LinearLayout) findViewById(R.id.Linear_songLRC);
		LrcView = new LyricView(page_LRC.getContext());
		LrcView.setLongClickable(true);
		// 歌词显示控件加入页面中
		page_LRC.addView(LrcView);
		song_curposition_tv = (TextView) findViewById(R.id.showsongcurr_tv);
		song_Name_tv = (TextView) findViewById(R.id.showsongname_tv);
		sond_duration_tv = (TextView) findViewById(R.id.showsonglen_tv);
		playTime_sb = (SeekBar) findViewById(R.id.seekbar);
		// /////////////////////////////////////////////////////////////
		// 中间部分，主列表页面相关空间初始化2
		// /////////////////////////////////////////////////////////////
		mainlistview = (ExpandableListView) findViewById(R.id.musicpage_mainListview);
		// 组长按时弹出菜单相关空间初始化
		groupLongPress_Layout = getLayoutInflater().inflate(
				R.layout.music_group_longpress_layout, null);
		listgroup_add2list = (Button) groupLongPress_Layout
				.findViewById(R.id.list_group_addtolist);
		listgroup_altername = (Button) groupLongPress_Layout
				.findViewById(R.id.list_group_altername);
		listgroup_cancel = (Button) groupLongPress_Layout
				.findViewById(R.id.list_group_cancelbnt);
		listgroup_clear = (Button) groupLongPress_Layout
				.findViewById(R.id.list_group_clear);
		listgroup_del = (Button) groupLongPress_Layout
				.findViewById(R.id.list_group_del);
		group_LongPress_Form = new PopupWindow(groupLongPress_Layout,
				getWindowManager().getDefaultDisplay().getWidth() - 40, 200);
		// 子长按时弹出菜单相关空间初始化
		childLongPress_Layout = getLayoutInflater().inflate(
				R.layout.music_child_longpress_layout, null);
		listchild_add2list = (Button) childLongPress_Layout
				.findViewById(R.id.list_child_addtolist);
		listchild_cancel = (Button) childLongPress_Layout
				.findViewById(R.id.list_child_cancel);
		listchild_remove = (Button) childLongPress_Layout
				.findViewById(R.id.list_child_remove);
		listchild_songinfo = (Button) childLongPress_Layout
				.findViewById(R.id.list_child_songinfo);
		chileLongPress_Form = new PopupWindow(childLongPress_Layout,
				getWindowManager().getDefaultDisplay().getWidth() - 40, 200);
		// /////////////////////////////////////////////////////////////
		// 中间部分，当前播放列表显示页面相关空间初始化3
		// /////////////////////////////////////////////////////////////
		curPlayListPage = (LinearLayout) findViewById(R.id.curPlayListPage);
		curList_Layout = getLayoutInflater().inflate(
				R.layout.currplaylist_layout, null);
		curListview = (ListView) curList_Layout.findViewById(R.id.currlistview);
		curPlayListPage.addView(curList_Layout);
		// /////////////////////////////////////////////////////////////
		// 中间部分，文件浏览页面相关空间初始化4
		// /////////////////////////////////////////////////////////////
		openFilePage = (LinearLayout) findViewById(R.id.openFilePage);
		scanFileView = getLayoutInflater().inflate(R.layout.scanfilepath, null);
		scanFile_lv = (ListView) scanFileView
				.findViewById(R.id.scanfile_listview);
		scanFile_cancelBnt = (Button) scanFileView
				.findViewById(R.id.scanfile_cancel_bnt);
		scanFile_playBnt = (Button) scanFileView
				.findViewById(R.id.scanfile_play_bnt);
		scanFile_playBnt.setEnabled(false);
		scanFile_retBnt = (Button) scanFileView
				.findViewById(R.id.scanfile_return_bnt);
		scanfile_filePathTv = (TextView) scanFileView
				.findViewById(R.id.scanfile_path_textview);
		openFilePage.addView(scanFileView);
		// /////////////////////////////////////////////////////////////
		// 菜单部分相关空间初始化
		// /////////////////////////////////////////////////////////////
		menu_Layout = getLayoutInflater().inflate(R.layout.music_menu_layout,
				null);
		menu_exit = (ImageButton) menu_Layout.findViewById(R.id.menu_exit);
		menu_musicLrc = (ImageButton) menu_Layout
				.findViewById(R.id.menu_musicLrc);
		menu_opinion = (ImageButton) menu_Layout
				.findViewById(R.id.menu_opinion);
		menu_refresh = (ImageButton) menu_Layout
				.findViewById(R.id.menu_refresh);
		menu_theme = (ImageButton) menu_Layout.findViewById(R.id.menu_theme);
		menu_timing = (ImageButton) menu_Layout.findViewById(R.id.menu_timing);
		myMenu = new PopupWindow(menu_Layout, LayoutParams.FILL_PARENT, 200);
		// /////////////////////////////////////////////////////////////
		// 歌词编码菜单相关空间初始化
		// /////////////////////////////////////////////////////////////
		LrcCode_Layout = getLayoutInflater().inflate(R.layout.lrccode_layout,
				null);
		LrcCode_form = new PopupWindow(LrcCode_Layout, getWindowManager()
				.getDefaultDisplay().getWidth() - 40, 200);
		GBK_bnt = (Button) LrcCode_Layout.findViewById(R.id.GBK_bnt);
		UTF16_bnt = (Button) LrcCode_Layout.findViewById(R.id.UTF16_bnt);
		Unicode_bnt = (Button) LrcCode_Layout.findViewById(R.id.Unicode_bnt);
		UTF8_bnt = (Button) LrcCode_Layout.findViewById(R.id.UTF8_bnt);
		LrcCode_cancelbnt = (Button) LrcCode_Layout
				.findViewById(R.id.LrcCode_cancelbnt);
		// /////////////////////////////////////////////////////////////
		// 主题选择菜单相关空间初始化
		// /////////////////////////////////////////////////////////////
		theme_Layout = getLayoutInflater().inflate(R.layout.bgtheme_choice,
				null);
		theme02 = (ImageView) theme_Layout.findViewById(R.id.bgtheme_bg2);
		theme03 = (ImageView) theme_Layout.findViewById(R.id.bgtheme_bg3);
		theme04 = (ImageView) theme_Layout.findViewById(R.id.bgtheme_bg4);
		theme06 = (ImageView) theme_Layout.findViewById(R.id.bgtheme_bg6);
		theme08 = (ImageView) theme_Layout.findViewById(R.id.bgtheme_bg8);
		theme09 = (ImageView) theme_Layout.findViewById(R.id.bgtheme_bg9);
		theme10 = (ImageView) theme_Layout.findViewById(R.id.bgtheme_bg10);
		theme12 = (ImageView) theme_Layout.findViewById(R.id.bgtheme_bg12);
		theme_Form = new PopupWindow(theme_Layout, getWindowManager()
				.getDefaultDisplay().getWidth() - 40, 250);
	}

	/**
	 * 更新各个播放列表&列表集合&相应的布局,从新获取列表集合，和当前列表
	 */
	private void UpdatePlayList() {
		// TODO Auto-generated method stub
		main_music_list = app.getAllMusicList();
		curPlayList = this.main_music_list.get(1).getList();
		if (this.curPlayList.isEmpty()) {
			this.curPlayList = this.main_music_list.get(0).getList();
			this.main_music_list.get(1).getList().addAll(this.curPlayList);
		}
		this.curListAdapter = new songListAdapter(this, this.curPlayList);
		curListview.setAdapter(this.curListAdapter);
		this.mainListAdapter = new exListAdapter(this, this.main_music_list);
		this.mainlistview.setAdapter(this.mainListAdapter);
		Log.i("UpdatePlayList", "函数结束");
	}

	/**
	 * 浏览path下的文件
	 * 
	 * @param path
	 *            浏览目录
	 * @return 文件列表（当前目录）
	 */
	private List<myFile> getFile(String path) {
		// TODO Auto-generated method stub
		// 设置路径
		scanfile_filePathTv.setText(path);
		File file = new File(path);
		List<myFile> list = new ArrayList<myFile>();
		if (file.exists()) {
			File[] files = file.listFiles();
			String filename;
			String filepath;
			int id;
			if (files.length != 0) {
				for (int i = 0; i < files.length; i++) {
					filepath = files[i].getPath();
					filename = files[i].getName();
					if (new File(filepath).isDirectory()) {
						// 是文件夹
						id = R.drawable.ic_fold;
					} else if (filepath.endsWith("MP3")
							|| filepath.endsWith("mp3")) {
						// 音乐文件
						id = R.drawable.ic_mp3;
					} else if (filepath.endsWith("3gp")
							|| filepath.endsWith("3GP")
							|| filepath.endsWith("MP4")
							|| filepath.endsWith("mp4")) {
						id = R.drawable.ic_mp4;
					} else {
						// 文件
						id = R.drawable.ic_copyfile;
					}
					list.add(new myFile(filename, id, filepath));
				}
			}
			fileListAdapter = new fileAdapter(RhythmPlayer.this, list);
			Log.i("listLen", "长度为		" + list.size());
			scanFile_lv.setAdapter(fileListAdapter);
			Log.i("fileAdapter", "到此");
		}
		return list;
	}

	/**
	 * 根据播放器当前状态更新标题栏的各个图标
	 * 
	 * @author Streamer
	 * @see {@link RhythmPlayer#change_titlebar_bnt_bg()}
	 */
	private boolean change_titlebar_bnt_bg() {
		if (music_Flipper.getCurrentView().getId() == R.id.page1) {
			titlebar_music.setBackgroundResource(R.drawable.ic_music_checked);
		} else {
			titlebar_music.setBackgroundResource(R.drawable.ic_music);
		}
		if (music_Flipper.getCurrentView().getId() == R.id.musicpage_mainListview) {
			titlebar_list.setBackgroundResource(R.drawable.ic_list_checked);
		} else {
			titlebar_list.setBackgroundResource(R.drawable.ic_list);
		}
		if (music_Flipper.getCurrentView().getId() == R.id.openFilePage) {
			titlebar_openDir.setBackgroundResource(R.drawable.dir_checked);
		} else {
			titlebar_openDir.setBackgroundResource(R.drawable.ic_savefile);
		}
		return true;
	}// End

	/**
	 * 根据当前的播放曲目以及当前的播放列表获取下一曲目。 需要注意，该方法用于自动获取下一首，与单曲循环与否
	 * 
	 * @author Streamer
	 * @param Song
	 *            当前曲目
	 * @return 下一曲目
	 * @see {@link RhythmPlayer#getNextSong(SongInfo)}
	 */
	protected SongInfo getNextSong(SongInfo Song) {
		// TODO Auto-generated method stub
		SongInfo nextSong = Song;
		Log.i("next", "进入");
		if (MODE_CURR_STATE == MODE_LOOPALL) {
			// 列表循环
			if (!curPlayList.isEmpty()) {
				int index1 = curPlayList.indexOf(Song) + 1;
				if (index1 < curPlayList.size() && index1 >= 0) {
					nextSong = curPlayList.get(index1);
				} else if (index1 == curPlayList.size()) {
					nextSong = curPlayList.get(0);
				} else {
					// nextSong.setEnable(false);
				}
				Log.i("next", "进入LOOPALL" + nextSong.isEnable());
			}
		} else if (MODE_CURR_STATE == MODE_ORDER) {
			// 顺序播放或单曲循环
			int index1 = curPlayList.indexOf(Song) + 1;
			if (index1 < curPlayList.size() && index1 > 0) {
				nextSong = curPlayList.get(index1);
			} else {
				// 歌曲不可用
				nextSong.setEnable(false);
			}
			Log.i("next", "进入order" + nextSong.isEnable());
		} else if (MODE_CURR_STATE == MODE_RANDOM) {
			// 随机播放
			// 返回指定范围的随机数(m-n之间)的公式[2]：Math.random()*(n-m)+m；
			int index1 = ((int) (Math.random() * curPlayList.size()) % curPlayList
					.size());
			Log.i("index", "" + index1);
			if (index1 >= 0 && index1 < curPlayList.size())
				nextSong = curPlayList.get(index1);
			else {
				nextSong.setEnable(false);
			}
			Log.i("next", "进入Random" + nextSong.isEnable());
		} else if (MODE_CURR_STATE == MODE_LOOP) {
			nextSong = Song;
			Log.i("next", "进入LOOP" + nextSong.isEnable());
		} else {
			nextSong.setEnable(false);
			Log.i("next", "进入else" + nextSong.isEnable());
		}
		Log.i("next", "退出" + nextSong.isEnable());
		return nextSong;
	}

	/**
	 * 根据当前的播放曲目以及当前的播放列表，根据播放模式获取前一曲目。
	 * 
	 * @author Streamer
	 * @param Song
	 *            当前曲目
	 * @return 下一曲目
	 * @see {@link RhythmPlayer#getPreSong(SongInfo)}
	 */
	protected SongInfo getPreSong(SongInfo Song) {
		// TODO Auto-generated method stub
		SongInfo preSong = Song;
		if (MODE_CURR_STATE == MODE_LOOPALL) {
			// 列表循环
			int index = curPlayList.indexOf(Song);
			if (index == 0)
				preSong = curPlayList.get(curPlayList.size() - 1);
			else if (index > 0 && index < curPlayList.size())
				preSong = curPlayList.get(index - 1);
			else {
				preSong.setEnable(false);
			}
		} else if (MODE_CURR_STATE == MODE_ORDER) {
			// 顺序播放
			int index = curPlayList.indexOf(Song) - 1;
			if (index >= 0 && index < curPlayList.size())
				preSong = curPlayList.get(index);
			else {
				preSong.setEnable(false);
			}
		} else if (MODE_CURR_STATE == MODE_RANDOM) {
			// 随机播放
			// 返回指定范围的随机数(m-n之间)的公式[2]：Math.random()*(n-m)+m；
			int index = ((int) (Math.random() * curPlayList.size()))
					% curPlayList.size();
			if (index > 0 && index < curPlayList.size())
				preSong = curPlayList.get(index);
			else {
				preSong.setEnable(false);
			}
		} else if (MODE_CURR_STATE == MODE_LOOP) {
			preSong = Song;
		} else {
			preSong.setEnable(false);
		}
		return preSong;
	}

	/**
	 * 播放指定路径的媒体，并改变响应的状态
	 * 
	 * @author Streamer
	 * @see {@link RhythmPlayer#changePlayState()}
	 */
	private void changePlayState() {

		ispause = !playService.getPlayerState();
		updatePlayBntEable();
		Log.i("MediaPath", curSong.getmFilePath());// 进度条可用
		if (app.getAllMusicList().get(2).getList().lastIndexOf(curSong) == -1) {
			/*
			 * 将歌曲加入最近播放
			 */
			app.getAllMusicList().get(2).getList().add(curSong);
			// 更新数据库
			SDSevice.InsertSong(curSong, app.getAllMusicList().get(2)
					.getListName());
			// 更新列表，只需更新exListview
			UpdatePlayList();
		}
	}

	private void updatePlayBntEable() {
		// TODO Auto-generated method stub
		// 设置歌词时长
		lrc.setMaxTime(playService.getDuration());
		int index = curPlayList.indexOf(curSong);
		if (index < curPlayList.size() - 1 || MODE_CURR_STATE == MODE_RANDOM) {
			toolbar_next.setEnabled(true); // 可用
		} else {
			toolbar_next.setEnabled(false);
		}
		if (index > 0 || MODE_CURR_STATE == MODE_RANDOM) {
			toolbar_pre.setEnabled(true); // 可用
		} else {
			toolbar_pre.setEnabled(false);
		}
		if (playService.isStop()) {
			toolbar_play.setEnabled(false);
			playTime_sb.setEnabled(false);
		} else {
			toolbar_play.setEnabled(true);
			playTime_sb.setEnabled(true);
		}
	}

	private void showInfo(String string) {
		// TODO Auto-generated method stub
		Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT)
				.show();
	}

	private void showExitInfo() {
		// TODO Auto-generated method stub
		Dialog d = new AlertDialog.Builder(RhythmPlayer.this)
				.setIcon(R.drawable.b15)
				.setTitle("系统消息")
				.setMessage("你将要退出播放器？")
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface paramDialogInterface,
							int paramInt) {
						paramDialogInterface.dismiss();
					}
				})
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface paramDialogInterface,
							int paramInt) {
						Toast t = new Toast(getApplicationContext());
						View v = getLayoutInflater().inflate(
								R.layout.exit_layout, null);
						t.setView(v);
						t.setDuration(Toast.LENGTH_SHORT);
						t.show();
						finish();
					}
				}).create();
		d.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.activity_rhythm_player, menu);
		if (this.menu_isShow) {
			this.myMenu.dismiss();
			this.toolbar_menu.setBackgroundResource(R.drawable.ic_bnt_menu);
			this.menu_isShow = false;
		} else {
			this.toolbar_menu.setBackgroundResource(R.drawable.ic_menu_checked);
			this.myMenu.showAtLocation(this.toolbar_menu, 80, 0, 80);
			this.menu_isShow = true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		notificationManager.cancel(1);
		this.unregisterReceiver(myReceiver);
		getApplicationContext().unbindService(serviceConnection);
		getApplicationContext().unbindService(SDSconnt);
		stopService(new Intent(RhythmPlayer.this, PlayService.class));
		stopService(new Intent(RhythmPlayer.this, saveDataService.class));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		app.setFirstLaunch(false);
		app.setLastSongIndex(this.curPlayList.lastIndexOf(this.curSong));
		app.resetAllMusicList(this.main_music_list);
		app.setMusicPlayerMode(this.MODE_CURR_STATE);
		app.SavaData();
	}

	/**
	 * 监听按键事件
	 * 
	 * @author Streamer
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK: {
			/*
			 * 点击后退事件
			 */

			if (music_Flipper.getCurrentView().getId() != R.id.page1) {
				/*
				 * 如果当前不是处在第一页面，显示上一页
				 */
				music_Flipper.showPrevious();
				change_titlebar_bnt_bg();
				return true;
			} else {
				/*
				 * 如果当前处在第一页面，弹出退出提示窗口
				 */
				showExitInfo();
			}
		}
		}
		return super.onKeyDown(keyCode, event);
	}// End

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return this.detector.onTouchEvent(event);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		this.detector.onTouchEvent(ev);
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

}
