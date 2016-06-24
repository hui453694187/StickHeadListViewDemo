package com.yunfangdata.stickdemo.view;

import android.app.Activity;
import android.view.View;
import android.widget.ListView;

import com.yunfangdata.stickdemo.R;

/**
 * Created by Kevin on 2016/6/3.
 */
public class TabHeadView extends HeadViewBase<String>{

    public TabHeadView(Activity context){
        super(context);

    }

    @Override
    void getView(String s, ListView lv) {
        View view=this.infater.inflate(R.layout.filter_head_layout,lv,false);
        lv.addHeaderView(view);
    }
}
