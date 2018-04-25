package com.shadxh.biological.biologicalclock.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shadxh.biological.biologicalclock.R;
import com.shadxh.biological.biologicalclock.bean.Apiece;

import java.util.List;

/**
 * Created by Administrator on 2017/6/15 0015.
 */

public class ListViewAdapter extends BaseAdapter {

    private List<Apiece> datas;
    private Context context;


    public ListViewAdapter(List<Apiece> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = View.inflate(context, R.layout.item_listview,null);

        LinearLayout item = (LinearLayout) convertView.findViewById(R.id.item);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.item_img);
        TextView textView = (TextView) convertView.findViewById(R.id.item_text);
        View view = convertView.findViewById(R.id.item_foot);

        switch (datas.get(position).getId()){
            case 1:
               // item.setBackgroundResource(R.drawable.round1);
                textView.setText("睡眠");
                imageView.setImageResource(R.drawable.img_1);
                break;
            case 2:
               // item.setBackgroundResource(R.drawable.round2);
                textView.setText("放松训练");
                imageView.setImageResource(R.drawable.img_2);
                break;
            case 3:
                textView.setText("睡眠日记");
               // item.setBackgroundResource(R.drawable.round3);
                imageView.setImageResource(R.drawable.img_3);
                break;
            case 4:
                textView.setText("睡眠教程");
               // item.setBackgroundResource(R.drawable.round4);
                imageView.setImageResource(R.drawable.img_4);
                break;
            case 5:
                textView.setText("运动");
               // item.setBackgroundResource(R.drawable.round5);
                imageView.setImageResource(R.drawable.img_5);
                break;
            case 6:
                textView.setText("工作");
               // item.setBackgroundResource(R.drawable.round6);
                imageView.setImageResource(R.drawable.img_6);
                break;
            case 7:
                textView.setText("娱乐");
              //  item.setBackgroundResource(R.drawable.round7);
                imageView.setImageResource(R.drawable.img_7);
                break;
            case 8:
                textView.setText("学习");
              //  item.setBackgroundResource(R.drawable.round8);
                imageView.setImageResource(R.drawable.img_8);
                break;
            case 9:
                textView.setText("自定义");
                //item.setBackgroundResource(R.drawable.round9);
                imageView.setImageResource(R.drawable.img_9);
                break;

        }

        if(datas.get(position).isPic()){
            imageView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
        }else {
            imageView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
        }

        if(position==datas.size()-1){
            view.setVisibility(View.VISIBLE);
        }else {
            view.setVisibility(View.GONE);
        }

        return convertView;
    }
}
