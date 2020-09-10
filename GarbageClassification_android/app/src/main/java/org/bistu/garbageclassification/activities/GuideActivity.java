package org.bistu.garbageclassification.activities;

import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.bistu.garbageclassification.R;
import org.bistu.garbageclassification.entities.Guide;
import org.bistu.garbageclassification.utils.HttpUtil;
import org.bistu.garbageclassification.utils.ServerUrl;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class GuideActivity extends AppCompatActivity {

    private Intent intent;

    private TextView title;
    private TextView content;

    private Guide guide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title = findViewById(R.id.guide_title);
        content = findViewById(R.id.guide_content);

        intent = getIntent();

        getContent(intent.getIntExtra("guide_id",0));
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

    //显示详细信息
    private void show() {
        getSupportActionBar().setTitle(guide.getTitle());

        title.setText(guide.getTitle());
        content.setText(guide.getContent());
    }

    //获取专题详细信息
    private void getContent(Integer guide_id) {
        new HttpUtil().getGuideInfo(ServerUrl.GUIDE_DETAIL, guide_id, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //异常处理
                Looper.prepare();
                Toast.makeText(GuideActivity.this, R.string.error_server, Toast.LENGTH_SHORT)
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
                            guide = new Guide();
                            guide.setGuide_id(jsonObject.getInt("guide_id"));
                            guide.setTitle(jsonObject.getString("title"));
                            guide.setContent(jsonObject.getString("content"));

                            show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}
