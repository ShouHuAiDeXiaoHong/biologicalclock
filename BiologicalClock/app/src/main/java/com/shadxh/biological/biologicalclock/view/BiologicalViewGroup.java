package com.shadxh.biological.biologicalclock.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.shadxh.biological.biologicalclock.R;
import com.shadxh.biological.biologicalclock.adapter.ListViewAdapter;
import com.shadxh.biological.biologicalclock.bean.Apiece;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC on 2018/4/25.
 */

public class BiologicalViewGroup extends RelativeLayout {

    private CircleMenu pie;
    private LinearLayout linearLayout;
    private RelativeLayout relativeLayout;
    private boolean flag = false;
    private List<Apiece> list;
    private int items;
    private boolean flagLong;
    private int id;
    private float oldSweepAngle;
    private ImageView imgs;
    private ListView listView;
    private boolean canAdd;
    private MyListView myListView;

    public BiologicalViewGroup(Context context) {
        super(context);
    }

    public BiologicalViewGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_biological_view, this);
        pie = (CircleMenu) view.findViewById(R.id.mpie);
        relativeLayout = (RelativeLayout)view.findViewById(R.id.rl);
        linearLayout = (LinearLayout) view.findViewById(R.id.img);
        imgs = (ImageView) view.findViewById(R.id.main_img);
        List<Apiece> piedata = new ArrayList<Apiece>();
        pie.AnalyticData(piedata);
        listView = (ListView) view.findViewById(R.id.listview);
        myListView = (MyListView) view.findViewById(R.id.mylistview);
        list = new ArrayList<>();
        list.add(new Apiece(0,0,1));
        list.add(new Apiece(0,0,2));
        list.add(new Apiece(0,0,3));
        list.add(new Apiece(0,0,4));
        list.add(new Apiece(0,0,5));
        list.add(new Apiece(0,0,6));
        list.add(new Apiece(0,0,7));
        list.add(new Apiece(0,0,8));
        list.add(new Apiece(0,0,9));
        final ListViewAdapter adapter = new ListViewAdapter(list,context);
        listView.setAdapter(adapter);
        myListView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Apiece apiece = list.get(position);
                if(apiece.isPic()){
                    apiece.setPic(false);
                }else {
                    apiece.setPic(true);
                }
                adapter.notifyDataSetChanged();
            }
        });

        pie.setOnItemLongClickListener(new CircleMenu.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(Apiece apiece, float x, float y) {
                flagLong = true;
                id = apiece.getId();
                oldSweepAngle = apiece.getSweepAngle();
                pie.removeItem(apiece);

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                setLayout(linearLayout,view.getX(),view.getY());
                linearLayout.setVisibility(View.VISIBLE);
                setBackground(position+1);
                items = position+1;
                flag = true;
                canAdd = true;
                return false;
            }
        });



        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_UP:
                        flag = false;
                        int height1 = relativeLayout.getHeight();
                        int pieHeight1 = pie.getHeight();
                        float x11 = event.getX() + pieHeight1;
                        //   float y11 = ((event.getY()) - (height1 - pieHeight1) / 2);
                        float y11 = event.getY();
                        //  System.out.println("--y:"+y11);
                        if (pie.isOnBigCircle(x11, y11)) {
                            if (!pie.isOnSmallCircle(x11, y11)) {
                                if(canAdd){
                                    pie.setPieRealItem();
                                    canAdd = false;
                                }

                            }
                        }
                        linearLayout.setVisibility(View.GONE);
                        pie.addMoveItem(pie.changX(x11), pie.changY(y11), false, items, 0);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (flag) {
                            /**
                             * 获得的点的x和y轴要转换成控件中的x和y轴
                             *因为x为负数（应该是居右边显示的原因） 经过测试 刚好加上环形菜单的宽度就可以转换成 控件的x左边
                             * y的坐标获取是带着状态栏的，所以要减去
                             */
                            int height = relativeLayout.getHeight();
                            int pieHeight = pie.getHeight();
                            float x1 = event.getX() + pieHeight;
                            //   float y1 = ((event.getY() ) - (height - pieHeight) / 2);
                            float y1 = event.getY();
                            if (pie.isOnBigCircle(x1, y1)) {
                                if (pie.isOnSmallCircle(x1, y1)) {
                                    linearLayout.setVisibility(View.VISIBLE);
                                    pie.addMoveItem(pie.changX(x1), pie.changY(y1), false, items, 0);
                                } else {
                                    linearLayout.setVisibility(View.GONE);
                                    pie.addMoveItem(pie.changX(x1), pie.changY(y1), true, items, (float) 15);
                                }
                            } else {
                                linearLayout.setVisibility(View.VISIBLE);
                                pie.addMoveItem(pie.changX(x1), pie.changY(y1), false, items, 0);
                            }
                            setLayout(linearLayout, event.getX(), event.getY());
                            return true;
                        } else {
                            return false;
                        }

                        // break;
                }

                return false;
            }
        });


    }

    private void setBackground(int position) {
        switch (position){
            case 1:
                // imageView.setBackgroundResource(R.drawable.round1_b);
                imgs.setImageResource(R.drawable.img_1);
                break;
            case 2:
                // imageView.setBackgroundResource(R.drawable.round2_b);
                imgs.setImageResource(R.drawable.img_10);
                break;
            case 3:
                // imageView.setBackgroundResource(R.drawable.round3_b);
                imgs.setImageResource(R.drawable.img_2);
                break;
            case 4:
                //  imageView.setBackgroundResource(R.drawable.round4_b);
                imgs.setImageResource(R.drawable.img_4);
                break;
            case 5:
                // imageView.setBackgroundResource(R.drawable.round5_b);
                imgs.setImageResource(R.drawable.img_3);
                break;
            case 6:
                // imageView.setBackgroundResource(R.drawable.round6_b);
                imgs.setImageResource(R.drawable.img_9);
                break;
            case 7:
                // imageView.setBackgroundResource(R.drawable.round7_b);
                imgs.setImageResource(R.drawable.img_5);
                break;
            case 8:
                // imageView.setBackgroundResource(R.drawable.round8_b);
                imgs.setImageResource(R.drawable.img_6);
                break;
            case 9:
                // imageView.setBackgroundResource(R.drawable.round9_b);
                imgs.setImageResource(R.drawable.img_8);
                break;
            case 10:
                // imageView.setBackgroundResource(R.drawable.round8_b);
                imgs.setImageResource(R.drawable.img_7);
                break;
            case 11:
                // imageView.setBackgroundResource(R.drawable.round9_b);
                imgs.setImageResource(R.drawable.img_11);
                break;
        }
    }


    /*
 * 设置控件所在的位置YY，并且不改变宽高，
 * XY为绝对位置
 */
    private int drawL;
    private int drawB;

    public void setLayout(View view, float x, float y) {
        drawL = pie.Dp2Px(getContext(),30);
        drawB = pie.Dp2Px(getContext(),45);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        params.setMargins(0, (int) y - drawB, -((int) x - drawL), 0);// 通过自定义坐标来放置你的控件
        view.setLayoutParams(params);
    }


    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context)
    {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();

        switch (action){

            case MotionEvent.ACTION_UP:
                //  flagLong = false;
                linearLayout.setVisibility(View.GONE);
                int height1 = relativeLayout.getHeight();
                int pieHeight1 = pie.getHeight();
                //  float x11 = event.getX()-600 + pieHeight1;
                //  float y11 = ((event.getY()-100 ) - (height1 - pieHeight1) / 2);
                float x11 = event.getX()-550 + pieHeight1;
                float y11 = event.getY();
                System.out.println("--xxx:"+x11+"--yyy:"+y11);
                if (pie.isOnBigCircle(x11,y11)) {
                    System.out.println("--调用了1");
                    if(!pie.isOnSmallCircle(x11,y11)){
                        System.out.println("--调用了2");
                        if(flagLong){
                            System.out.println("--调用了3");
                            pie.setPieRealItem();
                        }

                    }
                }
                pie.addMoveItem(pie.changX(x11), pie.changY(y11), false,items,15f);
                flagLong = false;

                break;

            case MotionEvent.ACTION_MOVE:
                if(flagLong){
                    /**
                     * 获得的点的x和y轴要转换成控件中的x和y轴
                     *因为x为负数（应该是居右边显示的原因） 经过测试 刚好加上环形菜单的宽度就可以转换成 控件的x左边
                     * y的坐标获取是带着状态栏的，所以要减去
                     */

                    float x = event.getX()-550;//这550是左边控件的一半（因为就露出了一半）
                    //               float y = event.getY()-50;//这50应该是通知栏的高度
                    float y = event.getY();
                    int height = relativeLayout.getHeight();
                    int pieHeight = pie.getHeight();
                    float x1 = x + pieHeight;
                    //  float y1 = (y - (height - pieHeight) / 2);
                    float y1 = y;
                    if (pie.isOnBigCircle(x1,y1)) {
                        if(pie.isOnSmallCircle(x1,y1)){
                            setBackground(id);
                            linearLayout.setVisibility(View.VISIBLE);
                            pie.addMoveItem(pie.changX(x1), pie.changY(y1), false,id,0);
                        }else {
                            linearLayout.setVisibility(View.GONE);
                            pie.addMoveItem(pie.changX(x1), pie.changY(y1), true,id,oldSweepAngle);
                        }
                    }else {
                        setBackground(id);
                        linearLayout.setVisibility(View.VISIBLE);
                        pie.addMoveItem(pie.changX(x1), pie.changY(y1), false,id,0);
                    }
                    setLayout(linearLayout, x, y);
                    return true;
                }
                else {
                    return false;
                }

        }

        return super.onTouchEvent(event);
    }

}
