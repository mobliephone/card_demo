package com.framework.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class J {

    public static String Msg = "MSG" ;
    public static final String Message = "Message" ;
    public static final String Content = "Content" ;
    public static final String CODE = "code" ;
    /**
     * 数据请求成功
     */
    public static final String SUCCESS = "10000";
    /**
     * 数据请求失败
     */
    public static final String FAILD = "10001";

    /**
     * 把服务器返回的数据，转换成一个实体类
     * @param str 服务器返回的数据
     * @param clazz 实体类名
     * @param <T>
     * @return
     */
    public static <T> T getEntity(String str, Class<T> clazz){
        Gson gson=new Gson();
//        java.lang.reflect.Type type = new TypeToken<T>() {}.getType();
        return gson.fromJson(str,clazz);
    }

    public static <T> T getEntity1(String str, Class<T> clazz){
        Gson gson=new Gson();
        java.lang.reflect.Type type = new TypeToken<T>(){}.getType();
        return gson.fromJson(str,type);
    }

    /**
     * 把服务器返回的数据，转换成一个list
     * @param str 服务器返回的数据
     * @param clazz 实体类名
     * @param <T>
     * @return
     */
    public static <T> List<T> getListEntity(String str, Class<T> clazz){
        List<T> lst =  new ArrayList<T>();

        JsonArray array = new JsonParser().parse(str).getAsJsonArray();

        Gson gson = null;
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Date.class, new DateAdapter());
        builder.setDateFormat("yyyy-MM-dd HH:mm");
        gson = builder.create();
        for(final JsonElement elem : array){
            lst.add(gson.fromJson(elem, clazz));
        }
        return lst;
    }

    public static String getRows(String string){
        try {
            JSONObject jsonObject=new JSONObject(string);
            JSONArray jsonArray=jsonObject.getJSONArray("rows");
            return jsonArray.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static int getCurrentPage(String string){
        try {
            JSONObject jsonObject=new JSONObject(string);
            return jsonObject.getInt("currentPage");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static boolean isResTypeSuccess(String string){
        try {
            JSONObject jsonObject=new JSONObject(string);
           if(jsonObject.getString("res_type").equals("success")){
               return true;
           }else{
               return false;
           }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean getMsg( String str ){
        try {
            JSONObject jsonObject=new JSONObject(str);
            String b = jsonObject.getString(J.Msg) ;
            return b.equals("true");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false ;
    }


    public static String getValue(String str , String key){
        try {
            JSONObject jsonObject=new JSONObject(str);
            return jsonObject.getString(key);
        } catch (JSONException e) {
            return "" ;
        }
    }

    /**
     * 解析jsonArray
     * @param json
     * @param arrTAG
     * @return
     */
    public static String getJsonArray(String json , String arrTAG){
        try {
            JSONObject jsonObject=new JSONObject(json);
            JSONArray jsonArray=jsonObject.getJSONArray(arrTAG);
            return jsonArray.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 对象转换为json
     * @param object
     * @return
     */
    public static String object2json(Object object){
        Gson gson =  new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create();
        return  gson.toJson(object) ;
    }

}
