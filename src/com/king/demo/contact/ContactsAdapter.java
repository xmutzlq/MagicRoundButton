package com.king.demo.contact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.king.demo.R;
import com.king.demo.contact.contacts.Contact;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class ContactsAdapter extends BaseAdapter implements StickyListHeadersAdapter, SectionIndexer {

	private Context mContext;
	private List<Contact> mInfos;
	private String[] sectionHeaders;
	private int[] sectionIndices;
	private Map<String, String> letterMap;

	public ContactsAdapter(Context context, List<Contact> infos) {
		mContext = context;
		mInfos = infos;
		letterMap = new HashMap<String, String>();
	}

	public int getLetterPosition(String letter){
		int position = letterMap != null && !TextUtils.isEmpty(letterMap.get(letter)) ? 
        		Integer.valueOf(letterMap.get(letter)) : -1;
        Log.e("zlq", "letter = " + letter + ", position = " + position);
        return position;
    }
	
	private int[] getSectionIndices() {
		List<String> hasCompare = new ArrayList<String>();
		List<Integer> sectionIndices = new ArrayList<Integer>();
		String lastCreateDate = String.valueOf(
				HanziToPinyin.getPinYin(mInfos.get(0).getDisplayName()).toLowerCase().charAt(0));
		sectionIndices.add(0);
		hasCompare.add(lastCreateDate);
		for (int i = 1; i < mInfos.size(); i++) {
			String createDate = String.valueOf(
					HanziToPinyin.getPinYin(mInfos.get(i).getDisplayName()).toLowerCase().charAt(0));
			if (hasCompare.size() > 0 && !hasCompare.contains(createDate)) {
				hasCompare.add(createDate);
				sectionIndices.add(i);
			}
		}
		int[] sections = new int[sectionIndices.size()];
		for (int i = 0; i < sectionIndices.size(); i++) {
			sections[i] = sectionIndices.get(i);
		}
		return sections;
	}

	private String[] getSectionHeaders() {
		String[] sectionHeaders = new String[sectionIndices.length];
		for (int i = 0; i < sectionIndices.length; i++) {
			sectionHeaders[i] = String.valueOf(
					HanziToPinyin.getPinYin(mInfos.get(sectionIndices[i]).getDisplayName()).toUpperCase().charAt(0));
			Log.e("zlq", "uu = " + sectionHeaders[i] + ", yy = " + sectionIndices[i]);
			letterMap.put(sectionHeaders[i], String.valueOf(sectionIndices[i]));
		}
		return sectionHeaders;
	}

	public void appendToTopList(ArrayList<Contact> list) {
		if(mInfos == null || list == null || list.size() == 0) {
			return;
		}
		Collections.sort(list, new ContactsComparator());
		this.mInfos.addAll(list);
		sectionIndices = getSectionIndices();
		sectionHeaders = getSectionHeaders();
		notifyDataSetChanged();
	}

	public void clear() {
		mInfos.clear();
		sectionIndices = null;
		sectionHeaders = null;
	}

	@Override
	public int getCount() {
		return mInfos.size();
	}

	@Override
	public Object getItem(int position) {
		return mInfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.contact_item, null);
			holder.contactName = (TextView) convertView.findViewById(R.id.contact_item_left_tv);
			holder.contactPhoneNum = (TextView) convertView.findViewById(R.id.contact_item_right_tv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Contact info = mInfos.get(position);

		holder.contactName.setText(info.getDisplayName());
		holder.contactPhoneNum.setText(info.getPhoneNumbers().get(0).getNumber());
		return convertView;
	}

	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.contact_group, null);
		}
		TextView tv = (TextView) convertView.findViewById(R.id.contact_group_left_tv);
		tv.setText(String.valueOf(HanziToPinyin.getPinYin(mInfos.get(position).getDisplayName()).toUpperCase().charAt(0)));
		return convertView;
	}

	@Override
	public long getHeaderId(int position) {
		return Long.valueOf(HanziToPinyin.getPinYin(mInfos.get(position).getDisplayName()).toUpperCase().charAt(0));
	}

	private class ViewHolder {
		private TextView contactName;
		private TextView contactPhoneNum;
	}

	@Override
	public Object[] getSections() {
		return sectionHeaders;
	}

	@Override
	public int getPositionForSection(int sectionIndex) {
		if (sectionIndex >= sectionIndices.length) {
			sectionIndex = sectionIndices.length - 1;
		} else if (sectionIndex < 0) {
			sectionIndex = 0;
		}
		return sectionIndices[sectionIndex];
	}

	@Override
	public int getSectionForPosition(int position) {
		for (int i = 0; i < sectionIndices.length; i++) {
			if (position < sectionIndices[i]) {
				return i - 1;
			}
		}
		return sectionIndices.length - 1;
	}
}
