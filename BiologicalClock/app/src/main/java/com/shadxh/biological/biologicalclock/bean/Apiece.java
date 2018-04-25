package com.shadxh.biological.biologicalclock.bean;

import java.io.Serializable;

/**
 * Created by jiangzuyun on 2015/8/31.
 *
 * 把每个扇形封装成一个对象 内部封装 改扇形的 描述 大小 颜色 起始角度 扫描角度
 */
public class Apiece implements Serializable {

    private float num;
    /**
     * 扇形的起始角度
     */
    private float startAngle;
    /**
     * 扇形的角度
     */
    private float sweepAngle;

    private boolean isAdd;

    private int id;

    private boolean isPic =true;

    private boolean isCheck = false;

    private String context="";

    private String isOpen = "1";

    public Apiece(float num, float startAngle, int id) {
        this.num = num;
        this.startAngle = startAngle;
        this.id = id;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(String isOpen) {
        this.isOpen = isOpen;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public boolean isPic() {
        return isPic;
    }

    public void setPic(boolean pic) {
        isPic = pic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isAdd() {
        return isAdd;
    }

    public void setAdd(boolean add) {
        isAdd = add;
    }

    public float getNum() {
        return num;
    }

    public void setNum(float num) {
        this.num = num;
    }


    public float getStartAngle() {
        return startAngle;
    }

    public void setStartAngle(float startAngle) {
        if(startAngle>360){
           startAngle= startAngle-360;
        }
        if(startAngle<0){
            startAngle=startAngle+360;
        }
        this.startAngle = startAngle;
    }

    public float getSweepAngle() {


        return sweepAngle;
    }

    public void setSweepAngle(float sweepAngle) {

        if(sweepAngle>360){
            sweepAngle= sweepAngle-360;
        }
        if(sweepAngle<0){
            sweepAngle=sweepAngle+360;
        }

        this.sweepAngle = sweepAngle;
    }

}
