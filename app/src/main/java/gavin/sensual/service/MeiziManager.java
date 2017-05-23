package gavin.sensual.service;

import android.support.v4.app.Fragment;

import org.jsoup.Jsoup;

import gavin.sensual.app.common.Image;
import gavin.sensual.service.base.BaseManager;
import gavin.sensual.service.base.DataLayer;
import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * DailyManager
 *
 * @author gavin.xiong 2017/4/28
 */
public class MeiziManager extends BaseManager implements DataLayer.MeiziPicService {

    @Override
    public Observable<Image> getPic(Fragment fragment, String type, int offset) {
        return "zipai".equals(type)
                ? get2(fragment, type, String.format("comment-page-%s", offset), "")
                : get1(fragment, type, "page", String.valueOf(offset));
    }

    private Observable<Image> get1(Fragment fragment, String type, String offset1, String offset2) {
        return getMeiziAPI().getPic(type, offset1, offset2)
                .map(ResponseBody::string)
                .map(Jsoup::parse)
                .map(document -> document.select("div[class=main-content] div[class=postlist] img"))
                .flatMap(Observable::fromIterable)
                .map(element -> element.attr("data-original"))
                .map(s -> s
                        .substring(0, s.lastIndexOf("/") + 1).replaceAll("thumbs/", "")
                        .concat(s.substring(s.indexOf("_") + 1, s.lastIndexOf("_")))
                        .concat(s.substring(s.lastIndexOf("."))))
                .map(s -> Image.newImage(fragment, s));
    }

    private Observable<Image> get2(Fragment fragment, String type, String offset1, String offset2) {
        return getMeiziAPI().getPic(type, offset1, offset2)
                .map(ResponseBody::string)
                .map(Jsoup::parse)
                .map(document -> document.select("div[class=comment-body] img"))
                .flatMap(Observable::fromIterable)
                .map(element -> element.attr("src"))
                .map(s -> Image.newImage(fragment, s));
    }

    private boolean flag = false;

    @Override
    public Observable<Image> getPic2(Fragment fragment, String url) {
        return Observable.just(url)
                .map(s -> s.substring(0, s.lastIndexOf(".") - 2).concat("%02d").concat(s.substring(s.lastIndexOf("."))))
                .flatMap(s -> Observable.range(1, 99).map(i -> String.format(s, i)))
                .map(s -> Image.newImage(fragment, s))
                .takeUntil(image -> {
                    // 允许图片连续不存在的最大间隔为1
                    if (image.isError()) {
                        flag = !flag;
                        return !flag;
                    } else {
                        flag = false;
                    }
                    return false;
                })
                .filter(image -> !image.isError());
    }
}
