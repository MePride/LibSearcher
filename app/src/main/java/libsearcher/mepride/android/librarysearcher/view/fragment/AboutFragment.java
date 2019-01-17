package libsearcher.mepride.android.librarysearcher.view.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import libsearcher.mepride.android.librarysearcher.R;
import libsearcher.mepride.android.librarysearcher.common.MyLazyFragment;
import libsearcher.mepride.android.librarysearcher.view.activity.QuestionActivity;

public class AboutFragment extends MyLazyFragment implements View.OnClickListener{

    @BindView(R.id.about)
    ImageView about;

    public static AboutFragment newInstance(){
        return new AboutFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_about;
    }

    @Override
    protected int getTitleBarId() {
        return 0;
    }

    @Override
    protected void initView() {
        about.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.about:
                startActivity(QuestionActivity.class);
                break;
                default:break;
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return super.isStatusBarEnabled();
    }
}
