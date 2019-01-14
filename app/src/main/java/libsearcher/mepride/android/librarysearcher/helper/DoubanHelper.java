package libsearcher.mepride.android.librarysearcher.helper;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.os.SystemClock.sleep;

/**
 * DoubanHelper class
 * 豆瓣封面获取类
 *
 * @author Pride
 * @date 2018/12/31
 */

public class DoubanHelper{

    private static String API_URL = "https://api.douban.com/v2/book/isbn/";
    private static String IMG_URL;
    public static String getImages(String href){
        ExecutorService ThreadPool = Executors.newFixedThreadPool(3);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient okHttpClient = new OkHttpClient();
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
                                switch (element.getElementsByTag("strong").text()){
                                    case "题名/责任者:":
                                        break;
                                    case "出版发行项:":
                                        break;
                                    case "ISBN及定价:":
                                        String list = element.text();
                                        String isbn = list.substring(list.indexOf(":")+1,list.lastIndexOf("/")).replaceAll("-","").replaceAll("[^(0-9)]","");
                                        getImage(isbn);
                                        break;
                                    case "载体形态项:":
                                        break;
                                    case "提要文摘附注:":
                                        break;
                                    default:break;
                                }
                            }

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        ThreadPool.execute(runnable);
        sleep(500);                                  //延迟主线程，让数据返回

        return IMG_URL;
    }

    private static void getImage(String isbn){
        ExecutorService ThreadPool = Executors.newFixedThreadPool(3);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient okHttpClient = new OkHttpClient();
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
                            if (jsonObject.has("image")) {
                                IMG_URL = jsonObject.get("image").getAsString();
                            }
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
