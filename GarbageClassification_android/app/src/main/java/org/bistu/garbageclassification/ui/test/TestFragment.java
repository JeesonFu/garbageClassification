package org.bistu.garbageclassification.ui.test;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import org.bistu.garbageclassification.R;
import org.bistu.garbageclassification.entities.Question;
import org.bistu.garbageclassification.entities.others.Option;
import org.bistu.garbageclassification.utils.HttpUtil;
import org.bistu.garbageclassification.utils.ServerUrl;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class TestFragment extends Fragment implements View.OnClickListener {

    private View view;

    private TextView tv_question;
    private TextView option1;
    private TextView option2;
    private TextView option3;
    private TextView option4;
    private TextView[] tv_option_list;

    private Question question = new Question();
    private Option option;
    private List<Option> optionlist;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_test, container, false);
        tv_question = view.findViewById(R.id.question);
        option1 = view.findViewById(R.id.option_1);
        option2 = view.findViewById(R.id.option_2);
        option3 = view.findViewById(R.id.option_3);
        option4 = view.findViewById(R.id.option_4);
        tv_option_list = new TextView[]{option1,option2,option3,option4};

        loadQuestion();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        TextView tooltitle = getActivity().findViewById(R.id.toolbar_title);
        tooltitle.setText(R.string.test);
    }

    //获取一道题
    private void loadQuestion() {
        new HttpUtil().getMethod(ServerUrl.GET_QUESTION,  new Callback() {
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
                            question.setQues_id(jsonObject.getInt("ques_id"));
                            question.setQuestion(jsonObject.getString("question"));
                            question.setExplain(jsonObject.getString("explain"));

                            JSONArray options = jsonObject.getJSONArray("options");
                            optionlist = new ArrayList<>();
                            for(int i = 0; i < options.length(); i++) {
                                JSONObject aop = options.getJSONObject(i);
                                option = new Option();
                                option.setOid(aop.getInt("oid"));
                                option.setSelect(aop.getString("select"));
                                option.setIs_true(aop.getBoolean("_true"));
                                optionlist.add(option);
                            }

                            Collections.shuffle(optionlist);
                            question.setOptions(optionlist);
                            showQuestion();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    //显示题目信息
    private void showQuestion() {
        tv_question.setText(question.getQuestion());
        for(int i = 0; i < tv_option_list.length; i++) {
            option = optionlist.get(i);
            tv_option_list[i].setText(option.getSelect());
            tv_option_list[i].setTag(option.isIs_true());

            tv_option_list[i].setBackground(ContextCompat.getDrawable(getContext(),R.drawable.shape_btn_test));
            tv_option_list[i].setOnClickListener(this);
        }
    }

    //监听按钮，调用-显示选择结果，记录作答次数
    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.option_1:
                    showResult(option1);
                    recAnsTimes(option1.getTag().toString());
                    break;
                case R.id.option_2:
                    showResult(option2);
                    recAnsTimes(option2.getTag().toString());
                    break;
                case R.id.option_3:
                    showResult(option3);
                    recAnsTimes(option3.getTag().toString());
                    break;
                case R.id.option_4:
                    showResult(option4);
                    recAnsTimes(option4.getTag().toString());
                    break;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //显示选择结果正误
    public void showResult(TextView tv) throws InterruptedException {
        if(Boolean.valueOf(tv.getTag().toString())) {
            tv.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.shape_btn_true));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadQuestion();
                }
            },500);
        } else {
            tv.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.shape_btn_false));
            //弹出提示,点击进入下一题
            AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
            builder.setTitle("回答错误");
            builder.setMessage("答案：" + getTrue() + "\n解析：" + question.getExplain());
            builder.setPositiveButton("下一题", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    loadQuestion();
                }
            });
            builder.show();
        }
    }

    //遍历四个选项，获取正确选项内容
    public String getTrue() {
        for(int i = 0; i < optionlist.size(); i++) {
            option = optionlist.get(i);
            if(option.isIs_true()) {
                return option.getSelect();
            }
        }
        return null;
    }

    //记录作答次数
    public void recAnsTimes(String is_true) {
        new HttpUtil().recordAnsTimes(ServerUrl.RECORD_ANSWER_TIMES, question.getQues_id(), is_true, new Callback() {
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
                    }
                });
            }
        });
    }
}
