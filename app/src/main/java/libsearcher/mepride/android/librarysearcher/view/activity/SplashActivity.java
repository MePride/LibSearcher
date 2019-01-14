package libsearcher.mepride.android.librarysearcher.view.activity;

import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import com.gyf.barlibrary.BarHide;
import butterknife.BindView;
import libsearcher.mepride.android.librarysearcher.R;
import libsearcher.mepride.android.librarysearcher.common.MyActivity;

public class SplashActivity extends MyActivity{


    @BindView(R.id.iv_launcher_icon)
    View mIconView;

    Handler handler = new Handler();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected int getTitleBarId() {
        return 0;
    }

    @Override
    protected void initView() {
        //设置状态栏和导航栏参数
        getStatusBarConfig()
                .fullScreen(true)//有导航栏的情况下，activity全屏显示，也就是activity最下面被导航栏覆盖，不写默认非全屏
                .hideBar(BarHide.FLAG_HIDE_STATUS_BAR)//隐藏状态栏
                .transparentNavigationBar()//透明导航栏，不写默认黑色(设置此方法，fullScreen()方法自动为true)
                .init();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(MainActivity.class);
                finish();
            }
        },500);
    }

    @Override
    protected void initData() {}

    @Override
    public void onBackPressed() {
        //禁用返回键
        //super.onBackPressed();
    }


    @Override
    public boolean isSupportSwipeBack() {
        //不使用侧滑功能
        return !super.isSupportSwipeBack();
    }

}
