package gavin.sensual.app.capture.jiandan;

import android.os.Bundle;
import android.support.annotation.Nullable;

import gavin.sensual.R;
import gavin.sensual.app.common.BigImagePopEvent;
import gavin.sensual.app.common.Image;
import gavin.sensual.app.common.LoadMoreEvent;
import gavin.sensual.app.common.ToolbarRecyclerViewModel;
import gavin.sensual.base.BindingFragment;
import gavin.sensual.base.RxBus;
import gavin.sensual.databinding.LayoutToolbarRecyclerBinding;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2017/5/11
 */
public class JiandanFragment extends BindingFragment<LayoutToolbarRecyclerBinding> {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private ToolbarRecyclerViewModel mViewModel;

    private Integer pageCount;

    public static JiandanFragment newInstance() {
        return new JiandanFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_toolbar_recycler;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        init();
        subscribeEvent();
        getImage(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mViewModel != null) {
            mViewModel.onDestroy();
        }
        compositeDisposable.dispose();
    }

    private void init() {
        mViewModel = new ToolbarRecyclerViewModel(_mActivity, this, binding);

        binding.includeToolbar.toolbar.setTitle("煎蛋妹子");
        binding.includeToolbar.toolbar.setNavigationOnClickListener(v -> pop());
        binding.refreshLayout.setOnRefreshListener(() -> getImage(false));
        binding.recycler.setOnLoadListener(() -> getImage(true));
    }

    private void subscribeEvent() {
        RxBus.get().toObservable(LoadMoreEvent.class)
                .doOnSubscribe(compositeDisposable::add)
                .filter(event -> event.requestCode == hashCode())
                .subscribe(event -> binding.recycler.performLoad());

        RxBus.get().toObservable(BigImagePopEvent.class)
                .doOnSubscribe(compositeDisposable::add)
                .observeOn(AndroidSchedulers.mainThread())
                .filter(event -> event.requestCode == hashCode())
                .subscribe(event -> binding.recycler.smoothScrollToPosition(event.position));
    }

    /**
     * 网络请求
     */
    private void getImage(boolean isMore) {
        mViewModel.getImage(getDataSource(isMore), isMore);
    }

    private Observable<Image> getDataSource(boolean isMore) {
        if (pageCount != null)
            return getDataLayer().getJiandanService().getPic(this, isMore ? pageCount - binding.recycler.offset : pageCount);

        return getDataLayer().getJiandanService().getPageCount()
                .flatMap(integer -> {
                    pageCount = integer;
                    return getDataSource(isMore);
                });
    }
}
