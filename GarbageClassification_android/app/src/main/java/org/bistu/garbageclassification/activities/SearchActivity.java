package org.bistu.garbageclassification.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.bistu.garbageclassification.R;
import org.bistu.garbageclassification.entities.Garbage;
import org.bistu.garbageclassification.components.FlowLayout;
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

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private Intent intent;
    private SharedPreferences city;
    private SharedPreferences.Editor editor;
    private SQLiteDBOperation dbOperate;

    private LinearLayout toolbar_city;
    private TextView toolbar_city_text;
    private TextView cancel;
    private SearchView searchView;

    private TextView[] tops;
    private TextView top1;
    private TextView top2;
    private TextView top3;
    private TextView top4;
    private TextView top5;
    private TextView top6;
    private TextView top7;
    private TextView top8;
    private TextView top9;
    private TextView top10;

    private TextView clear;
    private FlowLayout search_history;
    private List<String> history_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        toolbar_city = findViewById(R.id.toolbar_city);
        toolbar_city_text = findViewById(R.id.toolbar_city_text);
        cancel = findViewById(R.id.cancel);
        searchView = findViewById(R.id.searchBar_input);
        clear = findViewById(R.id.clear);

        toolbar_city.setOnClickListener(this);
        toolbar_city_text.setOnClickListener(this);
        cancel.setOnClickListener(this);
        clear.setOnClickListener(this);

        dbOperate = new SQLiteDBOperation(this, SQLiteDBHelper.TABLE_NAME_SH);

        //设置城市标签
        setCityText();
        //获取热门榜单
        getTopsItem();
        //最近搜索列表
        getRecentList();
    }

    @Override
    protected void onStart() {
        super.onStart();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) { //确认，搜索
                toResultPage(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //实时监听变化
                return false;
            }
        });
    }

    //获取最近搜索历史
    private void getRecentList() {
        search_history = findViewById(R.id.search_history);
        history_list = new ArrayList<>();
        history_list = dbOperate.getTop10();

        loadFlowList(search_history, history_list);
    }

    //加载最近搜索历史，并设置监听
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
            tv.setMaxEms(10);
            tv.setTextSize(16);
            tv.setSingleLine();
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

    //获取热门搜索榜单
    private void getTopsItem() {
        top1 = findViewById(R.id.tops_1);
        top2 = findViewById(R.id.tops_2);
        top3 = findViewById(R.id.tops_3);
        top4 = findViewById(R.id.tops_4);
        top5 = findViewById(R.id.tops_5);
        top6 = findViewById(R.id.tops_6);
        top7 = findViewById(R.id.tops_7);
        top8 = findViewById(R.id.tops_8);
        top9 = findViewById(R.id.tops_9);
        top10 = findViewById(R.id.tops_10);
        tops = new TextView[]{top1,top2,top3,top4,top5,top6,top7,top8,top9,top10};

        new HttpUtil().getMethod(ServerUrl.GET_TOPS_GARBAGE_LIST, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //异常处理
                Looper.prepare();
                Toast.makeText(SearchActivity.this, R.string.error_server, Toast.LENGTH_SHORT)
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
                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            for(int i = 0; i < tops.length; i++) {
                                JSONObject agarbage = jsonArray.getJSONObject(i);
                                final Garbage garbage = new Garbage();
                                garbage.setName(agarbage.getString("name"));

                                tops[i].setText(garbage.getName());
                                tops[i].setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        toResultPage(garbage.getName());
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    /**
     * 更新SharedPreferences的city_name
     */
    private void updateCityText() {
        city = this.getSharedPreferences("current_city", Context.MODE_PRIVATE);
        editor = city.edit();
        editor.putString("city_name", toolbar_city_text.getText().toString());
        editor.commit();
    }

    /**
     *  获取SharedPreferences中的city_name,更新城市标签
     */
    private void setCityText() {
        city = this.getSharedPreferences("current_city", Context.MODE_PRIVATE);
        toolbar_city_text.setText(city.getString("city_name","北京市"));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_city:
            case R.id.toolbar_city_text:
                intent = new Intent(SearchActivity.this, CityActivity.class);
                startActivityForResult(intent, 200);
                break;
            case R.id.cancel:
                finish();
                break;
            case R.id.clear:
                history_list.removeAll(history_list);
                //清除搜索历史
                dbOperate.deleteAllRecord();
                loadFlowList(search_history, history_list);
                Toast.makeText(this, "清除成功", Toast.LENGTH_SHORT).show();
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 200 && requestCode == 200) {
            //选择城市后返回，更新city_name
            toolbar_city_text.setText(data.getStringExtra("city_name"));
            updateCityText();
            //setCityText();
        }

        if(requestCode==1) { //返回搜索页，及时更新榜单和历史
            getTopsItem();
            getRecentList();
        }
    }

    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            intent.putExtra("city_name",toolbar_city_text.getText());
            setResult(200,intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }*/

    /*
        搜索记录写入数据库->跳转到结果页
        请求码1，返回，并重新加载榜单和最近搜索
     */
    public void toResultPage(String gname) {
        //写入sqlite
        dbOperate.insertRecord(gname);

        intent = new Intent(SearchActivity.this, ResultActivity.class);
        intent.putExtra("sign","text");
        intent.putExtra("name", gname);
        intent.putExtra("city", toolbar_city_text.getText());
        startActivityForResult(intent,1);
    }

}
