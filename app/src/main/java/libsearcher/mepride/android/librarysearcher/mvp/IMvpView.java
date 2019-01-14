package libsearcher.mepride.android.librarysearcher.mvp;


/**
 * IMvpView class
 *
 * MVP 通用性接口
 * @author Pride
 * @date 2018/12/31
 */
public interface IMvpView {

    /**
     * 用于页面请求数据时显示加载状态
     */
    void showLoading();

    /**
     * 用于请求数据完成
     */
    void loadingComplete();

    /**
     * 用于请求的数据为空的状态
     */
    void showEmpty();

    /**
     * 用于请求数据出错
     */
    void showError();
}