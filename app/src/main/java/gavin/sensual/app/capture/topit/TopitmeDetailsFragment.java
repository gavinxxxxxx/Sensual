package gavin.sensual.app.capture.topit;

import android.os.Bundle;
import android.support.annotation.Nullable;

import gavin.sensual.R;
import gavin.sensual.base.BindingFragment;
import gavin.sensual.base.BundleKey;
import gavin.sensual.databinding.LayoutPagingToolbarBinding;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2017/8/16
 */
public class TopitmeDetailsFragment extends BindingFragment<LayoutPagingToolbarBinding, TopitmeDetailsModel> {

    public static TopitmeDetailsFragment newInstance(long id) {
        Bundle args = new Bundle();
        args.putLong(BundleKey.NEWS_ID, id);
        TopitmeDetailsFragment fragment = new TopitmeDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void bindViewModel(@Nullable Bundle savedInstanceState) {
        mViewModel = new TopitmeDetailsModel(getContext(), this, mBinding,
                getArguments().getLong(BundleKey.NEWS_ID));
        mViewModel.afterCreate();
        mBinding.setVm(mViewModel);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_paging_toolbar;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        mBinding.includeToolbar.toolbar.setTitle("Topit.me");
        mBinding.includeToolbar.toolbar.setNavigationIcon(R.drawable.vt_arrow_back_24dp);
        mBinding.includeToolbar.toolbar.setNavigationOnClickListener(v -> pop());
    }
}
