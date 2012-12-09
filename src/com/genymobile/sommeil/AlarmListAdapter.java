package com.genymobile.sommeil;

import java.util.List;

import android.content.Context;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AlarmListAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<Alarm> mAlarm;

	public AlarmListAdapter(Context context) {
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return mAlarm.size();
	}

	@Override
	public Object getItem(int position) {
		return mAlarm.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		AlarmViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.row, null);
			holder = new AlarmViewHolder();
			holder.mTitle=(TextView) convertView.findViewById(R.id.reveiltitle);
			convertView.setTag(holder);
		}else{
			holder = (AlarmViewHolder) convertView.getTag();
			
		}
		holder.setTitle(mAlarm.get(pos).getHeure());
		return convertView;
	}

	public class AlarmViewHolder {
		private TextView mTitle;

		public void setTitle(Time time) {
			mTitle.setText((CharSequence) time);

		}
	}
}
