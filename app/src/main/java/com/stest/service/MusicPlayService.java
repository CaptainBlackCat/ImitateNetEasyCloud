package com.stest.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.stest.manage.MusicNotification;
import com.stest.manage.MusicPlayer;
import com.stest.manage.PlayEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MusicPlayService extends Service {
    //管理通知
    private NotificationManager manager=null;
    //通知ID
    private final int NOTIFICATION_ID=10001;

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        //创建通知栏控制
        manager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        manager.notify(NOTIFICATION_ID,MusicNotification.getMusicNotification().onCreatMusicNotification());
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
                MusicPlayer.getPlayer().setQueue(playevent.getQueue(), playevent.getCurrentIndex());
                break;
            case NEXT:
                MusicPlayer.getPlayer().next();
                break;
            case PREVIOES:
                MusicPlayer.getPlayer().previous();
                break;
            case PAUSE:
                MusicPlayer.getPlayer().pause();
                break;
            case RESUME:
                MusicPlayer.getPlayer().resume();

        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}



