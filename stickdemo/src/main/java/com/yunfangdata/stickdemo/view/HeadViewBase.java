package com.yunfangdata.stickdemo.view;

import android.app.Activity;
import android.view.LayoutInflater;
import android.widget.ListView;

import java.util.List;

/**
 * Created by Administrator on 2016/6/3.
 */
public abstract class  HeadViewBase<T> {

    protected Activity mContext;
    protected LayoutInflater infater;
    protected T mEntity;

    public HeadViewBase(Activity mContext){
        this.mContext=mContext;
        infater=LayoutInflater.from(this.mContext);
    }

    //填充数据，加入ListView
    public boolean fillView(T t, ListView listView) {
        if (t == null) {
            return false;
        }
        if ((t instanceof List) && ((List) t).size() == 0) {
            return false;
        }
        this.mEntity = t;
        getView(t, listView);
        return true;
    }

    abstract void getView(T t,ListView lv);


}
