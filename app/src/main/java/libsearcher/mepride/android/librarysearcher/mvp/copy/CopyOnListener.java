package libsearcher.mepride.android.librarysearcher.mvp.copy;

import java.util.List;

/**
 * CopyOnListener class
 *
 * 可进行拷贝的监听器
 * @author Pride
 * @date 2018/12/31
 */
public interface CopyOnListener {

    void onSucceed(List<String> data);

    void onFail(String msg);
}