package libsearcher.mepride.android.librarysearcher.mvp.copy;


import java.util.List;

import libsearcher.mepride.android.librarysearcher.mvp.IMvpView;


/**
 * CopyContract class
 *
 * 可进行拷贝的契约类
 * @author Pride
 * @date 2018/12/31
 */
public class CopyContract {

    public interface View extends IMvpView {

        void loginError(String msg);

        void loginSuccess(List<String> data);
    }

    public interface Presenter {
        void login(String account, String password);
    }
}