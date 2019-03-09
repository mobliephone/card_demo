package com.framework.adpter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class BaseViewAdapter<T> extends BaseAdapter {
	protected Context context;
	private List<T> list;
	private int viewId;
	private int selectedPosition = -1;
	public BaseViewAdapter(Context context){
		this.context = context;
	}
	/**
	 * @param context
	 * @param list
	 */
	public BaseViewAdapter(Context context, List<T> list) {
		super();
		this.context = context;
		this.list = list;
	}
	
	public void addAll(Collection<T> collection) {
		this.list.addAll(collection);
		updateListViewUI();
	}
	
	public void add(T item) {
		if( list == null ){
			list = new ArrayList<>() ;
		}
		list.add(item);
		updateListViewUI();
	}
	
	public void add(int position, T item) {
		list.add(position, item);
		updateListViewUI();
	}
	
	public void addFirst(T item) {
		list.add(0, item);
		updateListViewUI();
	}
	
	public void addLast(T item) {
		add(item);
		updateListViewUI();
	}
	
	public void remove(int ...positions) {
		for(int i = 0; positions != null && i < positions.length; i++) {
			list.remove(positions[i]);
		}
		updateListViewUI();
	}
	
	public void remove(T obj) {
		list.remove(obj);
		updateListViewUI();
	}
	
	public void resetData(List<T> list) {
		this.list = list;
		updateListViewUI();
	}
	
	public void removeAll() {
		list.clear();
		updateListViewUI();
	}
	
	public void updateListViewUI() {
		notifyDataSetChanged();
	}
	
	public void setSelectedPosition(int position) {
		selectedPosition = position;
	}

	public int getSelectedPosition() {
		return selectedPosition;
	}
	
	public void update(int position, T item) {
		list.set(position, item);
		updateListViewUI();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public T getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		AbsViewHolder viewHolder = null;
		
		if(convertView == null) {
			viewId = getLayoutId() ;
			convertView = LayoutInflater.from(context).inflate(viewId, parent, false);
			viewHolder = getViewHolder(convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (AbsViewHolder) convertView.getTag();
		}
		initListItemView(position, viewHolder, parent, getItem(position));
		
		return convertView;
	}

	public abstract int getLayoutId();

	public abstract AbsViewHolder getViewHolder(View convertView);
	
	public static class AbsViewHolder {};
	
	protected abstract void initListItemView(int position, AbsViewHolder absViewHolder, ViewGroup parent, T item);

	public List<T> getList() {
		return list;
	}

	@Override
	public boolean areAllItemsEnabled() {
		return false ;
	}

	@Override
	public boolean isEnabled(int position) {
		return true ;
	}
}
