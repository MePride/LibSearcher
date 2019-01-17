package libsearcher.mepride.android.librarysearcher.view.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.litepal.LitePal;

import java.util.List;

import butterknife.BindView;
import libsearcher.mepride.android.librarysearcher.R;
import libsearcher.mepride.android.librarysearcher.common.MyLazyFragment;
import libsearcher.mepride.android.librarysearcher.model.Book;
import libsearcher.mepride.android.librarysearcher.view.activity.DetailActivity;
import libsearcher.mepride.android.librarysearcher.view.adapter.BookGridAdapter;

public class ShelfFragment extends MyLazyFragment
        implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener{

    @BindView(R.id.gridView)
    GridView gridView;

    @BindView(R.id.refreshAll)
    SwipeRefreshLayout swipeRefreshLayout;

    BookGridAdapter bookGridAdapter;

    public static ShelfFragment newInstance(){
        return new ShelfFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_shelf;
    }

    @Override
    protected int getTitleBarId() {
        return 0;
    }

    @Override
    protected void initView() {
        bookGridAdapter = new BookGridAdapter(getContext());
        bookGridAdapter.notifyDataSetChanged();
        gridView.setAdapter(bookGridAdapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
                bookGridAdapter.notifyDataSetChanged();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initData();
                        bookGridAdapter.notifyDataSetChanged();
                        // 加载完数据设置为不刷新状态，将下拉进度收起来
                        swipeRefreshLayout.setRefreshing(false);
                    }

                }, 500);
            }
        });

        // ItemClickListener
        gridView.setOnItemClickListener(this);
        gridView.setOnItemLongClickListener(this);

    }

    @Override
    protected void initData() {
        LitePal.initialize(getContext());
        List<Book> books = LitePal.findAll(Book.class);
        bookGridAdapter.setData(books);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        LitePal.initialize(getContext());
        List<Book> books = LitePal.findAll(Book.class);
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra("href", books.get(position).getHref());
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("是否删除这本书?");
        dialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LitePal.delete(Book.class,position+1);
                        initData();
                        bookGridAdapter.notifyDataSetChanged();
                    }
                });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.show();
        return true;
    }
}
