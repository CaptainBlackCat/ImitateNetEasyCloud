package com.stest.neteasycloud;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.stest.manage.MusicPlayer;
import com.stest.model.MusicInfoDetail;
import com.stest.utils.MusicUtils;
import com.stest.utils.NetWorkUtils;
import com.stest.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Timer;
import java.util.TimerTask;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created by Limuyang on 2016/10/26.
 * 播放音乐界面
 */

public class PlayingActiivty extends AppCompatActivity implements View.OnClickListener {
    @ViewInject(R.id.toolbar)
    Toolbar toolbar;
    @ViewInject(R.id.needle)
    ImageView needle;
    @ViewInject(R.id.default_disk_img)
    ImageView img_disk;
    @ViewInject(R.id.img_disc)
    ImageView img_disc;
    @ViewInject(R.id.currentTime)
    TextView currentTime;
    @ViewInject(R.id.totalTime)
    TextView endTime;
    @ViewInject(R.id.song)
    TextView song_txt;
    @ViewInject(R.id.singer)
    TextView singer_txt;
    @ViewInject(R.id.play_back)
    ImageView play_back;
    @ViewInject(R.id.playing_pre)
    ImageView prev_btn;
    @ViewInject(R.id.playing_next)
    ImageView next_btn;
    @ViewInject(R.id.playing_play)
    ImageView play_btn;
    @ViewInject(R.id.playing_playlist)
    ImageView play_list;
    @ViewInject(R.id.playing_mode)
    ImageView play_mode;
    @ViewInject(R.id.playSeekBar)
    SeekBar bar;
    private ActionBar actionBar;
    private Timer timer;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_playing);
        EventBus.getDefault().register(this);
        ViewUtils.inject(this);
        //初始化
        initWidgets();

        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currentTime.setText(MusicUtils.makeShortTimeString(PlayingActiivty.this, MusicPlayer.getPlayer().getCurrentPosition() / 1000));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, PlayingActiivty.class);
        context.startActivity(intent);
    }

    private void initWidgets() {
        bar.setIndeterminate(false);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.actionbar_back);
        }
        toolbar.inflateMenu(R.menu.music_playing_item);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    //点击分享按钮
                    case R.id.action_share:
                        if (!NetWorkUtils.isNetworkConnected(PlayingActiivty.this)) {
                            //没有网
                            ToastUtils.show(PlayingActiivty.this, getResources().getString(R.string.net_wrong), Toast.LENGTH_SHORT);
                        } else {
                            //分享到...
                        }
                        break;
                }
                return false;
            }
        });

        prev_btn.setOnClickListener(this);
        next_btn.setOnClickListener(this);
        play_btn.setOnClickListener(this);
        play_list.setOnClickListener(this);
        play_mode.setOnClickListener(this);


    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onStickyUpdateUI(final MusicInfoDetail info) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                song_txt.setText(info.getTitle());
                singer_txt.setText(info.getArtist());
                play_btn.setImageResource(MusicPlayer.getPlayer().isNowPlaying() ? R.drawable.playing_btn_pause : R.drawable.playing_btn_play);
                endTime.setText(MusicUtils.makeShortTimeString(PlayingActiivty.this, info.getDuration()/1000));
                //高斯模糊的处理
                Glide.with(PlayingActiivty.this)
                        .load(info.getCoverUri())
                        .error(R.drawable.fm_run_result_bg)
                        .placeholder(play_back.getDrawable())
                        .crossFade(android.R.anim.fade_in, 1000)
                        .bitmapTransform(new BlurTransformation(PlayingActiivty.this))
                        .into(play_back);

                //加载专辑图片
                Glide.with(PlayingActiivty.this)
                        .load(info.getCoverUri())
                        .placeholder(R.drawable.placeholder_disk_play_song)
                        .error(R.drawable.placeholder_disk_play_song)
                        .centerCrop()
                        .crossFade(android.R.anim.fade_in, 500)
                        .into(img_disk);

                bar.setMax((int) info.getDuration());
                bar.postDelayed(runnable,10);
            }
        });

    }
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (MusicPlayer.getPlayer().isNowPlaying()){
                bar.setProgress(MusicPlayer.getPlayer().getCurrentPosition());
                bar.postDelayed(runnable, 50);
            }
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.music_playing_item, menu);
        return true;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        timer.cancel();
        bar.removeCallbacks(runnable);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        bar.removeCallbacks(runnable);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.playing_pre:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bar.setProgress(0);
                        MusicPlayer.getPlayer().setNowPlaying(true);
                        play_btn.setImageResource(R.drawable.playing_btn_pause);
                        MusicPlayer.getPlayer().previous();
                    }
                }, 20);
                break;
            case R.id.playing_next:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bar.setProgress(0);
                        MusicPlayer.getPlayer().setNowPlaying(true);
                        play_btn.setImageResource(R.drawable.playing_btn_pause);
                        MusicPlayer.getPlayer().next();
                    }
                }, 20);
                break;
            case R.id.playing_play:
                //如果正在播放
                if (MusicPlayer.getPlayer().isNowPlaying()) {
                    play_btn.setImageResource(R.drawable.playing_btn_play);
                    MusicPlayer.getPlayer().setNowPlaying(false);
                    MusicPlayer.getPlayer().pause();
                } else if (!MusicPlayer.getPlayer().isNowPlaying()) {
                    play_btn.setImageResource(R.drawable.playing_btn_pause);
                    MusicPlayer.getPlayer().setNowPlaying(true);
                    MusicPlayer.getPlayer().resume();
                }
                break;
            case R.id.playing_playlist:
                break;
            //播放模式
            case R.id.playing_mode:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (timer==null)
            timer=new Timer();
        play_btn.setImageResource(MusicPlayer.getPlayer().isNowPlaying() ? R.drawable.playing_btn_pause : R.drawable.playing_btn_play);
        TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {
                if (MusicPlayer.getPlayer().isNowPlaying()) {
                    bar.setProgress(MusicPlayer.getPlayer().getCurrentPosition());
                } else {
                    bar.removeCallbacks(this);
                }

            }
        };

        timer.schedule(timerTask, 0, 50);
    }


}
