package gavin.sensual.net;

import gavin.sensual.app.daily.Daily;
import gavin.sensual.app.daily.News;
import gavin.sensual.app.gank.Result;
import gavin.sensual.app.setting.Version;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * ClientAPI
 *
 * @author gavin.xiong 2016/12/9
 */
public interface ClientAPI {


    /* **************************************************************************** *
     * *********************************** 知乎日报 ******************************** *
     * **************************************************************************** */


    /**
     * 获取今日日报新闻列表 ( 最长缓存一天 60 * 60 * 24 )
     *
     * @return Daily
     */
    // 指定返回复用时间为 60s
    @Headers("Cache-Control: max-stale=60")
    @GET("news/latest")
    Observable<Daily> getDaily();

    /**
     * 获取新闻
     *
     * @param newsId long
     * @return News
     */
    @Headers("Cache-Control: max-stale=3600")
    @GET("news/{newsId}")
    Observable<News> getNews(@Path("newsId") long newsId);

    /**
     * 获取往期日报
     *
     * @param date yyyyMMdd
     * @return Daily
     */
    @Headers("Cache-Control: max-stale=86400")
    @GET("news/before/{date}")
    Observable<Daily> getDailyBefore(@Path("date") String date);


    /* **************************************************************************** *
     * *********************************** 干货集中营福利 *************************** *
     * **************************************************************************** */


    /**
     * 获取福利
     *
     * @param limit 分页大小
     * @param no    页码
     * @return Result
     */
    @Headers("Cache-Control: max-stale=1800")
    @GET("http://gank.io/api/data/福利/{limit}/{no}")
    Observable<Result> getGankImage(@Path("limit") int limit, @Path("no") int no);


    /* **************************************************************************** *
     * *********************************** 豆瓣妹子 ******************************** *
     * **************************************************************************** */


    @Headers("Cache-Control: max-stale=1800")
    @GET("http://www.dbmeinv.com/dbgroup/rank.htm")
    Observable<ResponseBody> getDoubanRank(@Query("pager_offset") int page);

    @Headers("Cache-Control: max-stale=1800")
    @GET("http://www.dbmeinv.com/dbgroup/show.htm")
    Observable<ResponseBody> getDoubanShow(@Query("cid") String type, @Query("pager_offset") int page);


    /* **************************************************************************** *
     * *********************************** mzitu ********************************** *
     * **************************************************************************** */


    @Headers("Cache-Control: max-stale=1800")
    @GET("http://www.mzitu.com/zipai/{offset}/")
    Observable<ResponseBody> getMzituZipai(@Path("offset") String offset);

    @Headers("Cache-Control: max-stale=1800")
    @GET("http://www.mzitu.com/{type}/page/{offset}/")
    Observable<ResponseBody> getMzituOther(@Path("type") String type, @Path("offset") int offset);

    @Headers("Cache-Control: max-stale=1800")
    @GET("http://m.mzitu.com/{type}/page/{offset}/")
    Observable<ResponseBody> getM(@Path("type") String type, @Path("offset") int offset);


    /* **************************************************************************** *
     * *********************************** meizitu ******************************** *
     * **************************************************************************** */


    @Headers({
            "Cache-Control: max-stale=1800",

//            "Accept:text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
//            "Accept-Encoding:gzip, deflate",
//            "Accept-Language:zh-CN,zh;q=0.8",
//            "Cache-Control:max-age=0",
//            "Cookie:UM_distinctid=15c2512c32ea7-00d2016ead08c-323f5c0f-1fa400-15c2512c32fd8e; safedog-flow-item=; CNZZDATA1253283067=1021046910-1502161137-%7C1502161137",
//            "Host:m.meizitu.com",
//            "If-Modified-Since:Thu, 03 Aug 2017 16:02:28 GMT",
//            "If-None-Match:%220c294e771cd31:6308%22",
//            "Proxy-Connection:keep-alive",
//            "Upgrade-Insecure-Requests:1",
//            "User-Agent:Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36",
    })
    @GET("http://m.meizitu.com/index.html")
    Observable<ResponseBody> getMeizituHome();

    @Headers("Cache-Control: max-stale=1800")
    @GET("http://m.meizitu.com/a/{type}_{offset}.html")
    Observable<ResponseBody> getMeizitu(@Path("type") String type, @Path("offset") int offset);


    /* **************************************************************************** *
     * *********************************** 知乎看图 ******************************** *
     * **************************************************************************** */


    @Headers({
            "authorization: oauth c3cef7c66a1843f8b3a9e6a1e3160e20",
            "Cache-Control: max-stale=3600"
    })
    @GET("https://www.zhihu.com/api/v4/questions/{id}/answers")
    Observable<ResponseBody> getZhihuAnswer(
            @Path("id") long id,
            @Query("include") String include,
            @Query("limit") int limit,
            @Query("offset") int offset);

    @Headers("Cache-Control: max-stale=3600")
    @GET("https://www.zhihu.com/collection/{id}")
    Observable<ResponseBody> getZhihuCollection(
            @Path("id") long id,
            @Query("page") int offset);


    /* **************************************************************************** *
     * *********************************** 简单妹子 ******************************** *
     * **************************************************************************** */


    // "ooxx/page-75#comments"
    @Headers("Cache-Control: max-stale=1800")
    @GET("http://jandan.net/ooxx/{offset}")
    Observable<ResponseBody> getJiandan(@Path("offset") String page);


    /* **************************************************************************** *
     * *********************************** 买家秀 ********************************** *
     * **************************************************************************** */


    @Headers("Cache-Control: max-stale=60")
    @GET("http://www.mjxzs.cc/")
    Observable<ResponseBody> getMaijiaxiu();


    /* **************************************************************************** *
     * *********************************** 版本检测 ******************************** *
     * **************************************************************************** */


    @Headers("Cache-Control: max-stale=86400")
    @GET("https://raw.githubusercontent.com/gavinxxxxxx/Sensual/master/apk/version.json")
    Observable<Version> getVersion();

}
