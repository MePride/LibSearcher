package libsearcher.mepride.android.librarysearcher.mvp.copy;

import android.view.View;


import java.util.List;

import libsearcher.mepride.android.librarysearcher.mvp.MvpActivity;

/**
 * CopyMvpActivity class
 *
 * 拷贝的MVP Activity 类
 * @author Pride
 * @date 2018/12/31
 */
public class CopyMvpActivity extends MvpActivity<CopyPresenter> implements CopyContract.View {

    @Override
    protected CopyPresenter createPresenter() {
        return new CopyPresenter();
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected int getTitleBarId() {
        return 0;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    public void onLogin(View view) {
        // 登录操作
        getPresenter().login("账户", "密码");
    }

    /**
     * {@link CopyContract.View}
     */

    @Override
    public void loginError(String msg) {
        toast(msg);
    }

    @Override
    public void loginSuccess(List<String> data) {
        toast("登录成功了");
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void loadingComplete() {

    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void showError() {

    }
}