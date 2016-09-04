package com.stest.neteasycloud;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * Created by Limuyang on 2016/8/19.
 * 搜索音乐、歌手、歌词、用户
 */
public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    @ViewInject(R.id.tool_bar)
    private Toolbar toolbar;
    @ViewInject(R.id.search_edittext)
    private EditText edit_text;
    @ViewInject(R.id.line)
    private View line;
    @ViewInject(R.id.bar_search)
    private ImageView bar_search;
    private ActionBar actionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.search_layout);
        ViewUtils.inject(this);
        initViews();
        applyKitKatTranslucency();

    }

    public void initViews() {
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        bar_search.setOnClickListener(this);
        //获取焦点
        //获取编辑框焦点
        edit_text.setFocusable(true);
        edit_text.requestFocus();

        //去除默认Title显示
        actionBar.setDisplayShowTitleEnabled(false);
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.actionbar_back);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    // 点击空白区域 自动隐藏软键盘
    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus()) {
            /**
             * 点击空白位置 隐藏软键盘
             */
            Log.d("当前点击控件----", this.getCurrentFocus().toString());
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }

    }

    private void applyKitKatTranslucency() {

        // KitKat translucent navigation/status bar.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager mTintManager = new SystemBarTintManager(this);
            mTintManager.setStatusBarTintEnabled(true);

            mTintManager.setStatusBarTintResource(R.color.themeColor);//通知栏所需颜色
        }

    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}
