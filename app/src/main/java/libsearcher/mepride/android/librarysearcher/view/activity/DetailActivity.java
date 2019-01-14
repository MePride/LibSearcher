package libsearcher.mepride.android.librarysearcher.view.activity;

import android.content.Intent;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.litepal.LitePal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import libsearcher.mepride.android.librarysearcher.R;
import libsearcher.mepride.android.librarysearcher.common.MyActivity;
import libsearcher.mepride.android.librarysearcher.model.Book;
import libsearcher.mepride.android.librarysearcher.model.Situation;
import libsearcher.mepride.android.librarysearcher.view.fragment.InfoFragment;
import libsearcher.mepride.android.librarysearcher.view.fragment.IntroFragment;
import libsearcher.mepride.android.librarysearcher.view.fragment.StateFragment;
import libsearcher.mepride.android.librarysearcher.widget.ViewPagerIndicator;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DetailActivity extends MyActivity 
        implements View.OnClickListener{

    private String title;       //题名
    private String press;       //出版社
    private String isbn;        //isbn
    private String pages;       //页数
    private String abztract;    //摘要
    private String author;      //作者

    private String bookNumber;      //书号
    private String barcode;         //条码
    private String year;            //年卷期
    private String collection;      //馆藏地址
    private String state;           //书刊状态
    private String average;         //评分

    private Book book;
    private String isbnNum;
    private static String API_URL = "https://api.douban.com/v2/book/isbn/";
    private List<String> titles = Arrays.asList("基本信息", "图书简介", "借阅情况");
    private List<Fragment> fragments = new ArrayList<>();
    private List<Situation> situationList = new ArrayList<>();
    private FragmentPagerAdapter pagerAdapter;
    private String href;
    private String image;
    OkHttpClient okHttpClient = new OkHttpClient();

    @BindView(R.id.iv_book)
    ImageView ivBook;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.indicator)
    ViewPagerIndicator viewPagerIndicator;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.fl_add)
    FloatingActionButton add;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_detail;
    }

    @Override
    protected int getTitleBarId() {
        return 0;
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        href = intent.getStringExtra("href");
        ExecutorService ThreadPool = Executors.newFixedThreadPool(3);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Looper.prepare();
                    Request request = new Request.Builder()
                            .url(href)
                            .build();
                    Call call = okHttpClient.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            Document document = Jsoup.parse(response.body().string());
                            Elements detailInfo = document.getElementsByClass("detailList");
                            Elements sheets = document.getElementsByClass("sheet");
                            for (Element element : detailInfo) {
                                switch (element.getElementsByTag("strong").text()){
                                    case "题名/责任者:":
                                        String article = element.text();
                                        title = article.substring(article.indexOf(":")+1,article.indexOf("/",7));
                                        author = article.substring(article.indexOf("/",7)+1,article.length());
                                        System.out.println(title);
                                        System.out.println(author);
                                        break;
                                    case "出版发行项:":
                                        press = element.text();
                                        break;
                                    case "ISBN及定价:":
                                        isbn = element.text();
                                        isbnNum = isbn.substring(isbn.indexOf(":")+1,isbn.lastIndexOf("/")).replaceAll("-","").replaceAll("[^(0-9)]","");
                                        break;
                                    case "载体形态项:":
                                        pages = element.text();
                                        break;
                                    case "提要文摘附注:":
                                        abztract = element.text();
                                        break;
                                        default:break;
                                }
                            }

                            for (Element element1:sheets){
                                bookNumber=element1.getElementsByTag("tr").first().getElementsByTag("td").first().text();
                                barcode=element1.getElementsByTag("tr").first().nextElementSibling().getElementsByTag("td").text();
                                year=element1.getElementsByTag("tr").first().nextElementSibling().nextElementSibling().getElementsByTag("td").text();
                                collection=element1.getElementsByTag("tr").last().lastElementSibling().getElementsByTag("td").text();
                                state=element1.getElementsByTag("tr").last().getElementsByTag("td").text();
                                situationList.add(new Situation( bookNumber, barcode, year, collection, state));
                            }
                            new Thread(){
                                @Override
                                public void run() {
                                    try {
                                        //你的处理逻辑,这里简单睡眠一秒
                                        sleep(500);
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                getImage(isbnNum);
                                            }
                                        });
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }.start();
                        }
                    });
                    Looper.loop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        ThreadPool.execute(runnable);


    }

    private void getImage(String isbn){
        ExecutorService ThreadPool = Executors.newFixedThreadPool(3);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try{
                    Request request = new Request.Builder()
                            .url(API_URL+isbn)
                            .build();
                    Call call = okHttpClient.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String infos = response.body().string();
                            JsonObject jsonObject = (JsonObject) new JsonParser().parse(infos);
                            JsonObject jsonObject1 = jsonObject.getAsJsonObject("rating");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setVisibility(View.GONE);
                                    if (jsonObject.has("image")) {
                                        image = jsonObject.get("image").getAsString();
                                        average = jsonObject1.get("average").getAsString();
                                        Glide.with(DetailActivity.this)
                                                .load(image)
                                                .into(ivBook);
                                        //fragment添加
                                        fragments.add(InfoFragment.newInstance(title,author,press,isbn,pages,abztract));
                                        fragments.add(IntroFragment.newInstance(jsonObject.get("author_intro").getAsString(),jsonObject.get("summary").getAsString()));
                                        fragments.add(StateFragment.newInstance(situationList));
                                        viewPagerIndicator.setTabItemTitles(titles);
                                        viewPagerIndicator.setVisibleTabCount(3);
                                        viewPagerIndicator.setViewPager(viewPager,0);
                                        pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
                                            @Override
                                            public Fragment getItem(int i) {
                                                return fragments.get(i);
                                            }

                                            @Override
                                            public int getCount() {
                                                return fragments.size();
                                            }
                                        };
                                        viewPager.setAdapter(pagerAdapter);
                                    }else{
                                        progressBar.setVisibility(View.GONE);;
                                        fragments.add(InfoFragment.newInstance(title,author,press,isbn,pages,abztract));
                                        fragments.add(IntroFragment.newInstance("",""));
                                        fragments.add(StateFragment.newInstance(situationList));
                                        viewPagerIndicator.setTabItemTitles(titles);
                                        viewPagerIndicator.setVisibleTabCount(3);
                                        viewPagerIndicator.setViewPager(viewPager,0);
                                        pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
                                            @Override
                                            public Fragment getItem(int i) {
                                                return fragments.get(i);
                                            }

                                            @Override
                                            public int getCount() {
                                                return fragments.size();
                                            }
                                        };
                                        viewPager.setAdapter(pagerAdapter);
                                    }
                                }
                            });
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        ThreadPool.execute(runnable);
    }

    @Override
    protected void initView() {
        add.setOnClickListener(this);
    }

    private void saveBook(Book book){
        Boolean isAdded = false;
        List<Book> books = LitePal.findAll(Book.class);
        for (int i=0;i < books.size();i++){
            Book book_db = books.get(i);
            if ((book_db.getIsbn()).equals(book.getIsbn())) {
                isAdded = true;
                break;
            } else {
                isAdded = false;
            }
        }
        if (isAdded) {
            Toast.makeText(getContext(), "你已经添加过了", Toast.LENGTH_SHORT).show();
        } else {
            if (book.save()) {
                Toast.makeText(getContext(), "保存成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "保存失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fl_add:
                book = new Book();
                book.setHref(href);
                book.setIsbn(isbn);
                book.setTitle(title);
                book.setPages(pages);
                book.setImage(image);
                book.setAverage(average);
                saveBook(book);
                break;
                default:break;
        }
    }
}
