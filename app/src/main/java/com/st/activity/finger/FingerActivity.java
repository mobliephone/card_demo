package com.st.activity.finger;

/**
 * Sample showing finger enrollment, identification, and removal.
 *
 * 指纹录入
 */

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.framework.json.J;
import com.framework.util.DateUtils;
import com.framework.util.NetWorkUtil;
import com.framework.util.StringUtils;
import com.framework.view.DialogUIUtils;
import com.framework.view.listener.DialogUIItemListener;
import com.st.CMEApplication;
import com.st.R;
import com.st.activity.adapter.WriteAdapter;
import com.st.activity.finger.help.FingerId;
import com.st.activity.finger.help.OpEnroll;
import com.st.activity.finger.inter.IAddFingerCallback;
import com.st.db.DBUtils;
import com.st.db.dao.FingerPrintDao;
import com.st.db.entity.EmployeeEntity;
import com.st.db.entity.FingerPrint;
import com.st.nfc.BasicActivity;
import com.st.nfc.MCReader;
import com.st.nfc.activity.DumpEditorActivity;
import com.st.nfc.activity.KeyMapCreatorActivity;
import com.st.nfc.activity.PreferencesActivity;
import com.st.persenter.ICallback;
import com.st.persenter.MainPresenter;
import com.st.service.request.RequestConfig;
import com.st.utils.BottomDialogUtils;
import com.st.utils.HintsManager;
import com.st.view.widget.FloatingLable;
import com.upek.android.ptapi.PtConnectionAdvancedI;
import com.upek.android.ptapi.PtConstants;
import com.upek.android.ptapi.PtException;
import com.upek.android.ptapi.PtGlobal;
import com.upek.android.ptapi.struct.PtInfo;
import com.upek.android.ptapi.struct.PtSessionCfgV5;
import com.upek.android.ptapi.usb.PtUsbHost;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.st.nfc.activity.PreferencesActivity.Preference.UseInternalStorage;

public class FingerActivity extends BasicActivity {

	// This variable configures support for STM32 area reader. This sensor
	// requires additional data storage (temporary file)
	// On emulated environment (emulator + bridge) it must be set to zero, so
	// the default setting will be used
	// On real Android device the default place for storage doesn't work as it
	// must be detected in runtime
	// Set this variable to 1 to enable this behavior
	public static final int miRunningOnRealHardware = 1;

	private PtGlobal mPtGlobal = null;
	private PtConnectionAdvancedI mConn = null;
	private PtInfo mSensorInfo = null;
	private Thread mRunningOp = null;
	private final Object mCond = new Object();

	// will contain path for temporary files on real Android device
	private String msNvmPath = null;


	@BindView(R.id.editTextWriteTagData)	FloatingLable mDataText;
	@BindView(R.id.write_cardRecycler)  	RecyclerView writeRecyclerView;
	@BindView(R.id.linearLayoutCreateKeyMapKeyFiles)  	LinearLayout mKeyFilesGroup;
    @BindView(R.id.select_finger)  	Button selectFinger;
	private WriteAdapter mAdapter;
	//课题
	private List<EmployeeEntity> listEntity;
	//请求控制类
	private MainPresenter mainPresenter;
	private DBUtils instance;
	private CMEApplication app;

	private FingerPrintDao fingerPrintDao;
	private String usernumber;

	private HintsManager hintsManager;


	/**NFC*/
	// Input parameters.
	/**
	 * Path to a directory with key files. The files in the directory
	 * are the files the user can choose from. This must be in the Intent.
	 *
	 * 带有密钥文件的目录路径。目录中的文件
	 * 用户可以从中选择的文件。这一定是出于意图。
	 */
	public final static String EXTRA_KEYS_DIR =
			"de.syss.MifareClassicTool.Activity.KEYS_DIR";

	private static final String LOG_TAG = KeyMapCreatorActivity.class.getSimpleName();

	private final Handler mHandler = new Handler();
	private int mProgressStatus;
	private boolean mIsCreatingKeyMap;
	private File mKeyDirPath;
	private int mFirstSector = 1;
	private int mLastSector = 3;

	//医通卡唯一ID
	private String onlyTagID;



	/** Initialize activity and obtain PTAPI session. */
	@Override
	public int getLayoutResId() {
		return R.layout.activity_finger_print;
	}

	@Override
	public void onInitView() {
		ButterKnife.bind(this);

		if (miRunningOnRealHardware != 0) {
			// find the directory for temporary files
			Context aContext = getBaseContext();
			if (aContext != null) {
				File aDir = aContext.getDir("tcstore",
						Context.MODE_WORLD_READABLE
								| Context.MODE_WORLD_WRITEABLE);
				if (aDir != null) {
					msNvmPath = aDir.getAbsolutePath();
				}
			}
		}

		// Load PTAPI library and initialize its interface
		if (initializePtapi()) {
			// Open PTAPI session
			openPtapiSession();
		}

		setTitle("指纹录入");
		app = (CMEApplication) getApplication();
		mainPresenter = new MainPresenter();
		fingerPrintDao = app.getDaoSeeion().getFingerPrintDao();

		hintsManager = HintsManager.getInstance();

		//初始化查询结果界面
		initRecycler();

	}


	/**
	 * 初始化RecyclerView
	 */
	private void initRecycler() {

		instance = DBUtils.getInstance(this, app);
		mAdapter = new WriteAdapter();
		writeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
		writeRecyclerView.setAdapter(mAdapter);


		mDataText.getEditText().addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void afterTextChanged(Editable editable) {
				//医通卡号
				String userNumber = mDataText.getEditText().getText().toString();
				if (NetWorkUtil.isNetWorkAvailable(FingerActivity.this)){
					//在线查询
					searchEmployee(userNumber);
				} else {
					//离线查询
					outLineSearch(userNumber);
				}
			}
		});

		//RecyclerView的item点击事件
		writeRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
			@Override
			public void onSimpleItemClick(final BaseQuickAdapter adapter, final View view, final int position) {
				//打卡点击事件
				usernumber = listEntity.get(position).getUsernumber();
				mDataText.getEditText().setText(usernumber);
			}
		});
	}


	/** Close PTAPI session. */
	@Override
	protected void onDestroy() {
		// Cancel running operation
		synchronized (mCond) {
			while (mRunningOp != null) {
				mRunningOp.interrupt();
				try {
					mCond.wait();
				} catch (InterruptedException e) {
				}
			}
		}

		// Close PTAPI session
		closeSession();

		// Terminate PTAPI library
		terminatePtapi();

		super.onDestroy();
	}



	/**
	 * Load PTAPI library and initialize its interface.
	 *
	 * @return True, if library is ready for use.
	 */
	private boolean initializePtapi() {
		// Load PTAPI library
		Context aContext = getApplicationContext();
		mPtGlobal = new PtGlobal(aContext);

		try {
			// Initialize PTAPI interface
			// Note: If PtBridge development technology is in place and a
			// network
			// connection cannot be established, this call hangs forever.
			mPtGlobal.initialize();
			return true;
		} catch (java.lang.UnsatisfiedLinkError ule) {
			// Library wasn't loaded properly during PtGlobal object
			// construction
			dislayMessage("libjniPtapi.so 未加载");
			mPtGlobal = null;
			return false;

		} catch (PtException e) {
			dislayMessage(e.getMessage());
			return false;
		}
	}

	/**
	 * Terminate PTAPI library.
	 */
	private void terminatePtapi() {
		try {
			if (mPtGlobal != null) {
				mPtGlobal.terminate();
			}
		} catch (PtException e) {
			// ignore errors
		}
		mPtGlobal = null;
	}

	private void configureOpenedDevice() throws PtException {
		PtSessionCfgV5 sessionCfg = (PtSessionCfgV5) mConn
				.getSessionCfgEx(PtConstants.PT_CURRENT_SESSION_CFG);
		sessionCfg.sensorSecurityMode = PtConstants.PT_SSM_DISABLED;
		sessionCfg.callbackLevel |= PtConstants.CALLBACKSBIT_NO_REPEATING_MSG;
		mConn.setSessionCfgEx(PtConstants.PT_CURRENT_SESSION_CFG, sessionCfg);
	}

	@SuppressWarnings("unused")
	private void openPtapiSessionInternal(String dsn) throws PtException {
		// Try to open device with given DSN string
		Context aContext = getApplicationContext();
		try {
			PtUsbHost.PtUsbCheckDevice(aContext, 0);
		} catch (PtException e) {
			throw e;
		}

		mConn = (PtConnectionAdvancedI) mPtGlobal.open(dsn);

		try {
			// Verify that emulated NVM is initialized and accessible
			mSensorInfo = mConn.info();
		} catch (PtException e) {

			if ((e.getCode() == PtException.PT_STATUS_EMULATED_NVM_INVALID_FORMAT)
					|| (e.getCode() == PtException.PT_STATUS_NVM_INVALID_FORMAT)
					|| (e.getCode() == PtException.PT_STATUS_NVM_ERROR)) {
				if (miRunningOnRealHardware != 0) {
					// try add storage configuration and reopen the device
					dsn += ",nvmprefix=" + msNvmPath + '/';
					// Reopen session
					mConn.close();
					mConn = null;

					mConn = (PtConnectionAdvancedI) mPtGlobal.open(dsn);
					try {
						// Verify that emulated NVM is initialized and
						// accessible
						mSensorInfo = mConn.info();
						configureOpenedDevice();
						return;
					} catch (PtException e2) {
						// ignore errors and continue
					}
				}

				// We have found the device, but it seems to be either opened
				// for the first time
				// or its emulated NVM was corrupted.
				// Perform the manufacturing procedure.
				// To properly initialize it, we have to:
				// 1. Format its emulated NVM storage
				// 2. Calibrate the sensor

				// Format internal NVM
				mConn.formatInternalNVM(0, null, null);

				// Reopen session
				mConn.close();
				mConn = null;

				mConn = (PtConnectionAdvancedI) mPtGlobal.open(dsn);

				// Verify that emulated NVM is initialized and accessible
				mSensorInfo = mConn.info();
				// check if sensor is calibrated
				if ((mSensorInfo.sensorType & PtConstants.PT_SENSORBIT_CALIBRATED) == 0) {
					// No, so calibrate it
					mConn.calibrate(PtConstants.PT_CALIB_TYPE_TURBOMODE_CALIBRATION);
					// Update mSensorInfo
					mSensorInfo = mConn.info();
				}

				// Device successfully opened
			} else {
				throw e;
			}
		}

		configureOpenedDevice();
	}

	/**
	 * Open PTAPI session.
	 */
	@SuppressWarnings("unused")
	private void openPtapiSession() {
		PtException openException = null;

		/*
		 * if(miUseSerialConnection != 0) { try { // Try to open session
		 * openPtapiSessionInternal(msDSNSerial); // Device successfully opened
		 * return; } catch (PtException e) { // Remember error and try remaining
		 * devices openException = e; } } else { // Walk through the most common
		 * DSN strings on USB for(int i=0; i<mDSN.length; i++) {
		 * PtDeviceListItem[] devices = null; // Enumerate devices try { devices
		 * = mPtGlobal.enumerateDevices(mDSN[i]); } catch (PtException e1) {
		 * if(e1.getCode() != PtException.PT_STATUS_INVALID_PARAMETER) {
		 * dislayMessage("Enumeration failed - " + e1.getMessage()); return; }
		 * 
		 * // Try to enumerate next DSN string continue; }
		 * 
		 * // Walk through enumerated devices and try to open them for(int d=0;
		 * d<devices.length; d++) { String dsn = devices[d].dsnSubString; try {
		 * // Try to open session openPtapiSessionInternal(dsn);
		 * 
		 * // Device successfully opened return; } catch (PtException e) { //
		 * Remember error and try remaining devices openException = e; } } } }
		 */

		try {
			// Try to open session
			openPtapiSessionInternal("sio,port=/dev/ttyMT1,speed=9600,timeout=2000");

			// Device successfully opened
			return;
		} catch (PtException e) {
			// Remember error and try remaining devices
			openException = e;
		}

		// No device has been opened
		if (openException == null) {
			dislayMessage("No device found");
		} else {
			dislayMessage("Error during device opening - "
					+ openException.getMessage());
		}
	}

	private void closeSession() {
		if (mConn != null) {
			try {
				mConn.close();
			} catch (PtException e) {
				// Ignore errors
			}
			mConn = null;
		}
	}

	/**
	 * Display message in TextView.
	 */
	public void dislayMessage(String text) {
		if (text.equals("upLoadFingerData")){
			fingerHandler.sendMessage(fingerHandler.obtainMessage(1, 0, 0, ""));
		} else if (text.equals("errorUserNumber")){
			fingerHandler.sendMessage(fingerHandler.obtainMessage(2, 0, 0, text));
		} else {
            fingerHandler.sendMessage(fingerHandler.obtainMessage(0, 0, 0, text));
        }
	}

	/**
	 * Transfer messages to the main activity thread.
	 */
	private Handler fingerHandler = new Handler() {
		public void handleMessage(Message aMsg) {

			switch (aMsg.what){

				case 0:
					((TextView) findViewById(R.id.EnrollmentTextView))
							.setText((String) aMsg.obj);
					break;
				case 1:
					showDialog("正在上传指纹信息中...");
					break;
                case 2:
                    showMessage("请输入正确的医通卡号！");
                    break;
			}
		}
	};



	/**
	 * 查询人员信息
	 *
	 * @param userNumber
	 */
	public void searchEmployee( String userNumber) {
		Map<String, Object> requestMap = new HashMap<>();
		requestMap.put("userNumber", userNumber);
		requestMap.put("page", "1");
		requestMap.put("rows", "14");
		mainPresenter.allRequestBase(RequestConfig.Url_QueryEmployeeLikeNumber, requestMap, new ICallback() {
			@Override
			public void onFail(Throwable e) {
				Log.d("logInfo", e.toString());
			}

			@Override
			public void onSuccess(String string) {
				Log.d("logInfo", string);

				String rows = J.getRows(string);
				//将json转化为List集合
				listEntity = J.getListEntity(rows, EmployeeEntity.class);
				mAdapter.replaceData(listEntity);
			}
		}, this);
	}

	/**
	 * 离线查询本地人员库
	 *
	 * @param userNumber 医通卡号
	 */
	private void outLineSearch(final String userNumber) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				//查询数据
				Observable.just("")
						.map(new Func1<String, List<EmployeeEntity>>() {
							@Override
							public List<EmployeeEntity> call(String mVoid) {
								return instance.likeNumberEmployee(app.getDaoSeeion().getEmployeeEntityDao(),userNumber);
							}
						}).subscribeOn(Schedulers.io())//把工作线程指定为了IO线程
						.observeOn(AndroidSchedulers.mainThread())//把回调线程指定为了UI线程
						.subscribe(new Action1<List<EmployeeEntity>>() {
							@Override
							public void call(List<EmployeeEntity> tempLifeList) {
								listEntity = tempLifeList;
								mAdapter.replaceData(tempLifeList);
							}
						});
			}
		}).start();
	}


	/**
	 * 录入指纹监听事件
	 *
	 * @param view
	 */
	public void onInputFinger(View view) {

		if (StringUtils.isEmpty(mDataText.getText()) ){
			showMessage("请输入医通卡号！");
		} else if (12 != mDataText.getText().length()){
            showMessage("请输入正确的医通卡号！");
        } else {
			final String[] sequences = new String[]{"左手-大拇指", "左手-食指", "左手-中指", "左手-无名指", "左手-小拇指"
					, "右手-大拇指", "右手-食指", "右手-中指", "右手-无名指", "右手-小拇指"};

			DialogUIUtils.showSingleChoose(FingerActivity.this, "选择手指", 0
					, sequences, false, true, new DialogUIItemListener() {
						@Override
						public void onItemClick(CharSequence text, int position) {

							if (0 == position){
								setInputListener(FingerId.LEFT_THUMB);
                                selectFinger.setText(sequences[0]);
							} else if (1 == position){
								setInputListener(FingerId.LEFT_INDEX);
                                selectFinger.setText(sequences[1]);
							} else if (2 == position){
								setInputListener(FingerId.LEFT_MIDDLE);
                                selectFinger.setText(sequences[2]);
							} else if (3 == position){
								setInputListener(FingerId.LEFT_RING);
                                selectFinger.setText(sequences[3]);
							} else if (4 == position){
								setInputListener(FingerId.LEFT_LITTLE);
                                selectFinger.setText(sequences[4]);
							} else if (5 == position){
								setInputListener(FingerId.RIGHT_THUMB);
                                selectFinger.setText(sequences[5]);
							} else if (6 == position){
								setInputListener(FingerId.RIGHT_INDEX);
                                selectFinger.setText(sequences[6]);
							} else if (7 == position){
								setInputListener(FingerId.RIGHT_MIDDLE);
                                selectFinger.setText(sequences[7]);
							} else if (8 == position){
								setInputListener(FingerId.RIGHT_RING);
                                selectFinger.setText(sequences[8]);
							} else if (9 == position){
								setInputListener(FingerId.RIGHT_LITTLE);
                                selectFinger.setText(sequences[9]);
							}
						}
					}).show();
		}

	}



	/**
	 * Set listener for an enrollment button.
	 *
	 * @param fingerId
	 * Finger ID.
	 */
	private void setInputListener( final int fingerId) {
		synchronized (mCond) {
			if (mRunningOp == null) {
				mRunningOp = new OpEnroll(mConn, fingerId, new IAddFingerCallback() {
					@Override
					public void onAddFinger(int finger_index, byte[] finger_data) {

						if (!StringUtils.isEmpty(usernumber)){
							FingerPrint fingerPrint = new FingerPrint();
							fingerPrint.setId(DateUtils.UUID());
							fingerPrint.setSlotId(finger_index);
							fingerPrint.setFingerData(finger_data);
							fingerPrint.setEmpNumber(usernumber);

							upFingerInfo(fingerPrint);

							Log.e("logInfo","setInputListener-->"+finger_index);
//							fingerPrintDao.insertOrReplace(fingerPrint);

							dislayMessage("upLoadFingerData");
						} else {
                            dislayMessage("errorUserNumber");
                        }
					}
				})
				{
					@Override
					protected void onDisplayMessage(String message) {
						dislayMessage(message);
					}

					@RequiresApi(api = Build.VERSION_CODES.O)
					@Override
					protected void onFinished() {
						synchronized (mCond) {
							mRunningOp = null;
							mCond.notifyAll(); // notify onDestroy that
							// operation has
							// finished
						}
					}
				};
				mRunningOp.start();
			}
		}
	}


	/**
	 * 上传指纹信息
	 * @param fingerPrint
	 */
	private void upFingerInfo(FingerPrint fingerPrint){

		Map<String,Object> requestMap = new HashMap<>();
		//医通卡号
		requestMap.put("empNumber",fingerPrint.getEmpNumber());

		//指纹信息
		String fingerData = android.util.Base64.encodeToString(fingerPrint.getFingerData(), android.util.Base64.DEFAULT);
		requestMap.put("fingerData",fingerData);

		//指纹ID
		requestMap.put("slotId",fingerPrint.getSlotId());

		mainPresenter.allRequestBase(RequestConfig.Url_UpDataEmployeeInfo, requestMap, new ICallback() {
			@Override
			public void onFail(Throwable e) {
				dismissDialog();
				Log.d("logInfo", e.toString());

				showMessage("指纹信息录入失败！");
				hintsManager.playVoice(5);
			}

			@Override
			public void onSuccess(String string) {
				dismissDialog();
				Log.d("logInfo", string);

				showMessage("指纹信息录入成功！");
				hintsManager.playVoice(4);

			}
		}, FingerActivity.this);



	}








	/**
	 * Cancel the mapping process and disable NFC foreground dispatch system.
	 * This method is not called, if screen orientation changes.
	 * @see com.st.CMEApplication#disableNfcForegroundDispatch(Activity)
	 */
	@Override
	public void onPause() {
		super.onPause();
		mIsCreatingKeyMap = false;

	}



	/**
	 * List files from the {@link #EXTRA_KEYS_DIR} and select the last used
	 * ones if {@link PreferencesActivity #SaveLastUsedKeyFiles} is enabled.
	 */
	@Override
	public void onStart() {
		super.onStart();

		if (mKeyDirPath == null) {
			// Is there a key directory in the Intent?
			if (!getIntent().hasExtra(EXTRA_KEYS_DIR)) {
				setResult(2);
				finish();
				return;
			}
			String path = getIntent().getStringExtra(EXTRA_KEYS_DIR);
			// Is path null?
			if (path == null) {
				setResult(4);
				finish();
				return;
			}
			mKeyDirPath = new File(path);
		}

		// Is external storage writable?
		if (!CMEApplication.getPreferences().getBoolean(UseInternalStorage.toString(),
				false) && !CMEApplication.isExternalStorageWritableErrorToast(this)) {
			setResult(3);
			finish();
			return;
		}
		// Does the directory exist?
		if (!mKeyDirPath.exists()) {
			setResult(1);
			finish();
			return;
		}

		// List key files and select last used (if corresponding
		// setting is active).
		boolean selectLastUsedKeyFiles = CMEApplication.getPreferences().getBoolean(
				PreferencesActivity.Preference.SaveLastUsedKeyFiles.toString(), true);
		ArrayList<String> selectedFiles = null;
		if (selectLastUsedKeyFiles) {
			SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
			// All previously selected key files are stored in one string
			// separated by "/".
			String selectedFilesChain = sharedPref.getString(
					"last_used_key_files", null);
			if (selectedFilesChain != null) {
				selectedFiles = new ArrayList<String>(
						Arrays.asList(selectedFilesChain.split("/")));
			}
		}
		File[] keyFiles = mKeyDirPath.listFiles();
		Arrays.sort(keyFiles);
		mKeyFilesGroup.removeAllViews();
		for(File f : keyFiles) {
			CheckBox c = new CheckBox(this);
			c.setText(f.getName());
			if (selectLastUsedKeyFiles && selectedFiles != null
					&& selectedFiles.contains(f.getName())) {
				// Select file.
				c.setChecked(true);
			}
			mKeyFilesGroup.addView(c);
			if ("std.keys".equals(f.getName())){
				c.setChecked(true);
			}
		}
	}


	@Override
	public void onResume() {
		super.onResume();

		onlyTagID = app.getTagID();

		BottomDialogUtils.dismiss();
		onCreateKeyMap();
	}


	/**
	 * 读取标签
	 */
	public void onCreateKeyMap() {

		boolean saveLastUsedKeyFiles = CMEApplication.getPreferences().getBoolean(
				PreferencesActivity.Preference.SaveLastUsedKeyFiles.toString(), true);
		String lastSelectedKeyFiles = "";
		// Check for checked check boxes.
		ArrayList<String> fileNames = new ArrayList<String>();
		for (int i = 0; i < mKeyFilesGroup.getChildCount(); i++) {
			CheckBox c = (CheckBox) mKeyFilesGroup.getChildAt(i);
			if (c.isChecked()) {
				fileNames.add(c.getText().toString());
			}
		}
		if (fileNames.size() > 0) {
			// Check if key files still exists.
			ArrayList<File> keyFiles = new ArrayList<File>();
			for (String fileName : fileNames) {
				File keyFile = new File(mKeyDirPath, fileName);
				if (keyFile.exists()) {
					// Add key file.
					keyFiles.add(keyFile);
					if (saveLastUsedKeyFiles) {
						lastSelectedKeyFiles += fileName + "/";
					}
				} else {
					Log.d(LOG_TAG, "Key file "
							+ keyFile.getAbsolutePath()
							+ "doesn't exists anymore.");
				}
			}
			if (keyFiles.size() > 0) {
				// Save last selected key files as "/"-separated string
				// (if corresponding setting is active).
				if (saveLastUsedKeyFiles) {
					SharedPreferences sharedPref = getPreferences(
							Context.MODE_PRIVATE);
					SharedPreferences.Editor e = sharedPref.edit();
					e.putString("last_used_key_files",
							lastSelectedKeyFiles.substring(
									0, lastSelectedKeyFiles.length() - 1));
					e.apply();
				}

				// Create reader.
				MCReader reader = CMEApplication.checkForTagAndCreateReader(this);
				if (reader == null) {
					return;
				}

				// Set key files.
				File[] keys = keyFiles.toArray(new File[keyFiles.size()]);
				if (!reader.setKeyFile(keys, this)) {
					// Error.
					reader.close();
					return;
				}
				// Don't turn screen of while mapping.
				getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

				// Set map creation range.
				if (!reader.setMappingRange(
						mFirstSector, mLastSector)) {//默认读取0~4扇区的数据
					// Error.
					showMessage("读取医通卡的信息出现异常！");
					reader.close();
					return;
				}
				CMEApplication.setKeyMapRange(mFirstSector, mLastSector);
				// Init. GUI elements.
				mProgressStatus = -1;
//                mProgressBar.setMax((mLastSector-mFirstSector)+1);
				mIsCreatingKeyMap = true;
				// Read as much as possible with given key file.
				createKeyMap(reader);
			}
		}
	}

	/**
	 * Triggered by {@link #onCreateKeyMap} this
	 * method starts a worker thread that first creates a key map and then
	 * calls {@link #keyMapCreated(MCReader)}.
	 * It also updates the progress bar in the UI thread.
	 *
	 * @param reader A connected {@link MCReader}.
	 * @see #onCreateKeyMap
	 * @see #keyMapCreated(MCReader)
	 */
	private void createKeyMap(final MCReader reader) {
		showDialog("正在读取医通卡的信息中...");
		new Thread(new Runnable() {
			@Override
			public void run() {
				// Build key map parts and update the progress bar.
				while (mProgressStatus < mLastSector) {
					mProgressStatus = reader.buildNextKeyMapPart();
					if (mProgressStatus == -1 || !mIsCreatingKeyMap) {
						// Error while building next key map part.
						break;
					}
				}

				mHandler.post(new Runnable() {
					@Override
					public void run() {
						getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
						reader.close();
						if (mIsCreatingKeyMap && mProgressStatus != -1) {
							keyMapCreated(reader);
							dismissDialog();
						} else {
							// Error during key map creation.
							CMEApplication.setKeyMap(null);
							CMEApplication.setKeyMapRange(-1, -1);
							showMessage("读取医通卡的信息出现异常！");
						}
						mIsCreatingKeyMap = false;
					}
				});
			}
		}).start();
	}

	/**
	 * Triggered by {@link #createKeyMap(MCReader)}, this method
	 * sets the result code to {@link Activity#RESULT_OK},
	 * saves the created key map to
	 * {@link CMEApplication#setKeyMap(android.util.SparseArray)}
	 * and finishes this Activity.
	 * @param reader A {@link MCReader}.
	 * @see #createKeyMap(MCReader)
	 * @see #onCreateKeyMap
	 */
	private void keyMapCreated(MCReader reader) {
		// LOW: Return key map in intent.
		if (reader.getKeyMap().size() == 0) {
			CMEApplication.setKeyMap(null);
			// Error. No valid key found.
			showMessage("读取医通卡的信息出现异常！");
		} else {
			CMEApplication.setKeyMap(reader.getKeyMap());
			setResult(Activity.RESULT_OK);
			readNfc();
		}
	}


	/**
	 * 在线程中读取医通卡
	 * */
	public void readNfc(){

		new Thread(new Runnable() {
			@Override
			public void run() {
				//查询数据
				Observable.just("")
						.map(new Func1<String, SparseArray<String[]>>() {
							@Override
							public SparseArray<String[]> call(String mVoid) {
								MCReader reader = CMEApplication.checkForTagAndCreateReader(FingerActivity.this);
								SparseArray<String[]> sparseArray = reader.readAsMuchAsPossible(CMEApplication.getKeyMap());
								reader.close();
								return sparseArray;
							}
						}).subscribeOn(Schedulers.io())//把工作线程指定为了IO线程
						.observeOn(AndroidSchedulers.mainThread())//把回调线程指定为了UI线程
						.subscribe(new Action1<SparseArray<String[]>>() {
							@Override
							public void call(SparseArray<String[]> sparseArray) {
								dismissDialog();
								createTagDump(sparseArray);
							}
						});
			}
		}).start();
	}


	/**
	 * Create a tag dump in a format the {@link DumpEditorActivity}
	 * can read (format: headers (sectors) marked with "+", errors
	 * marked with "*"), and then start the dump editor with this dump.
	 * @param rawDump A tag dump like {@link MCReader#readAsMuchAsPossible()}
	 * returns.
	 * @see DumpEditorActivity#EXTRA_DUMP
	 * @see DumpEditorActivity
	 */
	private void createTagDump(SparseArray<String[]> rawDump) {

		if (null == onlyTagID
				|| "".equals(onlyTagID)){
			showMessage("未检测到医通卡！");
			return;
		}

		ArrayList<String> tmpDump = new ArrayList<String>();
		if (rawDump != null) {
			if (rawDump.size() != 0) {
				for (int i = CMEApplication.getKeyMapRangeFrom();
					 i <= CMEApplication.getKeyMapRangeTo(); i++) {
					String[] val = rawDump.get(i);
					// Mark headers (sectors) with "+".
					tmpDump.add("+Sector: " + i);
					if (val != null) {
						for (int m = 0 ; m<4; m++){
							if(val[m].length() == 32 ){
								if (val[m].contains(onlyTagID)){
									Collections.addAll(tmpDump, val);
								}
							}
						}
					} else {
						// Mark sector as not readable ("*").
						tmpDump.add("*No keys found or dead sector");
					}
				}

				String[] dump = tmpDump.toArray(new String[tmpDump.size()]);
				//验证医通卡号
				if (!verifyNum(dump)) return;
			} else {
				// Error, keys from key map are not valid for reading.
				showMessage(R.string.info_none_key_valid_for_reading);
			}
		} else {
			showMessage(R.string.info_tag_removed_while_reading);
		}
	}

	/**
	 * 验证医通卡号
	 * @param dump
	 * @return
	 */
	private boolean verifyNum(String[] dump) {
		List<String> newStrings = null;
		if (dump.length != 0){
			//String[]转List
			List<String> strings = Arrays.asList(dump);
			newStrings = new ArrayList<>();
			for (int j = 0; j<strings.size(); j++){
				String str = strings.get(j);
				if (str.length() == 32 && str.contains(onlyTagID) ){
					str = str.substring(20,32);
					if (StringUtils.numberMatch(str)){
						newStrings.add(str);
					}
				}
			}
			//医通卡号：String数组转String
			String empNumber = "";
			List<String> arraylist  = new ArrayList<String>(new HashSet<String>(newStrings));
			if (arraylist.size() == 1){
				for (int i = 0; i < arraylist.size(); i++) {
					empNumber += arraylist.get(i);
				}
				mDataText.setText(empNumber);
				usernumber = empNumber;
			} else if (arraylist.size() == 0){
				showMessage("当前医通卡未写磁，请先写入医通卡号！");
				return false;
			} else {
				showMessage("当前医通卡存在多个账号，无法进行打卡！");
				return false;
			}
			return true;
		} else {
			showMessage("读取医通卡的信息出现异常！");
			return false;
		}
	}



}

