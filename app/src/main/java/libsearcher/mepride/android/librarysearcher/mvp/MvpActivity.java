package libsearcher.mepride.android.librarysearcher.mvp;


import libsearcher.mepride.android.librarysearcher.common.MyActivity;


/**
 * MvpActivity class
 *
 * MVP Activity 基类
 * @author Pride
 * @date 2018/12/31
 */

public abstract class MvpActivity<P extends MvpPresenter> extends MyActivity implements IMvpView {

    private P mPresenter;

    @Override
    public void initActivity() {
        mPresenter = createPresenter();
        mPresenter.attach(this);
        mPresenter.start();
        super.initActivity();
    }

    @Override
    protected void onDestroy() {
        mPresenter.detach();
        super.onDestroy();
    }

    public P getPresenter() {
        return mPresenter;
    }

    protected abstract P createPresenter();
}