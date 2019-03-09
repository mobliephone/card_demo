package com.st.activity.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.st.R;
import com.st.db.entity.ProjectEntity;

/**
 * 文 件 名: ProPullToRefreshAdapter
 * 创 建 人: Allen
 * 创建日期: 16/12/24 19:55
 * 邮   箱: AllenCoder@126.com
 * 修改时间：
 * 修改备注：
 */
public class ProPullToRefreshAdapter extends BaseQuickAdapter<ProjectEntity, BaseViewHolder> {


    public ProPullToRefreshAdapter() {
        super(R.layout.item_project_info,null);
    }

    @Override
    protected void convert(BaseViewHolder helper, ProjectEntity item) {

        helper.setText(R.id.pro_Name,"项目名称："+item.getProName());
        helper.setText(R.id.pro_holdUnit,"主办单位："+item.getHoldUnit());
        helper.setText(R.id.pro_mane,"负责人："+item.getMane());
    }

}
