package view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/***
 * Empty 화면 처리를 위한 RecyclerView.
 * Adapter 에 뭔가 들어오면 Empty 를 숨기고, Adapter 가 비었다면 Empty 를 보인다.
 *
 * @author Kyeongrak Choi
 * @version 0.0.1
 */
public class RecyclerEmptyView extends RecyclerView {
    public RecyclerEmptyView(@NonNull Context context) {
        super(context);
    }
    public RecyclerEmptyView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    public RecyclerEmptyView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private View mEmptyView = null;

    AdapterDataObserver mObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            initEmptyView();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
            initEmptyView();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
            super.onItemRangeChanged(positionStart, itemCount, payload);
            initEmptyView();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            initEmptyView();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            initEmptyView();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            initEmptyView();
        }
    };

    @Override
    public void setAdapter(@Nullable Adapter adapter) {
        Adapter old = getAdapter();
        if (old != null) {
            old.unregisterAdapterDataObserver(mObserver);
        }

        super.setAdapter(adapter);
        adapter.registerAdapterDataObserver(mObserver);
    }

    private void initEmptyView() {
        if (mEmptyView != null) {
            Adapter adapter = getAdapter();
            if ((adapter == null) || adapter.getItemCount() == 0) {
                mEmptyView.setVisibility(View.VISIBLE);
            } else {
                mEmptyView.setVisibility(View.GONE);
            }
        }
    }

    public void setEmptyView(View view) {
        mEmptyView = view;
        initEmptyView();
    }
}
