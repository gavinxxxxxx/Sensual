package gavin.sensual.base;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;

import com.android.databinding.library.baseAdapters.BR;

import java.util.List;

import gavin.sensual.R;

/**
 * DataBinding 基类适配器
 *
 * @author gavin.xiong 2016/12/28
 */
public class BindingAdapter<T> extends RecyclerAdapter<T, ViewDataBinding> {

    private OnItemClickListener mListener;

    public BindingAdapter(Context context, List<T> mData, @LayoutRes int layoutId) {
        super(context, mData, layoutId);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mListener = onItemClickListener;
    }

    @Override
    public void onBind(RecyclerHolder<ViewDataBinding> holder, T t, int position) {
        holder.binding.setVariable(BR.item, t);
        holder.binding.executePendingBindings();
        if (mListener != null) {
            holder.itemView.findViewById(R.id.item).setOnClickListener((v) -> mListener.onItemClick(position));
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
