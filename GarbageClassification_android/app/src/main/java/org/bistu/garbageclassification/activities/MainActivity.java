package org.bistu.garbageclassification.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.bistu.garbageclassification.R;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Intent intent;
    private SharedPreferences city;
    private SharedPreferences.Editor editor;

    private LinearLayout toolbar_city;
    private TextView toolbar_city_text;
    private TextView toolbar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //原生toolbar置空
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_test, R.id.navigation_guide)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        toolbar_city = findViewById(R.id.toolbar_city);
        toolbar_city_text = findViewById(R.id.toolbar_city_text);
        toolbar_title = findViewById(R.id.toolbar_title);

        toolbar_city.setOnClickListener(this);
        toolbar_city_text.setOnClickListener(this);

        //设置当前城市
        setCityText();
    }

    @Override
    protected void onStart() {
        super.onStart();
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

    @Override
    protected void onResume() {
        super.onResume();
        setCityText();
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
    }

    /**
     *  设置页面左上角的城市标签
     *  获取SharedPreferences中的city_name,并更新toolbar_city_text
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
                intent = new Intent(MainActivity.this, CityActivity.class);
                startActivityForResult(intent, 200);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 200 && requestCode == 200) {
            //更换当前城市
            toolbar_city_text.setText(data.getStringExtra("city_name"));
            updateCityText();
            //setCityText();
        }
    }

    /**
     *      返回键的处理:按两次退出程序
     */
    private long exitTime;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            //两次点击的时间差，大于2s提示一次
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(this, R.string.tip_exit, Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                //小于2s退出
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
