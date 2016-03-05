package android.cruciblecrab.quietmusicplayer;

import android.media.MediaPlayer;
import android.os.Handler;
import android.widget.SeekBar;

/**
 * Created by CrusaderCrab on 05/03/2016.
 */
public class MediaControls {





    public SeekBar.OnSeekBarChangeListener seekBarChangeListener(){
        return new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                MediaLogic.LocalBinder binder = MediaLogicConnection.getBinder();
                if (binder != null) {
                    if (binder.getMediaPlayer() != null) {
                        MediaPlayer player = binder.getMediaPlayer();
                        if (player != null && fromUser && binder.playerReady()) {
                            player.seekTo(progress * 1000);
                        }
                    }
                }
            }
        };
    }

}
