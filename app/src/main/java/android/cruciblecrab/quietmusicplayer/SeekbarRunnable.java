package android.cruciblecrab.quietmusicplayer;

import android.media.MediaPlayer;
import android.os.Handler;
import android.widget.SeekBar;

/**
 * Created by CrusaderCrab on 05/03/2016.
 */
public class SeekbarRunnable implements Runnable{

    MediaLogic.LocalBinder binder;
    SeekBar seekBar;
    Handler handler;

    public SeekbarRunnable(Handler handler, SeekBar seekBar){
        this.handler = handler;
        this.seekBar = seekBar;
    }
    @Override
    public void run() {
        binder = MediaLogicConnection.getBinder();
        if(binder != null) {
            if (binder.getMediaPlayer() != null) {
                MediaPlayer player = binder.getMediaPlayer();
                if(player.isPlaying()) {
                    seekBar.setMax(player.getDuration()/1000);
                    int mCurrentPosition = player.getCurrentPosition() / 1000;
                    seekBar.setProgress(mCurrentPosition);
                }
            }
        }
        handler.postDelayed(this, 1000);
    }
}