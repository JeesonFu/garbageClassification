package org.bistu.garbageclassification.utils;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 *  http请求，异步请求enqueue
 */
public class HttpUtil {

    private OkHttpClient client;
    private RequestBody body;
    private Request request;

    public HttpUtil() {
        this.client = new OkHttpClient().newBuilder()
                .connectTimeout(5000, TimeUnit.MILLISECONDS)
                .readTimeout(5000, TimeUnit.MILLISECONDS)
                .build();
    }

    /**
     *   根据垃圾名称，获取该垃圾的详细信息（精确查询）
     */
    public void getGarbageInfoByName(String address, String garbage, Callback callback) {
        body = new FormBody.Builder()
                .add("name", garbage)
                .build();
        request = new Request.Builder()
                .url(address)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    /**
     * 根据垃圾名称，获取类似名称的垃圾（类似垃圾列表）
     */
    public void getSimilarGarbage(String address, String garbage, Callback callback) {
        body = new FormBody.Builder()
                .add("name", garbage)
                .build();
        request = new Request.Builder()
                .url(address)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    /**
     * GET请求
     */
    public void getMethod(String address, Callback callback) {
        request = new Request.Builder()
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }

    /**
     *  获取当前城市的4类垃圾图标（首页）
     */
    public void getTypesPre(String address, String city, Callback callback) {
        body = new FormBody.Builder()
                .add("city", city)
                .build();
        request = new Request.Builder()
                .url(address)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    /**
     *  获取该类别详细信息（类别页/结果页
     */
    public void getTypeInfo(String address, String city, Integer type_id, Callback callback) {
        body = new FormBody.Builder()
                .add("type_id", type_id.toString())
                .add("city",city)
                .build();
        request = new Request.Builder()
                .url(address)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    /**
     *  获取该类别的垃圾列表10个（类别页）
     */
    public void getGListByType(String address, Integer type_id, Callback callback) {
        body = new FormBody.Builder()
                .add("type_id", type_id.toString())
                .build();
        request = new Request.Builder()
                .url(address)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    /**
     *  获取专题指南 详细信息（专题详情页）
     */
    public void getGuideInfo(String address, Integer guide_id, Callback callback) {
        body = new FormBody.Builder()
                .add("guide_id", guide_id.toString())
                .build();
        request = new Request.Builder()
                .url(address)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    /**
     *  获取生活垃圾管理条例
     */
    public void getPolicy(String address, String city, Callback callback) {
        body = new FormBody.Builder()
                .add("city", city)
                .build();
        request = new Request.Builder()
                .url(address)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    /**
     *      记录作答情况
     */
    public void recordAnsTimes(String address, Integer ques_id, String is_true, Callback callback) {
        body = new FormBody.Builder()
                .add("ques_id", String.valueOf(ques_id))
                .add("is_true", is_true)
                .build();
        request = new Request.Builder()
                .url(address)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    /**
     *  图像识别
     */
    public void getImgResult(String address, String imgBase64, Callback callback) {
        body = new FormBody.Builder()
                .add("image",imgBase64)
                .build();
        request = new Request.Builder()
                .url(address)
                .addHeader("Content-Type","application/x-www-form-urlencode")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
