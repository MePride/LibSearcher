package libsearcher.mepride.android.librarysearcher.helper;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import libsearcher.mepride.android.librarysearcher.model.UrlModel;
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

public class DoubanHelper {

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
                                            String isbn = list.substring(list.indexOf(":") + 1, list.lastIndexOf("/")).replaceAll("-", "").replaceAll("[^(0-9)]", "");
                                            getImage(isbn,href);
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
        Map map = new HashMap();
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
}