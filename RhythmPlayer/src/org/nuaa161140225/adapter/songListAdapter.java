/**
 * 
 */
package org.nuaa161140225.adapter;

import java.util.ArrayList;
import java.util.List;

import org.nuaa161140225.dataclass.SongInfo;

import org.nuaa161140225.rhythmplayer.R;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author 123
 * 
 */
public class songListAdapter extends BaseAdapter {

	/**
	 * 用于填充该音乐列表的数据。
	 * 
	 * @see {@link SongInfoListAdapter#songinfolist}
	 */
	private List<SongInfo> songinfolist = new ArrayList<SongInfo>();
	/**
	 * 上下文，用于获取布局
	 * 
	 * @see {@link SongInfoListAdapter#context}
	 */
	private Context context;

	public songListAdapter(Context context, List<SongInfo> curr_music_list) {
		super();
		this.context = context;
		this.songinfolist = curr_music_list;
		Log.i("SongInfoListAdapter", "num" + songinfolist.size());
	}

	/**
	 * 获取列表数据的个数
	 * 
	 * @author 吴香礼
	 * @return 数据个数
	 * @see {@link SongInfoListAdapter#getCount()}
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return songinfolist.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * 自定义布局信息，用自定义布局文件来填充。
	 * 
	 * @author 吴香礼
	 * @see {@link SongInfoListAdapter#getView(int, View, ViewGroup)}
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (position <= songinfolist.size()) {
			View view = ((Activity) context).getLayoutInflater().inflate(
					R.layout.songinfo_show_layout, null);
			ImageView imageview = (ImageView) view
					.findViewById(R.id.songinfo_imgv);
			TextView SongName_textview = (TextView) view
					.findViewById(R.id.songinfo_nameTv);
			TextView SongArtist_textview = (TextView) view
					.findViewById(R.id.songinfo_singerTv);

			SongName_textview.setText(songinfolist.get(position)
					.getmFileTitle().substring(0, songinfolist.get(position)
					.getmFileTitle().indexOf('.')));
			SongArtist_textview
					.setText(songinfolist.get(position).getmSinger());
			imageview.setBackgroundResource(R.drawable.ic_mp3);

			return view;
		} else
			return null;
	}

	/**
	 * 设置音乐列表
	 * 
	 * @author 吴香礼
	 * @param list
	 *            新的音乐列表
	 */
	public void setList(List<SongInfo> list) {
		// this.videoinfolist.clear();
		this.songinfolist = list;
	}
}
