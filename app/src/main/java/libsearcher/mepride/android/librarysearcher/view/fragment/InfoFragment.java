package libsearcher.mepride.android.librarysearcher.view.fragment;

import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import libsearcher.mepride.android.librarysearcher.R;
import libsearcher.mepride.android.librarysearcher.common.MyLazyFragment;

public class InfoFragment extends MyLazyFragment {

    @BindView(R.id.tv_detail_title)
    TextView detailTitle;
    @BindView(R.id.tv_detail_press)
    TextView detailPress;
    @BindView(R.id.tv_detail_isbn)
    TextView detailISBN;
    @BindView(R.id.tv_detail_pages)
    TextView detailPages;
    @BindView(R.id.tv_detail_author)
    TextView detailAuthor;

    public static InfoFragment newInstance(String title,String author,String press,String isbn,String pages,String abztract){
        InfoFragment infoFragment = new InfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title",title);
        bundle.putString("author",author);
        bundle.putString("press",press);
        bundle.putString("isbn",isbn);
        bundle.putString("pages",pages);
        bundle.putString("abztract",abztract);
        infoFragment.setArguments(bundle);
        return infoFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_info;
    }

    @Override
    protected int getTitleBarId() {
        return 0;
    }

    @Override
    protected void initView() {
        detailTitle.setText(getArguments().getString("title"));
        detailPress.setText(getArguments().getString("press"));
        detailISBN.setText("ISBN:" + getArguments().getString("isbn"));
        detailPages.setText(getArguments().getString("pages"));
        detailAuthor.setText(getArguments().getString("author"));
    }

    @Override
    protected void initData() {

    }
}
