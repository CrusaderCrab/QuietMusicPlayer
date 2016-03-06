package android.cruciblecrab.quietmusicplayer;

import android.media.MediaPlayer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.BindException;
import java.util.ArrayList;

/**
 * Created by CrusaderCrab on 05/03/2016.
 */
public class MediaControls {

    public static boolean playerPlaying = false;
    private static ArrayList<Button> playButtons = new ArrayList<Button>();
    private static ArrayList<TextView> songNames = new ArrayList<TextView>();
    private static int MAX_TITLE_LENGTH = 20;


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
                    if (binder.getMediaPlayer() != null) {
                        binder.setMusicWanted(true);
                        Button button = (Button) view;
                        if(binder.playerReady() && binder.songsReady()) {
                            if (playerPlaying) {
                                binder.pauseSong();
                                playerPlaying = false;
                            } else {
                                binder.unpauseSong();
                                playerPlaying = true;
                            }
                        }else if(binder.songsReady() && !binder.playerReady() && !playerPlaying){
                            try {
                                binder.startPlaying();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            button.setText("Pause");
                            playerPlaying = true;
                        }
                        setAllPlayButtons(playerPlaying);
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
                        playerPlaying = true;
                        MediaControls.setAllPlayButtons(MediaControls.playerPlaying);
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
                        playerPlaying = true;
                        MediaControls.setAllPlayButtons(MediaControls.playerPlaying);
                    }
                }
            }
        };
    }


    public static void setAllPlayButtons(boolean playing){
        String text = (playing?"pause":"play");
        for(Button pb : playButtons)
            pb.setText(text);
    }

    public static void addPlayButton(Button pb){
        playButtons.add(pb);
    }

    public static void setToSongName(TextView t){
        MediaLogic.LocalBinder b = MediaLogicConnection.getBinder();
        if(b!=null) {
            String song = b.getCurrentSong().title;
            if(song.length() > MAX_TITLE_LENGTH)
                song = song.substring(0, MAX_TITLE_LENGTH);

            t.setText(song);
        }
    }

}
