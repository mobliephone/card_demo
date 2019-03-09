package com.framework.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.framework.view.holder.SuperItemHolder;
import com.framework.view.listener.OnItemClickListener;

import java.util.List;

/**
 * ========================================
 * 版 权：dou361.com 版权所有 （C） 2015
 * 作 者：陈冠明
 * 个人网站：http://www.dou361.com
 * 版 本：1.0
 * 创建日期：2016/10/5 17:54
 * 描 述：RecyclerView的Adapter的基类
 * 修订历史：
 * ========================================
 */
public abstract class SuperAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    /**
     * 上下文
     */
    protected Context mContext;
    /**
     * 接收传递过来的数据
     */
    protected List<T> mDatas;
    /**
     * 获得holder
     */
    private SuperItemHolder baseHolder;
    protected OnItemClickListener mListener;

    public SuperAdapter(Context mContext, List<T> mDatas) {
        this.mContext = mContext;
        setmDatas(mDatas);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return getItemHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder != null) {
            baseHolder = (SuperItemHolder) holder;
            baseHolder.setPosition(position);
            baseHolder.setData(mDatas.get(position), countPosition(position));
        }
    }

    /**
     * 1top 2midle 3bottom 4all
     */
    protected int countPosition(int position) {
        return 2;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public List<T> getmDatas() {
        return mDatas;
    }

    public void setmDatas(List<T> mDatas) {
        this.mDatas = mDatas;
    }

    /**
     * 获得Holder
     */
    public abstract SuperItemHolder getItemHolder(ViewGroup parent, int viewType);

    /**
     * 设置Item点击监听
     *
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

}