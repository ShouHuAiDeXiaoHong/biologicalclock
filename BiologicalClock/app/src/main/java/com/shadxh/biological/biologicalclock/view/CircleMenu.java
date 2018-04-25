package com.shadxh.biological.biologicalclock.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.shadxh.biological.biologicalclock.R;
import com.shadxh.biological.biologicalclock.bean.Apiece;

import java.util.ArrayList;
import java.util.List;


@SuppressLint("DrawAllocation")
public class CircleMenu extends View {

    private Paint mPaint;//画笔对象
    private int width;//控件的宽
    private int height;//控件的高
    /**
     * 控件的背景色
     */
    private int backColor = Color.parseColor("#00000000");

    /**
     * 饼图数据
     */
    private List<Apiece> pieData = new ArrayList<Apiece>();

    /**
     * 旋转的角度
     */
    private float degrees;
    /**
     * 控件局四周的距离 根据外圆的大小可调饼图的半径
     */
    private int padings = Dp2Px(getContext(),35);;

    private float centX;//圆心的x
    private float centY;//圆心的y
    private float down_x;//下按的x（计算旋转角度有用）
    private float down_y;//下按的y（计算旋转角度有用）

    /**
     * 起开关作用 当旋转之后不会触发点击 获取被点击扇形位置 move执行了 就变为true
     * 当旋转 超过一定值才当成旋转
     */
    private boolean rotate = false;

    /**
     * 制定的某个 扇形 突出的大小 默认13 必须小于 提示线多出饼图的大小TsOut 如果超过则 设置为默认13
     */
    private float pointPieOut = 13;
    /**
     * 当前饼图是否可旋转 默认不可
     */
    private boolean pieRotateable = true;

    /**
     * 较小的边
     */
    private float just;

    /**
     * 饼图的 半径 只能获取
     */
    private float pieRadius;


    /**
     * 是否允许 滚动之后 触发选中事件 （点击事件）默认false 滚动后不触发点击扇形事件
     */
    public boolean selectAfterMove;

    /**
     * 扇形从内往外 变大 的速度
     */
    private float fillouting;


    /**
     * 较长的边
     */
    private int maxb;


    private ViewGroup[] clashView;

    /**
     * 旋转饼图逻辑，返回旋转角度
     */
   // private float rotedAngle;
    /**
     * 内往外扩展所用的 矩阵
     */
    private RectF fillOutRectF;
    /**
     * 画扇形用的矩阵
     */
    private RectF arcRectF;
    /**
     * 突出扇形的矩阵
     */
    private RectF outRectF;
    /**
     * true 旋转的时候 有突出的状态
     * 默认false 旋转的时候 清楚选中状态 up的时候 才出现
     */

    private int white = Dp2Px(getContext(),150);;//表盘的直径
    private float moveX;
    private float moveY;
    private boolean isAdd = false;
    private int itemN = 1;
    private Apiece apiece;

   // private String moveColor = "#dedede";//待添加元素的颜色
    private float startMovedess;//待添加元素的起始角度
    private boolean isMove = false;

    float smallSweepAngle;//从圆环往外拖出去的时候保存的一个旧角度
    private int selectN;
    private Bitmap bitmap1;
    private Bitmap bitmap2;
    private Bitmap bitmap3;
    private Bitmap bitmap4;
    private Bitmap bitmap5;
    private Bitmap bitmap6;
    private Bitmap bitmap7;
    private Bitmap bitmap8;
    private Bitmap bitmap9;
    private Bitmap bitmap10;
    private Bitmap bitmap11;
    private float middleR;
    private boolean isAddOrReduce;

    private int drawL = Dp2Px(getContext(),10);
    private int drawB = Dp2Px(getContext(),15);

    private OnItemClickListener onItemClickListener;


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    /**
     * 使用了自定义属性 会调用
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public CircleMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    public List<Apiece> getPieData() {
        return pieData;
    }
    public void setHaveTextPieData(List<Apiece> pieData ){
        this.pieData =pieData;
    }

    /**
     * 通过布局 创建
     *
     * @param context
     * @param attrs
     */
    public CircleMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleMenu(Context context) {
        this(context, null);
    }

    public void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.img_1);
        bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.img_2);
        bitmap3 = BitmapFactory.decodeResource(getResources(), R.drawable.img_3);
        bitmap4 = BitmapFactory.decodeResource(getResources(), R.drawable.img_4);
        bitmap5 = BitmapFactory.decodeResource(getResources(), R.drawable.img_5);
        bitmap6 = BitmapFactory.decodeResource(getResources(), R.drawable.img_6);
        bitmap7 = BitmapFactory.decodeResource(getResources(), R.drawable.img_7);
        bitmap8 = BitmapFactory.decodeResource(getResources(), R.drawable.img_8);
        bitmap9 = BitmapFactory.decodeResource(getResources(), R.drawable.img_9);
        bitmap10 = BitmapFactory.decodeResource(getResources(), R.drawable.img_10);
        bitmap11 = BitmapFactory.decodeResource(getResources(), R.drawable.img_11);

    }

    /**
     * 此时 获取控件的 宽高 onSizeChanged 在ondraw之前调用 一般 onSizeChanged调用之后可以认为
     * (在非onDraw中)控件显示了
     * <p/>
     * onSizeChanged在什么时候调用？
     * 当控件的位置或者大小改变或者被改变的时候
     * 线性布局中有A和B（A,B没有设定宽高也不似match） 当B变大或者变小 那么A也会改变
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = getWidth();
        height = getHeight();
        just = width > height ? height : width;
      //  maxb = width < height ? height : width;
        centX = width * 1f / 2;
        centY = height * 1f / 2;

        // 防止 突出扇形突出过大 限制突出的大小小于pading
        // pointPieOut = just/2-padings;
      //  pointPieOut = pointPieOut > padings ? padings : pointPieOut;

        pieRadius = just / 2 - padings;

        // 当背景为透明的时候 获取布局的背景色
        if (Integer.MAX_VALUE == backColor) {
            backColor = getPieBackColor();// ondraw也无法拿到布局中设置的背景色
        }

//        fillOutRectF = new RectF(centX - fillouting, centY - fillouting, centX
//                + fillouting, centY + fillouting);
        arcRectF = new RectF(centX - pieRadius, centY - pieRadius, centX + pieRadius, centY + pieRadius);
    //    outRectF = new RectF(centX - pieRadius - pointPieOut, centY - pieRadius - pointPieOut, centX + pieRadius + pointPieOut, centY + pieRadius + pointPieOut);
        middleR = white+(pieRadius-white)/2;
       // System.out.println("--m:"+middleR);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        // 画背景
        mPaint.setColor(backColor);
        RectF back = new RectF(0, 0, width, height);
        canvas.drawRect(back, mPaint);

        mPaint.setColor(Color.parseColor("#546cff"));
        mPaint.setAlpha(51);
        canvas.drawCircle(centX, centY, pieRadius+15, mPaint);


        //背景圆形
        mPaint.setColor(Color.parseColor("#39a7eb"));
        mPaint.setAlpha(51);
        canvas.drawCircle(centX, centY, pieRadius, mPaint);
        RectF oval = arcRectF;
        Apiece pie = null;
        for (int i = 0; i < pieData.size(); i++) {
            pie = pieData.get(i);
            mPaint.setColor(Color.parseColor("#3f8eca"));
            Bitmap bitmapss=null;
            switch (pie.getId()){
                case 1:
                    bitmapss = bitmap1 ;
                    break;
                case 2:
                    bitmapss = bitmap10 ;
                    break;
                case 3:
                    bitmapss = bitmap2 ;
                    break;
                case 4:
                    bitmapss = bitmap4 ;
                    break;
                case 5:
                    bitmapss = bitmap3 ;
                    break;
                case 6:
                    bitmapss = bitmap9 ;
                    break;
                case 7:
                    bitmapss = bitmap5 ;
                    break;
                case 8:
                    bitmapss = bitmap6;
                    break;
                case 9:
                    bitmapss = bitmap8;
                    break;
                case 10:
                    bitmapss = bitmap7;
                    break;
                case 11:
                    bitmapss = bitmap11;
                    break;
            }
            float lastS  =correctPie(pie.getStartAngle());//修正开始角度
            float lastD = correctPie(pie.getSweepAngle()+pie.getStartAngle());//因为有冲突，这里相当于修正了end的角度
            float drawS = lastS+degrees;
            float drawD = lastD-lastS;
            float middle = drawS+drawD/2;
            canvas.drawArc(oval,drawS,drawD, true, mPaint);//绘制修正过的数据，但是真实数据没有修正，使用的时候再做一次修正
            mPaint.setColor(Color.parseColor("#22def0"));
            canvas.drawLine(centX,centY,getBitMapX(drawS,pieRadius),getBitMapY(drawS,pieRadius),mPaint);
            canvas.drawLine(centX,centY,getBitMapX(drawS+drawD,pieRadius),getBitMapY(drawS+drawD,pieRadius),mPaint);
            float bitMapX = getBitMapX(middle,middleR);
            float bitMapY = getBitMapY(middle,middleR);
            RectF dst = null;
            if(drawD<=7.5){
                dst = new RectF(bitMapX-drawL,bitMapY-drawL,bitMapX+drawL,bitMapY+drawL);
            }else {
                dst = new RectF(bitMapX-drawB,bitMapY-drawB,bitMapX+drawB,bitMapY+drawB);
            }
            canvas.drawBitmap(bitmapss,null,dst,mPaint);
        }

        if (isAdd) {
            float moveDess = 0;
            moveDess = (float) ((Math.atan(moveY / moveX) / Math.PI) * 180);//这里使用手指在圆盘上移动的时候的x,y（已经转化成圆环控件里的坐标了）计算出夹角
            moveDess = moveDess > 360 ? moveDess - 360 : moveDess;
            moveDess = moveDess < 0 ? moveDess + 360 : moveDess;
            String color = "#3f8eca";
            Bitmap bitmaps = null;
            switch (itemN) {
                case 1:
                   // color = "#22d6c5";
                    bitmaps = bitmap1 ;
                    break;
                case 2:
                  //  color = "#ffc03d";
                    bitmaps = bitmap10 ;
                    break;
                case 3:
                  //  color = "#afa7ff";
                    bitmaps = bitmap2 ;
                    break;
                case 4:
                   // color = "#ff7977";
                    bitmaps = bitmap4 ;
                    break;
                case 5:
                   // color = "#00ab04";
                    bitmaps = bitmap3 ;
                    break;
                case 6:
                    //color = "#333333";
                    bitmaps = bitmap9 ;
                    break;
                case 7:
                   // color = "#5ebfb7";
                    bitmaps = bitmap5 ;
                    break;
                case 8:
                   // color = "#a1afde";
                    bitmaps = bitmap6;
                    break;
                case 9:
                  //  color = "#ffc994";
                    bitmaps = bitmap8;
                    break;
                case 10:
                    //  color = "#ffc994";
                    bitmaps = bitmap7;
                    break;
                case 11:
                    //  color = "#ffc994";
                    bitmaps = bitmap11;
                    break;
            }
            mPaint.setColor(Color.parseColor(color));
            //moveColor = color;
            smallSweepAngle = correctPie(smallSweepAngle);
            float des = degrees - correctPie(degrees);
            startMovedess = correctPie(moveDess-smallSweepAngle/2)+des;

            float middle = startMovedess+smallSweepAngle/2;
            if(!checkPosition(new Apiece((smallSweepAngle/360)*48, startMovedess-degrees, itemN))){
                mPaint.setAlpha(150);
            }
            canvas.drawArc(oval, startMovedess, smallSweepAngle, true, mPaint);//绘制扇形，15度是360度的24分之一，这个扇形表示1小时


            mPaint.setColor(Color.parseColor("#22def0"));
            canvas.drawLine(centX,centY,getBitMapX(startMovedess,pieRadius),getBitMapY(startMovedess,pieRadius),mPaint);
            canvas.drawLine(centX,centY,getBitMapX(startMovedess+smallSweepAngle,pieRadius),getBitMapY(startMovedess+smallSweepAngle,pieRadius),mPaint);

            float bitMapX = getBitMapX(middle,middleR);
            float bitMapY = getBitMapY(middle,middleR);
            RectF dst = null;
            if(smallSweepAngle<=7.5){
                dst = new RectF(bitMapX-drawL,bitMapY-drawL,bitMapX+drawL,bitMapY+drawL);
            }else {
                dst = new RectF(bitMapX-drawB,bitMapY-drawB,bitMapX+drawB,bitMapY+drawB);
            }
            canvas.drawBitmap(bitmaps,null,dst,mPaint);
        }

        mPaint.setColor(Color.parseColor("#252b58"));
        canvas.drawCircle(centX, centY, white, mPaint);//绘制白色的圆形（圆环内的表盘的白色底）
       // drawBigCircle(canvas);//绘制表盘外的大圆
       // drawSmallCircle(canvas);//绘制表盘的红色的圆心
        drawkedu(canvas);//绘制表盘的刻度
        drawNumber(canvas);//绘制表盘上的数字
    }


    /**
     * 假添加，只是让扇形在圆环上移动，并没有真正的添加
     *
     * @param x
     * @param y
     * @param isadd
     * @param item
     */
    public void addMoveItem(float x, float y, boolean isadd, int item,float smallSweepAngle) {
        isAdd = isadd;
        moveX = x;
        moveY = y;
        this.smallSweepAngle = smallSweepAngle;
        if (isAdd) {
            postInvalidate();
        }
        itemN = item;
    }


    /**
     * 真正的添加，外部监听到手指在圆环上松开的时候才会调用，此时才真正的添加到圆环上
     */
    public void setPieRealItem() {


        apiece = new Apiece( (smallSweepAngle/360)*48, startMovedess-degrees, itemN);
       if(checkPosition(apiece)) {
           pieData.add(apiece);
       }
        AnalyticData(pieData);
    }
    private float correctPie(float f){

        if(f>0){
            int i = (int) (f/7.5f);
            float is = i*7.5f;
            float v = f -is;
            if(v>=3.75){
                i=i+1;
            }
            return i*7.5f;
        }else {

            int i = (int) (f/7.5f);
            float is = i*7.5f;
            float v = f -is;
            if(v<=-3.75){
                i=i-1;
            }
          //  System.out.println("--负数修正："+(i*7.5f));
            return i*7.5f;
        }
    }


    /**
     * 检查是否与已有的扇形有冲突（还是有bug,但是应该是ondraw方法有延时造成的）
     * @param apiece
     * @return
     */
    private boolean checkPosition(Apiece apiece) {


       boolean noDoublication = true;

       // System.out.println("--degrees:"+degrees);
      //  float des = degrees - correctPie(degrees);
        float startAngle;
//        if(degrees!=0){
//            startAngle = apiece.getStartAngle()+360;
//        }else {
            startAngle = apiece.getStartAngle()+360;
     //   }


        if(startAngle>360){
            int k = (int) (startAngle/360);
            startAngle = startAngle - k*360;
        }

        startAngle = correctPie(startAngle);

        float sweepAngle = correctPie(360 * (apiece.getNum() / 48)) ;
        float endAngle = startAngle+sweepAngle;
        if(endAngle>360){
            endAngle =endAngle -360;
        }


        for (int i = 0; i <pieData.size() ; i++) {
            Apiece apiece1 = pieData.get(i);
            float startAngle1 = correctPie(apiece1.getStartAngle()) ;
            float sweepAngle1 = correctPie(apiece1.getSweepAngle()) ;


            float endAngle1 = startAngle1+sweepAngle1;
//            if(startAngle>=startAngle1&&startAngle<=endAngle1){
//                isOk = false;
//                break;
//            }
//            if(endAngle>=startAngle1&&endAngle<=endAngle1){
//                isOk = false;
//                break;
//            }

            if(endAngle1>360){
                endAngle1 = endAngle1-360;
            }


            System.out.println("--end:"+endAngle+"--start:"+startAngle);
            System.out.println("--end1:"+endAngle1+"--start1:"+startAngle1);
            if(startAngle1>endAngle1){//此时跨过零点了
                if(startAngle>endAngle){//操作的那个扇形也跨过零点了
                    noDoublication = false;
                    System.out.println("--两个都跨零点了");
                    break;
                }else {
                    if((startAngle>=0&&startAngle<endAngle1)||(endAngle>startAngle1&&endAngle<=360)){
                        noDoublication = false;
                        System.out.println("--上一个跨零点，但是操作的没跨零点");
                        break;
                    }
                }
            }else {
                if((startAngle>startAngle1&&startAngle<endAngle1)||(endAngle>startAngle1&&endAngle<endAngle1)){
                    System.out.println("--开始或者结束，在其它扇形里面了end:"+endAngle+"-start:"+startAngle+"-end1:"+endAngle1+"-start1:"+startAngle1);
                    noDoublication = false;
                    break;
                }


                if(startAngle<endAngle){
                    if((startAngle>=startAngle1&&endAngle<=endAngle1)||(startAngle1>=startAngle&&endAngle1<=endAngle)){
                        System.out.println("--包含了");
                        noDoublication = false;
                        break;
                    }
                }


            }
        }
        return noDoublication;
    }

    public void removeItem(Apiece apiece) {
        pieData.remove(apiece);
        AnalyticData(pieData);
    }


    //长按点击监听
    private OnItemLongClickListener onItemLongClickListener;

    //长按的子线程的计时方法
    private LongPressRunnable longPressRunnable = new LongPressRunnable();

    private float mLastMotionX, mLastMotionY;
    // 是否移动了
    private boolean isMoved;
    // 是否释放了
    private boolean isReleased;
    // 计数器，防止多次点击导致最后一次形成longpress的时间变短
    private int mCounter;
    //阈值
    private static final int TOUCH_SLOP = 20;
    /**
     * 在滑动变更元素的弧度大小中记录下按的坐标
     */
    private float addDownXMove;
    private float addDownYMove;

//    private float addDownXNoMove;
//    private float addDownYNoMove;

    /**
     * 在滑动变更元素的弧度大小中记录移动的坐标
     */
    float addMoveX;
    float addMoveY;

    //移动增加的角度大小
    float addMoveDegress;

    boolean isUp = false;

    boolean onClock = false;

    boolean onRing  =false;

    // view 没有拦截事件 所以只能选择dispatchTouchEvent或者onTouchevent来处理时间
    // 但是 view默认是不消费事件的 事件只会通过dispatchTouchEvent穿透view 所以事件不会被传递到onTouchevent,
    // 除非下层控件丢失事件上传到onTouchevent ，所以view的事件处理建议写在dispatchTouchEvent内
    // 每次事件都会 重新调用该方法
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //请求 会产生事件冲突的外部ViewGroup不要拦截事件
        if (clashView != null && clashView.length > 0) {
            for (int i = 0; i < clashView.length; i++) {
                clashView[i].requestDisallowInterceptTouchEvent(true);
            }
        }
        float x1 = event.getX();
        float y1 = event.getY();

        if (((((Math.pow((x1 - centX), 2) + Math.pow((y1 - centY), 2)) <= Math.pow(white, 2))||((Math.pow((x1 - centX), 2) + Math.pow((y1 - centY), 2)) > Math.pow(pieRadius, 2))) && !isMove&&!onRing)) {//这里只判断是否在表盘内

            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    if (!pieRotateable) {
                        return false;
                    }
                    rotate = false;// 清除 旋转状态
                    down_x = event.getX();
                    down_y = event.getY();
                    onClock = true;//在表盘上了
                    return true; // 当事件被消费之后 后续事件才会被该控件处理 原生view没处理 //父类FreeLine已经返回true了这里不需要 否则会阻断父类的down 当父类是view的时候需要打开break;  只要down事件被消费 后续事件即可被传入当前控
                case MotionEvent.ACTION_MOVE:
                    float move_x = event.getX();
                    float move_y = event.getY();
                    // 当移动的距离超过0.001就算滑动
                    if (Math.abs(move_x - down_x) > 0.0001
                            || Math.abs(move_y - down_y) > 0.0001) {
                        rotate = true;// 处于旋转状态
                    }
                   rotatePie(move_x, move_y);
                    break;
                case  MotionEvent.ACTION_UP:
                    onClock = false;//手指离开，不在表盘上了
                    break;
            }
        } else if(!onClock){

            if (longPressRunnable != null) {//这里是圆环上的逻辑，所以有长按监听，所以要判断长按监听是否为空（不管有没有触发，都判断一下）
                float x = event.getX();
                float y = event.getY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        addDownXMove = event.getX();//这两个监听是判断触摸到哪个扇形和滑动增加扇形面积的时候用的
                        addDownYMove = event.getY();
                        selectDownOrUp(event.getX(),event.getY());//在圆环下按的时候就要判断是触摸到了扇形的上半部还是下半部
                        mLastMotionX = x;//这两个坐标是长按监听用的
                        mLastMotionY = y;
                        mCounter++;
                        isReleased = false;//是否释放了触摸监听，如果松开是释放了
                        isMoved = false;//是否正在移动（这个是长按用的）
                        postCheckForLongTouch(x, y);//设置长按监听的方法
                        onRing = true;
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        addMoveX = event.getX();//手指移动的坐标，这里要存成全局的，这样才能实时的更新到具体的方法内
                        addMoveY = event.getY();
                        addOrReduce(event.getX(),event.getY());//判断触摸到扇形的上半部还是下半部之后，就可以来控制扇形曾大或减小的逻辑了
                        if (isMoved) //移动，就直接返回(因为这个值如果是true的话后面就不会创建长按的监听了)
                            break;
                        if (Math.abs(mLastMotionX - x) > TOUCH_SLOP
                                || Math.abs(mLastMotionY - y) > TOUCH_SLOP) {
                            // 移动超过阈值，则表示移动了
                            isMoved = true;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        isReleased = true; // 释放了（这个值为true的话后面的长按监听也不会被创建了）
                        isMove = false;//这个值是用来判断是否触发长按时间了，因为长按和触摸改变扇形的大小是冲突的，所以要用这个值来区分
                        addMoveDegress=0;//手指移动的轨迹的夹角
                        onRing = false;
                        System.out.println("--move:"+isMove+"--add:"+isAddOrReduce);
                        if(!isMove&&!isAddOrReduce){
                            int i = checkClickWhere(mLastMotionX, mLastMotionY);
                            if(i!= Integer.MAX_VALUE){
                                if(onItemClickListener!=null){
                                    onItemClickListener.onItemClick(pieData.get(i));
                                }
                            }
                        }
                        break;
                }
            }

        }
        return super.dispatchTouchEvent(event);
    }


    /**
     * 滑动增加或者减少弧度
     * @param moveX
     * @param moveY
     */
    private void addOrReduce(float moveX, float moveY) {
        if(!isMove&&selectN!= Integer.MAX_VALUE&&pieData.size()!=0){//判断是否触发长按事件
           // System.out.println("--增加");
            float moveD = getAddMoveDegrees(moveX, moveY);//手指移动的增量夹角
           // System.out.println("--moved:"+moveD);
            if(moveD==0){
                isAddOrReduce = false;
            }else {
                isAddOrReduce = true;
            }

            Apiece apiece = pieData.get(selectN);
            float startAngle = apiece.getStartAngle();
            float sweepAngle = apiece.getSweepAngle();
            //这一点处理开始角和结束角互换
            if(moveD<-350){//手指移动增量的夹角的数据会有问题，这里需要做处理
                moveD = 360+moveD;
            }
            if(moveD>350){
                moveD = 360-moveD;
            }
            float lastSweepAngle ;//待更改扇形的面积
            if(!isUp){//点到扇形的下半部分
                lastSweepAngle = sweepAngle+moveD ;//这里是操作的扇形的下半部分，往下滑动的时候就是增加
                if(lastSweepAngle<=7.5&&lastSweepAngle>=0){
                    lastSweepAngle=-7.5f;
                }
                if(lastSweepAngle<0){//这里判断是否拖到他起始角的上面了，如果小于0就是拖到上面了，此时要把状态改为触摸的扇形上部
                    isUp =true;
                    int position =  getConflictPie(startAngle+lastSweepAngle,-lastSweepAngle,selectN);//得到触摸到的扇形跟其它的扇形是否有冲突，没有的话返回-1
                    if(position!=-1){//发现有冲突
                        Apiece apiece1 = pieData.get(position);//得到了有冲突的扇形的数据
                        int position1 = getConflictPie(apiece1.getStartAngle(),apiece1.getSweepAngle(),position);//再拿有冲突的扇形来判断是否与另一个扇形有冲突(检查一下是否被挤在中间了)
                        if(position1!=-1){//有冲突
                            float v = apiece1.getNum() - 1;
                            if(!(v>=-0.1&&v<=0.1)){//这里判断没有达到最小值，所以可以继续进行下面的逻辑(增大获减小)
                              //  apiece1.setStartAngle(apiece1.getStartAngle()+moveD);
                                apiece1.setSweepAngle(apiece1.getSweepAngle()-moveD);//此时对被挤着的扇形做操作，因为是被挤，就是把这个扇形的面积减去手指移动的角度,起始角不变
                                apiece1.setNum(((apiece1.getSweepAngle()-moveD)/360)*48);
                                apiece.setStartAngle(startAngle+lastSweepAngle);//初始角的逻辑都一样，操作的上半部分，所以初始角要改变
                                apiece.setSweepAngle(-lastSweepAngle);//但是设置扇形的面积的逻辑要反一下
                                apiece.setNum((-lastSweepAngle/360)*48);
                            }
                        }else {//没有冲突的话
                            apiece1.setStartAngle(apiece1.getStartAngle()-moveD);//就只改变起始角的位置，不用管扇形的大小
                            apiece.setStartAngle(startAngle+lastSweepAngle);//下面的都一样
                            apiece.setSweepAngle(-lastSweepAngle);
                            apiece.setNum((-lastSweepAngle/360)*48);
                        }
                    }else {//手指操作的扇形跟任何扇形都没有冲突的话，就正常走逻辑
                        apiece.setStartAngle(startAngle+lastSweepAngle);
                        apiece.setSweepAngle(-lastSweepAngle);
                        apiece.setNum((-lastSweepAngle/360)*48);
                    }
                }else {//这里判断出来是正向拖动，没出现特殊情况
                    int position =  getConflictPie(startAngle,lastSweepAngle,selectN);//获取手指操作的扇形的位置
                    if(position!=-1){//有冲突
                        Apiece apiece1 = pieData.get(position);//获得有冲突的扇形的那个扇形
                        int position1 = getConflictPie(apiece1.getStartAngle(),apiece1.getSweepAngle(),position);
                        if(position1!=-1){//有冲突的那个扇形也跟其它扇形有冲突
                            float v = apiece1.getNum() - 1;
                            if(!(v>=-0.1&&v<=0.1)){//没有达到最小范围
                                apiece1.setStartAngle(apiece1.getStartAngle()+moveD);//没有达到最小值的时候因为是操作的下面，所以起始角要改变，初始角是增加的
                                apiece1.setSweepAngle(apiece1.getSweepAngle()-moveD);//扇形面积也要改变，这个是要减小的
                                apiece1.setNum(((apiece1.getSweepAngle()-moveD)/360)*48);
                                apiece.setSweepAngle(lastSweepAngle);//这里可以直接把增加的角度加上就行了
                                apiece.setNum((lastSweepAngle/360)*48);
                            }
                        }else {
                            apiece1.setStartAngle(apiece1.getStartAngle()+moveD);//如果没冲突，只改变起始角度的大小（增大）
                            apiece.setSweepAngle(lastSweepAngle);//这里可以直接把增加的角度加上就行了
                            apiece.setNum((lastSweepAngle/360)*48);
                        }
                    }else {
                        apiece.setSweepAngle(lastSweepAngle);//没有任何冲突的时候,只改变操作扇形的面积即可
                        apiece.setNum((lastSweepAngle/360)*48);
                    }
                }
            }
            if(isUp){//这里判断出来是操作的上半部分
                lastSweepAngle = sweepAngle-moveD ;//上半部分的话，往下滑动就是减少的
                if(lastSweepAngle<=7.5&&lastSweepAngle>=0){
                    lastSweepAngle=-7.5f;
                }
                if(lastSweepAngle<0){//这里小于0，滑动过了起始点

                    isUp = false;//这时就要反一下逻辑
                    int position =  getConflictPie(startAngle,-lastSweepAngle,selectN);//算出操作的扇形的位置
                    if(position!=-1){//得出操作的扇形与其它扇形有冲突
                        Apiece apiece1 = pieData.get(position);//得出与操作扇形有冲突的扇形
                        int position1 = getConflictPie(apiece1.getStartAngle(),apiece1.getSweepAngle(),position);//有冲突的扇形与其它扇形是否有冲突
                        if(position1!=-1){//有冲突的扇形与另一个扇形也有冲突
                            float v = apiece1.getNum() - 1;
                            if(!(v>=-0.1&&v<=0.1)){//没有达到最小值
                                apiece1.setStartAngle(apiece1.getStartAngle()+moveD);//此时操作的扇形的下半部分，所以要增加后面的扇形的初始角
                                apiece1.setSweepAngle(apiece1.getSweepAngle()-moveD);//减小扇形面积
                                apiece1.setNum(((apiece1.getSweepAngle()-moveD)/360)*48);
                                apiece.setStartAngle(startAngle);//操作的扇形还要正常操作，因为操作的下面，所以初始角不变
                                apiece.setSweepAngle(-lastSweepAngle);//只改变扇形面积，因为拖反了，所以要为负数
                                apiece.setNum((-lastSweepAngle/360)*48);
                            }
                        }else {//这里是有冲突的扇形没有与其它扇形有冲突
                            apiece1.setStartAngle(apiece1.getStartAngle()-moveD);//这里只改变有冲突的扇形的初始角，因为拖反了，所以要减去移动的角（负负得正）
                            apiece.setStartAngle(startAngle);
                            apiece.setSweepAngle(-lastSweepAngle);
                            apiece.setNum((-lastSweepAngle/360)*48);
                        }
                    }else {
                        apiece.setStartAngle(startAngle);
                        apiece.setSweepAngle(-lastSweepAngle);
                        apiece.setNum((-lastSweepAngle/360)*48);
                    }
                }else {//没有出现特殊情况，没有反着拖
                    int position =  getConflictPie(startAngle+moveD,lastSweepAngle,selectN);//得出操作的扇形的位置
                    if(position!=-1){//操作的扇形与其它扇形有冲突
                        Apiece apiece1 = pieData.get(position);
                        int position1 = getConflictPie(apiece1.getStartAngle(),apiece1.getSweepAngle(),position);
                        if(position1!=-1){//有冲突的那个扇形与其它扇形也有冲突
                            float v = apiece1.getNum() - 1;
                            if(!(v>=-0.1&&v<=0.1)){//没有达到最小值
                                apiece1.setSweepAngle(apiece1.getSweepAngle()+moveD);//因为操作的上部，所以只改变扇形的大小，初始角不变
                                apiece1.setNum(((apiece1.getSweepAngle()-moveD)/360)*48);
                                apiece.setStartAngle(startAngle+moveD);//操作的扇形的上部，就增加滑动的角度的初始角
                                apiece.setSweepAngle(lastSweepAngle);//增加扇形的大小
                                apiece.setNum((lastSweepAngle/360)*48);
                            }
                        }else {
                            apiece1.setStartAngle(apiece1.getStartAngle()+moveD);//这里有冲突的扇形与其它扇形没哟冲突，所以只改变初始角
                            apiece.setStartAngle(startAngle+moveD);
                            apiece.setSweepAngle(lastSweepAngle);
                            apiece.setNum((lastSweepAngle/360)*48);
                        }
                    }else {
                        apiece.setStartAngle(startAngle+moveD);
                        apiece.setSweepAngle(lastSweepAngle);
                        apiece.setNum((lastSweepAngle/360)*48);
                    }
                }
            }
            postInvalidate();
        }

    }


    /**
     * 传入一个角来判断是否为最小范围
     * @param vs
     * @return
     */
    private boolean checkIsLimit(float vs){
        boolean islimit = false;
        float v = (float)(vs - 7.5);//7.5度是最小的
        if(v>=-0.1&&v<=0.1){//在这个范围里了
            islimit = true;
        }else {
            islimit = false;
        }

        return islimit;
    }



    /**
     * 传入起始角和扇形的大小跟扇形的位置来判断是否跟其它扇形有冲突
     * 有的话就返回那个扇形的数组中的位置，没的话就返回-1
     * @param startd
     * @param sweepAngle
     * @param p
     * @return
     */
    public int getConflictPie(float startd,float sweepAngle,int p){
        int i = -1;
        if(isUp){//先判断触摸的是扇形的上半部分还是下半部分
            for (int j = 0; j <pieData.size() ; j++) {
                Apiece apiece = pieData.get(j);
                if(p!=j){//排除自己
                    float start=apiece.getStartAngle();//其它扇形的开始角
                    float end = apiece.getStartAngle()+apiece.getSweepAngle();//其它扇形的结束角
                    if(start>360){
                        start = start-360;
                    }
                    if(end>360){
                        end = end -360;
                    }
                    if(start<end){//这里是正常的情况下，扇形中间没有过零度
                        //因为是手指触摸的，会有误差，float的字段，误差在0.1以内
                        //下面的判断是传入的扇形的起始角是否在其它扇形的范围内
                        //如果在其它扇形的范围内，就可以确定与其它扇形有冲突，直接返回位置就行
                        if(startd+0.1<=end&&startd+0.1>=start){
                            i = j;//把这个位置返回出去就行了
                            break;
                        }

                    }else {//这里是特殊情况下，扇形跨越了零度，此时扇形的起始角会大于结束角
                        if(!(startd+0.1>=end&&startd+0.1<=start)){//这里使用圆形的互补面积来判断，其实也是判断是否在其它扇形上
                            i = j;
                            break;
                        }
                    }
                }
            }
        }else {//这里判断的是扇形的下半部分
            for (int j = 0; j <pieData.size() ; j++) {
                Apiece apiece = pieData.get(j);
                if(p!=j){
                    double v = startd+sweepAngle+0.1;//如果下半部分的话，就得拿传入的扇形的结束角度来做判断
                    if(v>360){
                        v = v-360;
                    }

                    float start=apiece.getStartAngle();//其它的扇形的开始角
                    float end = apiece.getStartAngle()+apiece.getSweepAngle();//其它扇形的结束角

                    if(start>360){
                        start = start-360;

                    }
                    if(end>360){
                        end = end -360;
                    }

                    if(start<end){//这里是正常情况下，扇形没有跨到0度
                        if(v>=start&&v<=end){//传入的结束角度在其它扇形的范围内
                            i = j;
                            break;
                        }

                    }else {//这里是特殊情况，扇形跨越0度了
                        if(!(v<=start&&v>=end)){//判断也是用了圆的互补
                            i= j;
                            break;
                        }
                    }
                }
            }
        }
        return i;//有冲突，返回位置，没冲突返回-1
    }


    /**
     * 这里判断手指所在扇形的上半部分还是下半部分
     * @param x
     * @param y
     * @return
     */
    public boolean selectDownOrUp(float x, float y) {
        //这里要一直变换x,y（有可能进入的别的元素里）这里其实是把移动中的x,y写上了
        selectN = checkClickWhere(addDownXMove, addDownYMove);//根据手指移动的坐标来判断位置是比较准的
        if (!isMove && selectN != Integer.MAX_VALUE) {//判断是否触发长按事件
            double downD = getPointAngle(x, y) - degrees;//手指所在的点的夹角
            Apiece apiece = pieData.get(selectN);
            float startAngle = apiece.getStartAngle();
            float sweepAngle = apiece.getSweepAngle();
            float endAngle = startAngle + sweepAngle;
            float middleAngle = startAngle + sweepAngle / 2;
            if (middleAngle >= 360) {
                middleAngle = middleAngle - 360;
            }
            if(middleAngle<0){
                middleAngle= middleAngle+360;
            }

            if(endAngle<0){
               endAngle= endAngle+360;
            }
            if (endAngle >= 360) {
                endAngle = endAngle - 360;
            }
            if(startAngle<0){
                startAngle= startAngle+360;
            }
            if (startAngle >= 360) {
                startAngle = startAngle - 360;
            }
            if(downD<0){
                downD= downD+360;
            }
            if (downD >= 360) {
                downD = downD - 360;
            }
            if(startAngle<endAngle){
                if (middleAngle <= downD && downD <= endAngle) {//点到扇形的下半部分
                    isUp = false;
                }
                if (middleAngle >= downD && downD >= startAngle) {
                    isUp = true;
                }
            }else {
                if(downD>startAngle){
                    if(downD-startAngle<360-downD+endAngle){
                        isUp = true;
                    }else {
                        isUp = false;
                    }
                }else {
                    if(360-startAngle+downD<endAngle-downD){
                        isUp = true;
                    }else {
                        isUp = false;
                    }
                }
            }
        }
        return isUp;
    }

    private void postCheckForLongTouch(float x, float y) {

        longPressRunnable.setPressLocation(x, y);

        postDelayed(longPressRunnable, 500);
    }


    /**
     * 旋转 饼图
     *
     * @param move_x
     * @param move_y
     */
    private float rotatePie(float move_x, float move_y) {

        // 方法 move的时候获取角度 - move之前的角度 = 旋转的角度
        double moveAngle = getPointAngle(move_x, move_y);
        double downAngle = getPointAngle(down_x, down_y);
        degrees += (float) (moveAngle - downAngle);

        // 限定角度 不超过 360 -360
        degrees = degrees > 360 ? degrees - 360 : degrees;
        degrees = degrees < -360 ? degrees + 360 : degrees;

        down_x = move_x;
        down_y = move_y;
        postInvalidate();

        return degrees;
    }


    private float getAddMoveDegrees(float x,float y){
        double moveAngle = getPointAngle(x, y);
        double downAngle = getPointAngle(addDownXMove, addDownYMove);
        addMoveDegress = (float) (moveAngle - downAngle);
        // 限定角度 不超过 360 -360
        addMoveDegress = addMoveDegress > 360 ? addMoveDegress - 360 : addMoveDegress;
        addMoveDegress = addMoveDegress < -360 ? addMoveDegress + 360 : addMoveDegress;
        addDownXMove = x;
        addDownYMove = y;
        return addMoveDegress;
    }


    /**
     * 根据 cos函数 获取当前up时的点 与水平右之间的角度 判断点击的是哪个扇形
     *
     * @param up_y
     * @param up_x
     */
    private int checkClickWhere(float up_x, float up_y) {
        double clickAngle = getPointAngle(up_x, up_y);
        // 根据 点击的角度 判断点中了那个扇形
        for (int i = 0; i < pieData.size(); i++) {
            Apiece pie = pieData.get(i);

            // 把每个扇形的起止角度 限定在360之内 方便
            float startPie = pie.getStartAngle() + degrees;
            startPie = startPie > 360 ? startPie - 360 : startPie;
            startPie = startPie < 0 ? startPie + 360 : startPie;
            float start = startPie;
            float end = startPie + pie.getSweepAngle();
            end = end > 360 ? end - 360 : end;
            end = end < 0 ? end + 360 : end;
            if ((end > clickAngle && start < clickAngle) || (end > clickAngle || start < clickAngle) && (end < start)) {
                return i;
            }
        }
        return Integer.MAX_VALUE;
    }

    /**
     * 获取当前点 与 水平右的夹角
     *
     * @param up_x
     * @param up_y
     * @return
     */
    public double getPointAngle(float up_x, float up_y) {
        float a = up_x - centX;
        float b = up_y - centY;
        // 斜边
        double c = Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
        // 获取 弧度
        double acos = Math.acos(a / c);
        // 获取 角度 角度=弧度/PI * 180
        double clickAngle = acos / Math.PI * 180;// 注意 获取的只是0-180 还需要判断
        if (up_y > centY) {
            // 点击位于 下半圆
        } else {
            // 点击位于 上半圆
            clickAngle = 2 * 180 - clickAngle;
        }
        return clickAngle;
    }


    private float getBitMapX(float middle,float r){
        if(middle>360){
            middle = middle-360;
        }

        if(middle<0){
            middle = middle+360;
        }
        float x = 0;
        float bitMapR = r;
        if(middle>0&&middle<90){
           x = (float) (Math.cos((middle/360)*2* Math.PI)*bitMapR);
        }else if(middle>=90&&middle<180){
          x = (float) (Math.cos((middle/360)*2* Math.PI)*bitMapR);
        }else if(middle>=180&&middle<270){
            x = (float) (Math.cos((middle/360)*2* Math.PI)*bitMapR);
        }else if(middle>=270&&middle<360){
            x = (float) (Math.cos((middle/360)*2* Math.PI)*bitMapR);
        }else if(middle==360||middle==0){
            x=r;
        }
       // System.out.println("--x:"+x);
        return x+centX;
    }


    private float getBitMapY(float middle,float r){

        if(middle>360){
            middle = middle-360;
        }
        if(middle<0){
            middle = middle+360;
        }

        float y = 0;
        float bitMapR = r;
        if(middle>0&&middle<90){
            y = (float) (Math.sin((middle/360)*2* Math.PI)*bitMapR);
        }else if(middle>=90&&middle<180){
            y = (float) (Math.sin((middle/360)*2* Math.PI)*bitMapR);

        }else if(middle>=180&&middle<270){
            y = (float) (Math.sin((middle/360)*2* Math.PI)*bitMapR);
        }else if(middle>=270&&middle<360){
            y = (float) (Math.sin((middle/360)*2* Math.PI)*bitMapR);
        }else if(middle==360||middle==0){
            y=0;
        }
      //  System.out.println("--y:"+y);
        return y+centY;
    }


    /**
     * 设置数据
     *
     * @param pieData
     */
    public void AnalyticData(List<Apiece> pieData) {
        //计算角度
        for (Apiece pie : pieData) {
            float sweepAngle = 360 * (pie.getNum() / 48);
            pie.setStartAngle(pie.getStartAngle());
            pie.setSweepAngle(sweepAngle);
        }
        this.pieData = pieData;
        postInvalidate();

    }




    public interface OnItemClickListener{
        void onItemClick(Apiece apiece);
    }

    /**
     * 长按的监听
     */

    public interface OnItemLongClickListener {
        void onItemLongClick(Apiece apiece, float x, float y);
    }


    /**
     * 控件的背景颜色
     */
    public int getPieBackColor() {
        return backColor;
    }


    /**
     * 设定 扇形是否可 旋转 默认不可以
     */
    public void setPieRotateable(boolean pieRotateable) {
        this.pieRotateable = pieRotateable;
    }


    /**
     * 这些是绘制表盘的逻辑
     *
     * @param canvas
     */

    // 外面的大圆
    private void drawBigCircle(Canvas canvas) {
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(Dp2Px(getContext(), 1));
        canvas.drawCircle(centX, centY, white, mPaint);
    }

    // 圆心
    private void drawSmallCircle(Canvas canvas) {
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(centX, centY, Dp2Px(getContext(), 5), mPaint);
    }

    // 刻度
    private void drawkedu(Canvas canvas) {

        float a, b, sA, sB, lA, lB;

        for (int i = 0; i < 48; i++) {

            int shortR = white - Dp2Px(getContext(), 9);
          //  int longR = white - Dp2Px(getContext(), 14);
            float r = i * (float) 7.5 + degrees;
            a = (float) (white * Math.sin(r * (Math.PI / 180)) + centX);
            b = (float) (centY - white * Math.cos(r * (Math.PI / 180)));

            sA = (float) (shortR * Math.sin(r * (Math.PI / 180)) + centX);
            sB = (float) (centY - shortR * Math.cos(r * (Math.PI / 180)));

          //  lA = (float) (longR * Math.sin(r * (Math.PI / 180)) + centX);
         //   lB = (float) (centY - longR * Math.cos(r * (Math.PI / 180)));


//            if (i % 2 == 0) {
//                mPaint.setColor(Color.BLACK);
//                mPaint.setStyle(Paint.Style.FILL);
//                mPaint.setStrokeWidth(Dp2Px(getContext(), 3));
//                canvas.drawLine(a, b, lA, lB, mPaint);
//            } else {

            if(i==0||i==12||i==24||i==36){
                mPaint.setColor(Color.WHITE);
                mPaint.setStyle(Paint.Style.FILL);
//                mPaint.setStrokeWidth(Dp2Px(getContext(), 2));
                canvas.drawCircle(sA,sB,5,mPaint);
            }else {
                mPaint.setColor(Color.WHITE);
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setAlpha(76);
                canvas.drawCircle(sA,sB,3,mPaint);
            }


               // canvas.drawLine(a, b, sA, sB, mPaint);

          //  }
        }
    }

    // 刻度上的时间数字
    private void drawNumber(Canvas canvas) {

        mPaint.setColor(Color.WHITE);

        mPaint.setTextAlign(Paint.Align.CENTER);  // 设置文本水平居中

        //float fontHeight = (mPaint.getFontMetrics().bottom - mPaint.getFontMetrics().top); // 获取文字高度用于设置文本垂直居中,这里不知道为啥会变
       // float fontHeight = 37.160156f;
        float fontHeight =Dp2Px(getContext(),19);;
       // System.out.println("--f:"+fontHeight);
        // 数字离圆心的距离
        float distance = white - Dp2Px(getContext(), 25);
        // 数字的坐标(a,b)
        float a, b,c,d;
        // 每30度写一个数字
        for (int i = 0; i < 24; i++) {
            float r = i * 15 + degrees;
            a = (float) (distance * Math.sin(r * (Math.PI / 180)) + centX);
            b = (float) (centY - distance * Math.cos(r * (Math.PI / 180)));

            c = (float) ((distance-8) * Math.sin(r * (Math.PI / 180)) + centX);
            d = (float) (centY - (distance-8) * Math.cos(r * (Math.PI / 180)));

          //  mPaint.setColor(Color.WHITE);
 //           if (i < 12) {//绘制上午的12个数字
//
//                if (i == 0) {
//                    // 本以为font_height/2即可获得文字一半的高度，实测除以3.5才是，不明觉厉
//                    canvas.drawText("12a", a, (float) (b + fontHeight / 3.5), mPaint);
//                } else {


            if(i==0||i==6||i==12||i==18) {
//                mPaint.setTextSize(Dp2Px(getContext(), 12));
//                canvas.drawText("点", a, (float) (b + fontHeight / 3.5), mPaint);
                mPaint.setTextSize(Dp2Px(getContext(), 14));
                canvas.drawText(String.valueOf(i)+"点", c, (float) (d + fontHeight / 3.5), mPaint);
            }else {
                mPaint.setTextSize(Dp2Px(getContext(), 14));
                canvas.drawText(String.valueOf(i), a, (float) (b + fontHeight / 3.5), mPaint);
            }


//                }
//            } else {//绘制下午的12个数字
//                int k = i - 12;
//                if (k == 0) {
//                    // 本以为font_height/2即可获得文字一半的高度，实测除以3.5才是，不明觉厉
//                    canvas.drawText("12p", a, (float) (b + fontHeight / 3.5), mPaint);
//                } else {
//                    canvas.drawText(String.valueOf(k) + "p", a, (float) (b + fontHeight / 3.5), mPaint);
//                }
//            }
        }
    }


    /**
     * 屏幕DP转PX
     **/
    public int Dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }


    /**
     * 判断x,y是否在打圆内（外部调用）
     *
     * @param x
     * @param y
     * @return
     */

    public boolean isOnBigCircle(float x, float y) {
        return (Math.pow((x - centX), 2) + Math.pow((y - centY), 2)) <= Math.pow(pieRadius, 2);
    }


    /**
     * 判断x,y是否在小圆的园内(外部调用)
     *
     * @param x
     * @param y
     * @return
     */
    public boolean isOnSmallCircle(float x, float y) {
        return (Math.pow((x - centX), 2) + Math.pow((y - centY), 2)) <= Math.pow(white, 2);
    }


    /**
     * 将外部的x转为这个view中的x
     * @param x
     * @return
     */

    public float changX(float x) {
        return x - centX;
    }


    /**
     * 将外部的y转为这个view中的y
     * @param y
     * @return
     */
    public float changY(float y) {
        return y - centY;
    }


    /**
     * 这里是一个长按记录的一个子线程
     * 这里还设置了长按的监听
     */
    private class LongPressRunnable implements Runnable {

        private int x, y;

        public void setPressLocation(float x, float y) {
            this.x = (int) x;
            this.y = (int) y;
        }

        @Override
        public void run() {
            int i = checkClickWhere(x, y);//根据传入的x,y来确定操作的扇形的位置
            mCounter--;
            // 计数器大于0，说明当前执行的Runnable不是最后一次down产生的。
            if (mCounter > 0 || isReleased || isMoved) {
                return;
            } else if (onItemLongClickListener != null && i != Integer.MAX_VALUE) {
                isMove = true;//这里表示长按的事件已经触发了
                onItemLongClickListener.onItemLongClick(pieData.get(i), x, y);//传入扇形的对象和坐标
            }
        }
    }
}
