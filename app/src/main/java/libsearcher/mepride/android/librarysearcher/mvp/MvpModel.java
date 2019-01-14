package libsearcher.mepride.android.librarysearcher.mvp;

/**
 * MvpModel class
 *
 *   MVP 模型基类
 * @author Pride
 * @date 2018/12/31
 */
public abstract class MvpModel<L> {

    private L mListener;

    public void setListener(L l) {
        mListener = l;
    }

    public L getListener() {
        return mListener;
    }
}