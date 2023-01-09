package com.darklake.myapplication.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.darklake.myapplication.databinding.ActivityMainBinding;

import adapter.DataAdapter;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import model.ImageDataProvider;
import model.ImageViewModel;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mBinding = null;

    private int mPageIndex = 1;
    private DataAdapter mViewAdapter = null;
    private String mSearchedText = null;
    private ImageViewModel mViewModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        super.onCreate(savedInstanceState);

        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mViewModel = new ViewModelProvider(this).get(ImageViewModel.class);

        mViewAdapter = new DataAdapter(mViewModel);
        mBinding.listView.setAdapter(mViewAdapter);
        mBinding.listView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.listView.setEmptyView(mBinding.emptyView);
        mBinding.listView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int lastItem = manager.findLastCompletelyVisibleItemPosition();
                int itemCount = recyclerView.getAdapter().getItemCount();

                if (lastItem + 1 == itemCount) {
                    if (!TextUtils.isEmpty(mSearchedText)) {
                        search(mSearchedText);
                    }
                }
            }
        });

        mBinding.etSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_ENTER:
                        mBinding.btnSearch.performClick();
                        return true;
                }
                return false;
            }
        });
    }

    // Loading 중에 스크롤 더해서 중복로드 하지 않도록
    private boolean isLoading = false;
    private void search(String search) {
        if (!isLoading) {
            isLoading = true;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                new ImageDataProvider().get(search, mPageIndex)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe((datas, t) -> {
                            if (t == null) {
                                if (datas.size() > 0) {
                                    mViewAdapter.addAll(datas);
                                    mPageIndex += 1;
                                }
                            }
                            isLoading = false;
                        });
            }
        }).start();
    }

    public void onSearchClick(View view) {
        mPageIndex = 1;
        mViewAdapter.clear();
        mSearchedText = mBinding.etSearch.getText().toString();
        search(mSearchedText);
    }

    public void onBackClick(View view) {
        finish();
    }
}