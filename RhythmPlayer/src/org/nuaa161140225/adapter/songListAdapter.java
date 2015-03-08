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
	 * �������������б�����ݡ�
	 * 
	 * @see {@link SongInfoListAdapter#songinfolist}
	 */
	private List<SongInfo> songinfolist = new ArrayList<SongInfo>();
	/**
	 * �����ģ����ڻ�ȡ����
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
	 * ��ȡ�б����ݵĸ���
	 * 
	 * @author ������
	 * @return ���ݸ���
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
	 * �Զ��岼����Ϣ�����Զ��岼���ļ�����䡣
	 * 
	 * @author ������
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
	 * ���������б�
	 * 
	 * @author ������
	 * @param list
	 *            �µ������б�
	 */
	public void setList(List<SongInfo> list) {
		// this.videoinfolist.clear();
		this.songinfolist = list;
	}
}
