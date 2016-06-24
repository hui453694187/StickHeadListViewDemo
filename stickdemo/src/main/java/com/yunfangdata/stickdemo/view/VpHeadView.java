package com.yunfangdata.stickdemo.view;

import android.app.Activity;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.yunfangdata.stickdemo.Adapter.VpAdatper;
import com.yunfangdata.stickdemo.R;
import com.yunfangdata.stickdemo.utile.DensityUtil;
import com.yunfangdata.stickdemo.utile.ImageManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/6/3.
 */
public class VpHeadView  extends HeadViewBase<List<String>>{
    @Bind(R.id.vp_ad)
    ViewPager vp;
    @Bind(R.id.ll_index_container)
    LinearLayout ll;

    private ImageManager imageManager;

    private List<ImageView> imgList;

    private VpAdatper vpAdatper;

    private static final int TYPE_CHANGE_AD = 0;
    private Thread mThread;

    private boolean isStopThread = false;
    private ImageManager mImageManager;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == TYPE_CHANGE_AD) {
                vp.setCurrentItem(vp.getCurrentItem() + 1);
            }
        }
    };

    public VpHeadView(Activity mContext){
        super(mContext);
        imgList=new ArrayList<ImageView>();
        imageManager=new ImageManager(this.mContext);

    }

    @Override
    void getView(List<String> imageBeans, ListView lv) {
        View view=infater.inflate(R.layout.vp_headview_layout,lv,false);
        ButterKnife.bind(this, view);
        initVpData(imageBeans);

        lv.addHeaderView(view);
    }

    // 广告数据
    public List<String> getAdData() {
        List<String> adList = new ArrayList<>();
        adList.add("http://img0.imgtn.bdimg.com/it/u=1270781761,1881354959&fm=21&gp=0.jpg");
        adList.add("http://img0.imgtn.bdimg.com/it/u=2138116966,3662367390&fm=21&gp=0.jpg");
        adList.add("http://img0.imgtn.bdimg.com/it/u=1296117362,655885600&fm=21&gp=0.jpg");
        return adList;
    }

    private void initVpData(List<String> imageBeans){
        imgList.clear();
        int size = imageBeans.size();
        for(String url:imageBeans){
            imgList.add(createImageView(url));
        }
        vpAdatper=new VpAdatper(imgList,this.mContext);
        vp.setAdapter(vpAdatper);

        addIndicatorImageViews(size);
        setViewPagerChangeListener(size);
        startADRotate();

    }

    // 创建要显示的ImageView
    private ImageView createImageView(String url) {
        ImageView imageView = new ImageView(mContext);
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(params);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageManager.loadUrlImage(url, imageView);
        return imageView;
    }

    // 添加指示图标
    private void addIndicatorImageViews(int size) {
        ll.removeAllViews();
        for (int i = 0; i < size; i++) {
            ImageView iv = new ImageView(mContext);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(DensityUtil.dip2px(mContext, 5), DensityUtil.dip2px(mContext, 5));
            if (i != 0) {
                lp.leftMargin = DensityUtil.dip2px(mContext, 7);
            }
            iv.setLayoutParams(lp);
            iv.setBackgroundResource(R.drawable.xml_round_orange_grey_sel);
            iv.setEnabled(false);
            if (i == 0) {
                iv.setEnabled(true);
            }
            ll.addView(iv);
        }
    }

    // 为ViewPager设置监听器
    private void setViewPagerChangeListener(final int size) {
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (imgList != null && imgList.size() > 0) {
                    int newPosition = position % size;
                    for (int i = 0; i < size; i++) {
                        ll.getChildAt(i).setEnabled(false);
                        if (i == newPosition) {
                            ll.getChildAt(i).setEnabled(true);
                        }
                    }
                }
            }

            @Override
            public void onPageScrolled(int position, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    // 启动循环广告的线程
    private void startADRotate() {
        // 一个广告的时候不用转
        if (imgList == null || imgList.size() <= 1) {
            return;
        }
        if (mThread == null) {
            mThread = new Thread(new Runnable() {

                @Override
                public void run() {
                    // 当没离开该页面时一直转
                    while (!isStopThread) {
                        // 每隔5秒转一次
                        SystemClock.sleep(5000);
                        // 在主线程更新界面
                        mHandler.sendEmptyMessage(TYPE_CHANGE_AD);
                    }
                }
            });
            mThread.start();
        }
    }

    // 停止循环广告的线程，清空消息队列
    public void stopADRotate() {
        isStopThread = true;
        if (mHandler != null && mHandler.hasMessages(TYPE_CHANGE_AD)) {
            mHandler.removeMessages(TYPE_CHANGE_AD);
        }
    }

}
