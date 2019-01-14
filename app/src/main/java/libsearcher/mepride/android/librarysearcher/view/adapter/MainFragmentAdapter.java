package libsearcher.mepride.android.librarysearcher.view.adapter;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import java.util.List;

import day.in.count.mepride.android.base.BaseFragmentAdapter;
import libsearcher.mepride.android.librarysearcher.common.MyLazyFragment;
import libsearcher.mepride.android.librarysearcher.view.fragment.AboutFragment;
import libsearcher.mepride.android.librarysearcher.view.fragment.SearchFragment;
import libsearcher.mepride.android.librarysearcher.view.fragment.ShelfFragment;

public final class MainFragmentAdapter extends BaseFragmentAdapter<MyLazyFragment> {

    public MainFragmentAdapter(FragmentActivity activity) {
        super(activity);
    }

    @Override
    protected void init(FragmentManager manager, List<MyLazyFragment> list) {
        list.add(SearchFragment.newInstance());
        list.add(ShelfFragment.newInstance());
        list.add(AboutFragment.newInstance());
    }
}