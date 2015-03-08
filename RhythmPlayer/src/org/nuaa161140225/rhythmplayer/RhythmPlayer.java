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
	// �ؼ����岿��
	// //////////////////////////////////////////////////
	/*
	 * �������ؼ�
	 */
	/**
	 * �������������б�ť
	 */
	private ImageButton titlebar_list;
	/**
	 * ����������ҳ�漴�����ʾҳ���л���ť
	 */
	private ImageButton titlebar_music;
	/**
	 * �����������ļ��������ť
	 */
	private ImageButton titlebar_openDir;
	/**
	 * ���������������ڰ�ť
	 */
	private ImageButton titlebar_voice;
	/**
	 * �����������๦�ܰ�ť
	 */
	private ImageButton title_more;
	/**
	 * ���๦�ܵ����˵�
	 */
	private PopupWindow more_Form;
	private View more_Layout;
	private TextView more_going;
	private TextView more_aboutus;

	// �������ؼ�
	/**
	 * ������������ģʽѡ��ť
	 */
	private ImageButton toolbar_mode;
	/**
	 * ������������ǰһ�װ�ť
	 */
	private ImageButton toolbar_pre;
	/**
	 * �����������Ű�ť
	 */
	private ImageButton toolbar_play;
	/**
	 * ��������������һ�װ�ť
	 */
	private ImageButton toolbar_next;
	/**
	 * ���������˵���ʾ��ť
	 */
	private ImageButton toolbar_menu;
	/*
	 * �м䲿��
	 */
	/**
	 * �м䲿�ֻ�������
	 */
	private ViewFlipper music_Flipper;
	// page1
	/**
	 * �����ʾҳ�棬���Բ���
	 */
	private LinearLayout page_LRC;
	/**
	 * �����ʾ�ؼ�
	 */
	private LyricView LrcView;
	private Dialog searLrcD;
	/**
	 * ��ǰ���Ÿ���������ʾ�ؼ�
	 */
	private TextView song_Name_tv;
	/**
	 * ��ǰ����λ����ʾ�ؼ�
	 */
	private TextView song_curposition_tv;
	/**
	 * ������ʱ��
	 */
	private TextView sond_duration_tv;
	/**
	 * �������Ž�����
	 */
	private SeekBar playTime_sb;
	// page2
	/**
	 * ���������б���ʾҳ�棬����չ�б�
	 */
	private ExpandableListView mainlistview;
	// /////////////////////////////////////////////////////////////////
	// ���б��������˵�����
	// /////////////////////////////////////////////////////////////////
	/**
	 * ���б��������˵�����view
	 */
	private View groupLongPress_Layout;
	/**
	 * ���б��������˵������ӵ��б�ť
	 */
	private Button listgroup_add2list;
	/**
	 * ���б��������˵�������б�ť
	 */
	private Button listgroup_clear;
	/**
	 * ���б��������˵���ɾ���б�ť
	 */
	private Button listgroup_del;
	/**
	 * ���б��������˵��������б����ְ�ť
	 */
	private Button listgroup_altername;
	/**
	 * ���б��������˵���ȡ����ť
	 */
	private Button listgroup_cancel;
	/**
	 * ���б��������˵�����
	 */
	private PopupWindow group_LongPress_Form;

	// /////////////////////////////////////////////////////////////////
	// ���б��������˵�����
	// /////////////////////////////////////////////////////////////////
	/**
	 * ���б��������˵�����view
	 */
	private View childLongPress_Layout;
	/**
	 * ���б��������˵�����
	 */
	private PopupWindow chileLongPress_Form;
	/**
	 * ���б��������˵������ӵ��б�ť
	 */
	private Button listchild_add2list;
	/**
	 * ���б��������˵���������Ϣ��ť
	 */
	private Button listchild_songinfo;
	/**
	 * ���б��������˵����Ƴ��б�ť
	 */
	private Button listchild_remove;
	/**
	 * ���б��������˵���ȡ����ť
	 */
	private Button listchild_cancel;
	// page3
	/**
	 * ��ǰ�����б���ʾҳ�棬���Բ���
	 */
	private LinearLayout curPlayListPage;
	/**
	 * ��ǰ�����б���view
	 */
	private View curList_Layout;
	/**
	 * ��ǰ�����б���ʾ�ؼ�
	 */
	private ListView curListview;
	// page4
	/**
	 * ���ļ���ʾҳ�棬���Բ���
	 */
	private LinearLayout openFilePage;
	private View scanFileView;
	private ListView scanFile_lv;
	private TextView scanfile_filePathTv;
	private Button scanFile_retBnt;
	private Button scanFile_playBnt;
	private Button scanFile_cancelBnt;
	// /////////////////////////////////////////////////////////////////
	// �˵���������ؿؼ�
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
	// ��������˵���ؿռ�
	// /////////////////////////////////////////////////////////////////
	private View LrcCode_Layout;
	private PopupWindow LrcCode_form;
	private Button GBK_bnt;
	private Button UTF8_bnt;
	private Button UTF16_bnt;
	private Button Unicode_bnt;
	private Button LrcCode_cancelbnt;
	// /////////////////////////////////////////////////////////////////
	// ����ѡ����ؿؼ�
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
	// ״̬������
	// /////////////////////////////////////////////////////////////////
	private RemoteViews statusView;// = new RemoteViews(this.getPackageName(),
	// R.layout.notification_layout);
	// /////////////////////////////////////////////////////////////////
	// ���ֵ�Ķ���
	// /////////////////////////////////////////////////////////////////
	// ��������
	/**
	 * �����б���������
	 */
	private exListAdapter mainListAdapter;
	/**
	 * �ļ��б�������
	 */
	private fileAdapter fileListAdapter;
	/**
	 * ��ǰ�����б�����
	 */
	private songListAdapter curListAdapter;
	// �б�������
	/**
	 * �����б���
	 */
	List<MediaList<SongInfo>> main_music_list;
	/**
	 * ��ǰ�����б�
	 */
	private List<SongInfo> curPlayList;
	/**
	 * �ļ��б�
	 */
	private List<myFile> fileList;
	// �����
	/**
	 * ��ǰ���Ÿ���
	 */
	private SongInfo curSong;
	/**
	 * ����������
	 */
	private AudioManager audioManager;
	/**
	 * ����ʶ����
	 */
	private GestureDetector detector;
	/**
	 * ��ʴ�����
	 */
	private SongLyric lrc;
	/**
	 * Ӧ����
	 */
	private RhythmApp app;
	/**
	 * ���ŷ�����
	 */
	private PlayService playService = null;
	/**
	 * ���ŷ���������
	 */
	private ServiceConnection serviceConnection;
	/**
	 * ���ݱ�����
	 */
	private saveDataService SDSevice = null;
	/**
	 * ���ݱ������������
	 */
	private ServiceConnection SDSconnt;

	protected PlayBinder playBinder;
	protected SDSBinder sdsBinder;
	/**
	 * ֪ͨ��
	 */
	private Notification notification;
	/**
	 * ֪ͨ������
	 */
	private NotificationManager notificationManager;
	private MyReceiver myReceiver;
	// ����
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
	// ����&����
	private int CURR_STATE = 0;
	private int MODE_CURR_STATE;
	private int groupid;
	private int hour1 = 25;
	private int minute1;
	/**
	 * ���ݵ�ǰģʽ������ѡ���Ӧͼ�꣬����洢ͼƬ��id
	 */
	int[] music_toolbar_mode_state = { R.drawable.ic_mode_order,
			R.drawable.ic_mode_random, R.drawable.ic_mode_loopall,
			R.drawable.ic_mode_loop };
	/**
	 * CURR_STATE=0����ͣ����ʾ���ǣ�CURR_STATE=1������ʱ��ʾ����
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
		Log.i("showNotifi10", "����");
		Log.i("showNotifi11", "����");
		int icon = R.drawable.notif;// ͼ��
		CharSequence name = song.getmFileTitle().subSequence(0,
				song.getmFileTitle().lastIndexOf('.'));// ����
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
		// �������
		// /////////////////////////////////////////////////////////////
		Intent i_play = new Intent(myAction);
		i_play.putExtra("playstate", 2); // ��ͣ
		PendingIntent pi_play = PendingIntent.getBroadcast(RhythmPlayer.this,
				0, i_play, PendingIntent.FLAG_UPDATE_CURRENT);
		notification.contentView.setOnClickPendingIntent(R.id.notifi_play,
				pi_play);
		notification.contentIntent = PendingIntent.getActivity(
				RhythmPlayer.this, 0, new Intent(RhythmPlayer.this,
						RhythmPlayer.class), PendingIntent.FLAG_UPDATE_CURRENT);
		notificationManager.notify(1, notification);
		Log.i("showNotifi12", "����");
	}

	public class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals(myAction)) {
				switch (intent.getIntExtra("playstate", 0)) {
				case 0: {// ���²��Ž�����
					if (playService.getPlayerState()) {// �����ڲ���״̬ʱ�ſ��ԣ���Ȼerror
						int i = playService.getCurPosition();
						i /= 1000;
						int minute = i / 60;
						int hour = minute / 60;
						int second = i % 60;
						minute %= 60;
						song_curposition_tv.setText(String.format(
								"%02d:%02d:%02d", hour, minute, second));
						playTime_sb.setProgress(playService.getCurPosition());
						// ���¸��
						LrcView.setTime(playService.getCurPosition());
						LrcView.postInvalidate();
						// Log.i("�㲥", "1");
						Intent myIntent = new Intent(myAction);
						myIntent.putExtra("playstate", 0);
						sendBroadcast(myIntent);
						// Log.i("�㲥", "2");
					}
					break;
				}
				case 1: {// �����������
					playTime_sb.setProgress(0);
					song_curposition_tv.setText("00:00:00");
					curSong = getNextSong(curSong);
					Log.i("Notifi", "��һ��");
					play(curSong);// ������һ��

					break;
				}
				case 2: {
					// ��ͣ
					playService.pause();
					curSong.setEnable(true);
					Log.i("�㲥2", "repalay");
					Log.i("notif", "��ͣ");
					showNotification(curSong, playService.getPlayerState());
					changePlayState();
					CURR_STATE = playService.getPlayerState() ? STATE_PLAY
							: STATE_PAUSE;
					// �ı�ð�ť�ı���
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
					// ����������
					Log.i("���չ㲥30", "&&" + SDSevice.getLrc());
					if (!SDSevice.getLrc().isEmpty()) {

						int index = app.getAllMusicList().get(0).getList()
								.indexOf(curSong);
						curSong.setmLrcPath(SDSevice.getLrc());
						Log.i("���չ㲥34", "&&" + SDSevice.getLrc());
						if (index >= 0
								&& index < app.getAllMusicList().get(0)
										.getList().size()) {
							app.getAllMusicList().get(0).getList()
									.remove(index);
							app.getAllMusicList().get(0).getList()
									.set(index, curSong);
							Log.i("���չ㲥34", "&&" + SDSevice.getLrc());
							// ��ʾ�¸��
						}
						index = app.getAllMusicList().get(3).getList()
								.indexOf(curSong);
						Log.i("���չ㲥35", "&&" + SDSevice.getLrc());
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
						Log.i("���չ㲥3", "&&" + curSong.getLrcPath());
					} else {
						Toast.makeText(getApplicationContext(), "δ�ҵ���ʣ�",
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
	 * ��Ϣ���д��������������ֲ���������ʱ�����ĵ���Ϣ
	 * 
	 * @see {@link MusicPlayer#myHandler}
	 */
	Handler myHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case TIMING: {
				// ��ʱ�˳�
				/*
				 * ��ʱ�жϣ��Ƿ�ʱ�䣬ʵ��ر�Ӧ��
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
				 * ����һ���������ڱ���SDcard���Ҹ��
				 */
				showInfo("��ʼ�������");
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
	 * ��ʼ��ֵ
	 */
	@SuppressWarnings("deprecation")
	private void initializeValue() {
		UpdatePlayList();// ��ʼ�������б�Listview
		MODE_CURR_STATE = app.getMusicPlayerMode();// ��ȡ��¼
		lrc = new SongLyric("");// ��ʼ�������
		// ��ʼ��״̬
		if (playService != null) {
			CURR_STATE = playService.getPlayerState() ? STATE_PLAY
					: STATE_PAUSE;
		} else {
			CURR_STATE = STATE_PAUSE; // ������ͣ״̬
		}
		/*
		 * ���س�ʼ��Դ
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
			// ��ʾ��������Ϣ����������������
			music_Flipper.setDisplayedChild(0); // ��ʾ��һҳ
		} else if (curPlayList.size() > 0) {
			curSong = curPlayList.get(0);
			// ��ʾ��������Ϣ����������������
			music_Flipper.setDisplayedChild(0); // ��ʾ��һҳ
		} else {
			toolbar_next.setEnabled(false);
			toolbar_play.setEnabled(false);
			toolbar_pre.setEnabled(false);
			playTime_sb.setEnabled(false);
			music_Flipper.setDisplayedChild(3); // ��ʾ����ҳ
		}
		// ��ʾ�ļ����
		fileList = getFile("/");
		myReceiver = new MyReceiver();
		this.registerReceiver(myReceiver, new IntentFilter(myAction));
		Log.i("initializeValue", "��������");
	}

	private void MenuListener() {

		this.menu_musicLrc.setOnClickListener(new View.OnClickListener() {
			public void onClick(View paramView) {
				myMenu.dismiss();
				toolbar_menu.setBackgroundResource(R.drawable.ic_bnt_menu);
				menu_isShow = false;
				if (LrcCode_form.isShowing()) {
					LrcCode_form.dismiss();
					Log.i("menu_showLRC_bnt", "�������");
				} else {
					LrcCode_form.showAtLocation(LrcCode_Layout, 17, 0, 0);
				}
			}
		});

		/**
		 * �˵����ڵĳ��ò˵��µĸ�������ѡ�ť����¼�
		 * 
		 * @author Streamer
		 */
		menu_theme.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * ��Ҫ��������Ӧ����������
				 */
				myMenu.dismiss();
				theme_Form.showAtLocation(theme_Layout, Gravity.CENTER, 0, 0);
				toolbar_menu.setBackgroundResource(R.drawable.ic_bnt_menu);
				menu_isShow = false;
				Log.i("menu_change_theme", "��������");
			}
		});// End

		/**
		 * �˵����ڵĳ��ò˵��µ�ˢ������ѡ�ť����¼�
		 * 
		 * @author Streamer
		 */
		menu_refresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * ��Ҫ��������Ӧ��ˢ������
				 */
				myMenu.dismiss();
				toolbar_menu.setBackgroundResource(R.drawable.ic_bnt_menu);
				menu_isShow = false;
				/*
				 * ˢ��Ӧ�õ�����
				 */
				app.refreshLocalMusicInfo(RhythmPlayer.this);
				app.getAllMusicList().get(0).getList().clear();
				app.getAllMusicList().get(0).getList()
						.addAll(app.getLocalMusicInfo());
				// �������ݿ�
				SDSevice.UpdateList(app.getAllMusicList().get(0).getList(), app
						.getAllMusicList().get(0).getListName());
				UpdatePlayList();
				Log.i("menu_refresh_list", "ˢ������");
			}
		});// End

		/**
		 * �˵����ڵĳ��ò˵��µ��˳�����ѡ�ť����¼�
		 * 
		 * @author Streamer
		 */
		menu_exit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * ��Ҫ��������Ӧ���˳�����������������
				 */
				// Log.i("menu_exit", "�˳�������");
				showExitInfo();
			}
		});// End

		/**
		 * �˵����ڵ����ò˵��µĶ�ʱ����ѡ�ť����¼�
		 * 
		 * @author Streamer
		 */
		menu_timing.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * ��Ҫ��������Ӧ����ʱ����
				 */
				myMenu.dismiss();
				final TimePicker timePicker = new TimePicker(RhythmPlayer.this);
				timePicker.setCurrentHour(Calendar.getInstance().get(
						Calendar.HOUR));
				timePicker.setCurrentMinute(Calendar.getInstance().get(
						Calendar.MINUTE));
				timePicker.setIs24HourView(true);
				Dialog dialog = new AlertDialog.Builder(RhythmPlayer.this)
						.setTitle("��ʱ�˳�")
						.setView(timePicker)
						.setNegativeButton("ȡ����ʱ",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										isT = false;
										dialog.dismiss();
									}
								})
						.setPositiveButton("ȷ��",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										hour1 = timePicker.getCurrentHour();
										minute1 = timePicker.getCurrentMinute();
										showInfo("����������" + hour1 + ":"
												+ minute1 + "�˳���");
										dialog.dismiss();
										// ������Ϣ
										isT = true;
										myHandler.sendEmptyMessage(TIMING);
									}
								}).create();
				dialog.show();
				toolbar_menu.setBackgroundResource(R.drawable.ic_bnt_menu);
				menu_isShow = false;
				Log.i("menu_timing", "��ʱ����");
			}
		});

		/**
		 * �˵����ڵĳ��ð����µ��������ѡ�ť����¼�
		 * 
		 * @author Streamer
		 */
		menu_opinion.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * ��Ҫ��������Ӧ�������������ڣ�E_mail���ʹ���
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
						.setTitle("�������")
						.setView(editopinion)
						.setNegativeButton("ȡ��",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										dialog.dismiss();
									}
								})
						.setPositiveButton("����",
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
														"���������"
																+ editopinion
																		.getText()
																		.toString(),
														null, null);
										showInfo("��������ɹ������Ĺ��������ǵĶ�����");
										dialog.dismiss();
									}
								}).create();
				dialog.show();
				Log.i("menu_opinion_return", "�û��������");
			}
		});// End

	}

	private void changeBgListener() {
		/**
		 * �����л�ҳ���û����������¼�����,���¼���������ʶ���ദ��
		 * 
		 * @author Streamer
		 */
		music_Flipper.setOnTouchListener(new OnTouchListener() {

			@SuppressLint("UnlocalizedSms")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				// ��������ʶ����
				return detector.onTouchEvent(event);
			}
		});// End

		/**
		 * ����ʶ�����ʵ���������ڴ����û�������Ļ�¼�
		 * 
		 * @author Streamer
		 */
		Log.i("main", "����");
		detector = new GestureDetector(RhythmPlayer.this,
				new OnGestureListener() {

					@Override
					public boolean onSingleTapUp(MotionEvent e) {
						// TODO Auto-generated method stub
						// �û��ᴥ��Ļ���ɿ���
						return false;
					}

					@Override
					public void onShowPress(MotionEvent e) {
						// TODO Auto-generated method stub
						// �û��ᴥ��Ļ����ĩ�ɿ����϶���ע�⣬ǿ������û���ɿ������϶�״̬
					}

					@Override
					public boolean onScroll(MotionEvent e1, MotionEvent e2,
							float distanceX, float distanceY) {
						// TODO Auto-generated method stub
						// �û�������Ļ���϶�

						if (music_Flipper.getCurrentView().getId() == R.id.page1) {
							int y = (int) (e1.getY() - e2.getY());
							if (y > 20 && CURR_STATE == STATE_PLAY
									&& playService != null) {
								// �û����ϻ�
								Log.i("���ϻ�", "" + y);

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
								// �û����»�
								Log.i("���ϻ�", "" + y);

								// ��ʱyΪ����
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
						// �û�������Ļ
					}

					@Override
					public boolean onFling(MotionEvent e1, MotionEvent e2,
							float velocityX, float velocityY) {
						// TODO Auto-generated method stub

						// �û�������Ļ�������ƶ����ɿ�����������Ļ�ϻ�����
						// e1:��һ��ACTION_DOWN�¼�����ָ���µ���һ�㣩
						// e2:���һ��ACTION_MOVE�¼� ����ָ�ɿ�����һ�㣩
						// velocityX:��ָ��x���ƶ����ٶ� ��λ������/��
						// velocityY:��ָ��y���ƶ����ٶ� ��λ������/��

						/*
						 * �û�������Ļ�Ƿ������¼��� ��Ҫ��������Ӧ�����û�������Ļ���ȴﵽһ���̶��ǣ������û��Ļ�����
						 * ���л�����Ӧҳ�棬���ı�������ť�ı���ͼƬ������˵�������ʾ״̬ͬ
						 * ʱ���˵����ڹرգ������ֲ��������ڲ���״̬ʱ����ʧЧ����������Ӧ��
						 */

						float x = (int) (e2.getX() - e1.getX());
						// ע�������жϻ���������Ӧ��������δ��ʾʱ ��Ϊ musicplayer����δ����״̬ʱ
						// �������� videoplayer����Ӧ����
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
						// �û��ᴥ��Ļ������

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
		 * ���ֲ����������������ֲ���������ѡ��ť����¼�����
		 * 
		 * @author Streamer
		 */
		titlebar_music.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * �¼���������ʾ�����л������ֲ��ŵ������棬���ı��� �е����ֲ�������������ѡ�ť�ı���ͼƬ
				 */
				if (music_Flipper.getCurrentView().getId() == R.id.page1) {
					// �������ȷ��ҳ�棬��������
				} else {
					// ������Ǵ�����ȷ��ҳ�棬�л�ҳ��
					music_Flipper.setDisplayedChild(0);
				}
				// �ı䰴ť����ͼƬ
				change_titlebar_bnt_bg();
			}
		});// End

		/**
		 * ���ֲ����������������ֲ����������б���ʾ����ѡ��ť����¼����� �����ѡ��ť����¼�
		 * 
		 * @author Streamer
		 */
		titlebar_list.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * �¼���������ʾ�����л������ֲ��ŵ����������б���ʾ���棬 ���ı����е����ֲ�������������ѡ�ť�ı���ͼƬ
				 */
				if (music_Flipper.getCurrentView().getId() == R.id.musicpage_mainListview) {
					// �������ȷ��ҳ�棬��������
					music_Flipper.setDisplayedChild(3);
				} else {
					// ������Ǵ�����ȷ��ҳ�棬�л�ҳ��
					music_Flipper.setDisplayedChild(1);
				}
				// �ı䰴ť����ͼƬ
				change_titlebar_bnt_bg();
			}
		});// End

		titlebar_list.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (music_Flipper.getCurrentView().getId() == R.id.musicpage_mainListview) {
					music_Flipper.setDisplayedChild(2);// ��ʾ��ǰ�����б�
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
		 * ���ֲ����������������ֲ������������ѡ�ť����¼�
		 * 
		 * @author Streamer
		 */
		titlebar_voice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * �¼�������ý�������ɵ���ʱ�򵯳������������ڴ��ڣ���رմ��� �Լ���¼�������ڴ��ڵ�״̬
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
				// ��ʾ�����Ϣ
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
		 * ���ֲ������������Ĳ���ģʽѡ��ť����¼�
		 * 
		 * @author Streamer
		 */
		toolbar_mode.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * ��Ҫ��������Ӧ�����θı��ǰ�Ĳ���ģʽ����ʾ��Ӧ��ģʽͼ�ꣻ �ı�һЩ�ؼ��Ŀ���״̬
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
		 * ���ֲ���������������һ��ѡ��ť����¼�
		 * 
		 * @author Streamer
		 */
		toolbar_pre.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * ��Ҫ��������Ӧ�����ݵ�ǰ��ģʽ��ȡ��һ�ײ�����Ŀ��������
				 */
				// ��ȡǰһ����Ŀ��Ϣ���滻��ǰ��Ŀ
				Log.i("Play", curSong.getmFileTitle() + "+++++++11"
						+ curPlayList.size());
				// ��ȡдһ����Ŀ��Ϣ���滻��ǰ��Ŀ
				int state = MODE_CURR_STATE;
				if (MODE_CURR_STATE == MODE_LOOP) {
					// ������ѭ��״̬ʱ��getPreSong����ȡԭ���ĸ���������ȡ���ɹ�
					MODE_CURR_STATE = MODE_ORDER;
				}
				curSong = getPreSong(curSong);
				MODE_CURR_STATE = state;
				if (playService == null) {
					Log.i("Play", curSong.getmFileTitle() + "+++++++12"
							+ curPlayList.size());
				}
				play(curSong); // ���Ÿ���
				Log.i("Play", curSong.getmFileTitle() + "+++++++13"
						+ curPlayList.size());
				changePlayState();
			}
		});// End

		/**
		 * ���ֲ������������Ĳ���/��ͣѡ��ť����¼�
		 * 
		 * @author Streamer
		 */
		toolbar_play.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * ��Ҫ��������Ӧ��������ڲ���״̬������ͣ�����򲥷ţ�����¼�������Ĳ���״̬
				 */
				lrc.setMaxTime(playService.getDuration());

				song_Name_tv.setText("��ǰ���� :"
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
				// ����֪ͨ
				showNotification(curSong, playService.getPlayerState());
				CURR_STATE = playService.getPlayerState() ? STATE_PLAY
						: STATE_PAUSE;
				// �ı�ð�ť�ı���
				playTime_sb.setEnabled(true);
				toolbar_play
						.setBackgroundResource(music_toolbar_play_state[CURR_STATE]);
			}
		});// End

		/**
		 * ���ֲ���������������һ��ѡ�ť����¼�
		 * 
		 * @author Streamer
		 */
		toolbar_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * ��Ҫ��������Ӧ�����ݵ�ǰ��ģʽ��ȡ��һ�ײ�����Ŀ��������
				 */
				// ��ȡдһ����Ŀ��Ϣ���滻��ǰ��Ŀ
				int state = MODE_CURR_STATE;
				if (MODE_CURR_STATE == MODE_LOOP) {
					// ������ѭ��״̬ʱ��getNextSong����ȡԭ���ĸ���������ȡ���ɹ�
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
		 * ���ֲ������������Ĳ˵���ʾѡ��ť����¼�
		 * 
		 * @author Streamer
		 */
		toolbar_menu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * ��Ҫ��������Ӧ������˵�������ʾ״̬����رգ�����򿪣�����¼�˵�����ʾ״̬
				 */
				if (menu_isShow) {
					// �˵�������ʾ״̬,�رղ˵�����
					myMenu.dismiss();
					// �ı䰴ť����
					toolbar_menu.setBackgroundResource(R.drawable.ic_bnt_menu);
					// ��¼״̬
					menu_isShow = false;
				} else {
					// �˵����ڹر�״̬����ʾ�˵�
					myMenu.showAtLocation(toolbar_menu, 80, 0, 80);
					toolbar_menu
							.setBackgroundResource(R.drawable.ic_menu_checked);
					// ��¼״̬
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
				 * �رձ���ѡ�񴰿�
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
		 * �ļ����������ļ��¼�
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
							/* ������ļ��о��ٽ�ȥ��ȡ */
							fileList = getFile(fileList.get(arg2).getFilePath());
						} else {
							/* ������ļ� */
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
						/* ����AlertDialog��ʾȨ�޲��� */
						showInfo("û��Ȩ�ޣ�");
					}
				}
			}
		});// End

		/**
		 * �ļ���������ؼ�����¼�
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
		 * �ļ���������ż�����¼�
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
				// �������ݿ��Ӧ���б�
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
		 * �ļ������ȡ����ť����¼�
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
		 * ��ǰ�����б���Ŀ����¼�
		 * 
		 * @author Streamer
		 */
		curListview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				/*
				 * ��Ҫ��������Ӧ������ѡ�����Ŀ������ͼ��ת����״̬��¼
				 */
				if (arg2 < curPlayList.size()) {
					Log.i("������Ŀ·��", curPlayList.get(arg2).getmFilePath());
					curSong = curPlayList.get(arg2);
					play(curSong);
					CURR_STATE = STATE_PLAY;
					music_Flipper.setDisplayedChild(0);
					change_titlebar_bnt_bg();
					toolbar_play
							.setBackgroundResource(music_toolbar_play_state[CURR_STATE]);
				} else {
					showInfo("����ʧ�ܣ�");
				}
			}
		});// End

		/**
		 * ��������չ�б�ĳ����¼�,ͨ���ж�view��id�����ж��ǳ����黹�ǳ�����
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
					// ������
					if (arg2 != main_music_list.size() - 1) {
						/*
						 * ��ʼ��һЩ���ܵĿ���״̬
						 */
						if (arg2 < 4) {
							/*
							 * ϵͳԤ���б��ɱ��
							 */
							listgroup_altername.setEnabled(false);
							listgroup_del.setEnabled(false);
						} else {
							listgroup_altername.setEnabled(true);
							listgroup_del.setEnabled(true);
						}
						if (arg2 == 1
								|| main_music_list.get(index).getList().size() == 0) {
							// Ĭ���б������
							listgroup_clear.setEnabled(false);
						} else {
							listgroup_clear.setEnabled(true);
						}
						if (main_music_list.get(index).getList().size() == 0) {
							listgroup_add2list.setEnabled(false);
						}
						if (index != 1) {
							// ��Ĭ���б�
							group_LongPress_Form.showAsDropDown(arg1, 20, 10);
						}
					}
					/**
					 * ����ý���б����¼������Ÿ��б�
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
									// ��ӵ�Ĭ���б�
									app.getAllMusicList()
											.get(1)
											.addAll(main_music_list.get(index)
													.getList());
									// �������ݿ�
									SDSevice.UpdateList(app.getAllMusicList()
											.get(index).getList(), app
											.getAllMusicList().get(1)
											.getListName());
									UpdatePlayList();
									updatePlayBntEable();
								}
							});
					/**
					 * ����ý���б����¼��������б�����
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
											.setTitle("���б���")
											.setView(editnewlist)
											.setNegativeButton(
													"ȡ��",
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
													"����",
													new DialogInterface.OnClickListener() {

														@Override
														public void onClick(
																DialogInterface dialog,
																int which) {
															// TODO
															// Auto-generated
															// method stub
															// ɾ��������
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
															// ���������
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
					 * ����ý���б����¼���ɾ���б�
					 * 
					 * @author Streamer
					 */
					listgroup_del.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							app.getAllMusicList().remove(index);
							// �������ݿ�
							SDSevice.clearList(app.getAllMusicList().get(index)
									.getListName());
							group_LongPress_Form.dismiss();
							UpdatePlayList();
						}
					});
					/**
					 * ����ý���б����¼���������б�
					 * 
					 * @author Streamer
					 */
					listgroup_clear.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							app.getAllMusicList().get(index).getList().clear();
							// �������ݿ�
							SDSevice.clearList(app.getAllMusicList().get(index)
									.getListName());
							UpdatePlayList();
							group_LongPress_Form.dismiss();
						}
					});
					/**
					 * ����ý���б����¼���ȡ������
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
					// ������
					chileLongPress_Form.showAsDropDown(arg1, 20, 10);
					/**
					 * �����б����е��������ӵ��б���
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
											.setTitle("��ӵ�")
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

															// �������ݿ�
															SDSevice.InsertSong(
																	(SongInfo) mainlistview
																			.getItemAtPosition(index),
																	main_music_list
																			.get(which)
																			.getListName());

														}
													})
											.setNegativeButton(
													"ȡ��",
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
					 * ȡ������
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
					 * �����б����е�������Ƴ��б���
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
								// �������ݿ�
								SDSevice.DeleteSong(
										app.getAllMusicList().get(groupid)
												.getList()
												.get(index - groupid - 1), app
												.getAllMusicList().get(groupid)
												.getListName());
								app.getAllMusicList().get(groupid).getList()
										.remove(index - groupid - 1);
								Log.i("remove", "&&" + groupid + "**" + index
										+ "�ɹ�");

								UpdatePlayList();
							}
							chileLongPress_Form.dismiss();
						}
					});// End
					/**
					 * �����б����е�������ʾý����Ϣ����
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
											.setTitle("������Ϣ")
											.setItems(
													new String[] {
															"�ļ�����"
																	+ s.getmFileTitle(),
															"ר������"
																	+ s.getmAlbum(),
															"��   �ͣ�"
																	+ s.getmFileType(),
															"��   �֣�"
																	+ s.getmSinger(),
															"ʱ   ����"
																	+ s.getmDuration()
																	+ "ms",
															"��   С��"
																	+ s.getmFileSize()
																	+ "byte",
															"�� �ݣ�"
																	+ s.getmYear()
																	+ "��",
															"· ����"
																	+ s.getmFilePath() },
													null)
											.setPositiveButton(
													"ȷ��",
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
				 * ����true������ϵͳ���ݰ�����Ϣ����ֹ��click��ͻ
				 */
				return true;
			}
		});// End

		/**
		 * ֮չ��һ���飬�ر�����չ������
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
		 * �����б��ϵ�չ���б��е���Ƶ����¼����� ���ŵ��������Ƶ����ҳ���л�������ҳ�棬�ı� ��Ӧ�ؼ�ͼ��
		 * 
		 * @author Streamer
		 */
		mainlistview.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				Log.i("������Ŀ·��", main_music_list.get(groupPosition).getList()
						.get(childPosition).getmFilePath());
				/*
				 * ��ȡ�����Ŀ
				 */
				curSong = main_music_list.get(groupPosition).getList()
						.get(childPosition);
				if (groupPosition != 1) {// ����Ĭ���б�ʱ
					// ��ӵ�Ĭ���б�
					app.getAllMusicList().get(1).getList().clear();
					// ��ӵ�Ĭ���б�
					app.getAllMusicList()
							.get(1)
							.addAll(main_music_list.get(groupPosition)
									.getList());
					// �������ݿ��Ӧ���б�
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
		 * ���������б������Ŀ����¼�
		 * 
		 * @author Streamer
		 */
		mainlistview.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				// TODO Auto-generated method stub
				/*
				 * ��Ҫ��������Ӧ��չ���������б�
				 */

				if (groupPosition == main_music_list.size() - 1) {
					/*
					 * ��������½��б�
					 */
					final EditText editnewlist = new EditText(RhythmPlayer.this);
					editnewlist.setHeight(70);
					editnewlist.setWidth(getWindowManager().getDefaultDisplay()
							.getWidth() - 60);
					editnewlist.setGravity(Gravity.LEFT);
					Dialog dialog = new AlertDialog.Builder(RhythmPlayer.this)
							.setTitle("�½��б���")
							.setView(editnewlist)
							.setNegativeButton("ȡ��",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub
											dialog.dismiss();
										}
									})
							.setPositiveButton("�½�",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub
											if (editnewlist.getText()
													.toString().isEmpty()) {
												showInfo("�б���ʧ�ܣ��б�������Ϊ�ա�");
											} else {
												MediaList<SongInfo> newlist = new MediaList<SongInfo>(
														editnewlist.getText()
																.toString(),
														0,
														new ArrayList<SongInfo>());
												if (app.newMusicListByListName(newlist)) {
													showInfo("�б����ɹ���");
													Toast.makeText(
															RhythmPlayer.this,
															"����"
																	+ editnewlist
																			.getText()
																			.toString()
																	+ "�б�������ݡ�������б��޷�����",
															Toast.LENGTH_LONG);
													// �����б�,����ȥ������Ϊ�޷�����
													SDSevice.UpdateList(
															new ArrayList<SongInfo>(),
															editnewlist
																	.getText()
																	.toString());
													UpdatePlayList();
												} else {
													showInfo("�б���ʧ�ܣ�");
												}
											}
											dialog.dismiss();
										}
									}).create();
					dialog.show();
					return true;
				}

				// Log.i("������",main_music_list.get(groupPosition).getListName());
				return false; // ����trueʱ�¼��˵�����ʾ
			}
		});
	}

	private void SeekBarListener() {
		/**
		 * �������ı��¼�
		 * 
		 * @author Streamer
		 */
		playTime_sb
				.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
					public void onProgressChanged(SeekBar seekbar,
							int progress, boolean fromUser) {
						// TODO Auto-generated method stub
						/*
						 * �û��ı����ʱ ��Ҫ��������Ӧ����λ����
						 */
						if (fromUser) {
							if (playService.getPlayerState()) {
								/*
								 * ���û�����������ʱ��ֻ�е�ǰ���ڲ���״̬�Ż���Ӧ
								 */
								playService.gotoTime(progress);
								Log.i("seekbar", "����");
							}
						}
					}

					public void onStartTrackingTouch(SeekBar arg0) {
						// TODO Auto-generated method stub
						// �û���ʼ����
					}

					public void onStopTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub
						// �û���������
					}
				});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ��������������View
		/*
		 * ��ʼ��ϵͳ����
		 */
		initialApp();
		showThemeView = getLayoutInflater().inflate(
				R.layout.activity_rhythm_player, null);
		/*
		 * ����������Ϊ�ޱ���
		 */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(showThemeView);
		/*
		 * ���������ֵı���
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
						showInfo("�����������ظ��......");
					} else {
						searLrcD = new AlertDialog.Builder(RhythmPlayer.this)
								.setIcon(R.drawable.b15)
								.setTitle("�������ظ��")
								.setMessage("��ȷ��Ҫ�������ظ�ʣ�")
								.setPositiveButton("ȷ��",
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
								.setNegativeButton("ȡ��",
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
	 * ��ʼ��Ӧ�ú���ص�ϵͳ����
	 */
	private void initialApp() {
		// ע��Ҫ���ȳ�ʼ��app
		app = (RhythmApp) getApplication();
		initializeSDS();// ��ʼ�����ݱ������
		initializePlayer();// ��ʼ��������
		notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
	}

	protected boolean initializeSDS() {
		boolean flag = false;
		SDSconnt = new ServiceConnection() {

			@Override
			public void onServiceDisconnected(ComponentName name) {
				// TODO Auto-generated method stub
				Log.i("SDS", "����ʧ��");
				SDSevice = null;
			}

			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				// TODO Auto-generated method stub
				Log.i("SDS", "���ӳɹ�");
				// ��ȡ���ݱ������
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
				Toast.makeText(getApplicationContext(), "����������ʧ�ܣ�������������",
						Toast.LENGTH_SHORT).show();
				playService = null;
			}

			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "��ӭʹ���ɶ����ֲ�����",
						Toast.LENGTH_SHORT).show();
				// ��ȡ���ŷ���
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
					song_Name_tv.setText("��ǰ���� :"
							+ curSong.getmFileTitle().substring(0,
									curSong.getmFileTitle().indexOf('.')));

				}

			}
		};

		// �󶨲��ŷ���
		flag = getApplicationContext().bindService(
				new Intent(RhythmPlayer.this, PlayService.class),
				serviceConnection, Service.BIND_AUTO_CREATE);

		return flag;
	}

	private void play(SongInfo curSong) {
		// TODO Auto-generated method stub

		// ������Ч
		showLrc(curSong, lrc.getCode());
		// ����
		playService.play(curSong);
		if (curSong.isEnable()) {
			showNotification(curSong, true);
			CURR_STATE = STATE_PLAY;
			toolbar_play
					.setBackgroundResource(music_toolbar_play_state[CURR_STATE]);
		} else {
			// ������
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
		Log.i("notifi", "����");
		lrc.setMaxTime(playService.getDuration());
		song_Name_tv.setText("��ǰ���� :"
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
		Log.i("play", "��������");
	}

	/**
	 * ��ʼ���ؼ�����
	 * 
	 * @author ������
	 */
	@SuppressWarnings("deprecation")
	private void initializeLayout() {
		// /////////////////////////////////////////////////////////////
		// �������ռ��ʼ��
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
		// �������ռ��ʼ��
		// /////////////////////////////////////////////////////////////
		toolbar_menu = (ImageButton) findViewById(R.id.toolbar_menu);
		toolbar_mode = (ImageButton) findViewById(R.id.toolbar_mode);
		toolbar_next = (ImageButton) findViewById(R.id.toolbar_next);
		toolbar_play = (ImageButton) findViewById(R.id.toolbar_play);
		toolbar_pre = (ImageButton) findViewById(R.id.toolbar_pre);
		// /////////////////////////////////////////////////////////////
		// �м䲿�֣�������ʾҳ����ؿռ��ʼ��1
		// /////////////////////////////////////////////////////////////
		music_Flipper = (ViewFlipper) findViewById(R.id.viewflipper);
		page_LRC = (LinearLayout) findViewById(R.id.Linear_songLRC);
		LrcView = new LyricView(page_LRC.getContext());
		LrcView.setLongClickable(true);
		// �����ʾ�ؼ�����ҳ����
		page_LRC.addView(LrcView);
		song_curposition_tv = (TextView) findViewById(R.id.showsongcurr_tv);
		song_Name_tv = (TextView) findViewById(R.id.showsongname_tv);
		sond_duration_tv = (TextView) findViewById(R.id.showsonglen_tv);
		playTime_sb = (SeekBar) findViewById(R.id.seekbar);
		// /////////////////////////////////////////////////////////////
		// �м䲿�֣����б�ҳ����ؿռ��ʼ��2
		// /////////////////////////////////////////////////////////////
		mainlistview = (ExpandableListView) findViewById(R.id.musicpage_mainListview);
		// �鳤��ʱ�����˵���ؿռ��ʼ��
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
		// �ӳ���ʱ�����˵���ؿռ��ʼ��
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
		// �м䲿�֣���ǰ�����б���ʾҳ����ؿռ��ʼ��3
		// /////////////////////////////////////////////////////////////
		curPlayListPage = (LinearLayout) findViewById(R.id.curPlayListPage);
		curList_Layout = getLayoutInflater().inflate(
				R.layout.currplaylist_layout, null);
		curListview = (ListView) curList_Layout.findViewById(R.id.currlistview);
		curPlayListPage.addView(curList_Layout);
		// /////////////////////////////////////////////////////////////
		// �м䲿�֣��ļ����ҳ����ؿռ��ʼ��4
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
		// �˵�������ؿռ��ʼ��
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
		// ��ʱ���˵���ؿռ��ʼ��
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
		// ����ѡ��˵���ؿռ��ʼ��
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
	 * ���¸��������б�&�б���&��Ӧ�Ĳ���,���»�ȡ�б��ϣ��͵�ǰ�б�
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
		Log.i("UpdatePlayList", "��������");
	}

	/**
	 * ���path�µ��ļ�
	 * 
	 * @param path
	 *            ���Ŀ¼
	 * @return �ļ��б���ǰĿ¼��
	 */
	private List<myFile> getFile(String path) {
		// TODO Auto-generated method stub
		// ����·��
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
						// ���ļ���
						id = R.drawable.ic_fold;
					} else if (filepath.endsWith("MP3")
							|| filepath.endsWith("mp3")) {
						// �����ļ�
						id = R.drawable.ic_mp3;
					} else if (filepath.endsWith("3gp")
							|| filepath.endsWith("3GP")
							|| filepath.endsWith("MP4")
							|| filepath.endsWith("mp4")) {
						id = R.drawable.ic_mp4;
					} else {
						// �ļ�
						id = R.drawable.ic_copyfile;
					}
					list.add(new myFile(filename, id, filepath));
				}
			}
			fileListAdapter = new fileAdapter(RhythmPlayer.this, list);
			Log.i("listLen", "����Ϊ		" + list.size());
			scanFile_lv.setAdapter(fileListAdapter);
			Log.i("fileAdapter", "����");
		}
		return list;
	}

	/**
	 * ���ݲ�������ǰ״̬���±������ĸ���ͼ��
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
	 * ���ݵ�ǰ�Ĳ�����Ŀ�Լ���ǰ�Ĳ����б��ȡ��һ��Ŀ�� ��Ҫע�⣬�÷��������Զ���ȡ��һ�ף��뵥��ѭ�����
	 * 
	 * @author Streamer
	 * @param Song
	 *            ��ǰ��Ŀ
	 * @return ��һ��Ŀ
	 * @see {@link RhythmPlayer#getNextSong(SongInfo)}
	 */
	protected SongInfo getNextSong(SongInfo Song) {
		// TODO Auto-generated method stub
		SongInfo nextSong = Song;
		Log.i("next", "����");
		if (MODE_CURR_STATE == MODE_LOOPALL) {
			// �б�ѭ��
			if (!curPlayList.isEmpty()) {
				int index1 = curPlayList.indexOf(Song) + 1;
				if (index1 < curPlayList.size() && index1 >= 0) {
					nextSong = curPlayList.get(index1);
				} else if (index1 == curPlayList.size()) {
					nextSong = curPlayList.get(0);
				} else {
					// nextSong.setEnable(false);
				}
				Log.i("next", "����LOOPALL" + nextSong.isEnable());
			}
		} else if (MODE_CURR_STATE == MODE_ORDER) {
			// ˳�򲥷Ż���ѭ��
			int index1 = curPlayList.indexOf(Song) + 1;
			if (index1 < curPlayList.size() && index1 > 0) {
				nextSong = curPlayList.get(index1);
			} else {
				// ����������
				nextSong.setEnable(false);
			}
			Log.i("next", "����order" + nextSong.isEnable());
		} else if (MODE_CURR_STATE == MODE_RANDOM) {
			// �������
			// ����ָ����Χ�������(m-n֮��)�Ĺ�ʽ[2]��Math.random()*(n-m)+m��
			int index1 = ((int) (Math.random() * curPlayList.size()) % curPlayList
					.size());
			Log.i("index", "" + index1);
			if (index1 >= 0 && index1 < curPlayList.size())
				nextSong = curPlayList.get(index1);
			else {
				nextSong.setEnable(false);
			}
			Log.i("next", "����Random" + nextSong.isEnable());
		} else if (MODE_CURR_STATE == MODE_LOOP) {
			nextSong = Song;
			Log.i("next", "����LOOP" + nextSong.isEnable());
		} else {
			nextSong.setEnable(false);
			Log.i("next", "����else" + nextSong.isEnable());
		}
		Log.i("next", "�˳�" + nextSong.isEnable());
		return nextSong;
	}

	/**
	 * ���ݵ�ǰ�Ĳ�����Ŀ�Լ���ǰ�Ĳ����б����ݲ���ģʽ��ȡǰһ��Ŀ��
	 * 
	 * @author Streamer
	 * @param Song
	 *            ��ǰ��Ŀ
	 * @return ��һ��Ŀ
	 * @see {@link RhythmPlayer#getPreSong(SongInfo)}
	 */
	protected SongInfo getPreSong(SongInfo Song) {
		// TODO Auto-generated method stub
		SongInfo preSong = Song;
		if (MODE_CURR_STATE == MODE_LOOPALL) {
			// �б�ѭ��
			int index = curPlayList.indexOf(Song);
			if (index == 0)
				preSong = curPlayList.get(curPlayList.size() - 1);
			else if (index > 0 && index < curPlayList.size())
				preSong = curPlayList.get(index - 1);
			else {
				preSong.setEnable(false);
			}
		} else if (MODE_CURR_STATE == MODE_ORDER) {
			// ˳�򲥷�
			int index = curPlayList.indexOf(Song) - 1;
			if (index >= 0 && index < curPlayList.size())
				preSong = curPlayList.get(index);
			else {
				preSong.setEnable(false);
			}
		} else if (MODE_CURR_STATE == MODE_RANDOM) {
			// �������
			// ����ָ����Χ�������(m-n֮��)�Ĺ�ʽ[2]��Math.random()*(n-m)+m��
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
	 * ����ָ��·����ý�壬���ı���Ӧ��״̬
	 * 
	 * @author Streamer
	 * @see {@link RhythmPlayer#changePlayState()}
	 */
	private void changePlayState() {

		ispause = !playService.getPlayerState();
		updatePlayBntEable();
		Log.i("MediaPath", curSong.getmFilePath());// ����������
		if (app.getAllMusicList().get(2).getList().lastIndexOf(curSong) == -1) {
			/*
			 * �����������������
			 */
			app.getAllMusicList().get(2).getList().add(curSong);
			// �������ݿ�
			SDSevice.InsertSong(curSong, app.getAllMusicList().get(2)
					.getListName());
			// �����б�ֻ�����exListview
			UpdatePlayList();
		}
	}

	private void updatePlayBntEable() {
		// TODO Auto-generated method stub
		// ���ø��ʱ��
		lrc.setMaxTime(playService.getDuration());
		int index = curPlayList.indexOf(curSong);
		if (index < curPlayList.size() - 1 || MODE_CURR_STATE == MODE_RANDOM) {
			toolbar_next.setEnabled(true); // ����
		} else {
			toolbar_next.setEnabled(false);
		}
		if (index > 0 || MODE_CURR_STATE == MODE_RANDOM) {
			toolbar_pre.setEnabled(true); // ����
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
				.setTitle("ϵͳ��Ϣ")
				.setMessage("�㽫Ҫ�˳���������")
				.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface paramDialogInterface,
							int paramInt) {
						paramDialogInterface.dismiss();
					}
				})
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
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
	 * ���������¼�
	 * 
	 * @author Streamer
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK: {
			/*
			 * ��������¼�
			 */

			if (music_Flipper.getCurrentView().getId() != R.id.page1) {
				/*
				 * �����ǰ���Ǵ��ڵ�һҳ�棬��ʾ��һҳ
				 */
				music_Flipper.showPrevious();
				change_titlebar_bnt_bg();
				return true;
			} else {
				/*
				 * �����ǰ���ڵ�һҳ�棬�����˳���ʾ����
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
