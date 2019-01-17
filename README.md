# LibSearcher

学校有个写读后感换学分的政策，于是有时候到了借书的高峰期就可以看到在查询机前排到门口的情况.....然后就有了这个，馆藏数据来自[超星馆藏](http://m.5read.com)，封面及作者信息来自豆瓣图书api(https://api.douban.com/v2/book/isbn/)。

**实现思路：**

1. *通过OkHttp/Retrofit请求查询获取response后通过Jsoup进行解析，将图书条目载入RecyclerView中。*
2. *点击对应RecyclerView取除Href通过Jsoup对目标地址进行解析，正则出ISBN以及相关信息。*
3. *通过ISBN在豆瓣图书进行详细信息查询，用Gson对Json数据进行解析，后载入ImageView、TextView。*

[![img_0991.png](https://i.loli.net/2019/01/17/5c403ed45c273.png)](https://i.loli.net/2019/01/17/5c403ed45c273.png)


  

**请求地址：**http://url/search?kw=title&xc=5&searchtype=title

**工程结构如下：**![TIM截图20190117031259](https://i.loli.net/2019/01/17/5c402e836b1bc.png)

**请求到的数据如下：**

如此即可通过Jsoup快速获取class为searchList，后通过foreach单独获取出标签<li>内容，单独解析出书的item和其目标地址用List<>储存后载入RecyclerView。

**定义Retrofit接口**

```java
public interface BookService {
    @GET("search")
    Call<ResponseBody> getBook(@Query("kw") String title,
                               @Query("xc") int style,
                               @Query("searchtype") String type,
                               @Query("page") int page);
}
```

**Activity中搜索方法**

```java
    public void Search(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UrlModel.ChaoXing)
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
                        //书总数/20+1
                        if(hrefEle.text().isEmpty()) {
                            return;
                            //这里以后可能会做修改，在遍历到数据为空时返回，后有两种遍历实现方式
                            //1.获取书总目后获得总的遍历页数，然后再从头遍历。
                            //2.获取当前页，如果为空，停止，如果不空继续进行请求。
                        }else{
                            for (int i=1;i<=pageNumber;i++){
                                Call<ResponseBody> responseBodyCall = service.getBook(mSearchView.getText().toString(),5,type,i);
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
                                                            } catch (InterruptedException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    }.start();
                                                }
                                            }
                                        }catch (IOException e){
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        }
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            });
    }

```

**Adapter中点击事件**

```java
 holder.itemView.setOnClickListener(new View.OnClickListener() {//点击对应item产生的事件
	@Override
	public void onClick(View v) {
		final Book books = mBooksList.get(holder.getAdapterPosition());
		Intent intent = new Intent(mContext, DetailActivity.class);
		intent.putExtra("href", UrlModel.ChaoXing + books.getHref());
		mContext.startActivity(intent);
	}
 });
```

**在DetailActivity中进行href的接收和解析**

```java
    protected void initData() {
        Intent intent = getIntent();
        href = intent.getStringExtra("href");							//接收href
        ExecutorService ThreadPool = Executors.newFixedThreadPool(3);	  //定义线程池
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
                            //获取相关信息
                            Elements sheets = document.getElementsByClass("sheet");
                            //获取借阅详情
                            for (Element element : detailInfo) {
                                //通过switch-case对Tag进行遍历
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
                                        isbnNum = isbn.substring(isbn.indexOf(":")+1,isbn.lastIndexOf("/")).replaceAll("-","").replaceAll("[^(0-9)]","");	//通过正则表达式过滤中文和字符
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
                                                getImage(isbnNum);		//获取图片及详细信息
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
```

**获取封面及图书详细信息**

```java
    private void getImage(String isbn){
        ExecutorService ThreadPool = Executors.newFixedThreadPool(3);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try{
                    Request request = new Request.Builder()	//通过豆瓣图书api进行请求
                            .url(UrlModel.DoubanApi+isbn)
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
                                    if (jsonObject.has("image")) {	//判断是否含有image
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
```

**加入书架（使用litepal进行数据持久化）**

```java
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
//点击事件中，建立book对象
book = new Book();
book.setHref(href);
book.setIsbn(isbn);
book.setTitle(title);
book.setPages(pages);
book.setImage(image);
book.setAverage(average);
saveBook(book);
```

**轮播图的实现(不包括轮播图控件与适配器，此部分自己阅读代码)**

```java
//这里获取十本热门搜索图书的Href,传给DoubanHelper类进行获取ISBN/ImageUrl  
Request request = new Request.Builder()
                            .url(UrlModel.Hot)
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
                            Map map = new HashMap();
                            map=DoubanHelper.getImages(hrefs);
                            images = (List<String>) map.get("images");
                            hrefs = (List<String>) map.get("hrefs");
                            }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        ThreadPool.execute(runnable);
```

```java
    private static int i;
    private static List<String> IMG_URL = new ArrayList<>();
    private static List<String> HREFS = new ArrayList<>();
    public static Map getImages(List<String> hrefs) {
        ExecutorService ThreadPool = Executors.newFixedThreadPool(10);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    for (i = 0; i < hrefs.size(); i++) {
                        String href = hrefs.get(i);
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
                                String infos = response.body().string();
                                Document document = Jsoup.parse(infos);
                                Elements detailInfo = document.getElementsByClass("detailList");
                                for (Element element : detailInfo) {
                                    switch (element.getElementsByTag("strong").text()) {
                                        case "ISBN及定价:":
                                            String list = element.text();
                                            String isbn = list.substring(list.indexOf(":") + 1, list.lastIndexOf("/")).replaceAll("-", "").replaceAll("[^(0-9)]", "");//正则出ISBN
                                            getImage(isbn,href);//获取图片
                                            break;
                                            default: break;
                                    }
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        ThreadPool.execute(runnable);
        sleep(500);//延迟主线程，让数据返回
        Map map = new HashMap();	//通过map返回
        map.put("images",IMG_URL);
        map.put("hrefs",HREFS);
        return map;
    }

    private static void getImage(String isbn,String href) {
        ExecutorService ThreadPool = Executors.newFixedThreadPool(10);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(UrlModel.DoubanApi + isbn)
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
                            if (jsonObject.has("image")) {
                                IMG_URL.add(jsonObject.get("image").getAsString());
                                HREFS.add(href);
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        ThreadPool.execute(runnable);
    }
```

后可载入轮播图控件中刷新适配器。

在最近的一次更新中，我去掉了搜索按钮替换为输入法键盘上的搜素，搜索按钮位置改为Spinner调整搜索的类型。

去掉了关于页面的Toolbar...目前学校的书应该正在清点吧，是获取不到详细信息的。

![AFFIX_20190117_020449](https://i.loli.net/2019/01/17/5c402ea1cd2c1.png)

![AFFIX_20190117_020520](https://i.loli.net/2019/01/17/5c402eb7e61b4.png)