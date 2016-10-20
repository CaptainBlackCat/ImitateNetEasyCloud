package com.stest.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.stest.manage.MusicPlayer;
import com.stest.manage.PlayEvent;
import com.stest.model.MusicInfoDetail;
import com.stest.neteasycloud.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Limuyang on 2016/9/8.
 * 底部控制栏
 */

public class ControlBarFragment extends Fragment implements View.OnClickListener {
    private View v;
    @ViewInject(R.id.progress)
    private ProgressBar mProgress;
    @ViewInject(R.id.bottom_layout)
    private RelativeLayout control_layout;
    @ViewInject(R.id.albumn_pic)
    private SimpleDraweeView albumn;
    @ViewInject(R.id.song)
    private TextView song_txt;
    @ViewInject(R.id.singer)
    private TextView singer_txt;
    @ViewInject(R.id.playlist_btn)
    private ImageView playlist;
    @ViewInject(R.id.play_btn)
    private ImageView play;
    @ViewInject(R.id.next_btn)
    private ImageView next;

    public static ControlBarFragment newInstance() {
        return new ControlBarFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (v != null) {
            ViewUtils.inject(this, v);
            return v;
        }
        v = inflater.inflate(R.layout.bottom_music_layout, container, false);
        ViewUtils.inject(this, v);
        addView();
        return v;
    }

    private void addView() {
        control_layout.setOnClickListener(this);
        playlist.setOnClickListener(this);
        play.setOnClickListener(this);
        next.setOnClickListener(this);
        mProgress.setMax(MusicPlayer.getPlayer().getDuration());
        mProgress.setProgress(MusicPlayer.getPlayer().getCurrentPosition());
        //如果正在播放
        if (MusicPlayer.getPlayer().isNowPlaying()) {
            getActivity().findViewById(R.id.bottom_layout).setVisibility(View.VISIBLE);
            play.setImageResource(R.drawable.pause_btn);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //整个底部播放栏布局
            case R.id.bottom_layout:
                break;
            //播放列表
            case R.id.playlist_btn:
                break;
            //播放控制
            case R.id.play_btn:
                break;
            //下一曲
            case R.id.next_btn:
                MusicPlayer.getPlayer().next();
                break;
        }

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateUI(MusicInfoDetail info) {
        song_txt.setText(info.getTitle());
        singer_txt.setText(info.getArtist());
        play.setImageResource(MusicPlayer.getPlayer().isNowPlaying() ? R.drawable.pause_btn : R.drawable.play_btn);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
