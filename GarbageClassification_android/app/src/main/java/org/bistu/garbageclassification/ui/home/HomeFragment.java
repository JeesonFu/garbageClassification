package org.bistu.garbageclassification.ui.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import org.bistu.garbageclassification.R;
import org.bistu.garbageclassification.activities.ResultActivity;
import org.bistu.garbageclassification.activities.SearchActivity;
import org.bistu.garbageclassification.activities.TypeActivity;
import org.bistu.garbageclassification.entities.GarbageType;
import org.bistu.garbageclassification.utils.ConvertUtil;
import org.bistu.garbageclassification.utils.HttpUtil;
import org.bistu.garbageclassification.utils.GlideImageLoader;
import org.bistu.garbageclassification.utils.ServerUrl;
import org.bistu.garbageclassification.utils.Token;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private final int CAMERA = 1;
    private View view;
    private SharedPreferences city;
    private Intent intent;

    private String city_name;

    private Banner banner;
    private List<Integer> imageBar;

    private FrameLayout searchBar;
    private Button btn_pic;
    private String imgstr_base64;
    private String access_token;

    private ImageView pic_khs;
    private ImageView pic_yh;
    private ImageView pic_cy;
    private ImageView pic_qt;
    private TextView type_khs;
    private TextView type_yh;
    private TextView type_cy;
    private TextView type_qt;
    private List<GarbageType> gTypeList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        //初始化轮播图和页面控件
        initBanner();
        init();

        return view;
    }

    private void init() {
        searchBar = view.findViewById(R.id.searchBar);
        btn_pic = view.findViewById(R.id.search_pic);
        pic_khs = view.findViewById(R.id.home_pic_khs);
        pic_yh = view.findViewById(R.id.home_pic_yh);
        pic_cy = view.findViewById(R.id.home_pic_cy);
        pic_qt = view.findViewById(R.id.home_pic_qt);
        type_khs = view.findViewById(R.id.home_type_khs);
        type_yh = view.findViewById(R.id.home_type_yh);
        type_cy = view.findViewById(R.id.home_type_cy);
        type_qt = view.findViewById(R.id.home_type_qt);

        searchBar.setOnClickListener(this);
        btn_pic.setOnClickListener(this);
        pic_khs.setOnClickListener(this);
        pic_yh.setOnClickListener(this);
        pic_cy.setOnClickListener(this);
        pic_qt.setOnClickListener(this);
        type_khs.setOnClickListener(this);
        type_yh.setOnClickListener(this);
        type_cy.setOnClickListener(this);
        type_qt.setOnClickListener(this);
    }

    /*
    设置标题
     */
    @Override
    public void onStart() {
        super.onStart();
        TextView tooltitle = getActivity().findViewById(R.id.toolbar_title);
        tooltitle.setText(R.string.home);

        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_PHONE_STATE,Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
    }

    /*
    获取当前城市
     */
    @Override
    public void onResume() {
        super.onResume();
        city = getActivity().getSharedPreferences("current_city", Context.MODE_PRIVATE);
        city_name = city.getString("city_name","北京市");
        getTypes(city_name); //底部4类图标
    }

    /**
     *  根据当前城市获取类别名称、图标
     */
    public void getTypes(String city_name) {
        new HttpUtil().getTypesPre(ServerUrl.GET_TYPES_4, city_name, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //异常处理
                Looper.prepare();
                Toast.makeText(getActivity(), R.string.error_server, Toast.LENGTH_SHORT)
                        .show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String responseData = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(responseData);
                            JSONArray data = jsonObject.getJSONArray("data");
                            for(int i = 0; i < data.length(); i++) {
                                JSONObject jo = data.getJSONObject(i);
                                GarbageType garbageType = new GarbageType();
                                garbageType.setType_id(jo.getInt("type_id"));
                                garbageType.setCity(jo.getString("city"));
                                garbageType.setSign(ServerUrl.URL + jo.getString("sign"));
                                garbageType.setName(jo.getString("name"));
                                gTypeList.add(garbageType);
                            }
                            //填充至页面
                            fillToPage(gTypeList);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    //填充到页面中
    private void fillToPage(List<GarbageType> garbageTypes) {
        for(GarbageType gt : garbageTypes) {
            switch (gt.getType_id()) {
                case 1:
                    type_khs.setText(gt.getName());
                    Glide.with(this).load(gt.getSign()).into(pic_khs);
                    break;
                case 2:
                    type_yh.setText(gt.getName());
                    Glide.with(this).load(gt.getSign()).into(pic_yh);
                    break;
                case 3:
                    type_cy.setText(gt.getName());
                    Glide.with(this).load(gt.getSign()).into(pic_cy);
                    break;
                case 4:
                    type_qt.setText(gt.getName());
                    Glide.with(this).load(gt.getSign()).into(pic_qt);
                    break;
            }
        }
    }

    /**
     *      初始化Banner轮播图
     */
    private void initBanner() {
        banner = view.findViewById(R.id.home_banner);

        imageBar = new ArrayList<>();
        imageBar.add(R.drawable.banner02);
        imageBar.add(R.drawable.banner03);
        imageBar.add(R.drawable.banner04);
        imageBar.add(R.drawable.banner05);

        //设置banner属性
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置轮播图片停留时间
        banner.setDelayTime(3000);
        //设置图片集
        banner.setImages(imageBar);
        //设置指示点的位置
        banner.setIndicatorGravity(BannerConfig.RIGHT);
        //设置自动播放
        banner.isAutoPlay(true);
        banner.start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.searchBar:
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;

                //调用照相机
            case R.id.search_pic:
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA},1);
                if(ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CAMERA);
                    getAuth();
                }
                break;

            case R.id.home_pic_khs:
            case R.id.home_type_khs:
                intent = new Intent(getActivity(), TypeActivity.class);
                intent.putExtra("type_id", 1);
                intent.putExtra("city", city_name);
                intent.putExtra("type_name", type_khs.getText());
                startActivity(intent);
                break;
            case R.id.home_pic_yh:
            case R.id.home_type_yh:
                intent = new Intent(getActivity(), TypeActivity.class);
                intent.putExtra("type_id", 2);
                intent.putExtra("city", city_name);
                intent.putExtra("type_name", type_yh.getText());
                startActivity(intent);
                break;
            case R.id.home_pic_cy:
            case R.id.home_type_cy:
                intent = new Intent(getActivity(), TypeActivity.class);
                intent.putExtra("type_id", 3);
                intent.putExtra("city", city_name);
                intent.putExtra("type_name", type_cy.getText());
                startActivity(intent);
                break;
            case R.id.home_pic_qt:
            case R.id.home_type_qt:
                intent = new Intent(getActivity(), TypeActivity.class);
                intent.putExtra("type_id", 4);
                intent.putExtra("city", city_name);
                intent.putExtra("type_name", type_qt.getText());
                startActivity(intent);
                break;
        }
    }

    //获取图像识别授权Token
    public void getAuth() {
        String tokenUrl= Token.getAuthUrl();
        new HttpUtil().getMethod(tokenUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(getActivity(), "授权失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final String getResponse=response.body().string();
                    if(!TextUtils.isEmpty(getResponse)) {
                        JSONObject tokenObject = new JSONObject(getResponse);
                        access_token = tokenObject.getString("access_token");
                    }
                    Log.i("TAG", "access_token: "+access_token);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){ //系统程序调用成功
            if (data != null && requestCode == CAMERA) {
                Bundle bundle = data.getExtras();
                Bitmap bitmap = (Bitmap) bundle.getParcelable("data");
                imgstr_base64 = ConvertUtil.bitmapToBase64(bitmap);

                intent = new Intent(getActivity(), ResultActivity.class);
                intent.putExtra("sign","img");
                intent.putExtra("access_token", access_token);
                intent.putExtra("city",city_name);
                intent.putExtra("base64",imgstr_base64);
                startActivity(intent);
            } else {
                Toast.makeText(getActivity(), "图片获取失败", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

