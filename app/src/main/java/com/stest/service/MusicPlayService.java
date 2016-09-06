package com.stest.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.MainThread;

import com.stest.manage.MusicPlayer;
import com.stest.manage.PlayEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

public class MusicPlayService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PlayEvent playevent) {
        switch (playevent.getAction()) {
            case PLAY:
                MusicPlayer.getPlayer().setQueue(playevent.getQueue(), 0);
                break;
            case NEXT:
                MusicPlayer.getPlayer().next();
                break;

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
