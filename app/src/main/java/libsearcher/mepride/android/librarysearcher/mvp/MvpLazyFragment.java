package libsearcher.mepride.android.librarysearcher.mvp;


import libsearcher.mepride.android.librarysearcher.common.MyLazyFragment;

/**
 * MvpLazyFragment class
 *
 *  Fragment 懒加载 基类
 * @author Pride
 * @date 2018/12/31
 */
public abstract class MvpLazyFragment<P extends MvpPresenter> extends MyLazyFragment implements IMvpView {

    private P mPresenter;

    @Override
    protected void initFragment() {
        mPresenter = createPresenter();
        mPresenter.attach(this);
        mPresenter.start();
        super.initFragment();
    }

    @Override
    public void onDestroy() {
        mPresenter.detach();
        super.onDestroy();
    }

    public P getPresenter() {
        return mPresenter;
    }

    protected abstract P createPresenter();
}