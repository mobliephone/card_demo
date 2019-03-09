package com.st.utils;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.widget.EditText;

import com.philliphsu.bottomsheetpickers.date.DatePickerDialog;
import com.st.view.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by cgw on 2017-10-25.
 * 日期选择框
 */

public class DateSelectUtils {

    public static void showDateSelect(final FragmentManager manager, final EditText editText, final Context context){
        DatePicker.showDateDialog(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
                dialog.setThemeDark(true);
                Calendar cal = new java.util.GregorianCalendar();
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, monthOfYear);
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String temp = sdf.format(cal.getTime());
                editText.setText(temp);
            }
        },context).show(manager,"");
    }
}
