package libsearcher.mepride.android.librarysearcher.view.activity;

import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.BindView;
import libsearcher.mepride.android.librarysearcher.R;
import libsearcher.mepride.android.librarysearcher.common.MyActivity;

public class QuestionActivity extends MyActivity {

    @BindView(R.id.include1)
    Toolbar toolbar;
    @Override
    protected int getTitleBarId() {
        return 0;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_question;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        toolbar.setTitle("问答");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
