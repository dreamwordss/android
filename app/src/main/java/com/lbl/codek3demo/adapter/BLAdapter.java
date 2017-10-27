package com.lbl.codek3demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;
//泛型基础adapter
/**
 * author：libilang
 * time: 17/7/18 17:24
 * 邮箱：libi_lang@163.com
 */
public abstract class BLAdapter<T> extends BaseAdapter {

	public List<T> list = new ArrayList<>();
	public Context context;
	public LayoutInflater inflater;

	public BLAdapter(List<T> list, Context context) {
		this.list = list;
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list != null && !list.isEmpty() ? list.get(position) : 0;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		return CreatView(position, convertView, parent);
	}

	public abstract View CreatView(int position, View convertView,
								   ViewGroup parent);
}
