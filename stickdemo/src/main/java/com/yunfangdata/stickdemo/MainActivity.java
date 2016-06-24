package com.yunfangdata.stickdemo;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;
import com.yunfangdata.stickdemo.utile.DensityUtil;
import com.yunfangdata.stickdemo.view.HeadLayout;
import com.yunfangdata.stickdemo.view.TabHeadView;
import com.yunfangdata.stickdemo.view.VpHeadView;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity {

    @Bind(R.id.lv)
    ListView lv;
    @Bind(R.id.head_layout)
    HeadLayout headLayout;

    @Bind(R.id.appBar_id)
    AppBarLayout appBarLayout;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    ActionBar actionBar;
    private ActionBarDrawerToggle mDrawerToggle;

    private static final int REQUEST_SELECT_PICTURE = 0x01;

    private static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 0x02;
    private static final String SAMPLE_CROPPED_IMAGE_NAME = "SampleCropImage";

    AlertDialog mAlertDialog;

    //是否在滚动
    private boolean isScroll=false;

    private TabHeadView currentHeadLayout;
    private View tabView;
    private int tabViewTopSpace;
    private int tabViewHeight;

    private VpHeadView vpHeadView;
    private View vpView;
    private int vpViewTopSpace;//距离顶部的高度
    private int vpViewHeight;//高度

    private boolean isStickyTop = false; // 是否吸附在顶部

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initToolbar();

        setData();

    }

    private void initToolbar(){
        mDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.tool_name,R.string.tool_name1);

        this.setSupportActionBar(this.toolbar);
        actionBar=this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setTitle("标题");

    }

    private void setData() {
        //列表项的数据
        String[] strs = {"1","2","3","4","5","6","7","8","1","2","3","4","5","6","7","8","1","2","3","4","5","6","7","8"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_expandable_list_item_1,strs);
        lv.setAdapter(adapter);

        vpHeadView=new VpHeadView(this);
        vpHeadView.fillView(vpHeadView.getAdData(), lv);

        currentHeadLayout=new TabHeadView(this);
        currentHeadLayout.fillView("", lv);

        this.setListener();
    }

    private void setListener(){
        lv.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                isScroll = scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE;
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (isScroll) {//没在滚动
                    return;
                }
                //计算轮播headView距离顶部的高度，和自身高度
                if (vpView == null) {
                    vpView = lv.getChildAt(0);
                }
                if (vpView != null) {
                    vpViewTopSpace = DensityUtil.px2dip(MainActivity.this, vpView.getTop());
                    vpViewHeight = DensityUtil.px2dip(MainActivity.this, vpView.getHeight());
                }
                //计算titleView height top
                if (tabView == null) {
                    tabView = lv.getChildAt(1);
                }
                if (tabView != null) {
                    tabViewHeight = DensityUtil.px2dip(MainActivity.this, tabView.getHeight());
                    tabViewTopSpace = DensityUtil.px2dip(MainActivity.this, tabView.getTop());
                }
                /*Log.d("kevin", "visible:" + firstVisibleItem);
                Log.d("kevin", "vpViewHeight:" + vpViewHeight + "--tabTop:" + tabViewTopSpace + "--->height:" + tabViewHeight);*/
                if (tabViewTopSpace > 60) {//标题栏高度
                    isStickyTop = false;
                    headLayout.setVisibility(View.INVISIBLE);
                } else {
                    isStickyTop = true;
                    headLayout.setVisibility(View.VISIBLE);
                }


            }
        });


        this.drawerLayout.addDrawerListener(mDrawerToggle);
        /*this.drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                Log.d("kevin", "slide-drawer");
                mDrawerToggle.onDrawerSlide(drawerView, slideOffset);

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                Log.d("kevin", "Opened-drawer");
                mDrawerToggle.onDrawerOpened(drawerView);

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                Log.d("kevin", "Closed-drawer");
                mDrawerToggle.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                Log.d("kevin", "StateChanged-drawer");
                mDrawerToggle.onDrawerStateChanged(newState);

            }
        });*/
    }

    @OnClick(R.id.but)
    void clickLeftBut(View view){
        Toast.makeText(this,"点击了！",Toast.LENGTH_LONG).show();

    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //箭头转换动画，左边的按钮
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("kevin","onItemSelected-->"+item.getTitle());

        switch(item.getItemId()){
            case R.id.menu_main_about:
                Toast.makeText(this,"显示图片裁剪界面",Toast.LENGTH_SHORT).show();
                //选择图片，或者请求权限
                pickFromGallery();
                return true;
            case android.R.id.home:
                this.drawerLayout.openDrawer(GravityCompat.START);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //创建  actionbar 菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.mune_main, menu);
        return true;
    }


    private void pickFromGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                    "标题",
                    REQUEST_STORAGE_READ_ACCESS_PERMISSION);
        } else {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(Intent.createChooser(intent, "标题"), REQUEST_SELECT_PICTURE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_STORAGE_READ_ACCESS_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickFromGallery();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_SELECT_PICTURE) {
                final Uri selectedUri = data.getData();
                if (selectedUri != null) {
                    startUCrop(this, selectedUri, UCrop.RESULT_ERROR, 16, 9);
                    //startCropActivity(data.getData());
                } else {
                    Toast.makeText(MainActivity.this, "提示！！！！", Toast.LENGTH_SHORT).show();
                }
            } /*else if (requestCode == UCrop.REQUEST_CROP) {
                //handleCropResult(data);
            }*/
        }
        /*if (resultCode == UCrop.RESULT_ERROR) {
            //handleCropError(data);
        }*/
    }

    /**
     * Requests given permission.
     * If the permission has been denied previously, a Dialog will prompt the user to grant the
     * permission, otherwise it is requested directly.
     */
    protected void requestPermission(final String permission, String rationale, final int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            showAlertDialog("标题", rationale,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{permission}, requestCode);
                        }
                    }, "label_ok", null, "label_cancel");
        } else {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        }
    }

    /**
     * This method shows dialog with given title & message.
     * Also there is an option to pass onClickListener for positive & negative button.
     *
     * @param title                         - dialog title
     * @param message                       - dialog message
     * @param onPositiveButtonClickListener - listener for positive button
     * @param positiveText                  - positive button text
     * @param onNegativeButtonClickListener - listener for negative button
     * @param negativeText                  - negative button text
     */
    protected void showAlertDialog(@Nullable String title, @Nullable String message,
                                   @Nullable DialogInterface.OnClickListener onPositiveButtonClickListener,
                                   @NonNull String positiveText,
                                   @Nullable DialogInterface.OnClickListener onNegativeButtonClickListener,
                                   @NonNull String negativeText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveText, onPositiveButtonClickListener);
        builder.setNegativeButton(negativeText, onNegativeButtonClickListener);
        mAlertDialog = builder.show();
    }

    private void startCropActivity(@NonNull Uri uri) {
        String destinationFileName = SAMPLE_CROPPED_IMAGE_NAME+".jpg";
//        switch (mRadioGroupCompressionSettings.getCheckedRadioButtonId()) {
//            case R.id.radio_png:
//                destinationFileName += ".png";
//                break;
//            case R.id.radio_jpeg:
//                destinationFileName += ".jpg";
//                break;
//        }
//
//        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationFileName)));
//
//        uCrop = basisConfig(uCrop);
//        uCrop = advancedConfig(uCrop);
//
//        uCrop.start(this);

    }

    /**
     * 启动裁剪
     * @param activity 上下文
     * @param sourceUri 需要裁剪图片的绝对路径
     * @param requestCode 比如：UCrop.REQUEST_CROP
     * @param aspectRatioX 裁剪图片宽高比
     * @param aspectRatioY 裁剪图片宽高比
     * @return
     */
    public static String startUCrop(Activity activity, Uri sourceUri,
                                    int requestCode, float aspectRatioX, float aspectRatioY) {
        /*Uri sourceUri = Uri.fromFile(new File(sourceFilePath));*/
        File outDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (!outDir.exists()) {
            outDir.mkdirs();
        }
        File outFile = new File(outDir, System.currentTimeMillis() + ".jpg");
        //裁剪后图片的绝对路径
        String cameraScalePath = outFile.getAbsolutePath();
        Uri destinationUri = Uri.fromFile(outFile);
        //初始化，第一个参数：需要裁剪的图片；第二个参数：裁剪后图片
        UCrop uCrop = UCrop.of(sourceUri, destinationUri);
        //初始化UCrop配置
        UCrop.Options options = new UCrop.Options();
        //设置裁剪图片可操作的手势
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        //是否隐藏底部容器，默认显示
        options.setHideBottomControls(false);
        //设置toolbar颜色
        options.setToolbarColor(ActivityCompat.getColor(activity, R.color.divider_1));
        //设置状态栏颜色
        options.setStatusBarColor(ActivityCompat.getColor(activity, R.color.divider_1));
        //是否能调整裁剪框
        options.setFreeStyleCropEnabled(true);
        //UCrop配置
        uCrop.withOptions(options);
        //设置裁剪图片的宽高比，比如16：9
        uCrop.withAspectRatio(aspectRatioX, aspectRatioY);
        //uCrop.useSourceImageAspectRatio();
        //跳转裁剪页面
        uCrop.start(activity, requestCode);
        return cameraScalePath;
    }

/*    *//**
     * In most cases you need only to set crop aspect ration and max size for resulting image.
     *
     * @param uCrop - ucrop builder instance
     * @return - ucrop builder instance
     *//*
    private UCrop basisConfig(@NonNull UCrop uCrop) {
        switch (mRadioGroupAspectRatio.getCheckedRadioButtonId()) {
            case R.id.radio_origin:
                uCrop = uCrop.useSourceImageAspectRatio();
                break;
            case R.id.radio_square:
                uCrop = uCrop.withAspectRatio(1, 1);
                break;
            case R.id.radio_dynamic:
                // do nothing
                break;
            default:
                try {
                    float ratioX = Float.valueOf(mEditTextRatioX.getText().toString().trim());
                    float ratioY = Float.valueOf(mEditTextRatioY.getText().toString().trim());
                    if (ratioX > 0 && ratioY > 0) {
                        uCrop = uCrop.withAspectRatio(ratioX, ratioY);
                    }
                } catch (NumberFormatException e) {
                    Log.i(TAG, String.format("Number please: %s", e.getMessage()));
                }
                break;
        }

        if (mCheckBoxMaxSize.isChecked()) {
            try {
                int maxWidth = Integer.valueOf(mEditTextMaxWidth.getText().toString().trim());
                int maxHeight = Integer.valueOf(mEditTextMaxHeight.getText().toString().trim());
                if (maxWidth > 0 && maxHeight > 0) {
                    uCrop = uCrop.withMaxResultSize(maxWidth, maxHeight);
                }
            } catch (NumberFormatException e) {
                Log.e(TAG, "Number please", e);
            }
        }

        return uCrop;
    }

    *//**
     * Sometimes you want to adjust more options, it's done via {@link com.yalantis.ucrop.UCrop.Options} class.
     *
     * @param uCrop - ucrop builder instance
     * @return - ucrop builder instance
     *//*
    private UCrop advancedConfig(@NonNull UCrop uCrop) {
        UCrop.Options options = new UCrop.Options();

        switch (mRadioGroupCompressionSettings.getCheckedRadioButtonId()) {
            case R.id.radio_png:
                options.setCompressionFormat(Bitmap.CompressFormat.PNG);
                break;
            case R.id.radio_jpeg:
            default:
                options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
                break;
        }
        options.setCompressionQuality(mSeekBarQuality.getProgress());

        options.setHideBottomControls(mCheckBoxHideBottomControls.isChecked());
        options.setFreeStyleCropEnabled(mCheckBoxFreeStyleCrop.isChecked());

        *//*
        If you want to configure how gestures work for all UCropActivity tabs
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        * *//*

        *//*
        This sets max size for bitmap that will be decoded from source Uri.
        More size - more memory allocation, default implementation uses screen diagonal.
        options.setMaxBitmapSize(640);
        * *//*


       *//*
        Tune everything (ﾉ◕ヮ◕)ﾉ*:･ﾟ✧
        options.setMaxScaleMultiplier(5);
        options.setImageToCropBoundsAnimDuration(666);
        options.setDimmedLayerColor(Color.CYAN);
        options.setOvalDimmedLayer(true);
        options.setShowCropFrame(false);
        options.setCropGridStrokeWidth(20);
        options.setCropGridColor(Color.GREEN);
        options.setCropGridColumnCount(2);
        options.setCropGridRowCount(1);
        // Color palette
        options.setToolbarColor(ContextCompat.getColor(this, R.color.your_color_res));
        options.setStatusBarColor(ContextCompat.getColor(this, R.color.your_color_res));
        options.setActiveWidgetColor(ContextCompat.getColor(this, R.color.your_color_res));
		options.setToolbarWidgetColor(ContextCompat.getColor(this, R.color.your_color_res));
       *//*

        return uCrop.withOptions(options);
    }*/
}
