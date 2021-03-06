package gavin.sensual.app.meizitu;

import android.os.Bundle;
import android.support.annotation.Nullable;

import gavin.sensual.R;
import gavin.sensual.base.BindingFragment;
import gavin.sensual.base.BundleKey;
import gavin.sensual.databinding.LayoutPagingToolbarBinding;

/**
 * MzituRangFragment
 *
 * @author gavin.xiong 2017/8/16
 */
public class MeizituRangFragment extends BindingFragment<LayoutPagingToolbarBinding, MeizituRangViewModel> {

    public static MeizituRangFragment newInstance(String url) {
        Bundle args = new Bundle();
        args.putString(BundleKey.PAGE_TYPE, url);
        MeizituRangFragment fragment = new MeizituRangFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void bindViewModel(@Nullable Bundle savedInstanceState) {
        mViewModel = new MeizituRangViewModel(getContext(), this, mBinding, getArguments().getString(BundleKey.PAGE_TYPE));
        mViewModel.afterCreate();
        mBinding.setVm(mViewModel);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_paging_toolbar;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        mBinding.includeToolbar.toolbar.setNavigationIcon(R.drawable.vt_arrow_back_24dp);
        mBinding.includeToolbar.toolbar.setNavigationOnClickListener(v -> pop());
    }
}
