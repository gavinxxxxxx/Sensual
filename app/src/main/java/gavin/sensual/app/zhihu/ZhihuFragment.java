package gavin.sensual.app.zhihu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.text.InputType;

import java.util.ArrayList;
import java.util.List;

import gavin.sensual.R;
import gavin.sensual.base.BindingAdapter;
import gavin.sensual.base.BindingFragment;
import gavin.sensual.databinding.FragCaptureBinding;

/**
 * 知乎看图
 *
 * @author gavin.xiong 2017/5/10
 */
public class ZhihuFragment extends BindingFragment<FragCaptureBinding> {

    private List<ZhihuQuestion> targetList;

    public static ZhihuFragment newInstance() {
        return new ZhihuFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_capture;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        binding.toolbar.setTitle("知乎看图");
        binding.toolbar.setNavigationIcon(R.drawable.vt_arrow_back_24dp);
        binding.toolbar.setNavigationOnClickListener(v -> pop());
        binding.toolbar.inflateMenu(R.menu.action_search);
        binding.refreshLayout.setEnabled(false);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(binding.toolbar.getMenu().findItem(R.id.action_search));
        searchView.setInputType(InputType.TYPE_CLASS_NUMBER);
        searchView.setQueryHint("请输入问题 id");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                try {
                    start(ZhihuQuestionFragment.newInstance(Long.parseLong(query)));
                } catch (NumberFormatException e) {
                    Snackbar.make(binding.recycler, "请输入正确问题id", Snackbar.LENGTH_LONG).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        init();
    }

    private void init() {
        targetList = new ArrayList<>();
        targetList.add(new ZhihuQuestion(37787176L, "37787176 - 当一个颜值很高的程序员是怎样一番体验？", "http://static.cfanz.cn/uploads/jpg/2012/11/01/23/df24d55432.jpg"));
        targetList.add(new ZhihuQuestion(20843119L, "20843119 - 拍照的时候怎么让表情自然？", "http://static.cfanz.cn/uploads/jpg/2012/11/01/23/df24d55432.jpg"));
        targetList.add(new ZhihuQuestion(22212644L, "22212644 - 胸大怎么搭配衣服？", "http://static.cfanz.cn/uploads/jpg/2012/11/01/23/df24d55432.jpg"));
        targetList.add(new ZhihuQuestion(38285230L, "38285230 - 有一群漂亮的朋友是什么体验？", "http://static.cfanz.cn/uploads/jpg/2012/11/01/23/df24d55432.jpg"));

        BindingAdapter adapter = new BindingAdapter<>(_mActivity, targetList, R.layout.item_capture);
        binding.recycler.setAdapter(adapter);
        adapter.setOnItemClickListener(position -> start(ZhihuQuestionFragment.newInstance(targetList.get(position).getId())));
    }
}