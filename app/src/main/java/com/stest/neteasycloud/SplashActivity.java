package com.stest.neteasycloud;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.lidroid.xutils.ViewUtils;

/**
 * Created by muyang on 2016/3/1.
 *
 */
public class SplashActivity extends AppCompatActivity {
    private static final int DELAY_TIME = 2000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, HomePageActivity.class));
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                finish();
            }
        }, DELAY_TIME);

    }
}
