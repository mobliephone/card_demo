package com.st.activity.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.st.R;
import com.st.db.entity.EmployeeEntity;

/**
 * 人员管理信息
 */
public class EmpPullToRefreshAdapter extends BaseQuickAdapter<EmployeeEntity, BaseViewHolder> {


    public EmpPullToRefreshAdapter() {
        super(R.layout.item_project_info,null);
    }

    @Override
    protected void convert(BaseViewHolder helper, EmployeeEntity item) {

        helper.setText(R.id.pro_Name, "姓名：" + item.getUsername());
        helper.setText(R.id.pro_holdUnit, "性别：" + item.getSex());
        helper.setText(R.id.pro_mane, "证件号：" + item.getIdcard());

    }

}
