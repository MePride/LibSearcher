package libsearcher.mepride.android.librarysearcher.view.fragment;

import libsearcher.mepride.android.librarysearcher.R;
import libsearcher.mepride.android.librarysearcher.common.MyLazyFragment;

public class AboutFragment extends MyLazyFragment {

    public static AboutFragment newInstance(){
        return new AboutFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_about;
    }

    @Override
    protected int getTitleBarId() {
        return R.id.tb_test_d_title;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return super.isStatusBarEnabled();
    }
}
