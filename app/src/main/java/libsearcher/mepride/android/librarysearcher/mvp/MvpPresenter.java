package libsearcher.mepride.android.librarysearcher.mvp;

/**
 * MvpPresenter class
 *
 *  MVP 业务基类
 * @author Pride
 * @date 2018/12/31
 */
public abstract class MvpPresenter<V extends IMvpView> {

    private V mView;

    public void attach(V view){
        mView = view;
    }

    public void detach() {
        mView = null;
    }

    public boolean isAttached() {
        return mView != null;
    }

    public V getView() {
        return mView;
    }

    public abstract void start();
}