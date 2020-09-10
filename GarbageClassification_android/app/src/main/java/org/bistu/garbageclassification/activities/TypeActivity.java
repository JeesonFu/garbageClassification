package org.bistu.garbageclassification.activities;

import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.bistu.garbageclassification.R;
import org.bistu.garbageclassification.adapter.GarbageAdapter;
import org.bistu.garbageclassification.components.NoScrollListView;
import org.bistu.garbageclassification.entities.Garbage;
import org.bistu.garbageclassification.entities.GarbageType;
import org.bistu.garbageclassification.utils.HttpUtil;
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

public class TypeActivity extends AppCompatActivity {

    private Intent intent;

    private LinearLayout typeArea;
    private TextView type_name;
    private TextView describe;
    private TextView tips;
    private NoScrollListView listView;

    private List<Garbage> garbagelist = new ArrayList<>();
    private Garbage garbage;
    private GarbageType garbageType = new GarbageType();
    private String city;
    private Integer type_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        intent = getIntent();
        type_id = intent.getIntExtra("type_id",0);
        city = intent.getStringExtra("city");

        getSupportActionBar().setTitle(intent.getStringExtra("type_name"));

        typeArea = findViewById(R.id.type_area);
        type_name = findViewById(R.id.type_name);
        describe = findViewById(R.id.describe);
        tips = findViewById(R.id.tip_list);
        listView = findViewById(R.id.garbage_list);

        //获取类别信息和垃圾列表
        getTypeInfo(city,type_id);
        getGarbageList(type_id);
    }

    //属于该类垃圾的列表10个
    private void getGarbageList(Integer type_id) {
        new HttpUtil().getGListByType(ServerUrl.GET_GLIST_BY_TYPE, type_id, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //异常处理
                Looper.prepare();
                Toast.makeText(TypeActivity.this, R.string.error_server, Toast.LENGTH_SHORT)
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
                                JSONObject agarbage = data.getJSONObject(i);
                                garbage = new Garbage();
                                garbage.setName(agarbage.getString("name"));
                                //garbage.setGarbage_id(Integer.valueOf(agarbage.getString("garbage_id")));
                                //garbage.setType_id(Integer.valueOf(agarbage.getString("type_id")));
                                garbagelist.add(garbage);
                            }

                            GarbageAdapter adapter = new GarbageAdapter(TypeActivity.this, R.layout.item_garbage, garbagelist);
                            listView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    //获取类别详细信息
    private void getTypeInfo(String city, Integer type_id) {
        new HttpUtil().getTypeInfo(ServerUrl.TYPE_DETAIL, city, type_id, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //异常处理
                Looper.prepare();
                Toast.makeText(TypeActivity.this, R.string.error_server, Toast.LENGTH_SHORT)
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
                                JSONObject agarbage = data.getJSONObject(i);
                                garbageType.setCity(agarbage.getString("city"));
                                garbageType.setSign_white(ServerUrl.URL+agarbage.getString("sign_white"));
                                garbageType.setType_id(agarbage.getInt("type_id"));
                                garbageType.setName(agarbage.getString("name"));
                                garbageType.setDescrib(agarbage.getString("describ"));

                                JSONArray tips = agarbage.getJSONArray("tips");
                                List<String> tiplist = new ArrayList<>();
                                for(int j = 0; j < tips.length(); j++) {
                                    tiplist.add(tips.getString(j));
                                }
                                garbageType.setTips(tiplist);
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

    //显示类别信息
    private void show() {
        if(type_id != null && type_id != 0) {
            type_name.setText(garbageType.getName());
            describe.setText(garbageType.getDescrib());
            tips.setText("");
            for(String i : garbageType.getTips()) {
                tips.append("▶" + i +"\n");
            }

            //背景色
            switch (type_id) {
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
        }
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

}
