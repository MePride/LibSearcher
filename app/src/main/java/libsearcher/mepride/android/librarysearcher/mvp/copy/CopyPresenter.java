package libsearcher.mepride.android.librarysearcher.mvp.copy;



import java.util.List;

import libsearcher.mepride.android.librarysearcher.mvp.MvpPresenter;

/**
 * CopyPresenter class
 *
 * 可进行拷贝的业务处理类
 * @author Pride
 * @date 2018/12/31
 */
public class CopyPresenter extends MvpPresenter<CopyContract.View> implements CopyContract.Presenter {

    private CopyModel mModel;

    @Override
    public void start() {
        mModel = new CopyModel();
    }

    @Override
    public void login(String account, String password) {
        mModel.setAccount(account);
        mModel.setPassword(password);
        mModel.setListener(new CopyOnListener() {

            @Override
            public void onSucceed(List<String> data) {
                getView().loginSuccess(data);
            }

            @Override
            public void onFail(String msg) {
                getView().loginError(msg);
            }
        });
        mModel.login();
    }
}