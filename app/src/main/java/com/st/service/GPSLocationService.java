package com.st.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.st.db.entity.GpsData;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017-11-24.
 * GPS服务
 */

public class GPSLocationService extends Service {

    public LocationClient mLocationClient = null;
    public BDAbstractLocationListener myListener = new MyLocationListener();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public GPSLocationService() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("logInfo" , "onCreate");

        initLocation();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("logInfo" , "开启定位");
        //声明LocationClient类
        mLocationClient.start();
        mLocationClient.requestLocation();
        return START_STICKY ;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    public class MyLocationListener extends BDAbstractLocationListener{
        @Override
        public void onLocDiagnosticMessage(int i, int i1, String s) {
            Log.e("logInfo","onLocDiagnosticMessage") ;
            super.onLocDiagnosticMessage(i, i1, s);
        }

        @Override
        public void onReceiveLocation(BDLocation location) {
            //获取定位结果
            location.getTime();          //获取定位时间
            location.getLocationID();    //获取定位唯一ID，v7.2版本新增，用于排查定位问题
            location.getLocType();       //获取定位类型
            location.getLatitude();      //获取纬度信息
            location.getLongitude();     //获取经度信息

//            Log.e("logInfo" , "纬度 = " + location.getLatitude() );
//            Log.e("logInfo" , "经度 = " + location.getLongitude() );
            BigDecimal db = new BigDecimal(location.getAltitude() + "");
//            Log.e("logInfo" , "高程 = " + db.toPlainString() );
//            Log.e("logInfo" , "定位精准度 = " + location.getRadius() );

            location.getRadius();        //获取定位精准度
            location.getAddrStr();       //获取地址信息
            location.getCountry();       //获取国家信息
            location.getCountryCode();   //获取国家码
            location.getCity();          //获取城市信息
            location.getCityCode();      //获取城市码
            location.getDistrict();      //获取区县信息
            location.getStreet();        //获取街道信息
            location.getStreetNumber();  //获取街道码
            location.getLocationDescribe();    //获取当前位置描述信息
            location.getPoiList();       //获取当前位置周边POI信息
            location.getBuildingID();    //室内精准定位下，获取楼宇ID
            location.getBuildingName();  //室内精准定位下，获取楼宇名称
            location.getFloor();         //室内精准定位下，获取当前位置所处的楼层信息

//            Log.e("logInfo","获取城市信息---->"+location.getCity());
//            Log.e("logInfo","获取城市码---->"+location.getCityCode());
//            Log.e("logInfo","获取区县信息---->"+location.getDistrict());
//            Log.e("logInfo","获取街道信息---->"+location.getStreet());
//            Log.e("logInfo","获取当前位置描述信息---->"+location.getLocationDescribe());

            GpsData gpsData = new GpsData() ;
            String position = String.valueOf(location.getLatitude()+"/"+location.getLongitude());
            String placeName = location.getCity()+"/"+location.getDistrict()+"/"+location.getStreet()
                    +"/"+location.getLocationDescribe();
            gpsData.setPosition(position);
            gpsData.setPlaceName(placeName);
            sendGpsBase(gpsData) ;

            if (location.getLocType() == BDLocation.TypeGpsLocation){
                //当前为GPS定位结果，可获取以下信息
                location.getSpeed();                //获取当前速度，单位：公里每小时
                location.getSatelliteNumber();      //获取当前卫星数
                location.getAltitude();             //获取海拔高度信息，单位米
                location.getDirection();            //获取方向信息，单位度
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
                //当前为网络定位结果，可获取以下信息
                location.getOperators();    //获取运营商信息
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {
                //当前为网络定位结果
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                //当前网络定位失败
                //可将定位唯一ID、IMEI、定位失败时间反馈至loc-bugs@baidu.com
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                //当前网络不通
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                //当前缺少定位依据，可能是用户没有授权，建议弹出提示框让用户开启权限
                //可进一步参考onLocDiagnosticMessage中的错误返回码
            }

        }
    }

    private void initLocation(){

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系
        int span=3000;
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的

        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要

        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps

        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果

        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
//        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener( myListener );
    }

    private void sendGpsBase( GpsData gpsData){
        Intent intent = new Intent() ;
        Bundle bundle = new Bundle() ;

        bundle.putParcelable("gpsinfo" , gpsData);
        intent.putExtras(bundle) ;
        intent.setAction("gpsService");
        sendBroadcast(intent);
    }


}
