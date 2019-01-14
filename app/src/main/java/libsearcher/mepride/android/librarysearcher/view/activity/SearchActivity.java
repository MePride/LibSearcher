package libsearcher.mepride.android.librarysearcher.view.activity;

import android.os.Looper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import libsearcher.mepride.android.librarysearcher.R;
import libsearcher.mepride.android.librarysearcher.common.MyActivity;
import libsearcher.mepride.android.librarysearcher.helper.KeyboardUtils;
import libsearcher.mepride.android.librarysearcher.model.Book;
import libsearcher.mepride.android.librarysearcher.model.BookService;
import libsearcher.mepride.android.librarysearcher.view.adapter.SearchAdapter;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchActivity extends MyActivity
        implements View.OnClickListener,View.OnKeyListener{

    @BindView(R.id.btn_search1)
    Button mSearchButton;
    @BindView(R.id.et_search)
    EditText mSearchView;
    @BindView(R.id.rec_book)
    RecyclerView mBookRecyclerview;
    @BindView(R.id.search_progressBar)
    ProgressBar progressBar;

    int pageNumber=0;
    int bookNumber=0;
    private List<Book> bookArrayList = new ArrayList<>();
    private SearchAdapter searchAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected int getTitleBarId() {
        return 0;
    }

    @Override
    protected void initView() {
        mSearchButton.setOnClickListener(this);
        searchAdapter = new SearchAdapter(bookArrayList);
        mBookRecyclerview.setAdapter(searchAdapter);
        mBookRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mSearchView.setOnKeyListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_search1:
                bookArrayList.clear();
                progressBar.setVisibility(View.VISIBLE);
                System.out.println("Clicked");
                KeyboardUtils.hideKeyboard(v);
                Search();
                break;
                default:break;
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
            //先隐藏键盘
            ((InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(getActivity().getCurrentFocus()
                            .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            bookArrayList.clear();
            progressBar.setVisibility(View.VISIBLE);
            KeyboardUtils.hideKeyboard(v);
            Search();
        }
        return false;
    }

    public void Search(){
        if (mSearchView.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "请输入要搜索的内容", Toast.LENGTH_SHORT).show();
        }else {
            String baseUrl = "http://211.86.140.145:8080/";
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .build();
            BookService service = retrofit.create(BookService.class);
            Call<ResponseBody> call = service.getBook(mSearchView.getText().toString(),5,"title",1);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try{
                        Document document = Jsoup.parse(response.body().string());
                        Elements hrefEle = document.getElementsByClass("searchList");
                        //正则表达式提取数字
                        //获取书的总数
                        Elements bookNum = document.getElementsByClass("tips searchCount");
                        Pattern pattern = Pattern.compile("[^0-9]");
                        Matcher matcher = pattern.matcher(bookNum.toString());
                        bookNumber = Integer.parseInt(matcher.replaceAll(""));
                        pageNumber = Integer.parseInt(matcher.replaceAll(""))/20 + 1;
                        if(hrefEle.text().isEmpty()) {
                            return;
                        }else{
                            for (int i=1;i<=pageNumber;i++){
                                Call<ResponseBody> responseBodyCall = service.getBook(mSearchView.getText().toString(),5,"title",i);
                                responseBodyCall.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        try {
                                            Document document = Jsoup.parse(response.body().string());
                                            Elements hrefEle = document.getElementsByClass("searchList");
                                            Document book = Jsoup.parse(hrefEle.toString().replaceAll("<font color=\"red\">", "").replaceAll("</font>", "").replaceAll("\n</ul>", "").replaceAll("<ul class=\"searchList\"> \n", ""));
                                            Elements bookInfo = book.getElementsByTag("li");
                                            for (Element element : bookInfo) {
                                                Elements bookList = element.getElementsByTag("a");
                                                for (Element element1 : bookList) {
                                                    String title = element1.getElementsByAttributeValue("class", "name").text();
                                                    String info = element1.getElementsByAttributeValue("class", "info").text().replaceAll("&nbsp;", "\n").replaceAll("馆藏信息：", "\n");
                                                    String href = element1.attr("href");
                                                    bookArrayList.add(new Book(title,info,href));
                                                    new Thread(){
                                                        @Override
                                                        public void run() {
                                                            try {
                                                                //你的处理逻辑,这里简单睡眠一秒
                                                                sleep(500);
                                                                getActivity().runOnUiThread(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        progressBar.setVisibility(View.GONE);
                                                                        searchAdapter.notifyDataSetChanged();
                                                                    }
                                                                });
                                                            } catch (InterruptedException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    }.start();
                                                }
                                            }
//                                            Looper.prepare();
//                                            Toast.makeText(SearchActivity.this,"一共找到"+bookNumber+"本馆藏资源",Toast.LENGTH_SHORT).show();
//                                            Looper.loop();
                                        }catch (IOException e){
                                            e.printStackTrace();
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                    }
                                });
                            }
                        }
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

}
