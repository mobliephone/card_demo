package com.st.activity.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.st.R;
import com.st.db.entity.CreditRecordEntity;

/**
 * 文 件 名: ProPullToRefreshAdapter
 * 创 建 人: Allen
 * 创建日期: 16/12/24 19:55
 * 邮   箱: AllenCoder@126.com
 * 修改时间：
 * 修改备注：
 */
public class CrePullToRefreshAdapter extends BaseQuickAdapter<CreditRecordEntity, BaseViewHolder> {


    public CrePullToRefreshAdapter() {
        super(R.layout.item_project_info,null);
    }

    @Override
    protected void convert(BaseViewHolder helper, CreditRecordEntity item) {

        helper.setText(R.id.pro_Name,"核入状态 ："+item.getIsCheck());
        helper.setText(R.id.pro_holdUnit,"添加者 ："+item.getManName());
        helper.setText(R.id.pro_mane,"人员姓名 ：");
    }

}
