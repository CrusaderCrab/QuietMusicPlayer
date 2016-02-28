package android.cruciblecrab.quietmusicplayer;

import android.media.AudioManager;
import android.media.MediaPlayer;

import java.util.ArrayList;

/**
 * Created by CrusaderCrab on 28/02/2016.
 */
public class MediaLogic {

    public static MediaLogic mediaLogic;
    public static float VOLUME_STEP = 0.1f;
    public static float MAX_VOLUME = 0.999f;

    public MediaPlayer mediaPlayer;
    public ArrayList<Song> songs;
    public long volume;


    public static MediaLogic getMediaLogic(){
        if(mediaLogic==null)
            mediaLogic = new MediaLogic();
        return mediaLogic;
    }

    private MediaLogic(){
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        songs = null;
    }
}
