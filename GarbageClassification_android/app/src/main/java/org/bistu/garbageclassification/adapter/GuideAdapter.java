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
import org.bistu.garbageclassification.entities.Guide;

import java.util.List;

public class GuideAdapter extends ArrayAdapter {
    private int resourceId;

    public GuideAdapter(Context context, int resource, List<Guide> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Guide guide = (Guide) getItem(position);//获取当前项的实例

        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        //获取实例
        TextView guidetitle = view.findViewById(R.id.item_guide_title);

        //设置文字
        guidetitle.setText(guide.getTitle());
        return view;
    }
}
