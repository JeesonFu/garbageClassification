package org.bistu.garbageclassification.ui.guide;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import org.bistu.garbageclassification.activities.GuideActivity;
import org.bistu.garbageclassification.activities.PolicyActivity;
import org.bistu.garbageclassification.R;
import org.bistu.garbageclassification.adapter.GuideAdapter;
import org.bistu.garbageclassification.components.NoScrollListView;
import org.bistu.garbageclassification.entities.Guide;
import org.bistu.garbageclassification.entities.Policy;
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

public class GuideFragment extends Fragment{

    private View view;
    private Intent intent;
    private SharedPreferences city;

    private TextView top;
    private Policy policy;

    private NoScrollListView listView;
    private List<Guide> guideList = new ArrayList<>();
    private Guide guide;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_guide, container, false);

        top = view.findViewById(R.id.shljgltl);
        listView = view.findViewById(R.id.guide_list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), GuideActivity.class);
                intent.putExtra("guide_id", guideList.get(position).getGuide_id());
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        city = getActivity().getSharedPreferences("current_city", Context.MODE_PRIVATE);

        TextView tooltitle = getActivity().findViewById(R.id.toolbar_title);
        tooltitle.setText(R.string.guide);

        //获取条例和专题列表
        getTop();
        getGuideList();
    }

    //获取专题指南列表
    private void getGuideList() {
        guideList.clear();
        new HttpUtil().getMethod(ServerUrl.GET_GUIDE_LIST,  new Callback() {
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
                            JSONArray responseMsg = jsonObject.getJSONArray("data");

                            for(int i = 0; i < responseMsg.length(); i++) {
                                JSONObject aguide = responseMsg.getJSONObject(i);
                                guide = new Guide();
                                guide.setGuide_id(aguide.getInt("guide_id"));
                                guide.setTitle(aguide.getString("title"));
                                guideList.add(guide);
                            }

                            GuideAdapter adapter = new GuideAdapter(getActivity(), R.layout.item_guide, guideList);
                            listView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    /**
     * 请求获取条例，并显示和设置监听
     */
    private void getTop() {

        new HttpUtil().getPolicy(ServerUrl.GET_POLICY, city.getString("city_name","北京市"), new Callback() {
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
                            policy = new Policy();
                            policy.setPolicy_id(jsonObject.getString("policy_id"));
                            policy.setTitle(jsonObject.getString("title"));
                            policy.setCity(jsonObject.getString("city"));
                            policy.setContent(ServerUrl.URL+jsonObject.getString("content"));

                            top.setText(policy.getTitle());
                            top.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    intent = new Intent(getActivity(), PolicyActivity.class);
                                    intent.putExtra("title", policy.getTitle());
                                    intent.putExtra("path", policy.getContent());
                                    startActivity(intent);
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

}
