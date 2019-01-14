package libsearcher.mepride.android.librarysearcher.view.fragment;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import libsearcher.mepride.android.librarysearcher.R;
import libsearcher.mepride.android.librarysearcher.common.MyLazyFragment;
import libsearcher.mepride.android.librarysearcher.helper.DoubanHelper;
import libsearcher.mepride.android.librarysearcher.view.activity.DetailActivity;
import libsearcher.mepride.android.librarysearcher.view.activity.SearchActivity;
import libsearcher.mepride.android.librarysearcher.view.adapter.WebBannerAdapter;
import libsearcher.mepride.android.librarysearcher.widget.BannerLayout;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchFragment extends MyLazyFragment
        implements View.OnClickListener, BannerLayout.OnBannerItemClickListener{

    @BindView(R.id.progressBar_hot)
    ProgressBar progressBar;
    @BindView(R.id.abl_test_bar)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.tv_test_address)
    TextView mAddressView;
    @BindView(R.id.tv_test_search)
    TextView mSearchView;
    @BindView(R.id.bander)
    BannerLayout bannerLayout;
    @BindView(R.id.tv_hot)
    TextView textView;
    OkHttpClient okHttpClient = new OkHttpClient();
    private WebBannerAdapter webBannerAdapter;

    List<String> hrefs = new ArrayList<>();
    List<String> images = new ArrayList<>();
    private static String URL = "http://211.86.140.145:8080";
    public static SearchFragment newInstance(){
        return new SearchFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search;
    }

    @Override
    protected int getTitleBarId() {
        return 0;
    }

    @Override
    protected void initView() {
        getHot();
        mSearchView.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public boolean isStatusBarEnabled() {
        return super.isStatusBarEnabled();
    }


    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.tv_test_search:
                startActivity(SearchActivity.class);
                break;
                default:break;
        }
    }


    @Override
    public void onItemClick(int position) {

    }

    private void getHot(){
        final int i=0;
        ExecutorService ThreadPool = Executors.newFixedThreadPool(3);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try{
                    Request request = new Request.Builder()
                            .url("http://211.86.140.145:8080/sms/opac/news/showNewsList.action?type=3&xc=5&pageSize=10")
                            .build();
                    Call call = okHttpClient.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            Document document = Jsoup.parse(response.body().string());
                            Element hots1 = document.getElementsByClass("boxBd").first();
                            Elements hots = hots1.getElementsByTag("a");
                            for (Element element:hots){
                                hrefs.add(URL + element.attr("href"));
                            }
                            for (int i=0; i<hrefs.size();i++){
                                images.add(DoubanHelper.getImages(hrefs.get(i)));
                            }
                            images.remove(0);
                            new Thread(){
                                @Override
                                public void run() {
                                    try {
                                        //你的处理逻辑,这里简单睡眠一秒
                                        sleep(1500);
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                webBannerAdapter=new WebBannerAdapter(getContext(),images);
                                                webBannerAdapter.setOnBannerItemClickListener(new BannerLayout.OnBannerItemClickListener() {
                                                    @Override
                                                    public void onItemClick(int position) {
                                                        Intent intent = new Intent(getContext(), DetailActivity.class);
                                                        intent.putExtra("href",hrefs.get(position));
                                                        startActivity(intent);
                                                    }
                                                });
                                                progressBar.setVisibility(View.GONE);
                                                textView.setVisibility(View.GONE);
                                                bannerLayout.setAdapter(webBannerAdapter);
                                                webBannerAdapter.notifyDataSetChanged();

                                            }
                                        });
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }.start();
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        ThreadPool.execute(runnable);
    }

}
