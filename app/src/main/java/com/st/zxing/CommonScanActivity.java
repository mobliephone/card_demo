package com.st.zxing;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.framework.base.BaseActivity;
import com.google.zxing.Result;
import com.st.R;
import com.st.activity.project.PunchCardActivity;
import com.st.zxing.decode.DecodeThread;
import com.st.zxing.decode.Utils;
import com.st.zxing.inter.Constant;
import com.st.zxing.listener.ScanListener;
import com.st.zxing.manager.ScanManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017-11-21.
 * 摄像机扫描条形码
 */

public class CommonScanActivity extends BaseActivity implements ScanListener, View.OnClickListener {

    static final String TAG = CommonScanActivity.class.getSimpleName();

    @BindView(R.id.capture_preview)
    SurfaceView scanPreview;
    @BindView(R.id.authorize_return)
    ImageView authorize_return;
    @BindView(R.id.common_title_TV_center)
    TextView title;
    @BindView(R.id.title_bar)
    RelativeLayout titleBar;
    @BindView(R.id.tv_scan_result)
    TextView tv_scan_result;
    @BindView(R.id.top_mask)
    RelativeLayout topMask;
    @BindView(R.id.scan_hint)
    TextView scan_hint;
    @BindView(R.id.iv_light)
    TextView iv_light;
    @BindView(R.id.qrcode_ic_back)
    TextView qrcode_ic_back;
    @BindView(R.id.qrcode_g_gallery)
    TextView qrcode_g_gallery;
    @BindView(R.id.service_register_rescan)
    Button rescan;
    @BindView(R.id.bottom_mask)
    RelativeLayout bottomMask;
    @BindView(R.id.left_mask)
    ImageView leftMask;
    @BindView(R.id.right_mask)
    ImageView rightMask;
    @BindView(R.id.capture_scan_line)
    ImageView scanLine;
    @BindView(R.id.scan_image)
    ImageView scan_image;
    @BindView(R.id.capture_crop_view)
    View scanCropView;
    @BindView(R.id.capture_container)
    View scanContainer;


    private ScanManager scanManager;
    final int PHOTOREQUESTCODE = 1111;
    private int scanMode;//扫描模型（条形，二维码，全部）
    private String result;//扫描结果

    @Override
    public int getLayoutResId() {
        return R.layout.activity_scan_code;
    }

    @Override
    public void onInitView() {
        ButterKnife.bind(this);
        qrcode_g_gallery.setOnClickListener(this);
        qrcode_ic_back.setOnClickListener(this);
        iv_light.setOnClickListener(this);
        rescan.setOnClickListener(this);
        authorize_return.setOnClickListener(this);

        scanMode=getIntent().getIntExtra(Constant.REQUEST_SCAN_MODE,Constant.REQUEST_SCAN_MODE_ALL_MODE);
        setTitle("条形码扫描");
        scan_hint.setText(R.string.scan_barcode_hint);
        //构造出扫描管理器
        scanManager = new ScanManager(CommonScanActivity.this, scanPreview, scanContainer, scanCropView, scanLine, scanMode,this);
    }

    @Override
    public void onResume() {
        super.onResume();
        scanManager.onResume();
        rescan.setVisibility(View.INVISIBLE);
        scan_image.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();
        scanManager.onPause();
    }

    public void scanResult(Result rawResult, Bundle bundle) {
        //扫描成功后，扫描器不会再连续扫描，如需连续扫描，调用reScan()方法。
        //scanManager.reScan();
//		Toast.makeText(that, "result="+rawResult.getText(), Toast.LENGTH_LONG).show();

        if (!scanManager.isScanning()) { //如果当前不是在扫描状态
            //设置再次扫描按钮出现
            rescan.setVisibility(View.VISIBLE);
            scan_image.setVisibility(View.VISIBLE);
            Bitmap barcode = null;
            byte[] compressedBitmap = bundle.getByteArray(DecodeThread.BARCODE_BITMAP);
            if (compressedBitmap != null) {
                barcode = BitmapFactory.decodeByteArray(compressedBitmap, 0, compressedBitmap.length, null);
                barcode = barcode.copy(Bitmap.Config.ARGB_8888, true);
            }
            scan_image.setImageBitmap(barcode);
        }
        rescan.setVisibility(View.VISIBLE);
        scan_image.setVisibility(View.VISIBLE);
        tv_scan_result.setVisibility(View.VISIBLE);
        tv_scan_result.setText("结果："+rawResult.getText());
        result = rawResult.getText();
    }

    @Override
    public void scanError(Exception e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        //相机扫描出错时
        if(e.getMessage()!=null&&e.getMessage().startsWith("相机")){
            scanPreview.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String photo_path;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTOREQUESTCODE:
                    String[] proj = {MediaStore.Images.Media.DATA};
                    Cursor cursor = this.getContentResolver().query(data.getData(), proj, null, null, null);
                    if (cursor.moveToFirst()) {
                        int colum_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        photo_path = cursor.getString(colum_index);
                        if (photo_path == null) {
                            photo_path = Utils.getPath(getApplicationContext(), data.getData());
                        }
                        scanManager.scanningImage(photo_path);
                    }
            }
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.qrcode_g_gallery:
                showPictures(PHOTOREQUESTCODE);
                break;
            case R.id.iv_light:
                scanManager.switchLight();
                break;
            case R.id.qrcode_ic_back:
                finish();
                break;
            case R.id.service_register_rescan://再次开启扫描
//                startScan();
//                writeNfc();
                break;
            case R.id.authorize_return:
                finish();
                break;
            default:
                break;
        }
    }

    private void writeNfc() {
        if (null != result && !"".equals(result)){
            Intent intent = new Intent(this, PunchCardActivity.class);
            //传值医通卡号
            intent.putExtra("userNumber",result);
            startActivity(intent);
            finish();
            Log.d("logInfo", "result--->"+result);
        } else {
            showMessage("当前操作无法进行！");
        }

    }

    void startScan() {
        if (rescan.getVisibility() == View.VISIBLE) {
            rescan.setVisibility(View.INVISIBLE);
            scan_image.setVisibility(View.GONE);
            scanManager.reScan();
        }
    }

    public void showPictures(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, requestCode);
    }
}
