package com.st.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.framework.json.J;
import com.framework.manager.AppCacheManager;
import com.framework.manager.AppManager;
import com.framework.util.DateUtils;
import com.framework.widget.ui.UIDrawLayout;
import com.st.CMEApplication;
import com.st.R;
import com.st.activity.finger.FingerActivity;
import com.st.activity.project.PunchCardActivity;
import com.st.activity.project.WriteCardActivity;
import com.st.db.DBUtils;
import com.st.db.dao.CourseEntityDao;
import com.st.db.dao.EmployeeEntityDao;
import com.st.db.dao.StudyRegistrationEntityDao;
import com.st.db.entity.CourseEntity;
import com.st.db.entity.EmployeeEntity;
import com.st.activity.adapter.CouPullToRefreshAdapter;
import com.st.nfc.BasicActivity;
import com.st.nfc.activity.TagInfoToolActivity;
import com.st.persenter.ICallback;
import com.st.persenter.MainPresenter;
import com.st.service.request.RequestConfig;
import com.st.zxing.CommonScanActivity;
import com.st.zxing.inter.Constant;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.st.nfc.activity.KeyMapCreatorActivity.EXTRA_KEYS_DIR;

/**
 * Created by cgw on 2017-11-23.
 * 主界面
 */

public class CMEActivity extends BasicActivity implements View.OnClickListener {

    @BindView(R.id.cme_main)            LinearLayout cmeMain;
    @BindView(R.id.menu_readNfc)        LinearLayout readNfc;
    @BindView(R.id.menu_readZXing)      LinearLayout readZXing;
    @BindView(R.id.menu_menuClear)      LinearLayout upClear;
    @BindView(R.id.menu_menuSetting)    LinearLayout menuSetting;
    @BindView(R.id.menu_fingerPrint)    LinearLayout menuFingerPrint;
    @BindView(R.id.menu_exit)           Button exitLogin;
    @BindView(R.id.main_drawer)         UIDrawLayout mDrawerLayout;
    @BindView(R.id.course_recyclerView)    RecyclerView proRecyclerView;
    @BindView(R.id.course_swipeRefresh)    SwipeRefreshLayout proSwipeRefresh;
    @BindView(R.id.main_text)           TextView mainText;

    private EditText searchCourse;
    private ActionBarDrawerToggle mToggle;

    //控制类
    private MainPresenter mainPresenter;
    private CMEApplication app;

    //人员信息数据类
    private EmployeeEntityDao employeeDao;
    private CourseEntityDao courseDao;

    private StudyRegistrationEntityDao studyDao;
    //刷新适配器
    private CouPullToRefreshAdapter mAdapter;
    //所查询数据总条数
    private String total;
    //当前页数
    private int nowPage= 2;
    private List<CourseEntity> listEntity;
    private AppCacheManager sharePrenManager;
    private DBUtils dbUtils;

    //初始化NFC
    private static final int REQUEST_WRITE_STORAGE_CODE = 1;
    private AlertDialog mEnableNfc;
    private boolean mResume = true;
    private Intent mOldIntent = null;
    //前三个月的时间
    private String currentDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if there is an NFC hardware component.
        // 检查是否有在NFC的硬件组件。
        CMEApplication.setNfcAdapter(NfcAdapter.getDefaultAdapter(this));
        if (CMEApplication.getNfcAdapter() == null) {
            createNfcEnableDialog();
            mEnableNfc.show();
            mDrawerLayout.setEnabled(false);
            mResume = false;
        }

        // Check if there is MIFARE Classic support.
        // 检查是否有支持MIFARE经典。
        if (!CMEApplication.useAsEditorOnly() && !CMEApplication.hasMifareClassicSupport()) {
            // Disable read/write tag options.
            mDrawerLayout.setEnabled(false);
            CharSequence styledText = Html.fromHtml(
                    getString(R.string.dialog_no_mfc_support_device));
            AlertDialog ad = new AlertDialog.Builder(this)
                    .setTitle(R.string.dialog_no_mfc_support_device_title)
                    .setMessage(styledText)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(R.string.action_exit_app,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                    .setNegativeButton(R.string.action_continue,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    mResume = true;
                                    checkNfc();
                                }
                            })
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            finish();
                        }
                    })
                    .show();
            // Make links clickable.
            ((TextView)ad.findViewById(android.R.id.message)).setMovementMethod(
                    LinkMovementMethod.getInstance());
            mResume = false;
        }

        if (app.hasWritePermissionToExternalStorage(this)) {
            initFolders();
        } else {
            // Request the permission.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE_CODE);
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_cme_main;
    }

    @Override
    public void onInitView() {
        ButterKnife.bind(this);

        //显示搜索框
        setSearchLinear(true);
        searchCourse = getSearchEdit();
        setTitle("学分管理");
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, getBaseToolBar(), R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();/*同步状态*/

        app = (CMEApplication) getApplication();
        mainPresenter = new MainPresenter();
        employeeDao = app.getDaoSeeion().getEmployeeEntityDao();
        courseDao  = app.getDaoSeeion().getCourseEntityDao();
        studyDao = app.getDaoSeeion().getStudyRegistrationEntityDao();
        sharePrenManager = AppCacheManager.getInstance();
        dbUtils = DBUtils.getInstance(this,app);
        //扫描一维码或得当前扫描类型
        int mode = getIntent().getIntExtra(Constant.REQUEST_SCAN_MODE, Constant.REQUEST_SCAN_MODE_ALL_MODE);

        //获得当前时间
        currentDate = DateUtils.currentDate();
        //初始化界面
        initView();
    }

    //初始化界面
    private void initView() {
        listEntity = new ArrayList<>();

        //离线状态
        if (!sharePrenManager.popBooleanFromPrefs(com.st.activity.data.Constant.LOGIN_STATE)){
            outLineCourse();
        //在线状态
        } else {
            //初始化RecyclerView
            initRecycler();
            //RecyclerView添加刷新功能
            initRefreshLayout();
            //初始化界面数据
            refresh();
            //事件监听
            initListener();
            //添加头部
            addHeadView();
        }

    }


    private void initListener() {

        readNfc.setOnClickListener(this);
        readZXing.setOnClickListener(this);
        exitLogin.setOnClickListener(this);
        upClear.setOnClickListener(this);
        menuSetting.setOnClickListener(this);
        menuFingerPrint.setOnClickListener(this);
        //搜索框的跳转
        searchCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CMEActivity.this,SearchActivity.class);
                startActivity(intent);
            }
        });

        //打卡Button的点击事件
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                // 打卡点击事件
                Intent intent = new Intent(CMEActivity.this, PunchCardActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("course",mAdapter.getData().get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View view) {

        Intent intent;
        switch (view.getId()){
            //写卡
            case R.id.menu_readNfc:
                intent = new Intent(this, WriteCardActivity.class);

                intent.putExtra(EXTRA_KEYS_DIR,
                        CMEApplication.getFileFromStorage(CMEApplication.HOME_DIR + "/" +
                                CMEApplication.KEYS_DIR).getAbsolutePath());

                startActivity(intent);
                break;

            case R.id.menu_readZXing:
                intent = new Intent(this, CommonScanActivity.class);
                //扫描条形码
//                intent.putExtra(Constant.REQUEST_SCAN_MODE,Constant.REQUEST_SCAN_MODE_BARCODE_MODE);
                //扫描条形码或二维码
                intent.putExtra(Constant.REQUEST_SCAN_MODE,Constant.REQUEST_SCAN_MODE_ALL_MODE);
                startActivity(intent);
                break;
            case R.id.menu_exit:
                intent = new Intent(CMEActivity.this,LoginActivity.class);
                startActivity(intent);
                this.finish();
                break;
            //清除缓存
            case R.id.menu_menuClear:
                showOutLine();
                break;
            //设置
            case R.id.menu_menuSetting:
                intent = new Intent(CMEActivity.this,SettingActivity.class);
                startActivity(intent);
                break;
            //指纹
            case R.id.menu_fingerPrint:
                intent = new Intent(CMEActivity.this,FingerActivity.class);
                intent.putExtra(EXTRA_KEYS_DIR,
                        CMEApplication.getFileFromStorage(CMEApplication.HOME_DIR + "/" +
                                CMEApplication.KEYS_DIR).getAbsolutePath());
                startActivity(intent);
                break;
        }

    }

    /**
     * 添加适配器
     * */
    private void initRecycler() {

        proSwipeRefresh.setColorSchemeColors(Color.rgb(0, 140, 255 ));
        proRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new CouPullToRefreshAdapter();
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        });
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        proRecyclerView.setAdapter(mAdapter);

        proRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(final BaseQuickAdapter adapter, final View view, final int position) {
                // 打卡点击事件
                Intent intent = new Intent(CMEActivity.this, PunchCardActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("course",mAdapter.getData().get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    /**
     * 离线操作出事化Adapter
     */
    private void outLineRecycler(final List<CourseEntity> listEntity) {
        //禁止下拉刷新
        proSwipeRefresh.setEnabled(false);
        proRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new CouPullToRefreshAdapter();
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        proRecyclerView.setAdapter(mAdapter);

        mAdapter.addData(listEntity);
        proRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(final BaseQuickAdapter adapter, final View view, final int position) {
                // 打卡点击事件
                Intent intent = new Intent(CMEActivity.this, PunchCardActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("course",listEntity.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    /**
     * 添加刷新功能
     * */
    private void initRefreshLayout() {
        proSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }

    /**
     * 刷新数据
     */
    private void refresh() {

        loadCourse("1", "10", "", "", "", currentDate,true);
    }

    /**
     * 加载更多
     */
    private void loadMore() {
            loadCourse(String.valueOf(nowPage), "10", "", "", "", currentDate,false);
    }

    /**
     * 添加头部
     * */
    private void addHeadView() {
        View headView = getLayoutInflater().inflate(R.layout.item_head_main, (ViewGroup) proRecyclerView.getParent(), false);
        mAdapter.addHeaderView(headView);
    }


    /**
     * 查询并缓存课题
     *
     * @param page
     * @param rows
     * @param proName
     * @param proId
     * @param coursesTask
     * @param startDate
     */
    private void loadCourse(String page, String rows, String proName, String proId
            , String coursesTask, String startDate, final boolean isRefresh) {
        showDialog("正在查询中...");
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("page", page);
        requestMap.put("rows", rows);
        requestMap.put("proName", proName);
        requestMap.put("proId", proId);
        requestMap.put("coursesTask", coursesTask);
        requestMap.put("startDate", startDate);
        mainPresenter.allRequestBase(RequestConfig.Url_QueryListCourse, requestMap, new ICallback() {
            @Override
            public void onFail(Throwable e) {
                dismissDialog();
                Log.d("logInfo", e.toString());

                mAdapter.setEnableLoadMore(true);
                proSwipeRefresh.setRefreshing(false);

                showMessage("请检查服务器配置！");
            }

            @Override
            public void onSuccess(String string) {
                dismissDialog();

                total = J.getValue(string, "total");
                if (null == total || "".equals(total)){
                    total = "0";
                }

                Log.d("logInfo", string);
                String rows = J.getRows(string);
                //将json转化为List集合
                listEntity = J.getListEntity(rows, CourseEntity.class);

                if (isRefresh){//第一次刷新数据
                    mAdapter.setNewData(listEntity);

                    mAdapter.setEnableLoadMore(true);
                    proSwipeRefresh.setRefreshing(false);

                    mAdapter.notifyDataSetChanged();
                    nowPage = 2;
                } else {//加载更多数据
                    nowPage++;
                    Double aDouble = Double.valueOf(total);
                    //向下转换
                    double ceil = Math.ceil(aDouble/10);
                    int allPage = (int) ceil;
                    if (nowPage > allPage+1){
                        mAdapter.loadMoreEnd();

                        mAdapter.notifyDataSetChanged();
//                        showMessage("没有更多数据了");
                        return;
                    } else {
                        mAdapter.addData(listEntity);
                        mAdapter.loadMoreComplete();

                        mAdapter.notifyDataSetChanged();
                    }

                }
            }
        }, this);
    }

    /**
     * If resuming is allowed because all dependencies from
     * 如果允许恢复，因为所有依赖项来自
     * {@link #onCreate(Bundle)} are satisfied, call
     * {@link #checkNfc()}
     * @see #onCreate(Bundle)
     * @see #checkNfc()
     */
    @Override
    public void onResume() {
        super.onResume();
        if (mResume) {
            checkNfc();
        }
    }

    /**
     * 检查是否启用了NFC适配器。如果没有，向用户显示一个对话框并让
     * 他在“转到NFC设置”、“只使用编辑器”和“退出应用程序”之间进行选择。
     * 启用NFC前台调度系统。
     * @see CMEApplication#enableNfcForegroundDispatch(Activity)
     */
    private void checkNfc() {
        // Check if the NFC hardware is enabled.
        if (CMEApplication.getNfcAdapter() != null
                && !CMEApplication.getNfcAdapter().isEnabled()) {
            // NFC is disabled.
            // Use as editor only?
            if (!CMEApplication.useAsEditorOnly()) {
                //  Show dialog.
                if (mEnableNfc == null) {
                    createNfcEnableDialog();
                }
                mEnableNfc.show();
            }
            // Disable read/write tag options.
        } else {
            // NFC is enabled. Hide dialog and enable NFC
            // foreground dispatch.
            if (mOldIntent != getIntent()) {
                int typeCheck = CMEApplication.treatAsNewTag(getIntent(), this);
                if (typeCheck == -1 || typeCheck == -2) {
                    // Device or tag does not support MIFARE Classic.
                    // Run the only thing that is possible: The tag info tool.
                    Intent i = new Intent(this, TagInfoToolActivity.class);
                    startActivity(i);
                }
                mOldIntent = getIntent();
            }
            CMEApplication.enableNfcForegroundDispatch(this);
            CMEApplication.setUseAsEditorOnly(false);
            if (mEnableNfc == null) {
                createNfcEnableDialog();
            }
            mEnableNfc.hide();
            if (CMEApplication.hasMifareClassicSupport() &&
                    CMEApplication.hasWritePermissionToExternalStorage(this)) {
                mDrawerLayout.setEnabled(true);
            }
        }
    }

    /**
     * Disable NFC foreground dispatch system.
     * @see CMEApplication#disableNfcForegroundDispatch(Activity)
     */
    @Override
    public void onPause() {
        super.onPause();
        CMEApplication.disableNfcForegroundDispatch(this);
    }

    /**
     * Handle new Intent as a new tag Intent and if the tag/device does not
     * support MIFARE Classic, then run {@link TagInfoToolActivity}.
     * 处理新的意图作为一个新的标签意图，如果标签/设备不
     * 支持Mifare Classic，然后运行{@链接taginfotoolactivity }。
     * @see CMEApplication#treatAsNewTag(Intent, android.content.Context)
     * @see TagInfoToolActivity
     */
    @Override
    public void onNewIntent(Intent intent) {
        int typeCheck = CMEApplication.treatAsNewTag(intent, this);
        if (typeCheck == -1 || typeCheck == -2) {
            // Device or tag does not support MIFARE Classic.
            // Run the only thing that is possible: The tag info tool.
            Intent i = new Intent(this, TagInfoToolActivity.class);
            startActivity(i);
        }
    }


    /**
     * 创建一个对话框，将用户发送到NFC设置，如果NFC关闭（并保存）
     * 对话框{@链接# menablenfc }）。另外，用户可以选择到
     * 只在编辑器中使用应用程序或退出应用程序。
     */
    private void createNfcEnableDialog() {

        mEnableNfc = new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_nfc_not_enabled_title)
                .setMessage(R.string.dialog_nfc_not_enabled)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton(R.string.action_nfc,
                        new DialogInterface.OnClickListener() {
                            @Override
                            @SuppressLint("InlinedApi")
                            public void onClick(DialogInterface dialog, int which) {
                                // Goto NFC Settings.
                                if (Build.VERSION.SDK_INT >= 16) {
                                    startActivity(new Intent(
                                            Settings.ACTION_NFC_SETTINGS));
                                } else {
                                    startActivity(new Intent(
                                            Settings.ACTION_WIRELESS_SETTINGS));
                                }
                            }
                        })
                .setNeutralButton(R.string.action_editor_only,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Only use Editor.
                                CMEApplication.setUseAsEditorOnly(true);
                                showMessage("NFC功能未开启，将无法进行医通卡操作！");
                            }
                        })
                .setNegativeButton(R.string.action_exit_app,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                // Exit the App.
                                AppManager.getAppManager().finishActivity((Activity)mContext);
                            }
                        }).create();
    }



    /**
     * 初始化密钥文件
     *
     * Create the directories needed by MCT and clean out the tmp folder.
     */
    @SuppressLint("ApplySharedPref")
    private void initFolders() {
        boolean isUseInternalStorage = app.getPreferences().getBoolean(
                "use_internal_storage", false);

        // Run twice and init the folders on the internal and external storage.
        for (int i = 0; i < 2; i++) {
            if (!isUseInternalStorage &&
                    !app.isExternalStorageWritableErrorToast(this)) {
                continue;
            }

            // Create keys directory.
            File path = app.getFileFromStorage(
                    app.HOME_DIR + "/" + app.KEYS_DIR);

            if (!path.exists() && !path.mkdirs()) {
                // Could not create directory.
                Log.e("logInfo", "Error while creating '" + app.HOME_DIR
                        + "/" + app.KEYS_DIR + "' directory.");
                return;
            }

            // Create dumps directory.
            path = app.getFileFromStorage(
                    app.HOME_DIR + "/" + app.DUMPS_DIR);
            if (!path.exists() && !path.mkdirs()) {
                // Could not create directory.
                Log.e("logInfo", "Error while creating '" + app.HOME_DIR
                        + "/" + app.DUMPS_DIR + "' directory.");
                return;
            }

            // Create tmp directory.
            path = app.getFileFromStorage(
                    app.HOME_DIR + "/" + app.TMP_DIR);
            if (!path.exists() && !path.mkdirs()) {
                // Could not create directory.
                Log.e("logInfo", "Error while creating '" + app.HOME_DIR
                        + app.TMP_DIR + "' directory.");
                return;
            }
            // Clean up tmp directory.
            for (File file : path.listFiles()) {
                file.delete();
            }

            // Create std. key file if there is none.
            copyStdKeysFilesIfNecessary();

            // Change the storage for the second run.
            app.getPreferences().edit().putBoolean(
                    "use_internal_storage",
                    !isUseInternalStorage).commit();
        }
        // Restore the storage preference.
        app.getPreferences().edit().putBoolean(
                "use_internal_storage",
                isUseInternalStorage).commit();

    }

    /**
     * 复制Assets中的文件
     *
     * Key files are simple text files. Any plain text editor will do the trick.
     * All key and dump data from this App is stored in
     * getExternalStoragePublicDirectory(Common.HOME_DIR) to remain
     * there after App uninstallation.
     */
    private void copyStdKeysFilesIfNecessary() {
        File std = app.getFileFromStorage(app.HOME_DIR + "/" +
                app.KEYS_DIR + "/" + app.STD_KEYS);
        File extended = app.getFileFromStorage(app.HOME_DIR + "/" +
                app.KEYS_DIR + "/" + app.STD_KEYS_EXTENDED);
        AssetManager assetManager = getAssets();

        if (!std.exists()) {
            // Copy std.keys.
            try {
                InputStream in = assetManager.open(
                        app.KEYS_DIR + "/" + app.STD_KEYS);
                OutputStream out = new FileOutputStream(std);
                app.copyFile(in, out);
                in.close();
                out.flush();
                out.close();
            } catch(IOException e) {
                Log.e("logInfo", "Error while copying 'std.keys' from assets "
                        + "to external storage.");
            }
        }
        if (!extended.exists()) {
            // Copy extended-std.keys.
            try {
                InputStream in = assetManager.open(
                        app.KEYS_DIR + "/" + app.STD_KEYS_EXTENDED);
                OutputStream out = new FileOutputStream(extended);
                app.copyFile(in, out);
                in.close();
                out.flush();
                out.close();
            } catch(IOException e) {
                Log.e("logInfo", "Error while copying 'extended-std.keys' "
                        + "from assets to external storage.");
            }
        }
    }

    /**
     * 离线清除缓存
     */
    public void showOutLine(){

        showDialog("正在清空本地数据...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                //查询数据
                Observable.just("")
                        .map(new Func1<String, Void>() {
                            @Override
                            public Void call(String mVoid) {
                                studyDao.queryBuilder().where(StudyRegistrationEntityDao.Properties.Mark.eq("1")).buildDelete().executeDeleteWithoutDetachingEntities();
                                employeeDao.queryBuilder().buildDelete().executeDeleteWithoutDetachingEntities();
                                courseDao.queryBuilder().buildDelete().executeDeleteWithoutDetachingEntities();
                                return null;
                            }
                        }).subscribeOn(Schedulers.io())//把工作线程指定为了IO线程
                        .observeOn(AndroidSchedulers.mainThread())//把回调线程指定为了UI线程
                        .subscribe(new Action1<Void>() {
                            @Override
                            public void call(Void str) {
                                dismissDialog();
                                showMessage("缓存清除成功！");
                                //离线状态
                                if (!sharePrenManager.popBooleanFromPrefs(com.st.activity.data.Constant.LOGIN_STATE)){
                                    Intent intent = new Intent(CMEActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
            }
        }).start();
    }


    /**
     * 查询本地人员信息
     * @param courseEntities
     */
    public void loadAllEmployees(final List<CourseEntity> courseEntities){

        //查询数据
        Observable.just("")
                .map(new Func1<String, List<EmployeeEntity>>() {
                    @Override
                    public List<EmployeeEntity> call(String mVoid) {
                        return dbUtils.loadAllEmployee(employeeDao);
                    }
                }).subscribeOn(Schedulers.io())//把工作线程指定为了IO线程
                .observeOn(AndroidSchedulers.mainThread())//把回调线程指定为了UI线程
                .subscribe(new Action1<List<EmployeeEntity>>() {
                    @Override
                    public void call(List<EmployeeEntity> tempLifeList) {
                        if (courseEntities.size() == 0 || tempLifeList.size() == 0 ){
                            mainText.setVisibility(View.VISIBLE);
                            mainText.setSelected(true);
                            proRecyclerView.setVisibility(View.GONE);
                            proSwipeRefresh.setVisibility(View.GONE);
                            showAlertHorizontal(CMEActivity.this,"数据缓存","未缓存课题和人员信息，是否选择退出APP？");
                        }
                    }
                });
    }


    /**
     * 离线查询课题
     */
    private void outLineCourse(){
        showDialog("正在查询中...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                //查询数据
                Observable.just("")
                        .map(new Func1<String, List<CourseEntity>>() {
                            @Override
                            public List<CourseEntity> call(String mVoid) {
//                                return dbUtils.loadAllCourse(courseDao);
                                return dbUtils.loadAllCourseByTime(courseDao);
                            }
                        }).subscribeOn(Schedulers.io())//把工作线程指定为了IO线程
                        .observeOn(AndroidSchedulers.mainThread())//把回调线程指定为了UI线程
                        .subscribe(new Action1<List<CourseEntity>>() {
                            @Override
                            public void call(List<CourseEntity> tempLifeList) {
                                dismissDialog();


//                                readNfc.setVisibility(View.GONE);
                                menuFingerPrint.setVisibility(View.GONE);

                                listEntity = tempLifeList;
                                outLineRecycler(listEntity);
                                loadAllEmployees(listEntity);

                                //事件监听
                                initListener();
                                //添加头部
                                addHeadView();
                            }
                        });
            }
        }).start();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK ){
            showAlertHorizontal(CMEActivity.this,"退出学分打卡APP","是否退出打卡学分APP?");
        }
        return false;
    }

}
