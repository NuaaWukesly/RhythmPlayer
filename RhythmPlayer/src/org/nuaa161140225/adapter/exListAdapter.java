/**
 * 
 */
package org.nuaa161140225.adapter;

import java.util.List;

import org.nuaa161140225.dataclass.MediaList;
import org.nuaa161140225.dataclass.SongInfo;
import org.nuaa161140225.rhythmplayer.R;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author 123
 * 
 */
public class exListAdapter extends BaseExpandableListAdapter {

	private Context context;
	private List<MediaList<SongInfo>> music_class_list;

	public exListAdapter(Context context, List<MediaList<SongInfo>> paramList) {
		this.context = context;
		this.music_class_list = paramList;
	}

	public Object getChild(int paramInt1, int paramInt2) {
		return ((MediaList) this.music_class_list.get(paramInt1)).getList()
				.get(paramInt2);
	}

	public long getChildId(int paramInt1, int paramInt2) {
		return 0L;
	}

	public View getChildView(int paramInt1, int paramInt2,
			boolean paramBoolean, View paramView, ViewGroup paramViewGroup) {
		View localView = ((Activity) this.context).getLayoutInflater().inflate(
				R.layout.songinfo_show_layout, null);
		ImageView migv = ((ImageView) localView
				.findViewById(R.id.songinfo_imgv));
		TextView nameTv = (TextView) localView
				.findViewById(R.id.songinfo_nameTv);
		TextView singerTv = (TextView) localView
				.findViewById(R.id.songinfo_singerTv);
		String songName = ((SongInfo) ((MediaList) this.music_class_list
				.get(paramInt1)).getList().get(paramInt2)).getmFileTitle();
		nameTv.setText(songName.subSequence(0, songName.indexOf('.')));
		singerTv.setText(((SongInfo) ((MediaList) this.music_class_list
				.get(paramInt1)).getList().get(paramInt2)).getmSinger());
		return localView;
	}

	public int getChildrenCount(int paramInt) {
		return ((MediaList) this.music_class_list.get(paramInt)).getList()
				.size();
	}

	public Object getGroup(int paramInt) {
		return this.music_class_list.get(paramInt);
	}

	public int getGroupCount() {
		return this.music_class_list.size();
	}

	public long getGroupId(int paramInt) {
		return 0L;
	}

	public View getGroupView(int paramInt, boolean paramBoolean,
			View paramView, ViewGroup paramViewGroup) {
		View localView = ((Activity) this.context).getLayoutInflater().inflate(
				R.layout.music_mainlist_exlist_group, null);
		ImageView imgv = ((ImageView) localView
				.findViewById(R.id.mainlist_exlist_imageview));
		TextView listName = (TextView) localView
				.findViewById(R.id.mainlist_exlist_listname);
		TextView songNum = (TextView) localView
				.findViewById(R.id.mainlist_exlist_num);
		listName.setText(((MediaList<SongInfo>) this.music_class_list.get(paramInt))
				.getListName());
		if (paramInt < -1 + this.music_class_list.size()) {
			songNum.setText(((MediaList<SongInfo>) this.music_class_list.get(paramInt))
					.getList().size() + "Ê×¸èÇú");
			return localView;
		}
		songNum.setText("");
		return localView;
	}

	public boolean hasStableIds() {
		return false;
	}

	public boolean isChildSelectable(int paramInt1, int paramInt2) {
		return true;
	}

	public void onGroupExpanded(int paramInt) {
		for (int i = 0;; i++) {
			if (i >= getGroupCount()) {
				super.onGroupExpanded(paramInt);
				return;
			}
			if (i == paramInt)
				continue;
			onGroupCollapsed(i);
		}
	}

	public void setMediaList(List<MediaList<SongInfo>> paramList) {
		this.music_class_list = paramList;
	}

}
