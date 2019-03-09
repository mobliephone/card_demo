package com.st.activity.project;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.framework.json.J;
import com.framework.manager.AppCacheManager;
import com.framework.util.DateUtils;
import com.framework.util.StringUtils;
import com.st.CMEApplication;
import com.st.R;
import com.st.activity.data.EventRecord;
import com.st.db.DBUtils;
import com.st.db.dao.StudyRegistrationEntityDao;
import com.st.db.entity.CourseEntity;
import com.st.db.entity.EmployeeEntity;
import com.st.db.entity.StudyRegistrationEntity;
import com.st.nfc.BasicActivity;
import com.st.nfc.MCReader;
import com.st.nfc.activity.DumpEditorActivity;
import com.st.nfc.activity.KeyMapCreatorActivity;
import com.st.nfc.activity.PreferencesActivity;
import com.st.persenter.ICallback;
import com.st.persenter.MainPresenter;
import com.st.service.request.RequestConfig;
import com.st.utils.BottomMsgUtils;
import com.st.utils.HintsManager;
import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.st.nfc.activity.PreferencesActivity.Preference.UseInternalStorage;

/**
 * 读取卡中的内容，并返回给PayByCardActivity来处理
 */
public class PayByCardResultActivity extends BasicActivity {

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


    private LinearLayout mKeyFilesGroup;
    private Button scanBtn;
    private final Handler mHandler = new Handler();
    private int mProgressStatus;
    private boolean mIsCreatingKeyMap;
    private File mKeyDirPath;
//    private int mFirstSector = 1;
//    private int mLastSector = 2;


    private int mFirstSector = 1;
    private int mLastSector = 1;

    //医通卡唯一ID
    private String onlyTagID;

    //课题实体类
    private CourseEntity courseBean;
    private CMEApplication app;

    //广播Action
    private String m_Broadcastname;
    private DBUtils dbUtils;
    private StudyRegistrationEntityDao studyDao;

    private AppCacheManager sharePrenManager;
    private HintsManager hintsManager;


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
        Log.d("logInfo","onlyTagID--->"+onlyTagID);

        dismissBottomDialog();
        onCreateKeyMap();


        //扫描枪
        final IntentFilter intentFilter = new IntentFilter();
        m_Broadcastname = "com.barcode.sendBroadcast";// com.barcode.sendBroadcastScan
        intentFilter.addAction(m_Broadcastname);
        registerReceiver(receiver, intentFilter);

    }

    /**
     * Cancel the mapping process and disable NFC foreground dispatch system.
     * This method is not called, if screen orientation changes.
     * @see CMEApplication#disableNfcForegroundDispatch(Activity)
     */
    @Override
    public void onPause() {
        super.onPause();
        mIsCreatingKeyMap = false;

        unregisterReceiver(receiver);
    }

    @OnClick({R.id.scan_button})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.scan_button:

                // sendKeyEvent(220);
                Intent intent = new Intent();
                intent.setAction("com.barcode.sendBroadcastScan");
                sendBroadcast(intent);
                break;
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_pay_by_card_result;
    }

    @Override
    public void onInitView() {
        mKeyFilesGroup = (LinearLayout) findViewById(R.id.linearLayoutCreateKeyMapKeyFiles);
        scanBtn = findViewById(R.id.scan_button);
        setTitle("上课打卡");

        app = (CMEApplication) getApplication();
        dbUtils = DBUtils.getInstance(this,app);
        studyDao = app.getDaoSeeion().getStudyRegistrationEntityDao();
        sharePrenManager = AppCacheManager.getInstance();
        hintsManager = HintsManager.getInstance();
        hintsManager.init(PayByCardResultActivity.this);
        Bundle bundle = getIntent().getExtras();
        if (null != bundle){
            courseBean = (CourseEntity) bundle.getSerializable("course");
        }


    }


    /**
     * 扫描枪
     */
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent arg1) {
            if (arg1.getAction().equals(m_Broadcastname)) {
                dismissBottomDialog();

                String empNumber = arg1.getStringExtra("BARCODE");
                Log.d("logInfo","扫描到的医通卡号----->"+empNumber);
                if (!"".equals(empNumber)) {
                    if(!PayByCardResultActivity.this.isFinishing()){
                        upData(empNumber);
                    }
                }
            }
        }
    };

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
                        dismissDialog();

                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                        reader.close();
                        if (mIsCreatingKeyMap && mProgressStatus != -1) {
                            keyMapCreated(reader);
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
                            MCReader reader = CMEApplication.checkForTagAndCreateReader(PayByCardResultActivity.this);
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
                showMessage("无法识别当前医通卡！");
//                showMessage(R.string.info_none_key_valid_for_reading);
            }
        } else {
            showMessage("请在刷卡过程中，勿移动医通卡！");
//            showMessage(R.string.info_tag_removed_while_reading);
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
                upData(empNumber);
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



    /**
     * 根据医通卡，设置数据
     * @param empNumber
     */
    private void upData(final String empNumber) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                //查询数据
                Observable.just("")
                        .map(new Func1<String,  List<StudyRegistrationEntity>>() {
                            @Override
                            public  List<StudyRegistrationEntity> call(String mVoid) {
                                return dbUtils.loadStudyByEmp(studyDao,empNumber,courseBean.getCourseId());
                            }
                        }).subscribeOn(Schedulers.io())//把工作线程指定为了IO线程
                        .observeOn(AndroidSchedulers.mainThread())//把回调线程指定为了UI线程
                        .subscribe(new Action1< List<StudyRegistrationEntity>>() {
                            @Override
                            public void call( List<StudyRegistrationEntity> entities) {
                                if (!sharePrenManager.popBooleanFromPrefs(com.st.activity.data.Constant.LOGIN_STATE)){
                                    verifyPunchCardOutLine(empNumber, entities);
                                } else {
                                    verifyPunchCard("1","10",empNumber, entities);
                                }
                             }
                        });
            }
        }).start();
    }


    /**
     * 离线验证
     * @param empNumber
     * @param entities
     */
    private void verifyPunchCardOutLine(String empNumber, List<StudyRegistrationEntity> entities) {

        if (entities.size() > 0 ){
            StudyRegistrationEntity bean = entities.get(0);
            String checkNumber = courseBean.getCheckNumber();
            if ("2".equals(checkNumber) && "2".equals(bean.getIsvalid())){//两次打卡
                showBottomDialog(BottomMsgUtils.bottomMessage("9"),26f);

                hintsManager.playVoice(9);
            } else if ("1".equals(checkNumber)){//一次打卡
                showBottomDialog(BottomMsgUtils.bottomMessage("9"),26f);

                hintsManager.playVoice(9);
            } else {
                verifyData(courseBean,entities,empNumber,false);
            }
        } else {
            verifyData(courseBean,entities,empNumber,false);
        }

    }

    /**
     * 验证是否打卡
     * @param page
     * @param rows
     * @param empNumber
     * @param entities
     */
    private void verifyPunchCard(String page, String rows,final String empNumber
            ,final List<StudyRegistrationEntity> entities){

        MainPresenter mainPresenter = new MainPresenter();

        if (null != courseBean){
            String courseId = courseBean.getCourseId();
            Map<String, Object> requestMap = new HashMap<>();
            requestMap.put("page", page);
            requestMap.put("rows", rows);
            requestMap.put("empNumber", empNumber);
            requestMap.put("courseId", courseId);
            mainPresenter.allRequestBase(RequestConfig.Url_QueryListStudyRegistration, requestMap, new ICallback() {
                @Override
                public void onFail(Throwable e) {
                    Log.d("logInfo", e.toString());

                    showMessage("请检查服务器配置！");
                }

                @Override
                public void onSuccess(String string) {

                    Log.d("logInfo", string);
                    String rows = J.getRows(string);
                    //将json转化为List集合
                    List<StudyRegistrationEntity> listEntity = J.getListEntity(rows, StudyRegistrationEntity.class);
                    if (listEntity.size() > 0 ){
                        StudyRegistrationEntity bean = listEntity.get(0);
                        String isvalid= bean.getIsvalid();
                        String checkNumber = courseBean.getCheckNumber();
                        if ("2".equals(checkNumber) && "2".equals(isvalid)){//两次打卡
                            showBottomDialog(BottomMsgUtils.bottomMessage("9"),26f);

                            hintsManager.playVoice(9);
                        } else if ("1".equals(checkNumber)){//一次打卡
                            showBottomDialog(BottomMsgUtils.bottomMessage("9"),26f);

                            hintsManager.playVoice(9);
                        } else if ("2".equals(checkNumber) && "1".equals(isvalid)//微信第二次打卡
                                && "1".equals(bean.getState())){
                            verifyData(courseBean,entities,empNumber,true);
                        } else if ("2".equals(checkNumber) && "1".equals(isvalid)//APP第二次打卡
                                && "0".equals(bean.getState())){
                            verifyData(courseBean,entities,empNumber,false);
                        }
                    } else {
                        verifyData(courseBean,entities,empNumber,false);
                    }
                }
            }, this);
        } else {
            showBottomDialog(BottomMsgUtils.bottomMessage("6"),20f);
        }
    }

    /**
     * 打卡验证
     * @param courseEntity
     * @param entities
     * @param empNumber
     */
    public void verifyData(CourseEntity courseEntity,List<StudyRegistrationEntity> entities
            ,String empNumber,boolean isWeChat){

        String currentDates = DateUtils.currentTime();

        if (courseEntity == null){
            showBottomDialog(BottomMsgUtils.bottomMessage("6"),20f);
            return ;
        }

        if (StringUtils.isEmpty(courseEntity.getStartDate())
                || StringUtils.isEmpty(courseEntity.getEndDate())){
            showBottomDialog(BottomMsgUtils.bottomMessage("5"),20f);
            return ;
        } else if (!DateUtils.isSameDate(currentDates,courseEntity.getStartDate())){
            showBottomDialog(BottomMsgUtils.bottomMessage("4"),20f);

            hintsManager.playVoice(8);
            return ;
        }

        if (StringUtils.isEmpty(courseEntity.getValidTime())){
            showBottomDialog(BottomMsgUtils.bottomMessage("7"),20f);
            return ;
        } else {

            //提前打卡时间（30分钟）
//            Integer validTime = Integer.valueOf(courseEntity.getValidTime());
            Integer validTime = Integer.valueOf("30");

            String startDates = courseEntity.getStartDate();
            String endDates = courseEntity.getEndDate();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Date startDate = null;
            Date endDate = null;
            Date currentDate = null;
            try {
                startDate = sdf.parse(startDates);
                endDate = sdf.parse(endDates);
                currentDate = sdf.parse(currentDates);

                if (currentDate.getTime() < startDate.getTime()) {//提前打卡
                    if (DateUtils.betweenTime(currentDates,startDates) <= validTime) {
                        punchCard(entities,empNumber,"0",isWeChat);

                        Log.d("logInfo", "PayByCardResultActivity-----提前打卡");
                    }
                } else if (currentDate.getTime() > endDate.getTime()
                        && "1".equals(courseBean.getCheckNumber())) {//迟到打卡
                    punchCard(entities,empNumber,"1",isWeChat);

                    Log.d("logInfo", "PayByCardResultActivity-----迟到打卡");
                } else if (currentDate.getTime() > endDate.getTime()
                        && DateUtils.betweenTime(endDates,currentDates) > 30
                        && "2".equals(courseBean.getCheckNumber())) {//第二次迟到打卡

                    punchCard(entities,empNumber,"1",isWeChat);

                    Log.d("logInfo", "PayByCardResultActivity-----迟到打卡");
                } else if (currentDate.getTime() >= startDate.getTime()
                        && currentDate.getTime() <= endDate.getTime()){//正常打卡
                    punchCard(entities,empNumber,"0",isWeChat);

                    Log.d("logInfo", "PayByCardResultActivity-----正常打卡");
                } else if (currentDate.getTime() > endDate.getTime()//正常打卡
                        && DateUtils.betweenTime(endDates,currentDates) < 30
                        && "2".equals(courseBean.getCheckNumber())){
                    punchCard(entities,empNumber,"0",isWeChat);
                }
            } catch (ParseException e) {
                e.printStackTrace();
                showBottomDialog(BottomMsgUtils.bottomMessage("8"),20f);

                hintsManager.playVoice(2);
            }
        }
    }

    /**
     * 打卡
     * @param entities
     * @param empNumber
     * @param awardState
     */
    public void punchCard(List<StudyRegistrationEntity> entities,String empNumber,String awardState,boolean isWeChat){
        if (entities.size() == 1 && "2".equals(courseBean.getCheckNumber())) {//打卡两次


            if (!punchCardTime()) {
                showBottomDialog(BottomMsgUtils.bottomMessage("4"),20f);

                hintsManager.playVoice(8);
            } else {
                if ("1".equals(awardState)){
                    showBottomDialog(BottomMsgUtils.bottomMessage("3"),26f);

                    hintsManager.playVoice(3);
                } else {
                    showBottomDialog(BottomMsgUtils.bottomMessage("1"),26f);

                    hintsManager.playVoice(1);
                }
                //发送数据
                EventRecord record = new EventRecord(EventRecord.USERNUMBER,empNumber,awardState,isWeChat);
                EventBus.getDefault().post(record);

                Log.d("logInfo", "PayByCardResultActivity-----打卡状态"+awardState);
            }
        } else {//一次打卡
            if ("1".equals(awardState)){
                showBottomDialog(BottomMsgUtils.bottomMessage("3"),26f);

                hintsManager.playVoice(3);
            } else {
                showBottomDialog(BottomMsgUtils.bottomMessage("1"),26f);

                hintsManager.playVoice(1);
            }
            //发送数据
            EventRecord record = new EventRecord(EventRecord.USERNUMBER,empNumber,awardState,isWeChat );
            EventBus.getDefault().post(record);

            Log.d("logInfo", "PayByCardResultActivity-----打卡状态"+awardState);
        }
    }

    /**
     * 根据课题要求打卡两次，验证打卡时间
     * @return
     */
    public boolean punchCardTime() {


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String endDate= courseBean.getEndDate();
        String currentTime= DateUtils.currentTime();
        Date currentDates;
        Date endDates;
        try {
            currentDates = sdf.parse(currentTime);
            endDates = sdf.parse(endDate);
            //判断两次打卡时间间隔
            if ( currentDates.getTime() < endDates.getTime() ){
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            showBottomDialog(BottomMsgUtils.bottomMessage("8"),20f);

            hintsManager.playVoice(2);
        }
        return true;
    }




}
