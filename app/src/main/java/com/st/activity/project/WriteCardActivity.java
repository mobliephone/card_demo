package com.st.activity.project;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.framework.json.J;
import com.framework.util.NetWorkUtil;
import com.framework.util.StringUtils;
import com.framework.view.DialogUIUtils;
import com.framework.view.listener.DialogUIItemListener;
import com.framework.view.listener.DialogUIListener;
import com.st.CMEApplication;
import com.st.R;
import com.st.activity.adapter.WriteAdapter;
import com.st.db.DBUtils;
import com.st.db.entity.EmployeeEntity;
import com.st.nfc.BasicActivity;
import com.st.nfc.MCReader;
import com.st.nfc.activity.DumpEditorActivity;
import com.st.nfc.activity.FileChooserActivity;
import com.st.nfc.activity.PreferencesActivity;
import com.st.persenter.ICallback;
import com.st.persenter.MainPresenter;
import com.st.service.request.RequestConfig;
import com.st.utils.HintsManager;
import com.st.view.widget.FloatingLable;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.st.nfc.activity.PreferencesActivity.Preference.UseInternalStorage;

/**
 * Created by cgw on 2017-12-06.
 *
 */

public class WriteCardActivity extends BasicActivity implements View.OnClickListener {

    /**
     * The corresponding Intent will contain a dump. Headers
     * (e.g. "Sector: 1") are marked with a "+"-symbol (e.g. "+Sector: 1").
     */
    public final static String EXTRA_DUMP =
            "de.syss.MifareClassicTool.Activity.DUMP";

    private static final int CKM_WRITE_DUMP = 2;
    private static final int CKM_WRITE_BLOCK = 3;
    private static final int CKM_WRITE_NEW_VALUE = 5;



    private HashMap<Integer, HashMap<Integer, byte[]>> mDumpWithPos;
    private boolean mWriteDumpFromEditor = false;
    private String[] mDumpFromEditor;

    @BindView(R.id.editTextWriteTagSector)    FloatingLable mSectorTextBlock;
    @BindView(R.id.editTextWriteTagBlock)    FloatingLable mBlockTextBlock;
    @BindView(R.id.editTextWriteTagData)    FloatingLable mDataText;
    @BindView(R.id.write_cardRecycler)    RecyclerView writeRecyclerView;
    @BindView(R.id.linearLayoutCreateKeyMapKeyFiles)    LinearLayout mKeyFilesGroup;


    private WriteAdapter mAdapter;
    //课题
    private List<EmployeeEntity> listEntity;
    //请求控制类
    private MainPresenter mainPresenter;
    private DBUtils instance;

    //需要加密的医通卡号
    private String newData, md5Number;
    private String userNumber;
    //医通卡输入框内容
    private String data;
    //医通卡唯一ID
    private String onlyTagID;

    private CMEApplication app;

    private int tagSelector;
    private int blockSelector;

    private HintsManager hintsManager;
    //广播Action
    private String m_Broadcastname;

















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

    private final Handler mHandler = new Handler();
    private int mProgressStatus;
    private boolean mIsCreatingKeyMap;
    private File mKeyDirPath;
//    private int mFirstSector = 1;
//    private int mLastSector = 2;
    private int mFirstSector = 1;
    private int mLastSector = 1;


    /**
     * 初始化布局和一些成员变量。
     *
     * Initialize the layout and some member variables. If the Intent
     * contains {@link #EXTRA_DUMP} (and therefore was send from
     * {@link DumpEditorActivity}), the write dump option will be adjusted
     * accordingly.
     */
    // It is checked but the IDE don't get it.
    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app = (CMEApplication) getApplication();
        // Restore mDumpWithPos and the "write to manufacturer block"-state.
        if (savedInstanceState != null) {

            Serializable s = savedInstanceState
                    .getSerializable("dump_with_pos");
            if (s instanceof HashMap<?, ?>) {
                mDumpWithPos = (HashMap<Integer, HashMap<Integer, byte[]>>) s;
            }
        }

        Intent intent = getIntent();

        userNumber = intent.getStringExtra("userNumber");
        Log.d("logInfo", "userNumber--->"+userNumber);

        if (intent.hasExtra(EXTRA_DUMP)) {
            // Write dump directly from editor.
            mDumpFromEditor = intent.getStringArrayExtra(EXTRA_DUMP);
            mWriteDumpFromEditor = true;
        }

        setTitle("写入医通卡号");
        if (null != userNumber && !"".equals(userNumber)){
            mDataText.getEditText().setText(userNumber);
        }

        hintsManager = HintsManager.getInstance();
        hintsManager.init(this);

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
        Log.d("logInfo","onlyTagID--->"+onlyTagID);

        //扫描枪
        final IntentFilter intentFilter = new IntentFilter();
        m_Broadcastname = "com.barcode.sendBroadcast";// com.barcode.sendBroadcastScan
        intentFilter.addAction(m_Broadcastname);
        registerReceiver(receiver, intentFilter);

    }

    @Override
    public void onPause() {
        super.onPause();
        mIsCreatingKeyMap = false;
        unregisterReceiver(receiver);

    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_write_card;
    }

    @Override
    public void onInitView() {
        ButterKnife.bind(this);

        mSectorTextBlock.setOnClickListener(this);
        mBlockTextBlock.setOnClickListener(this);
        mDataText.setOnClickListener(this);

//        tagSelector = RandomUntils.getNum(1,3);
//        blockSelector = RandomUntils.getNum(3);
        tagSelector = 1;
        blockSelector = 0;

        mainPresenter = new MainPresenter();
        //初始化查询结果界面
        initRecycler();
    }

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
                if (NetWorkUtil.isNetWorkAvailable(WriteCardActivity.this)){
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
                String usernumber = listEntity.get(position).getUsernumber();
                mDataText.getEditText().setText(usernumber);
            }
        });
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
                    mDataText.setText(empNumber);
                }
            }
        }
    };

    /**
     * Save {@link # mWriteManufBlock} state and {@link #mDumpWithPos}.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("dump_with_pos", mDumpWithPos);
    }

    /**
     * Handle incoming results from {@link WriteCardResultActivity} or
     * {@link FileChooserActivity}.
     *
     * @see #writeBlock()
     * @see #checkTag()
     */
    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int ckmError = -1;

        switch (requestCode) {
            case CKM_WRITE_DUMP:
                if (resultCode != Activity.RESULT_OK) {
                    // Error.
                    ckmError = resultCode;
                } else {
                    checkTag();
                }
                break;
            case CKM_WRITE_BLOCK:
                if (resultCode != Activity.RESULT_OK) {
                    // Error.
                    ckmError = resultCode;
                } else {
                    // Write block.
                    writeBlock();
                }
                break;
        }

        // Error handling for the return value of WriteCardResultActivity.
        // So far, only error nr. 4 needs to be handled.
        switch (ckmError) {
            case 4:
                // Error. Path from the calling intend was null.
                // (This is really strange and should not occur.)
                Toast.makeText(this, R.string.info_strange_error,
                        Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Check the user input and, if necessary, the BCC value
     * ({@link # checkBCC(boolean)}). If everythin is O.K., show the
     * {@link WriteCardResultActivity} with predefined mapping range (se
     * {@link #createKeyMapForBlock(int, boolean)}).
     * After a key map was created, {@link #writeBlock()} will be triggered.
     *
     * @param view The View object that triggered the method
     *             (in this case the write block button).
     * @see WriteCardResultActivity
     * @see # checkBCC(boolean)
     * @see #createKeyMapForBlock(int, boolean)
     */
    public void onWriteBlock(View view) throws Exception {


        //是否选择扇区
        if (!checkSectorAndBlock(tagSelector, blockSelector)) {
            return;
        }

        //验证是否是12位医通卡
        data = mDataText.getText().trim().toString();
        if (12 != data.trim().length()) {
            showMessage("请输入12位医通卡号");
            return;
        }

        //验证医通卡唯一标识UID
        if (null == onlyTagID
                || "".equals(onlyTagID)){
            showMessage("未检测到医通卡！");
            return;
        }

        //将医通卡与UID关联
        if (null != userNumber && !"".equals(userNumber)){
            newData = onlyTagID+"901234567890" + data;
            Log.d("logInfo", newData);
        } else {
            newData = onlyTagID+"901234567890" + data;
        }

        //验证输入的医通卡是否满足16位验证条件
        if (!CMEApplication.isHexAnd16Byte(newData, this)) {
            return;
        }



        /*md5Number = MD5Utils.GetMD5Code(data);
        //验证输入的医通卡是否满足16位验证条件
        if (!CMEApplication.isHexAnd16Byte(md5Number, this)) {
            return;
        }*/



        //验证是否选择扇区
        if (!isSectorInRage(this, true)) {
            return;
        }

        //写入医通卡提示框
        DialogUIUtils.showAlert(WriteCardActivity.this, "写入医通卡号", "确定写入当前输入的医通卡号吗？"
                , "", "", "取消", "确定", false
                , true, true, new DialogUIListener() {
                    @Override
                    public void onPositive() {

                    }
                    @Override
                    public void onNegative() {

                        onCreateKeyMap();

//                        createKeyMapForBlock(tagSelector, false);
                    }
                }).show();
    }

    /**
     * Check the user input of the sector and the block field. This is a
     * helper function for {@link #onWriteBlock(View)} and
     * {@link # onWriteValue(View)}.
     *
     * @param sector Sector input field.
     * @param block  Block input field.
     * @return True if both values are okay. False otherwise.
     */
    private boolean checkSectorAndBlock(int sector, int block) {
        if (sector > WriteCardResultActivity.MAX_SECTOR_COUNT - 1
                || sector < 0) {
            // Error, sector is out of range for any MIFARE tag.
            Toast.makeText(this, R.string.info_sector_out_of_range,
                    Toast.LENGTH_LONG).show();
            return false;
        }
        if (block > WriteCardResultActivity.MAX_BLOCK_COUNT_PER_SECTOR - 1
                || block < 0) {
            // Error, block is out of range for any MIFARE tag.
            Toast.makeText(this, R.string.info_block_out_of_range,
                    Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


    /**
     * Helper function for {@link #onWriteBlock(View)} and
     * {@link # onWriteValue(View)} to show
     * the {@link WriteCardResultActivity}.
     *
     * @param sector       The sector for the mapping range of
     *                     {@link WriteCardResultActivity}
     * @param isValueBlock If true, the key map will be created for a Value
     *                     Block ({@link #()}).
     * @see WriteCardResultActivity
     * @see #onWriteBlock(View)
     * @see # onWriteValue(View)
     */
    private void createKeyMapForBlock(int sector, boolean isValueBlock) {
        Intent intent = new Intent(this, WriteCardResultActivity.class);
        intent.putExtra(WriteCardResultActivity.EXTRA_KEYS_DIR,
                CMEApplication.getFileFromStorage(CMEApplication.HOME_DIR + "/" +
                        CMEApplication.KEYS_DIR).getAbsolutePath());
        intent.putExtra(WriteCardResultActivity.EXTRA_SECTOR_CHOOSER, false);
        intent.putExtra(WriteCardResultActivity.EXTRA_SECTOR_CHOOSER_FROM, sector);
        intent.putExtra(WriteCardResultActivity.EXTRA_SECTOR_CHOOSER_TO, sector);
        if (isValueBlock) {
            intent.putExtra(WriteCardResultActivity.EXTRA_BUTTON_TEXT, getString(
                    R.string.action_create_key_map_and_write_value_block));
            startActivityForResult(intent, CKM_WRITE_NEW_VALUE);
        } else {
            intent.putExtra(WriteCardResultActivity.EXTRA_BUTTON_TEXT, getString(
                    R.string.action_create_key_map_and_write_block));
            startActivityForResult(intent, CKM_WRITE_BLOCK);
        }
    }

    /**
     * Called from {@link #onActivityResult(int, int, Intent)}
     * after a key map was created, this method tries to write the given
     * data to the tag. Possible errors are displayed to the user via Toast.
     *
     * @see #onActivityResult(int, int, Intent)
     * @see #onWriteBlock(View)
     */
    private void writeBlock() {
        MCReader reader = CMEApplication.checkForTagAndCreateReader(this);
        if (reader == null) {
            return;
        }

        byte[][] keys = CMEApplication.getKeyMap().get(tagSelector);
        int result = -1;

        newData = onlyTagID+"901234567890" + mDataText.getText().toString();

        if (keys != null){
            if (keys[1] != null) {
                result = reader.writeBlock(tagSelector, blockSelector,
                        CMEApplication.hexStringToByteArray(newData),
                        keys[1], true);
            }

            if (result == -1 && keys[0] != null) {
                result = reader.writeBlock(tagSelector, blockSelector,
                        CMEApplication.hexStringToByteArray(newData),
                        keys[0], false);
            }




            //验证医通卡唯一标识UID
            /* if (null == onlyTagID
                    || "".equals(onlyTagID)){
                showMessage("未检测到医通卡！");
                return;
            }
            md5Number = onlyTagID + md5Number;

            if (keys[1] != null) {
                result = reader.writeBlock(tagSelector, blockSelector,
                        CMEApplication.hexStringToByteArray(md5Number),
                        keys[1], true);
            }

            if (result == -1 && keys[0] != null) {
                result = reader.writeBlock(tagSelector, blockSelector,
                        CMEApplication.hexStringToByteArray(md5Number),
                        keys[0], false);
            }*/


            reader.close();

            // Error handling.
            switch (result) {
                case 2:
                    Toast.makeText(this, R.string.info_block_not_in_sector,
                            Toast.LENGTH_LONG).show();
                    return;
                case -1:
                    Toast.makeText(this, R.string.info_error_writing_block,
                            Toast.LENGTH_LONG).show();
                    return;
            }
//            showMessage(getString(R.string.info_write_successful));
            showMessage("医通卡号写入成功！");

            hintsManager.playVoice(6);
            mDataText.getEditText().setText("");
        } else {
            showMessage("写入医通卡的信息出现异常！");

            hintsManager.playVoice(7);
        }

    }

    /**
     * Check if the chosen sector or last sector of a dump is in the
     * range of valid sectors (according to {@link PreferencesActivity}).
     *
     * @param context The context in error messages are displayed.
     * @return True if the sector is in range, False if not. Also,
     * if there was no tag False will be returned.
     */
    private boolean isSectorInRage(Context context, boolean isWriteBlock) {
        MCReader reader = CMEApplication.checkForTagAndCreateReader(this);
        if (reader == null) {
            return false;
        }
        int lastValidSector = reader.getSectorCount() - 1;
//        int lastSector;
        reader.close();
        // Initialize last sector.
        if (isWriteBlock) {
//            lastSector = Integer.parseInt(
//                    mSectorTextBlock.getText().toString());
        } else {
//            lastSector = Collections.max(mDumpWithPos.keySet());
        }

        // Is last sector in range?
        if (tagSelector > lastValidSector) {
//        if (lastSector > lastValidSector) {
            // Error. Tag too small for dump.
            Toast.makeText(context, R.string.info_tag_too_small,
                    Toast.LENGTH_LONG).show();
            reader.close();
            return false;
        }
        return true;
    }

    /**
     * Check if the tag is suitable for the dump ({@link #mDumpWithPos}).
     * This is done in three steps. The first check determines if the dump
     * fits on the tag (size check). The second check determines if the keys for
     * relevant sectors are known (key check). At last this method will check
     * whether the keys with write privileges are known and if some blocks
     * are read-only (write check).<br />
     * If some of these checks "fail", the user will get a report dialog
     * with the two options to cancel the whole write process or to
     * write as much as possible(call {@link #writeDump(HashMap,
     * SparseArray)}).
     *
     * @see MCReader#isWritableOnPositions(HashMap, SparseArray)
     * @see CMEApplication #getOperationInfoForBlock(byte, byte,
     * byte, de.syss.MifareClassicTool.CMEApplication.Operations, boolean, boolean)
     * @see #writeDump(HashMap, SparseArray)
     */
    private void checkTag() {
        // Create reader.
        MCReader reader = CMEApplication.checkForTagAndCreateReader(this);
        if (reader == null) {
            return;
        }

        // Check if tag is correct size for dump.
        if (reader.getSectorCount() - 1 < Collections.max(
                mDumpWithPos.keySet())) {
            // Error. Tag too small for dump.
            Toast.makeText(this, R.string.info_tag_too_small,
                    Toast.LENGTH_LONG).show();
            reader.close();
            return;
        }

        // Check if tag is writable on needed blocks.
        // Reformat for reader.isWritableOnPosition(...).
        final SparseArray<byte[][]> keyMap =
                CMEApplication.getKeyMap();
        HashMap<Integer, int[]> dataPos =
                new HashMap<Integer, int[]>(mDumpWithPos.size());
        for (int sector : mDumpWithPos.keySet()) {
            int i = 0;
            int[] blocks = new int[mDumpWithPos.get(sector).size()];
            for (int block : mDumpWithPos.get(sector).keySet()) {
                blocks[i++] = block;
            }
            dataPos.put(sector, blocks);
        }
        HashMap<Integer, HashMap<Integer, Integer>> writeOnPos =
                reader.isWritableOnPositions(dataPos, keyMap);
        reader.close();

        if (writeOnPos == null) {
            // Error while checking for keys with write privileges.
            Toast.makeText(this, R.string.info_check_ac_error,
                    Toast.LENGTH_LONG).show();
            return;
        }

        // Skip dialog:
        // Build a dialog showing all sectors and blocks containing data
        // that can not be overwritten with the reason why they are not
        // writable. The user can chose to skip all these blocks/sectors
        // or to cancel the whole write procedure.
        List<HashMap<String, String>> list = new
                ArrayList<HashMap<String, String>>();
        final HashMap<Integer, HashMap<Integer, Integer>> writeOnPosSafe =
                new HashMap<Integer, HashMap<Integer, Integer>>(
                        mDumpWithPos.size());
        // Keys that are missing completely (mDumpWithPos vs. keyMap).
        HashSet<Integer> sectors = new HashSet<Integer>();
        for (int sector : mDumpWithPos.keySet()) {
            if (keyMap.indexOfKey(sector) < 0) {
                // Problem. Keys for sector not found.
                addToList(list, getString(R.string.text_sector) + ": " + sector,
                        getString(R.string.text_keys_not_known));
            } else {
                sectors.add(sector);
            }
        }
        // Keys with write privileges that are missing or some
        // blocks (block-parts) are read-only (writeOnPos vs. keyMap).
        for (int sector : sectors) {
            if (writeOnPos.get(sector) == null) {
                // Error. Sector is dead (IO Error) or ACs are invalid.
                addToList(list, getString(R.string.text_sector) + ": " + sector,
                        getString(R.string.text_invalid_ac_or_sector_dead));
                continue;
            }
            byte[][] keys = keyMap.get(sector);
            Set<Integer> blocks = mDumpWithPos.get(sector).keySet();
            for (int block : blocks) {
                boolean isSafeForWriting = true;
                if (/*!mWriteManufBlock.isChecked()
                        && */sector == 0 && block == 0) {
                    // Block 0 is read-only. This is normal.
                    // Do not add an entry to the dialog and skip the
                    // "write info" check (except for some
                    // special (non-original) MIFARE tags).
                    continue;
                }
                String position = getString(R.string.text_sector) + ": "
                        + sector + ", " + getString(R.string.text_block)
                        + ": " + block;
                int writeInfo = writeOnPos.get(sector).get(block);
                switch (writeInfo) {
                    case 0:
                        // Problem. Block is read-only.
                        addToList(list, position, getString(
                                R.string.text_block_read_only));
                        isSafeForWriting = false;
                        break;
                    case 1:
                        if (keys[0] == null) {
                            // Problem. Key with write privileges (A) not known.
                            addToList(list, position, getString(
                                    R.string.text_write_key_a_not_known));
                            isSafeForWriting = false;
                        }
                        break;
                    case 2:
                        if (keys[1] == null) {
                            // Problem. Key with write privileges (B) not known.
                            addToList(list, position, getString(
                                    R.string.text_write_key_b_not_known));
                            isSafeForWriting = false;
                        }
                        break;
                    case 3:
                        // No Problem. Both keys have write privileges.
                        // Set to key A or B depending on which one is available.
                        writeInfo = (keys[0] != null) ? 1 : 2;
                        break;
                    case 4:
                        if (keys[0] == null) {
                            // Problem. Key with write privileges (A) not known.
                            addToList(list, position, getString(
                                    R.string.text_write_key_a_not_known));
                            isSafeForWriting = false;
                        } else {
                            // Problem. ACs are read-only.
                            addToList(list, position, getString(
                                    R.string.text_ac_read_only));
                        }
                        break;
                    case 5:
                        if (keys[1] == null) {
                            // Problem. Key with write privileges (B) not known.
                            addToList(list, position, getString(
                                    R.string.text_write_key_b_not_known));
                            isSafeForWriting = false;
                        } else {
                            // Problem. ACs are read-only.
                            addToList(list, position, getString(
                                    R.string.text_ac_read_only));
                        }
                        break;
                    case 6:
                        if (keys[1] == null) {
                            // Problem. Key with write privileges (B) not known.
                            addToList(list, position, getString(
                                    R.string.text_write_key_b_not_known));
                            isSafeForWriting = false;
                        } else {
                            // Problem. Keys are read-only.
                            addToList(list, position, getString(
                                    R.string.text_keys_read_only));
                        }
                        break;
                    case -1:
                        // Error. Some strange error occurred. Maybe due to some
                        // corrupted ACs...
                        addToList(list, position, getString(
                                R.string.text_strange_error));
                        isSafeForWriting = false;
                }
                // Add if safe for writing.
                if (isSafeForWriting) {
                    if (writeOnPosSafe.get(sector) == null) {
                        // Create sector.
                        HashMap<Integer, Integer> blockInfo =
                                new HashMap<Integer, Integer>();
                        blockInfo.put(block, writeInfo);
                        writeOnPosSafe.put(sector, blockInfo);
                    } else {
                        // Add to sector.
                        writeOnPosSafe.get(sector).put(block, writeInfo);
                    }
                }
            }
        }

        // Show skip/cancel dialog (if needed).
        if (list.size() != 0) {
            // If the user skips all sectors/blocks that are not writable,
            // the writeTag() method will be called.
            LinearLayout ll = new LinearLayout(this);
            int pad = CMEApplication.dpToPx(5);
            ll.setPadding(pad, pad, pad, pad);
            ll.setOrientation(LinearLayout.VERTICAL);
            TextView textView = new TextView(this);
            textView.setText(R.string.dialog_not_writable);
            textView.setTextAppearance(this,
                    android.R.style.TextAppearance_Medium);
            ListView listView = new ListView(this);
            ll.addView(textView);
            ll.addView(listView);
            String[] from = new String[]{"position", "reason"};
            int[] to = new int[]{android.R.id.text1, android.R.id.text2};
            ListAdapter adapter = new SimpleAdapter(this, list,
                    android.R.layout.two_line_list_item, from, to);
            listView.setAdapter(adapter);

            new AlertDialog.Builder(this)
                    .setTitle(R.string.dialog_not_writable_title)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setView(ll)
                    .setPositiveButton(R.string.action_skip_blocks,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Skip not writable blocks and start writing.
                                    writeDump(writeOnPosSafe, keyMap);
                                }
                            })
                    .setNegativeButton(R.string.action_cancel_all,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Do nothing.
                                }
                            })
                    .show();
        } else {
            // Write.
            writeDump(writeOnPosSafe, keyMap);
        }
    }

    /**
     * A helper function for {@link #checkTag()} adding an item to
     * the list of all blocks with write issues.
     * This list will be displayed to the user in a dialog before writing.
     *
     * @param list     The list in which to add the key-value-pair.
     * @param position The key (position) for the list item
     *                 (e.g. "Sector 2, Block 3").
     * @param reason   The value (reason) for the list item
     *                 (e.g. "Block is read-only").
     */
    private void addToList(List<HashMap<String, String>> list,
                           String position, String reason) {
        HashMap<String, String> item = new HashMap<String, String>();
        item.put("position", position);
        item.put("reason", reason);
        list.add(item);
    }

    /**
     * This method is triggered by {@link #checkTag()} and writes a dump
     * to a tag.
     *
     * @param writeOnPos A map within a map (all with type = Integer).
     *                   The key of the outer map is the sector number and the value is another
     *                   map with key = block number and value = write information. The write
     *                   information must be filtered (by {@link #checkTag()}) return values
     *                   of {@link MCReader#isWritableOnPositions(HashMap, SparseArray)}.<br />
     *                   Attention: This method does not any checking. The position and write
     *                   information must be checked by {@link #checkTag()}.
     * @param keyMap     A key map generated by {@link WriteCardResultActivity}.
     */
    private void writeDump(
            final HashMap<Integer, HashMap<Integer, Integer>> writeOnPos,
            final SparseArray<byte[][]> keyMap) {
        // Check for write data.
        if (writeOnPos.size() == 0) {
            // Nothing to write. Exit.
            Toast.makeText(this, R.string.info_nothing_to_write,
                    Toast.LENGTH_LONG).show();
            return;
        }

        // Create reader.
        final MCReader reader = CMEApplication.checkForTagAndCreateReader(this);
        if (reader == null) {
            return;
        }

        // Display don't remove warning.
        LinearLayout ll = new LinearLayout(this);
        int pad = CMEApplication.dpToPx(10);
        ll.setPadding(pad, pad, pad, pad);
        ll.setGravity(Gravity.CENTER);
        ProgressBar progressBar = new ProgressBar(this);
        progressBar.setIndeterminate(true);
        pad = CMEApplication.dpToPx(5);
        progressBar.setPadding(0, 0, pad, 0);
        TextView tv = new TextView(this);
        tv.setText(getString(R.string.dialog_wait_write_tag));
        tv.setTextSize(18);
        ll.addView(progressBar);
        ll.addView(tv);
        final AlertDialog warning = new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_wait_write_tag_title)
                .setView(ll)
                .create();
        warning.show();


        // Start writing in new thread.
        final Activity a = this;
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Write dump to tag.
                for (int sector : writeOnPos.keySet()) {
                    byte[][] keys = keyMap.get(sector);
                    for (int block : writeOnPos.get(sector).keySet()) {
                        // Select key with write privileges.
                        byte writeKey[] = null;
                        boolean useAsKeyB = true;
                        int wi = writeOnPos.get(sector).get(block);
                        if (wi == 1 || wi == 4) {
                            writeKey = keys[0]; // Write with key A.
                            useAsKeyB = false;
                        } else if (wi == 2 || wi == 5 || wi == 6) {
                            writeKey = keys[1]; // Write with key B.
                        }

                        // Write block.
                        int result = reader.writeBlock(sector, block,
                                mDumpWithPos.get(sector).get(block),
                                writeKey, useAsKeyB);

                        if (result != 0) {
                            // Error. Some error while writing.
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(a,
                                            R.string.info_write_error,
                                            Toast.LENGTH_LONG).show();
                                }
                            });
                            reader.close();
                            warning.cancel();
                            return;
                        }
                    }
                }
                // Finished writing.
                reader.close();
                warning.cancel();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(a, R.string.info_write_successful,
                                Toast.LENGTH_LONG).show();
                    }
                });
                a.finish();
            }
        }).start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.editTextWriteTagSector:
                final String[] sequences = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"};
                DialogUIUtils.showSingleChoose(WriteCardActivity.this, "选择扇区", 0
                        , sequences, false, true, new DialogUIItemListener() {
                            @Override
                            public void onItemClick(CharSequence text, int position) {
                                mSectorTextBlock.setText(String.valueOf(position));
                            }
                        }).show();
                break;
            case R.id.editTextWriteTagBlock:
                final String[] sequencess = new String[]{"0", "1", "2", "3"};
                DialogUIUtils.showSingleChoose(WriteCardActivity.this, "选择块区", 0
                        , sequencess, false, true, new DialogUIItemListener() {
                            @Override
                            public void onItemClick(CharSequence text, int position) {
                                mBlockTextBlock.setText(String.valueOf(position));
                            }
                        }).show();
                break;
        }
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


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
                    Log.d(WriteCardActivity.class.getSimpleName(), "Key file "
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
                                MCReader reader = CMEApplication.checkForTagAndCreateReader(WriteCardActivity.this);
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
            if (arraylist.size() > 0){
                for (int i = 0; i < arraylist.size(); i++) {
                    empNumber += arraylist.get(i);
                }


                //写入医通卡提示框
                DialogUIUtils.showAlert(WriteCardActivity.this, "写入医通卡号", "该医通卡已经存在医通卡号，确定要重新写入吗？"
                        , "", "", "取消", "确定", false
                        , true, true, new DialogUIListener() {
                            @Override
                            public void onPositive() {

                            }
                            @Override
                            public void onNegative() {

                                createKeyMapForBlock(tagSelector, false);
                            }
                        }).show();

            } else {

                createKeyMapForBlock(tagSelector, false);

                return false;
            }
            return true;
        } else {
            showMessage("读取医通卡的信息出现异常！");
            return false;
        }
    }




}
