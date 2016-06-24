package com.yunfangdata.stickdemo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunfangdata.stickdemo.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Kevin on 2016/6/2.
 */
public class HeadLayout extends LinearLayout {

    @Bind(R.id.title1)
    TextView titleTv1;
    @Bind(R.id.title2)
    TextView titleTv2;
    @Bind(R.id.title3)
    TextView titleTv3;

    private Context mContext;

    public HeadLayout(Context context) {
        super(context);
        initView(context);
    }

    public HeadLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public HeadLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    //初始化视图
    private void initView(Context context){
        this.mContext = context;
        View view = LayoutInflater.from(this.mContext).inflate(R.layout.head_view_layout, this);
        ButterKnife.bind(this, view);

        titleTv1.setText("标题1");
        titleTv2.setText("标题2");
        titleTv3.setText("标题3");

    }
}
