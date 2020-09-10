package org.bistu.garbageclassification.activities;

import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.bistu.garbageclassification.R;
import org.bistu.garbageclassification.adapter.CityAdapter;
import org.bistu.garbageclassification.entities.City;
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

public class CityActivity extends AppCompatActivity {

    private ListView listView;

    private List<City> city_list = new ArrayList<>();
    private City city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = findViewById(R.id.city_list);

        //获取城市列表
        getCityList();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("city_name",city_list.get(position).getCity_name());
                setResult(200,intent);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home://左上角返回键处理
                this.finish();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //获取并显示城市列表
    private void getCityList() {
        new HttpUtil().getMethod(ServerUrl.GET_CITY_LIST, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //异常处理
                Looper.prepare();
                Toast.makeText(CityActivity.this, R.string.error_server, Toast.LENGTH_SHORT)
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
                            JSONArray responseMsg = jsonObject.getJSONArray("data");

                            for(int i = 0; i < responseMsg.length(); i++) {
                                JSONObject acity = responseMsg.getJSONObject(i);
                                city = new City();
                                city.setCity_name(acity.getString("city_name"));
                                city_list.add(city);
                            }

                            CityAdapter adapter = new CityAdapter(CityActivity.this, R.layout.item_city, city_list);
                            listView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

}
