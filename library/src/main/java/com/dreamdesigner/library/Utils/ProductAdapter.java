package com.dreamdesigner.library.Utils;

import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class ProductAdapter<T> extends BaseAdapter {
	private List<T> mList;

	public abstract View getProductView(int position, View convertView,
			ViewGroup parent, T t);

	public ProductAdapter(List<T> list) {
		super();
		this.mList = list;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getProductView(position, convertView, parent,
				mList.get(position));
	}

	public void setList(List<T> list) {
		this.mList = list;
	}
}
