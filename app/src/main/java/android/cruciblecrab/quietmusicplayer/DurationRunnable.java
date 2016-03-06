package android.cruciblecrab.quietmusicplayer;

import android.media.MediaPlayer;
import android.os.Handler;
import android.widget.TextView;

/**
 * Created by CrusaderCrab on 06/03/2016.
 */
public class DurationRunnable implements Runnable{

    MediaLogic.LocalBinder binder;
    TextView text;
    Handler handler;

    public DurationRunnable(Handler handler, TextView text){
        this.handler = handler;
        this.text = text;
    }
    @Override
    public void run() {
        binder = MediaLogicConnection.getBinder();
        if(binder != null) {
            if (binder.getMediaPlayer() != null) {
                MediaPlayer player = binder.getMediaPlayer();
                if(player.isPlaying()) {
                    int maxRaw = player.getDuration() / 1000;
                    int currentRaw = player.getCurrentPosition() / 1000;
                    int maxMinutes = maxRaw/60; int maxSeconds = maxRaw%60;
                    int curMinutes = currentRaw/60; int curSeconds = currentRaw%60;
                    String cur = curMinutes + ":" + (curSeconds<10? "0"+curSeconds : ""+curSeconds);
                    String max = maxMinutes + ":" + (maxSeconds<10? "0"+maxSeconds : ""+maxSeconds);
                    text.setText(cur+"/"+max);
                }
            }
        }
        handler.postDelayed(this, 500);
    }
}
