package gavin.sensual.app.daily;

import android.content.Context;
import android.databinding.ViewDataBinding;

import java.util.List;
import java.util.concurrent.TimeUnit;

import gavin.sensual.R;
import gavin.sensual.app.common.banner.BannerChangeEvent;
import gavin.sensual.app.common.banner.BannerModel;
import gavin.sensual.app.main.StartFragmentEvent;
import gavin.sensual.base.BaseFragment;
import gavin.sensual.base.RxBus;
import gavin.sensual.base.recycler.BindingHeaderFooterAdapter;
import gavin.sensual.base.recycler.PagingViewModel;
import gavin.sensual.util.L;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 知乎日报列表
 *
 * @author gavin.xiong 2017/8/11
 */
public class DailyViewModel extends PagingViewModel<Daily.Story, BindingHeaderFooterAdapter<Daily.Story>> {

    private int mBannerType;

    DailyViewModel(Context context, BaseFragment fragment, ViewDataBinding binding, int type) {
        super(context, fragment, binding);
        this.mBannerType = type;
    }

    @Override
    protected void initAdapter() {
        adapter = new BindingHeaderFooterAdapter<>(mContext.get(), mList, R.layout.item_daily);
        adapter.setOnItemClickListener(i ->
                RxBus.get().post(new StartFragmentEvent(NewsFragment.newInstance(mList.get(i).getId()))));
    }

    @Override
    protected void getData(boolean isMore) {
        getDataLayer().getDailyService().getDaily(isMore ? pagingOffset : 0)
                .delay(1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    mCompositeDisposable.add(disposable);
                    doOnSubscribe(isMore);
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> doOnComplete(isMore))
                .doOnError(e -> doOnError(isMore, e))
                .doOnNext(daily -> {
                    daily.getStories().get(0).setDate(!isMore ? "今日热文" : daily.getDate());
                    // 知乎日报的生日为 2013 年 5 月 19 日
                    pagingHaveMore = !autoLoadMore(isMore, daily) && Integer.parseInt(daily.getDate()) > 20130519;
                    // 轮播
                    if (!isMore) initBanner(daily.getTopStories());
                })
                .doAfterNext(daily -> {
                    if (autoLoadMore(isMore, daily)) getData(true);
                })
                .subscribe(daily -> accept(isMore, daily.getStories()), L::e);
    }

    /**
     * 满足什么条件时自动加载下一页
     * 解决今日热文过少时下拉刷新后上拉加载更多失效问题
     *
     * @param isMore isMore
     * @param daily  Daily
     * @return boolean
     */
    private boolean autoLoadMore(boolean isMore, Daily daily) {
        return !isMore && daily.getStories().size() < 10;
    }

    private void initBanner(List<Daily.Story> storyList) {
        Observable.fromIterable(storyList)
                .doOnSubscribe(mCompositeDisposable::add)
                .map(story -> new BannerModel<>(story.getImageUrl(), story.getTitle(), story))
                .toList()
                .subscribe(list -> RxBus.get().post(new BannerChangeEvent<>(mBannerType, list)));
    }
}
