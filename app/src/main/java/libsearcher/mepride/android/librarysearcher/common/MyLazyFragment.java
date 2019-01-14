package libsearcher.mepride.android.librarysearcher.common;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * MyLazyFragment class
 *
 * 项目中 Fragment 懒加载基类
 * @author Pride
 * @date 2018/12/31
 */
public abstract class  MyLazyFragment extends UILazyFragment {

    private Unbinder mButterKnife;// View注解

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

          mButterKnife = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 友盟统计
//        UmengHelper.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        // 友盟统计
//        UmengHelper.onPause(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mButterKnife.unbind();
    }

    /**
     * 显示吐司
     */
//    public void toast(CharSequence s) {
//        ToastUtils.show(s);
//    }
//
//    public void toast(int id) {
//        ToastUtils.show(id);
//    }
//
//    public void toast(Object object) {
//        ToastUtils.show(object);
//    }
}