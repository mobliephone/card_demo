package com.st.view.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.st.R;

/**
 * 自定义输入框
 */
public class FloatingLable extends LinearLayout {

	final String TAG = getClass().getSimpleName();
	private EditText input;
	private TextView display;
	private LinearLayout.LayoutParams inputTextParams;
	private LinearLayout.LayoutParams displayTextParams;

	private boolean is1 = true;
	private boolean is2 = true;
	private Object object ;
	public FloatingLable(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (!isInEditMode()) {
			createLayout(attrs);
		}
	}

	public FloatingLable(Context context) {
		super(context);
		createLayout(null);
	}

	private void createLayout(AttributeSet attrs) {
        Context context = getContext();
		input = new EditText(context);
		display = new TextView(context);

		//利用权重分配布局
		inputTextParams = new LayoutParams(0, 70, 1.0f);
		displayTextParams = new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 70, 0f);

//		inpuTextParams = new LinearLayout.LayoutParams(
//				LayoutParams.WRAP_CONTENT,
//				DensityUtils.dp2px(context , 45));
//		displayTextParams = new LinearLayout.LayoutParams(
//				LayoutParams.WRAP_CONTENT,
//				LinearLayout.LayoutParams.WRAP_CONTENT);

		input.setLayoutParams(inputTextParams);
		//单行显示
		input.setSingleLine(true);
		display.setLayoutParams(displayTextParams);
		setOrientation(LinearLayout.HORIZONTAL);
		createDefaultLayout();

		if (attrs != null) {
			createCustomLayout(attrs);
		}

		//不加背景边框
//		input.setBackgroundResource(R.drawable.comm_input_background);
		input.setGravity(Gravity.CENTER_VERTICAL);
		input.setPadding( 8 , 0 , 0 , 0 );
		addView(display);
		addView(input);
	}

	private void createDefaultLayout() {
		input.setGravity(Gravity.LEFT | Gravity.TOP);
		display.setGravity(Gravity.LEFT);

		display.setTextColor(Color.BLACK);
		input.setTextColor(Color.BLACK);
		display.setPadding(5, 2, 5, 2);
	}

	private void createCustomLayout(AttributeSet attrs) {

		TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.FloatingLabel, 0, 0);
		// For Floating Hint

		String floatHintText = a.getString(R.styleable.FloatingLabel_floatHintText);
		ColorStateList floatHintTextColorFocused = a.getColorStateList(R.styleable.FloatingLabel_floatHintTextColorFocused);
		ColorStateList floatHintTextColorUnFocused = a.getColorStateList(R.styleable.FloatingLabel_floatHintTextColorUnFocused);
		int floatHintTextSize = a.getInt(R.styleable.FloatingLabel_floatHintTextSize, 18);
		String floatHintTextTypefaceName = a.getString(R.styleable.FloatingLabel_floatHintTextTypeface);
		int floatHintTextStyle = a.getInt(R.styleable.FloatingLabel_floatHintTextStyle, Typeface.NORMAL);
		int floatHintTextGravity = a.getInt(R.styleable.FloatingLabel_floatHintTextGravity, Gravity.LEFT);
		Drawable floatHintTextBackground = a.getDrawable(R.styleable.FloatingLabel_floatHintTextBackground);

		// For Actual Text
		String text = a.getString(R.styleable.FloatingLabel_text);
		ColorStateList textColor = a.getColorStateList(R.styleable.FloatingLabel_textColor1);
		int textSize = a.getInt(R.styleable.FloatingLabel_textSize1, 16);
		String textTypefaceName = a	.getString(R.styleable.FloatingLabel_textTypeface);
		int textStyle = a.getInt(R.styleable.FloatingLabel_textStyle, Typeface.NORMAL);
		int textNumber = a.getInt(R.styleable.FloatingLabel_textNumberStyle,0);
		Drawable textBackground = a.getDrawable(R.styleable.FloatingLabel_textBackground);

		boolean isPassword = a.getBoolean(R.styleable.FloatingLabel_isPassword,false);
		boolean isEditable = a.getBoolean(R.styleable.FloatingLabel_isEditable,true);
		is1 = isEditable;
		boolean isFocusableInTouchMode = a.getBoolean(R.styleable.FloatingLabel_isFocusableInTouchMode,true);
		is2 = isFocusableInTouchMode;
		a.recycle();

		setFloatHintText(floatHintText);
//		setFloatHintTextColor(getColorStateList(floatHintTextColorFocused,floatHintTextColorUnFocused));
		//设置文字样式
		setFloatHintTextColor(textColor);

		setFloatHintTextSize(floatHintTextSize);
		setFloatHintTypeFace(floatHintTextTypefaceName, floatHintTextStyle);
		setFloatHintGravity(floatHintTextGravity);
		setFloatHintTextBackGround(floatHintTextBackground);

		//setTextHint(floatHintText);
//		setTextColor(textColor);
		//设置文字样式
		setTextColor(getColorStateList(floatHintTextColorFocused,floatHintTextColorUnFocused));

		setTextSize(textSize);
		setTextTypeFace(textTypefaceName, textStyle);
		setTextGravity(textNumber);
		setText(text);
		setTextBackGround(textBackground);
		setPassword(isPassword);
		setEditableAndisFocusableInTouchMode(isEditable,isFocusableInTouchMode);
	}

	public void setEnable(boolean isEnable){
			input.setEnabled(isEnable);
	}

	public void setEditableAndisFocusableInTouchMode(boolean isEditable,
													 boolean isFocusableInTouchMode) {
			input.setEnabled(isEditable);
			input.setFocusableInTouchMode(isFocusableInTouchMode);
	}

	private ColorStateList getColorStateList(ColorStateList focused,
                                             ColorStateList unfocused) {
		int[][] states = new int[][] {
				new int[] { android.R.attr.state_selected }, // selected
				new int[] {} // default
		};

		int[] colors = new int[] {
				(focused != null) ? focused.getDefaultColor() : Color.BLACK,
				(unfocused != null) ? unfocused.getDefaultColor() : Color.GRAY };

		return new ColorStateList(states, colors);
	}

	/** FOR LABEL **/
	private void setFloatHintGravity(int floatHintTextGravity) {
		display.setGravity(floatHintTextGravity);
	}

	private void setFloatHintTypeFace(String floatHintTextTypefaceName,
			int floatHintTextStyle) {
        try {
            Typeface face = Typeface.createFromAsset(getContext().getAssets(),
                    floatHintTextTypefaceName);
            display.setTypeface(face);
		} catch (Exception e) {
			display.setTypeface(null, floatHintTextStyle);
		}
	}

	private void setFloatHintTextSize(int floatHintTextSize) {
		display.setTextSize(TypedValue.COMPLEX_UNIT_SP, floatHintTextSize);
	}

	private void setFloatHintTextColor(ColorStateList floatHintTextColor) {
		if (floatHintTextColor != null) {
			display.setTextColor(floatHintTextColor);
		}
	}

	public void setFloatHintText(String string) {
		if (string != null) {
			display.setText(string);
		}
	}

	private void setFloatHintTextBackGround(Drawable textBackground) {
		input.setBackgroundDrawable(textBackground);
	}

	/** FOR TEXT **/

	private void setPassword(boolean isPassword) {
		// TODO Auto-generated method stub
		if (isPassword) {
			input.setTransformationMethod(PasswordTransformationMethod
					.getInstance());
		}
	}

	private void setTextBackGround(Drawable textBackground) {
		input.setBackgroundDrawable(textBackground);
	}

	private void setTextGravity(int textNumber) {
		if(textNumber == 1){
			input.setInputType(InputType.TYPE_CLASS_NUMBER);
		}else if(textNumber == 2){
			input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
		}else if( textNumber == 3 ){
			input.setInputType(InputType.TYPE_CLASS_PHONE);
		}
	}

	private void setTextTypeFace(String textTypefaceName, int textStyle) {
		try {
			Typeface face = Typeface.createFromAsset(getContext().getAssets(),
					textTypefaceName);
			display.setTypeface(face);
		} catch (Exception e) {
			display.setTypeface(null, textStyle);
		}
	}

	private void setTextSize(int textSize) {
		input.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
	}

	private void setTextColor(ColorStateList textColor) {
		if (textColor != null) {
			input.setTextColor(textColor);
		}
	}

	public void setText(String string) {
		if (string != null) {
			input.setText(string);
		}
	}

	public void setTextHint(String hintText) {
		if (hintText != null) {
			input.setHint(hintText);
		}
	}

	public String getText() {
		return input.getText().toString();
	}

	public EditText getEditText() {
		return input;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if(is1 && is2){
			return false;
		}
		return true;
	}

	public boolean isIs1() {
		return is1;
	}

	public void setIs1(boolean is1) {
		this.is1 = is1;
	}

	public boolean isIs2() {
		return is2;
	}

	public void setIs2(boolean is2) {
		this.is2 = is2;
	}

	public Object getObject() {
		if( null == object ){
			return "";
		}
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public TextView getDisplayTextView() {
		return display;
	}
}
