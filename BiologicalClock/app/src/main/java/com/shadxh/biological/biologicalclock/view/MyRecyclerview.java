package com.shadxh.biological.biologicalclock.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by PC on 2018/4/26.
 */

public class MyRecyclerview extends RecyclerView{

    private String  TAG = MyRecyclerview.class.getName();

    public MyRecyclerview(Context context) {
        super(context);
    }

    public MyRecyclerview(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        Adapter adapter = getAdapter();
        adapter.getItemCount();
        Log.d(TAG,"count:"+adapter.getItemCount());

        return super.onTouchEvent(e);
    }
}
