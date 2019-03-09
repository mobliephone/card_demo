package com.st.view;


import android.content.Context;
import android.support.v4.app.DialogFragment;

import com.philliphsu.bottomsheetpickers.BottomSheetPickerDialog;
import com.philliphsu.bottomsheetpickers.date.DatePickerDialog;
import com.st.R;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by cgw on 2017-10-25.
 * 日期选择弹出框
 */

public class DatePicker {

    public static DialogFragment showDateDialog(DatePickerDialog.OnDateSetListener listener, Context context){

        Calendar now = Calendar.getInstance();
        Calendar max = Calendar.getInstance();
        max.add(Calendar.YEAR, 1);
//        max.add(Calendar.MONTH,0);
//        max.add(Calendar.DAY_OF_MONTH,1);

        BottomSheetPickerDialog.Builder builder  = new DatePickerDialog.Builder(
                listener,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH));

        DatePickerDialog.Builder dateDialogBuilder = (DatePickerDialog.Builder) builder;

        Calendar min = Calendar.getInstance() ;
        min.setTime(new Date());//把当前时间赋给日历
        min.add(Calendar.DAY_OF_MONTH, -1);  //设置为前一天
//        Year end must be larger than year start
//        dateDialogBuilder.setMaxDate(max).setMinDate(min);

        builder.setThemeDark( false ) ;
        //设置头部颜色
        builder.setHeaderColor(context.getResources().getColor(R.color.colorPrimary));
        //设置日期选择显示的颜色
        builder.setAccentColor(context.getResources().getColor(R.color.colorPrimary));
        return dateDialogBuilder.build() ;
    }
}
