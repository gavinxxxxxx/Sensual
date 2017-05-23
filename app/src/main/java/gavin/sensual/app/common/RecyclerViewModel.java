package gavin.sensual.app.common;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import gavin.sensual.R;
import gavin.sensual.base.BindingViewModel;
import gavin.sensual.base.RxBus;
import gavin.sensual.databinding.FooterLoadingBinding;
import gavin.sensual.databinding.LayoutRecyclerBinding;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2017/5/22
 */
public class RecyclerViewModel extends BindingViewModel<LayoutRecyclerBinding> {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private WeakReference<Context> mContext;
    private WeakReference<Fragment> mFragment;

    private List<Image> imageList = new ArrayList<>();
    private ImageAdapter adapter;
    private FooterLoadingBinding loadingBinding;

    private boolean clearFlag;

    public RecyclerViewModel(Context context, Fragment fragment, LayoutRecyclerBinding binding) {
        super(binding);
        this.mContext = new WeakReference<>(context);
        this.mFragment = new WeakReference<>(fragment);
        init();
    }

    private void init() {
        binding.refreshLayout.setColorSchemeResources(R.color.colorVector);

        adapter = new ImageAdapter(mContext.get(), mFragment.get(), imageList);
        binding.recycler.setAdapter(adapter);
        loadingBinding = FooterLoadingBinding.inflate(LayoutInflater.from(mContext.get()));
        adapter.setFooterBinding(loadingBinding);
    }

    public void doOnSubscribe(boolean isMore) {
        if (isMore) {
            loadingBinding.root.setVisibility(View.VISIBLE);
            loadingBinding.progressBar.setVisibility(View.VISIBLE);
            loadingBinding.textView.setText("加载中...");
        } else {
            clearFlag = true;
            binding.refreshLayout.setRefreshing(true);
        }
    }

    public void doOnError(boolean isMore) {
        if (isMore) {
            loadingBinding.root.setVisibility(View.VISIBLE);
            loadingBinding.progressBar.setVisibility(View.GONE);
            loadingBinding.textView.setText("玩坏了...");
        } else {
            binding.refreshLayout.setRefreshing(false);
        }
        RxBus.get().post(new LoadStateEvent(mFragment.get().hashCode(), LoadStateEvent.STATE_ERROR));
    }

    public void doOnComplete() {
        binding.refreshLayout.setRefreshing(false);
        loadingBinding.root.setVisibility(View.VISIBLE);
        loadingBinding.progressBar.setVisibility(View.GONE);
        loadingBinding.textView.setText(binding.recycler.haveMore ? "发呆中..." : "再也没有了...");
        RxBus.get().post(new LoadStateEvent(mFragment.get().hashCode(),
                binding.recycler.haveMore ? LoadStateEvent.STATE_NONE : LoadStateEvent.STATE_NO_MORE));
    }

    public void onNext(boolean isMore, Image image) {
        if (!isMore && clearFlag) {
            clearFlag = false;
            imageList.clear();
            adapter.notifyDataSetChanged();
        }
        imageList.add(image);
        adapter.notifyItemInserted(imageList.size() - 1);
        RxBus.get().post(new NewImageEvent(mFragment.get().hashCode(), image));
    }

    public void onError(Throwable e, boolean isMore) {
        if (isMore) {
            Snackbar.make(binding.recycler, e.getMessage(), Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(binding.recycler, e.getMessage(), Snackbar.LENGTH_INDEFINITE).show();
        }
    }

    public void onDestroy() {
        compositeDisposable.dispose();
    }

    /**
     * 网络请求
     */
    public void getImage(Observable<Image> observable, boolean isMore) {
        observable.subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    compositeDisposable.add(disposable);
                    this.doOnSubscribe(isMore);
                    binding.recycler.loadData(isMore);
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> {
                    this.doOnComplete();
                    binding.recycler.loading = false;
                })
                .doOnError(throwable -> {
                    this.doOnError(isMore);
                    binding.recycler.loading = false;
                    binding.recycler.offset--;
                })
                .subscribe(image -> {
                    binding.recycler.haveMore = true;
                    this.onNext(isMore, image);
                }, e -> this.onError(e, isMore));
    }
}
