package gavin.sensual.app.zhihu;

import android.os.Bundle;
import android.support.annotation.Nullable;

import gavin.sensual.R;
import gavin.sensual.app.base.BigImageViewModel;
import gavin.sensual.base.BindingFragment;
import gavin.sensual.base.BundleKey;
import gavin.sensual.databinding.LayoutToobleRecyclerBinding;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2017/5/11
 */
public class ZhihuQuestionFragment extends BindingFragment<LayoutToobleRecyclerBinding> {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private long question;

    private BigImageViewModel mViewModel;

    public static ZhihuQuestionFragment newInstance(long question) {
        Bundle bundle = new Bundle();
        bundle.putLong(BundleKey.PAGE_TYPE, question);
        ZhihuQuestionFragment fragment = new ZhihuQuestionFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_tooble_recycler;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        init();
        getPic(false);
    }

//    @Override
//    public void onItemClick(List<Image> imageList, int position) {
//        ArrayList<String> stringList = new ArrayList<>();
//        for (Image image : imageList) {
//            stringList.add(image.getUrl());
//        }
//        RxBus.get().post(new StartFragmentEvent(
//                BigImageMultiFragment.newInstance(stringList, position)));
//
//    }

    @Override
    public boolean onBackPressedSupport() {
        return mViewModel.onBackPressedSupport() || super.onBackPressedSupport();
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
        question = getArguments().getLong(BundleKey.PAGE_TYPE);

        mViewModel = new BigImageViewModel(_mActivity, this, binding);
//        binding.setViewModel(mViewModel);

        binding.includeToolbar.toolbar.setNavigationOnClickListener(v -> pop());
        binding.refreshLayout.setOnRefreshListener(() -> getPic(false));
        binding.recycler.setOnLoadListener(() -> getPic(true));
    }

    private void getPic(boolean isMore) {
        getDataLayer().getZhihuPicService().getPic(this, question, 20, isMore ? binding.recycler.offset : 0)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    compositeDisposable.add(disposable);
                    mViewModel.doOnSubscribe(isMore);
                    binding.recycler.loadData(isMore);
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> {
                    mViewModel.doOnComplete();
                    binding.recycler.loading = false;
                })
                .doOnError(throwable -> {
                    mViewModel.doOnError(isMore);
                    binding.recycler.loading = false;
                    binding.recycler.offset--;
                })
                .subscribe(image -> {
                    binding.recycler.haveMore = true;
                    mViewModel.onNext(isMore, image);
                }, e -> mViewModel.onError(e, isMore));
    }
}
