package com.st.activity.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.st.R;
import com.st.db.entity.StudyRegistrationEntity;

/**
 * 文 件 名: CardRecordAdapter
 * 创 建 人: Allen
 * 创建日期: 16/12/24 19:55
 * 邮   箱: AllenCoder@126.com
 * 修改时间：
 * 修改备注：
 */
public class CardRecordAdapter extends BaseQuickAdapter<StudyRegistrationEntity, BaseViewHolder>
    implements  BaseQuickAdapter.OnItemChildClickListener{


    public CardRecordAdapter() {
        super(R.layout.item_card_record,null);
    }

    @Override
    protected void convert(BaseViewHolder helper, StudyRegistrationEntity item) {

        helper.setText(R.id.record_userNumber,item.getEmpNumber());
        helper.setText(R.id.record_name,item.getRemarks1());
        helper.setText(R.id.record_date,item.getDakaTime().substring(0,10));

    }


    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

    }
}
