package gavin.sensual.service.base;

import gavin.sensual.app.daily.Daily;
import gavin.sensual.app.daily.News;
import gavin.sensual.app.gank.Result;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Query;

/**
 * DataLayer
 *
 * @author gavin.xiong 2017/4/28
 */
public class DataLayer {

    private DailyService mDailyService;
    private GankService mGankService;
    private DBService mDBService;

    public DataLayer(DailyService dailyService, GankService gankService, DBService dbService) {
        mDailyService = dailyService;
        mGankService = gankService;
        mDBService = dbService;
    }

    public DailyService getDailyService() {
        return mDailyService;
    }

    public GankService getGankService() {
        return mGankService;
    }

    public DBService getDBService() {
        return mDBService;
    }

    public interface DailyService {

        /**
         * 获取最新日报新闻列表
         *
         * @return Daily
         */
        Observable<Daily> getDaily(int dayDiff);

        /**
         * 获取新闻
         *
         * @param newsId long
         * @return News
         */
        Observable<News> getNews(long newsId);

        /**
         * 缓存今日热文列表
         *
         * @param daily Daily
         */
        void cacheDaily(final Daily daily);
    }

    public interface GankService {

        /**
         * 获取福利
         *
         * @param limit 分页大小
         * @param no    页码
         * @return Result
         */
        Observable<Result> getWelfare(int limit, int no);
    }

    public interface DBService {

        Observable<ResponseBody> getRank(int page);
    }
}
