package org.bistu.garbageclassification.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.bistu.garbageclassification.R;
import org.bistu.garbageclassification.entities.City;

import java.util.List;

public class CityAdapter extends ArrayAdapter {
    private int resourceId;

    public CityAdapter(Context context, int resource, List<City> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        City city = (City) getItem(position);//获取当前项的实例
        //LayoutInflater的inflate()方法接收3个参数：需要实例化布局资源的id（item布局文件）、ViewGroup类型视图组对象、false
        //false表示不将layout布局添加到父布局
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        //获取实例（单项item组件 布局文件内的id）
        TextView cityname = view.findViewById(R.id.item_city_name);

        //设置文字
        cityname.setText(city.getCity_name());
        return view;
    }
}
