package com.yunfangdata.stickdemo.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Administrator on 2016/6/3.
 */
public class VpAdatper extends PagerAdapter{

    private List<ImageView> imageViewList;

    private Context mContext;

    private int count=1;

    public VpAdatper(List<ImageView> imageViews,Context mContext) {
        this.imageViewList=imageViews;
        this.mContext=mContext;
        if(imageViews != null && imageViews.size() > 0){
            count = imageViews.size();
        }
    }

    @Override
    public int getCount() {
        if (count == 1) {
            return 1;
        } else {
            return Integer.MAX_VALUE;
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int newPosition = position % count;
        // 先移除在添加，更新图片在container中的位置（把iv放至container末尾）
        ImageView iv = imageViewList.get(newPosition);
        container.removeView(iv);
        container.addView(iv);
        return iv;
    }
}
