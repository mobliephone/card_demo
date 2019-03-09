package com.st.activity.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.st.R;
import com.st.db.entity.EmployeeEntity;

/**
 * 文 件 名: ProPullToRefreshAdapter
 * 创 建 人: Allen
 * 创建日期: 16/12/24 19:55
 * 邮   箱: AllenCoder@126.com
 * 修改时间：
 * 修改备注：
 */
public class WriteAdapter extends BaseQuickAdapter<EmployeeEntity, BaseViewHolder> {


    public WriteAdapter() {
        super(R.layout.item_search_course,null);
    }

    @Override
    protected void convert(BaseViewHolder helper, EmployeeEntity item) {

        helper.setText(R.id.search_CourseName,"姓名:"+item.getUsername()+"   医通卡号:"+item.getUsernumber());
    }



}
