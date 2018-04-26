package com.shadxh.biological.biologicalclock.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Created by Administrator on 2017/6/23 0023.
 */

public class MyListView extends ListView {

    String TAG = MyListView.class.getName();

    public MyListView(Context context) {
        super(context);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        Log.d(TAG,"onTouchEventChildCount:"+(getSelectedItemPosition()));

        return super.onTouchEvent(ev);
    }


}
