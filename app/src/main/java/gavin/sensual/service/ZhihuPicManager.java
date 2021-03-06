package gavin.sensual.service;

import android.support.v4.app.Fragment;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.util.List;

import gavin.sensual.app.capture.Capture;
import gavin.sensual.app.common.Image;
import gavin.sensual.service.base.BaseManager;
import gavin.sensual.service.base.DataLayer;
import io.reactivex.Observable;
import okhttp3.ResponseBody;

import static gavin.sensual.app.capture.zhihu.ZhihuViewModel.TYPE_COLLECTION;

/**
 * DailyManager
 *
 * @author gavin.xiong 2017/4/28
 */
public class ZhihuPicManager extends BaseManager implements DataLayer.ZhihuPicService {

    @Override
    public Observable<List<Capture>> getList(int type) {
        return getApi().getZhihuList(type == TYPE_COLLECTION ? "zhihu_collection.json" : "zhihu_question.json");
    }

    @Override
    public Observable<Image> getQuestionPic(Fragment fragment, long id, int limit, int offset) {
        return getApi().getZhihuAnswer(id, "data[*].is_normal,content", limit, offset)
                .map(ResponseBody::string)
                .map(Jsoup::parse)
                .map(document -> document.select("img[data-actualsrc]"))
                .flatMap(Observable::fromIterable)
                .map(element -> element.attr("data-actualsrc"))
                .filter(s -> s.length() > 6)
                .map(s -> s.substring(2, s.length() - 2))
                .map(s -> Image.newImage(fragment, s));
    }

    @Override
    public Observable<Image> getCollectionPic(Fragment fragment, long id, int offset) {
        return getApi().getZhihuCollection(id, offset)
                .map(ResponseBody::string)
                .map(Jsoup::parse)
                .map(document -> document.select("div[data-action=/answer/content] textarea[class=content]"))
                .map(Elements::html)
                .map(s -> s.replaceAll("&lt;", "<").replaceAll("&gt;", ">"))
                .map(Jsoup::parse)
                .map(document -> document.select("img"))
                .flatMap(Observable::fromIterable)
                .map(element -> element.attr("src"))
                .filter(s -> s.length() > 6)
                .map(s -> Image.newImage(fragment, s));
    }
}
