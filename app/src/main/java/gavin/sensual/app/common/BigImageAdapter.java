package gavin.sensual.app.common;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.ViewConfiguration;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.chrisbanes.photoview.PhotoViewAttacher;

import java.lang.reflect.Field;
import java.util.List;

import gavin.sensual.R;
import gavin.sensual.base.RecyclerHeaderFooterAdapter;
import gavin.sensual.base.RecyclerHolder;
import gavin.sensual.base.RxBus;
import gavin.sensual.databinding.ItemBigImageBinding;
import gavin.sensual.databinding.RighterLoadingBinding;
import gavin.sensual.util.L;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2017/5/17
 */
public class BigImageAdapter extends RecyclerHeaderFooterAdapter<Image, ItemBigImageBinding, RighterLoadingBinding, RighterLoadingBinding> {

    private Fragment fragment;

    public BigImageAdapter(Context context, Fragment fragment, List<Image> mList) {
        super(context, mList, R.layout.item_big_image);
        this.fragment = fragment;
    }

    @Override
    public void onBind(RecyclerHolder<ItemBigImageBinding> holder, int position, Image t) {

        PhotoViewAttacher pvt = holder.binding.photoView.getAttacher();
        if (!holder.binding.photoView.isSelected()) {
            fixPhotoView(pvt);
            holder.binding.photoView.setSelected(true);
        }
        initScaleLevels(pvt, t.getWidth(), t.getHeight());

        pvt.setOnPhotoTapListener((v, x, y) -> RxBus.get().post(new BigImageClickEvent(position)));
        pvt.setOnOutsidePhotoTapListener(v -> RxBus.get().post(new BigImageClickEvent(position)));

        Glide.with(fragment)
                .load(t.getUrl())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder.binding.photoView);
    }

    private void fixPhotoView(PhotoViewAttacher attacher) {
        try {
            Field mScaleDragDetector = attacher.getClass().getDeclaredField("mScaleDragDetector");
            mScaleDragDetector.setAccessible(true);
            Object customGestureDetector = mScaleDragDetector.get(attacher);
            float touchShop = ViewConfiguration.get(mContext).getScaledTouchSlop();
            Field mTouchSlop = customGestureDetector.getClass().getDeclaredField("mTouchSlop");
            mTouchSlop.setAccessible(true);
            mTouchSlop.set(customGestureDetector, touchShop * 3);
        } catch (Exception e) {
            L.e(e);
        }
    }

    /**
     * 重置放大级别 - 最大边长 & 长宽比
     */
    private void initScaleLevels(PhotoViewAttacher pvt, int width, int height) {
        float MAX_SCALE = 3.0f;
        float MID_SCALE = 1.75f;
        float MIN_SCALE = 1.0f;

        float larger = Math.max(width, height);
        float lesser = Math.min(width, height);

        float x = larger / 1000;
        float y = larger / lesser;

        if (x * y > 3) {
            MAX_SCALE = x * y;
            MID_SCALE = (float) Math.sqrt(MAX_SCALE / MIN_SCALE);
        }

        pvt.setScaleLevels(MIN_SCALE, MID_SCALE, MAX_SCALE);
    }
}
