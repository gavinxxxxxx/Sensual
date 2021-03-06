package gavin.sensual.app.mzitu;

import android.content.Context;
import android.databinding.ViewDataBinding;

import gavin.sensual.app.common.Image;
import gavin.sensual.app.common.ImageViewModel;
import gavin.sensual.base.BaseFragment;
import io.reactivex.Observable;

/**
 * 豆瓣
 *
 * @author gavin.xiong 2017/8/15
 */
class MzituRangViewModel extends ImageViewModel {

    private String url;

    MzituRangViewModel(Context context, BaseFragment fragment, ViewDataBinding binding, String url) {
        super(context, fragment, binding);
        this.url = url;
    }

    @Override
    protected Observable<Image> getDataSrc(boolean isMore) {
        return getDataLayer().getMeiziPicService().getImageRange(mFragment.get(), url);
    }

}
