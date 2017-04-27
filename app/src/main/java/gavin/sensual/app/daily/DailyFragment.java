package gavin.sensual.app.daily;

import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;

import gavin.sensual.BR;
import gavin.sensual.R;
import gavin.sensual.app.main.DrawerToggleEvent;
import gavin.sensual.base.BindingAdapter;
import gavin.sensual.base.BindingFragment;
import gavin.sensual.base.RxBus;
import gavin.sensual.databinding.FragDailyBinding;
import gavin.sensual.util.L;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 知乎日报 - 列表
 *
 * @author gavin.xiong 2017/4/26
 */
public class DailyFragment extends BindingFragment<FragDailyBinding> {

    public static DailyFragment newInstance() {
        return new DailyFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_daily;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        init();
        getTodayNews();
    }

    private void init() {
        binding.toolbar.setTitle("知乎日报");
        binding.toolbar.setNavigationIcon(R.drawable.vt_menu_24dp);
        binding.toolbar.setNavigationOnClickListener((v) -> RxBus.get().post(new DrawerToggleEvent(true)));

        binding.refreshLayout.setColorSchemeResources(R.color.colorVector);
        binding.refreshLayout.setOnRefreshListener(this::getTodayNews);
    }

    private void getTodayNews() {
        getDataLayer().getDailyService().getTodayNews()
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    L.e(Looper.myLooper() == Looper.getMainLooper());
                    binding.refreshLayout.setRefreshing(true);
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> binding.refreshLayout.setRefreshing(false))
                .doOnError(e -> binding.refreshLayout.setRefreshing(false))
                .subscribe(todayNews -> binding.recycler.setAdapter(
                        new BindingAdapter<>(_mActivity, todayNews.getStories(), R.layout.item_daily, BR.item)));
    }
}
