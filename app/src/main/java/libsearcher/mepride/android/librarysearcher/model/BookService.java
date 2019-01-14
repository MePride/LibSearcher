package libsearcher.mepride.android.librarysearcher.model;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * BookService class
 *
 * Book请求接口类
 * @author Pride
 * @date 2018/12/31
 */
public interface BookService {
    @GET("search")
    Call<ResponseBody> getBook(@Query("kw") String title,
                               @Query("xc") int style,
                               @Query("searchtype") String type,
                               @Query("page") int page);
}
