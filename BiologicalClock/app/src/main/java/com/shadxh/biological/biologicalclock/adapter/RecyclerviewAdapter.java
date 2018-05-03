package com.shadxh.biological.biologicalclock.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.shadxh.biological.biologicalclock.R;
import com.shadxh.biological.biologicalclock.bean.Apiece;

import java.util.List;

/**
 * Created by PC on 2018/4/26.
 */

public class RecyclerviewAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    private List<Apiece> datas;
    private Context context;
    private final LayoutInflater inflater;

    private onItemLongClickListner onItemLongClickListner;

    public RecyclerviewAdapter(List<Apiece> datas, Context context) {
        this.datas = datas;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setOnItemLongClickListner(RecyclerviewAdapter.onItemLongClickListner onItemLongClickListner) {
        this.onItemLongClickListner = onItemLongClickListner;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_listview, parent, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.view.setTag(position);
        Apiece apiece = datas.get(position);
        switch (apiece.getId()){
            case 1:
                // item.setBackgroundResource(R.drawable.round1);

                viewHolder.imageView.setImageResource(R.drawable.img_1);
                break;
            case 2:
                // item.setBackgroundResource(R.drawable.round2);

                viewHolder.imageView.setImageResource(R.drawable.img_2);
                break;
            case 3:

                // item.setBackgroundResource(R.drawable.round3);
                viewHolder.imageView.setImageResource(R.drawable.img_3);
                break;
            case 4:

                // item.setBackgroundResource(R.drawable.round4);
                viewHolder.imageView.setImageResource(R.drawable.img_4);
                break;
            case 5:

                // item.setBackgroundResource(R.drawable.round5);
                viewHolder.imageView.setImageResource(R.drawable.img_5);
                break;
            case 6:

                // item.setBackgroundResource(R.drawable.round6);
                viewHolder.imageView.setImageResource(R.drawable.img_6);
                break;
            case 7:

                //  item.setBackgroundResource(R.drawable.round7);
                viewHolder.imageView.setImageResource(R.drawable.img_7);
                break;
            case 8:

                //  item.setBackgroundResource(R.drawable.round8);
                viewHolder.imageView.setImageResource(R.drawable.img_8);
                break;
            case 9:

                //item.setBackgroundResource(R.drawable.round9);
                viewHolder.imageView.setImageResource(R.drawable.img_9);
                break;

        }



    }

    public interface onItemLongClickListner{

        void onItemLongClick(int position ,View view);

    }


    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public void onClick(View view) {

        int position = (int) view.getTag();
        if(onItemLongClickListner!=null){
            onItemLongClickListner.onItemLongClick(position,view);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public View view;
        public ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            imageView = (ImageView) itemView.findViewById(R.id.item_img);

        }
    }
}
