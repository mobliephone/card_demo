package com.st.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.st.R;

/**
 * Created by Administrator on 2017-12-04.
 * 底部提示框
 */

public class BottomDialogUtils {

    private static Dialog bottomDialog;
    private static BottomDialogUtils instance;
    public static BottomDialogUtils getInstance(){
        if (instance == null){
            instance = new BottomDialogUtils();
        }
        return instance;
    }

    /**
     * 自定义底部提示框
     * @param context
     */
    public void bottomDialog(Context context, String info, float textSize){
        bottomDialog = new Dialog(context, R.style.BottomDialog);
        View contentView = LayoutInflater.from(context).inflate(R.layout.item_bottom_dialog, null);
        TextView textView = contentView.findViewById(R.id.bottom_text);
        textView.setText(info);
        textView.setTextSize(textSize);
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = context.getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);
        Window bottomWindow = bottomDialog.getWindow();
        bottomWindow.setGravity(Gravity.BOTTOM);
//        bottomDialog.setCanceledOnTouchOutside(true);
        bottomWindow.setWindowAnimations(R.style.BottomDialog_Animation);
        bottomDialog.show();
    }

    /**
     * 关闭弹出框
     */
    public static void dismiss() {
        if (bottomDialog != null && bottomDialog.isShowing()) {
            bottomDialog.dismiss();
        }
    }


    public boolean isDialogShowing(){

        if (bottomDialog !=null){
            if (bottomDialog.isShowing()){
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


}
