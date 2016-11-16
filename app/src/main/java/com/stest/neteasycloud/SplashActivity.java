package com.stest.neteasycloud;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.lidroid.xutils.ViewUtils;
import com.stest.model.MusicInfoDetail;
import com.stest.utils.BackgroundThread;
import com.stest.utils.MusicUtils;
import com.stest.utils.SPUtils;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by muyang on 2016/3/1.
 */
public class SplashActivity extends AppCompatActivity {
    private static final int DELAY_TIME = 2000;
    private SQLiteDatabase db = Connector.getDatabase();
    private List<MusicInfoDetail> musicInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
        BackgroundThread.prepareThread();
        musicInfo = new ArrayList<>();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //第一次登陆扫描本地音乐
                if (SPUtils.getValue(SplashActivity.this, "isFirst", "First", true)) {

                    BackgroundThread.post(new Runnable() {
                        @Override
                        public void run() {
                            MusicUtils.scanMusic(SplashActivity.this, musicInfo);
                            DataSupport.saveAll(musicInfo);
                            SPUtils.putValue(SplashActivity.this, "isFirst", "First", false);
                        }
                    });
                }
                startActivity(new Intent(SplashActivity.this, HomePageActivity.class));
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                finish();
            }
        }, DELAY_TIME);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BackgroundThread.destroyThread();
    }
}
