package libsearcher.mepride.android.librarysearcher.view.fragment;

import android.os.Bundle;
import android.widget.TextView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import libsearcher.mepride.android.librarysearcher.R;
import libsearcher.mepride.android.librarysearcher.common.MyLazyFragment;

public class IntroFragment extends MyLazyFragment {

    @BindView(R.id.tv_author_intor)
    TextView authorIntor;
    @BindView(R.id.tv_book_intro)
    TextView bookIntor;


    public static IntroFragment newInstance(String author_intro,String book_intro){
        IntroFragment introFragment = new IntroFragment();
        Bundle bundle = new Bundle();
        bundle.putString("author_intro",author_intro);
        bundle.putString("book_intro",book_intro);
        introFragment.setArguments(bundle);
        return introFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_intro;
    }

    @Override
    protected int getTitleBarId() {
        return 0;
    }

    @Override
    protected void initView() {
        authorIntor.setText(getArguments().getString("author_intro"));
        bookIntor.setText(getArguments().getString("book_intro"));
    }

    @Override
    protected void initData() {

    }
}
