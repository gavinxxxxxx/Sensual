package gavin.sensual.util;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;

import java.util.concurrent.ExecutionException;

import gavin.sensual.R;
import gavin.sensual.util.glide.BlurTransformation;
import gavin.sensual.util.glide.GlideCircleTransformation;
import gavin.sensual.util.glide.GlideRoundTransformation;

/**
 * 图片加载器
 *
 * @author gavin.xiong 2017/3/15
 */
public class ImageLoader {

    /**
     * 图片占位色
     */
    private static final int[] COLORS = new int[]{
            R.color.holderColor00,
            R.color.holderColor01,
            R.color.holderColor02,
            R.color.holderColor03,
            R.color.holderColor04,
            R.color.holderColor05,
            R.color.holderColor06,
    };

    /**
     * 正常加载图片
     */
    public static void loadImage(ImageView imageView, String url) {
        int colorRes = getPlaceholderColor();
        Glide.with(imageView.getContext())
                .load(url)
                .placeholder(colorRes)
                .error(colorRes)
                .into(imageView);
    }

    /**
     * 正常加载图片
     */
    public static void loadImage(ImageView imageView, String url, int width, int height) {
        int colorRes = getPlaceholderColor();
        Glide.with(imageView.getContext())
                .load(url)
                .placeholder(colorRes)
                .error(colorRes)
                .override(width, height)
                .into(imageView);
    }

    /**
     * 正常加载图片
     */
    public static void loadImage(Fragment fragment, ImageView imageView, String url) {
        int colorRes = getPlaceholderColor();
        Glide.with(fragment)
                .load(url)
                .placeholder(colorRes)
                .error(colorRes)
                .into(imageView);
    }

    /**
     * 正常加载图片
     */
    public static void loadImageNoAnim(Fragment fragment, ImageView imageView, String url) {
        int colorRes = getPlaceholderColor();
        Glide.with(fragment)
                .load(url)
                .placeholder(colorRes)
                .error(colorRes)
                .dontAnimate()
                .into(imageView);
    }

    /**
     * 加载头像
     */
    public static void loadHead(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(url)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .override(200, 200)
                .transform(new GlideCircleTransformation(imageView.getContext()))
                .into(imageView);
    }

    /**
     * 加载头像
     */
    public static void loadHead(Fragment fragment, ImageView imageView, String url) {
        Glide.with(fragment)
                .load(url)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .override(200, 200)
                .transform(new GlideCircleTransformation(imageView.getContext()))
                .into(imageView);
    }

    /**
     * 加载圆形图片
     */
    public static void loadCircleImage(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(url)
                .transform(new GlideCircleTransformation(imageView.getContext()))
                .into(imageView);
    }

    /**
     * 加载圆形图片
     */
    public static void loadCircleImage(Fragment fragment, ImageView imageView, String url) {
        Glide.with(fragment)
                .load(url)
                .transform(new GlideCircleTransformation(imageView.getContext()))
                .into(imageView);
    }

    /**
     * 加载圆角图片
     */
    public static void loadRoundImage(ImageView imageView, String url) {
        int colorRes = getPlaceholderColor();
        Glide.with(imageView.getContext())
                .load(url)
                .placeholder(colorRes)
                .error(colorRes)
                .transform(new GlideRoundTransformation(imageView.getContext()))
                .into(imageView);
    }

    /**
     * 加载圆角图片
     */
    public static void loadRoundImage(Fragment fragment, ImageView imageView, String url) {
        int colorRes = getPlaceholderColor();
        Glide.with(fragment)
                .load(url)
                .placeholder(colorRes)
                .error(colorRes)
                .transform(new GlideRoundTransformation(imageView.getContext()))
                .into(imageView);
    }

    /**
     * 加载高斯模糊图片
     */
    public static void loadBlurImage(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(url)
//                .placeholder(R.drawable.bg_default_head_blur)
//                .error(R.drawable.bg_default_head_blur)
                .bitmapTransform(new BlurTransformation(imageView.getContext(), 50))
                .into(imageView);
    }

    /**
     * 加载高斯模糊图片
     */
    public static void loadBlurImage(Fragment fragment, ImageView imageView, String url) {
        Glide.with(fragment)
                .load(url)
//                .placeholder(R.drawable.bg_default_head_blur)
//                .error(R.drawable.bg_default_head_blur)
                .bitmapTransform(new BlurTransformation(imageView.getContext(), 50))
                .into(imageView);
    }

    /**
     * 获取 bitmap 对象 可用来获取尺寸等
     */
    public static Bitmap getBitmap(Fragment fragment, String url) throws InterruptedException, ExecutionException {
        return Glide.with(fragment)
                .load(url)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .get();
    }

    /**
     * 获取随机占位色
     */
    private static int getPlaceholderColor() {
        return COLORS[(int) (Math.random() * 7)];
    }

}