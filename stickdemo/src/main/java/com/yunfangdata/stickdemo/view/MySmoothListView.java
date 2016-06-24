package com.yunfangdata.stickdemo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Scroller;

/**
 * Created by Administrator on 2016/6/6.
 */
public class MySmoothListView extends ListView implements OnScrollListener {

    //是否在滚动
    private boolean isScroll=false;
    private Scroller mScroller; // used for scroll back

    public MySmoothListView(Context context) {
        super(context);
    }

    public MySmoothListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MySmoothListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}
