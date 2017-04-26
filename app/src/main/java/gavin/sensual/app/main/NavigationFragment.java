package gavin.sensual.app.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.view.Gravity;
import android.view.MenuItem;

import gavin.sensual.R;
import gavin.sensual.app.daily.DailyFragment;
import gavin.sensual.base.BaseFragment;
import gavin.sensual.base.RxBus;
import gavin.sensual.databinding.FragNavigationBinding;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * 侧滑菜单页
 *
 * @author gavin.xiong 2017/4/25
 */
public class NavigationFragment extends BaseFragment<FragNavigationBinding> implements NavigationView.OnNavigationItemSelectedListener {

    private Disposable disposable;

    public static NavigationFragment newInstance() {
        return new NavigationFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_navigation;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            loadRootFragment(R.id.holder, DailyFragment.newInstance());
        }
        subscribe();
        init();
    }

    @Override
    public boolean onBackPressedSupport() {
        if (binding.drawer.isDrawerOpen(Gravity.START)) {
            binding.drawer.closeDrawer(Gravity.START);
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        binding.drawer.closeDrawer(Gravity.START);
        switch (item.getItemId()) {
            case R.id.nav_news:
                return true;
        }
        return false;
    }

    @Override
    public void onDestroyView() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        super.onDestroyView();
    }

    private void subscribe() {
        disposable = RxBus.get().toObservable(DrawerToggleEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((event -> {
                    if (event.open) {
                        binding.drawer.openDrawer(Gravity.START);
                    } else {
                        binding.drawer.closeDrawer(Gravity.START);
                    }
                }));
    }

    private void init() {
        binding.navigation.setNavigationItemSelectedListener(this);
    }

}
