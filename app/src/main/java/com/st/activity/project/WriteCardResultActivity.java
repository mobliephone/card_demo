package com.st.activity.project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.st.CMEApplication;
import com.st.R;
import com.st.nfc.BasicActivity;
import com.st.nfc.MCReader;
import com.st.nfc.activity.PreferencesActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import static com.st.nfc.activity.PreferencesActivity.Preference.UseInternalStorage;


/**
 * 读取医通卡回调执行界面
 * Configure key map process and create key map.
 * This Activity should be called via startActivityForResult() with
 * an Intent containing the {@link #EXTRA_KEYS_DIR}.
 *
 * 配置键映射进程并创建键映射。
 * 此活动应该被称为startactivityforresult()与经
 * 一个意图包含{@链接# extra_keys_dir }。
 *
 * The result codes are:
 * <li>{@link Activity#RESULT_OK} - Everything is O.K. The key map can be
 * retrieved by calling {@link CMEApplication#getKeyMap()}.</li>
 * <li>1 - Directory from {@link #EXTRA_KEYS_DIR} does not
 * exist.</li>
 * <li>2 - No directory specified in Intent
 * ({@link #EXTRA_KEYS_DIR})</li>
 * <li>3 - External Storage is not read/writable. This error is
 * displayed to the user via Toast.</li>
 * <li>4 - Directory from {@link #EXTRA_KEYS_DIR} is null.</li>
 * </ul>
 * @author Gerhard Klostermeier
 */
public class WriteCardResultActivity extends BasicActivity {

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


    /**
     * A boolean value to enable (default) or disable the possibility for the
     * user to change the key mapping range.
     * 启用（默认）的布尔值或禁用
     * 用户更改键映射范围。
     */
    public final static String EXTRA_SECTOR_CHOOSER =
            "de.syss.MifareClassicTool.Activity.SECTOR_CHOOSER";


    /**
     * An integer value that represents the number of the
     * first sector for the key mapping process.
     * 表示值的整数值。
     * 关键制图过程的第一部门。
     */
    public final static String EXTRA_SECTOR_CHOOSER_FROM =
            "de.syss.MifareClassicTool.Activity.SECTOR_CHOOSER_FROM";


    /**
     * An integer value that represents the number of the
     * last sector for the key mapping process.
     * 表示值的整数值。
     * 关键制图过程的最后扇区。
     */
    public final static String EXTRA_SECTOR_CHOOSER_TO =
            "de.syss.MifareClassicTool.Activity.SECTOR_CHOOSER_TO";


    /**
     * The title of the activity. Optional.
     * e.g. "Map Keys to Sectors"
     * 活动名称。可选。
     * “对扇区的映射键”
     */
    public final static String EXTRA_TITLE =
            "de.syss.MifareClassicTool.Activity.TITLE";


    /**
     * The text of the start key mapping button. Optional.
     * e.g. "Map Keys to Sectors"
     * 开始键映射按钮的文本。可选。
     * “对扇区的映射键”
     */
    public final static String EXTRA_BUTTON_TEXT =
            "de.syss.MifareClassicTool.Activity.BUTTON_TEXT";

    // Sector count of the biggest MIFARE Classic tag (4K Tag)
    public static final int MAX_SECTOR_COUNT = 40;
    // Block count of the biggest sector (4K Tag, Sector 32-39)
    public static final int MAX_BLOCK_COUNT_PER_SECTOR = 16;

    private static final String LOG_TAG =
            WriteCardResultActivity.class.getSimpleName();

    private LinearLayout mKeyFilesGroup,card_result_main;
    private final Handler mHandler = new Handler();
    private int mProgressStatus;
    private boolean mIsCreatingKeyMap;
    private File mKeyDirPath;
    private int mFirstSector = 0;
    private int mLastSector = 15;

    /**
     * Set layout, set the mapping range
     * and initialize some member variables.
     * @see #EXTRA_SECTOR_CHOOSER
     * @see #EXTRA_SECTOR_CHOOSER_FROM
     * @see #EXTRA_SECTOR_CHOOSER_TO
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Init. sector range.
        Intent intent = getIntent();
        // Init. title and button text.
        if (intent.hasExtra(EXTRA_TITLE)) {
            setTitle(intent.getStringExtra(EXTRA_TITLE));
        }

        setTitle("写入医通卡号");
        //禁止退出界面
        setShowNavigation(false);
    }

    /**
     * List files from the {@link #EXTRA_KEYS_DIR} and select the last used
     * 清单文件从{@链接# extra_keys_dir }选择最后使用
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
        onCreateKeyMap();
    }

    /**
     * Cancel the mapping process and disable NFC foreground dispatch system.
     * This method is not called, if screen orientation changes.
     * 取消映射过程并禁用NFC前台调度系统。
     * 如果屏幕方向更改，则不调用此方法。
     * @see CMEApplication#disableNfcForegroundDispatch(Activity)
     */
    @Override
    public void onPause() {
        super.onPause();
        mIsCreatingKeyMap = false;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_pay_by_card_result;
    }

    @Override
    public void onInitView() {

        mKeyFilesGroup = (LinearLayout) findViewById(R.id.linearLayoutCreateKeyMapKeyFiles);
        card_result_main = (LinearLayout) findViewById(R.id.card_result_main);
        card_result_main.setVisibility(View.GONE);

    }

    /**
     * Create a key map and save it to
     * {@link CMEApplication#setKeyMap(android.util.SparseArray)}.
     * For doing so it uses other methods (
     * {@link #createKeyMap(MCReader)},
     * {@link #keyMapCreated(MCReader)}).
     * If {@link PreferencesActivity #SaveLastUsedKeyFiles} is active, this will also
     * save the selected key files.
     * @paramThe View object that triggered the method
     * (in this case the map keys to sectors button).
     *
     * 创建一个键映射并将其保存到
     * {@链接cmeapplication # setkeymap（Android使用java. sparsearray）}。
     * 这样做，它使用其他方法（
     * {@链接# createkeymap（com.st.nfc.mcreader，上下文）}，
     * {@链接# keymapcreated（mcreader）}）。
     * 如果{@链接preferencesactivity # savelastusedkeyfiles }是活跃的，这也
     * 保存选定的密钥文件。
     * @param视图触发方法的视图对象
     *（在这种情况下，映射键为扇区按钮）。
     * @see #createKeyMap(MCReader)
     * @see #keyMapCreated(MCReader)
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
                    Editor e = sharedPref.edit();
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
                getWindow().addFlags(
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                // Set map creation range.
                if (!reader.setMappingRange(
                        mFirstSector, mLastSector)) {
                    // Error.

                    showMessage("写入医通卡的信息出现异常！");
                    reader.close();

                    finish();

                    return;
                }
                CMEApplication.setKeyMapRange(mFirstSector, mLastSector);
                // Init. GUI elements.
                mProgressStatus = -1;
                mIsCreatingKeyMap = true;
                // Read as much as possible with given key file.
                createKeyMap(reader);
            }
        }
    }

    /**
     * Triggered by {@link #onCreateKeyMap()} this
     * method starts a worker thread that first creates a key map and then
     * calls {@link #keyMapCreated(MCReader)}.
     * It also updates the progress bar in the UI thread.
     *
     * 引发{@链接# oncreatekeymap（）}这
     * 方法首先创建一个工作线程，然后创建一个键映射，然后
     * 回调{@链接# keymapcreated（mcreader）}。
     * 它还更新UI线程中的进度条。
     * @param reader A connected {@link MCReader}.
     * @see #onCreateKeyMap()
     * @see #keyMapCreated(MCReader)
     */
    private void createKeyMap(final MCReader reader) {
        showDialog("正在往医通卡写入信息中...");
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
                        getWindow().clearFlags(
                                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                        reader.close();
                        if (mIsCreatingKeyMap && mProgressStatus != -1) {
                            keyMapCreated(reader);
                        } else {
                            // Error during key map creation.
                            CMEApplication.setKeyMap(null);
                            CMEApplication.setKeyMapRange(-1, -1);
                            showMessage("写入医通卡的信息出现异常！");

                            finish();
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
     *
     * 引发{@链接# createkeymap（mcreader，上下文）}，这种方法
     * 集结果代码{@链接活动# result_ok }，
     * 将创建的键映射保存到
     * {@链接cmeapplication # setkeymap（Android使用java. sparsearray）}
     * 完成这项活动。
     * @param reader A {@link MCReader}.
     * @see #createKeyMap(MCReader)
     * @see #onCreateKeyMap()
     */
    private void keyMapCreated(MCReader reader) {
        // LOW: Return key map in intent.
        if (reader.getKeyMap().size() == 0) {
            CMEApplication.setKeyMap(null);
            // Error. No valid key found.
            showMessage("写入医通卡的信息出现异常！");

            finish();
        } else {
            CMEApplication.setKeyMap(reader.getKeyMap());
            setResult(Activity.RESULT_OK);
            finish();
        }

    }

    /**
     * 禁止退出当前界面
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK ){
            showMessage("当前界面不可进行退出操作！");
            Log.e("logInfo","onKeyDown--->WriteCardResultActivity");
//            showAlertExit(WriteCardResultActivity.this,"当前界面不可进行退出操作！","");
        }
        return false;
    }
}
