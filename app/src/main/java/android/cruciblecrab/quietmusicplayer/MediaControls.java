package android.cruciblecrab.quietmusicplayer;

import android.media.MediaPlayer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

/**
 * Created by CrusaderCrab on 05/03/2016.
 */
public class MediaControls {

    public static boolean playerPlaying = false;



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

    public Button.OnClickListener playButtonListener(){
        return new Button.OnClickListener(){

            public void onClick(View view){
                MediaLogic.LocalBinder binder = MediaLogicConnection.getBinder();
                if (binder != null) {
                    if (binder.getMediaPlayer() != null && binder.playerReady()) {
                        Button button = (Button) view;
                        if (playerPlaying) {
                            button.setText("Play");
                            binder.pauseSong();
                            playerPlaying = false;
                        } else {
                            button.setText("Pause");
                            binder.unpauseSong();
                            playerPlaying = true;
                        }
                    }
                }
            }

        };
    }

    public Button.OnClickListener nextButtonListener() {
        return new Button.OnClickListener() {

            public void onClick(View view) {
                MediaLogic.LocalBinder binder = MediaLogicConnection.getBinder();
                if (binder != null) {
                    if (binder.getMediaPlayer() != null && binder.playerReady()) {
                        binder.playNextSong();
                    }
                }
            }
        };
    }

    public Button.OnClickListener prevButtonListener() {
        return new Button.OnClickListener() {

            public void onClick(View view) {
                MediaLogic.LocalBinder binder = MediaLogicConnection.getBinder();
                if (binder != null) {
                    if (binder.getMediaPlayer() != null && binder.playerReady()) {
                        binder.playPreviousSong();
                    }
                }
            }
        };
    }


    public void preparePlayButton(Button button){
        if(!playerPlaying){
            button.setText("Play");
        }else{
            button.setText("Pause");
        }
    }

}
