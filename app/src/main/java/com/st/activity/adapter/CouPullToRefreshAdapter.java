package com.st.activity.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.st.R;
import com.st.db.entity.CourseEntity;

/**
 * 文 件 名: ProPullToRefreshAdapter
 * 创 建 人: Allen
 * 创建日期: 16/12/24 19:55
 * 邮   箱: AllenCoder@126.com
 * 修改时间：
 * 修改备注：
 */
public class CouPullToRefreshAdapter extends BaseQuickAdapter<CourseEntity, BaseViewHolder>
    implements  BaseQuickAdapter.OnItemChildClickListener{


    public CouPullToRefreshAdapter() {
        super(R.layout.item_project_info,null);
    }

    @Override
    protected void convert(BaseViewHolder helper, CourseEntity item) {

        helper.setText(R.id.pro_Name,item.getCoursesTask());
        helper.setText(R.id.pro_CourseName,item.getProName());
        helper.setText(R.id.pro_holdUnit,item.getStartDate().substring(0,10));
        helper.setText(R.id.pro_mane,item.getPlaceName());

        helper.addOnClickListener(R.id.pro_punchCard);

        if ("1".equals(item.getCredit())){
            helper.setImageResource(R.id.pro_Source,R.mipmap.credit_one);
        } else if ("2".equals(item.getCredit())){
            helper.setImageResource(R.id.pro_Source,R.mipmap.credit_two);
        } else if ("3".equals(item.getCredit())){
            helper.setImageResource(R.id.pro_Source,R.mipmap.credit_three);
        } else if ("4".equals(item.getCredit())){
            helper.setImageResource(R.id.pro_Source,R.mipmap.credit_four);
        } else if ("5".equals(item.getCredit())){
            helper.setImageResource(R.id.pro_Source,R.mipmap.credit_five);
        } else {
            helper.setImageResource(R.id.pro_Source,R.mipmap.credit_one);
        }

    }


    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

    }
}
