package com.st.activity.finger.help;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.framework.base.BaseActivity;
import com.st.R;


public class FPDisplay extends BaseActivity {
	public static final int FPDR_NONE   =0;
	public static final int FPDR_PASS   =1;
	public static Bitmap mImage = null;
	public static String msTitle = null;
	private ImageView mImgv = null;
	
	@Override
	public int getLayoutResId() {
		return R.layout.activity_fpdisplay;
	}

	@Override
	public void onInitView() {

		setTitle("指纹图片");
		Button passButton = findViewById(R.id.pass);

		mImgv = findViewById(R.id.img);

		if(mImage != null){
			mImgv.setImageBitmap(mImage);
		}

		passButton.setOnClickListener(new View.OnClickListener(){
			public void onClick(View view)
			{
				setResult(FPDR_PASS);
				finish();
			}
		});
	}

}
