package org.bistu.garbageclassification.activities;

import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.bistu.garbageclassification.R;
import org.bistu.garbageclassification.entities.Garbage;
import org.bistu.garbageclassification.entities.GarbageType;
import org.bistu.garbageclassification.components.FlowLayout;
import org.bistu.garbageclassification.utils.ConvertUtil;
import org.bistu.garbageclassification.utils.HttpUtil;
import org.bistu.garbageclassification.utils.SQLiteDBHelper;
import org.bistu.garbageclassification.utils.SQLiteDBOperation;
import org.bistu.garbageclassification.utils.ServerUrl;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ResultActivity extends AppCompatActivity {

    private Intent intent;
    private SQLiteDBOperation sqLiteDBOperation;

    private ImageView imageView;
    private TextView title;
    private TextView type;
    private TextView type_describe;
    private TextView garbage_tip;
    private LinearLayout typeArea;
    private TextView noresult;
    private TextView tips;
    private FlowLayout similar;
    private TextView no_similar;

    private String sign;
    private String city;
    private String gname;
    private String base64;
    private String access_token;

    private Garbage garbage = new Garbage();
    private GarbageType garbageType = new GarbageType();
    private List<String> others = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();

        intent = getIntent();
        sign = intent.getStringExtra("sign");
        city = intent.getStringExtra("city");

        if(sign.equals("text")) {
            gname = intent.getStringExtra("name");
            getGarbageDetail(gname);
            getSimilar(gname);
        } else if(sign.equals("img")) {
            base64 = intent.getStringExtra("base64");
            access_token = intent.getStringExtra("access_token");
            uploadImage();
        }
    }

    //上传图片，返回识别结果
    public void uploadImage() {
        String url = "https://aip.baidubce.com/rest/2.0/image-classify/v2/advanced_general?access_token=" + access_token;
        new HttpUtil().getImgResult(url, base64, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseData = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONArray jsonArray = new JSONObject(responseData).getJSONArray("result");
                            String keyword = jsonArray.getJSONObject(0).getString("keyword");
                            getGarbageDetail(keyword);

                            //较低置信度的，若个数>=6，除第一个外，取五个
                            int num = jsonArray.length()-1;
                            if(num > 5) num = 5;
                            for(int i = 1; i <= num; i++){
                                others.add(jsonArray.getJSONObject(i).getString("keyword"));
                            }

                            getSimilar(keyword);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    //初始化组件/数据库
    private void init() {
        imageView = findViewById(R.id.image);
        title = findViewById(R.id.result_title);
        type = findViewById(R.id.result_type);
        type_describe = findViewById(R.id.describe);
        garbage_tip = findViewById(R.id.garbage_tip);
        typeArea = findViewById(R.id.result_type_area);
        noresult = findViewById(R.id.noresult);
        tips = findViewById(R.id.tip_list);
        similar = findViewById(R.id.similar);
        no_similar = findViewById(R.id.no_similar);

        sqLiteDBOperation = new SQLiteDBOperation(this, SQLiteDBHelper.TABLE_NAME_SH);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //展示页面内容
    private void show() {
        if(sign.equals("img")) {  //图片
            sqLiteDBOperation.insertRecord(garbage.getName());
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageBitmap(ConvertUtil.base64ToBitmap(base64));
            getSupportActionBar().setTitle("识别结果：" + garbage.getName());
        } else { //文字方式
            getSupportActionBar().setTitle(garbage.getName());
        }

        //有分类结果
        if(garbageType.getName() != null) {
            title.setText(garbage.getName() + "\n属于 " + garbage.getType_name());
            type.setText(garbageType.getName());
            type_describe.setText(garbageType.getDescrib());

            //该垃圾的特别提示/注意事项
            if(garbage.getTips().isEmpty())
                garbage_tip.setText(garbage_tip.getText() + "（无）");
            else
                garbage_tip.setText(garbage_tip.getText() + garbage.getTips());

            //类别投放说明
            tips.setText("");
            for(String i : garbageType.getTips()) {
                tips.append("▶" + i +"\n");
            }

            //背景色
            switch (garbageType.getType_id()) {
                case 1:
                    typeArea.setBackground(getDrawable(R.drawable.shape_khs));
                    break;
                case 2:
                    typeArea.setBackground(getDrawable(R.drawable.shape_yh));
                    break;
                case 3:
                    typeArea.setBackground(getDrawable(R.drawable.shape_cy));
                    break;
                case 4:
                    typeArea.setBackground(getDrawable(R.drawable.shape_qt));
                    break;
            }
        } else { //无结果
            title.setText(garbage.getName());
            noresult.setVisibility(View.VISIBLE);
            typeArea.setVisibility(View.GONE);
            garbage_tip.setVisibility(View.GONE);
        }
    }

    //获取类似垃圾名称
    private void getSimilar(String garbage_name) {
        new HttpUtil().getSimilarGarbage(ServerUrl.GET_SIMILAR_GARBAGE, garbage_name, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //异常处理
                Looper.prepare();
                Toast.makeText(ResultActivity.this, R.string.error_server, Toast.LENGTH_SHORT)
                        .show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String responseData = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(responseData);
                            JSONArray data = jsonObject.getJSONArray("data");

                            for(int i = 0; i < data.length(); i++) {
                                others.add(data.getString(i));
                            }

                            if(!others.isEmpty()) {
                                loadFlowList(similar, others);
                            } else {
                                similar.setVisibility(View.GONE);
                                no_similar.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    //加载类似垃圾
    private void loadFlowList(FlowLayout flowLayout, List<String> list) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10, 5, 10, 5);
        if (flowLayout != null) {
            flowLayout.removeAllViews();
        }
        for (int i = 0; i < list.size(); i++) {
            final TextView tv = new TextView(this);
            tv.setPadding(28, 10, 28, 10);
            tv.setText(list.get(i));
            tv.setMaxEms(10);  //最多字符个数
            tv.setTextSize(16);  //字号
            tv.setSingleLine();  //单行
            tv.setBackgroundResource(R.drawable.shape_flow);
            tv.setLayoutParams(layoutParams);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toResultPage(tv.getText().toString());
                }
            });
            flowLayout.addView(tv);
        }
    }

    //根据当前城市和该垃圾的类别  获取类别详情
    private void getTypeInfo(String city) {
        new HttpUtil().getTypeInfo(ServerUrl.TYPE_DETAIL, city, garbage.getType_id(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //异常处理
                Looper.prepare();
                Toast.makeText(ResultActivity.this, R.string.error_server, Toast.LENGTH_SHORT)
                        .show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String responseData = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(responseData);
                            JSONArray data = jsonObject.getJSONArray("data");
                            for(int i = 0; i < data.length(); i++) {
                                JSONObject agtype = data.getJSONObject(i);
                                garbageType.setCity(agtype.getString("city"));
                                garbageType.setSign_white(ServerUrl.URL+agtype.getString("sign_white"));
                                garbageType.setType_id(agtype.getInt("type_id"));
                                garbageType.setName(agtype.getString("name"));
                                garbageType.setDescrib(agtype.getString("describ"));

                                JSONArray tips = agtype.getJSONArray("tips");
                                List<String> tiplist = new ArrayList<>();
                                for(int j = 0; j < tips.length(); j++) {
                                    tiplist.add(tips.getString(j));
                                }
                                garbageType.setTips(tiplist);
                                garbage.setType_name(garbageType.getName());
                            }
                            show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    //根据垃圾名称获取垃圾详情
    private void getGarbageDetail(final String garbage_name) {
        new HttpUtil().getGarbageInfoByName(ServerUrl.EXACT_QUERY, garbage_name, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //异常处理
                Looper.prepare();
                Toast.makeText(ResultActivity.this, R.string.error_server, Toast.LENGTH_SHORT)
                        .show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String responseData = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(responseData);
                            JSONArray data = jsonObject.getJSONArray("data");
                            if(!data.isNull(0)) {
                                JSONObject agarbage = data.getJSONObject(0);
                                garbage.setName(agarbage.getString("name"));
                                garbage.setGarbage_id(agarbage.getInt("garbage_id"));
                                garbage.setType_id(agarbage.getInt("type_id"));
                                garbage.setTips(agarbage.getString("tips"));
                                getTypeInfo(city);
                            } else {
                                garbage.setName(garbage_name);
                                show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

    }

    //点击类似名称跳转
    public void toResultPage(String gname) {
        //写入sql
        sqLiteDBOperation.insertRecord(gname);

        intent = new Intent(ResultActivity.this, ResultActivity.class);
        intent.putExtra("sign", "text");
        intent.putExtra("name", gname);
        intent.putExtra("city", city);
        startActivity(intent);
    }

}
