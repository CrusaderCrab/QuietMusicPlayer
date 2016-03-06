package android.cruciblecrab.quietmusicplayer;

import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by CrusaderCrab on 28/02/2016.
 */
public class MediaLogic extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener{

    public static float VOLUME_STEP = 0.01f;
    public static float MAX_VOLUME = 0.999f;
    private static final String ACTION_PLAY = "crucibleCrab.qmp.PLAY";

    private float volume;
    private MediaPlayer mediaPlayer = null;
    private ArrayList<Song> songs;
    private int songIndex;
    private LocalBinder binder = null;
    private boolean playerReady = false;

    public static boolean hasPermissions;

    @Override
    public void onCreate(){

    }

    @Override
    public void onDestroy(){
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("XXX_M.L._START_COM", "started");
        if(mediaPlayer==null){
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK); //allowed to play even when phone is "sleeping"
            volume = VOLUME_STEP*30;
        }
        if(binder==null){
            binder = new LocalBinder();
            MediaLogicConnection.BINDER = binder;
        }
        if(intent != null){
            //might have use later
        }else{

        }
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    /** Called when MediaPlayer is ready */
    public void onPrepared(MediaPlayer player) {
        player.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        songIndex = (songIndex+1)%songs.size();
        try {
            binder.playSong(songs.get(songIndex).id);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public class LocalBinder extends Binder {
        public MediaLogic getService() {
            return MediaLogic.this;
        }

        public MediaPlayer getMediaPlayer(){ return mediaPlayer; }

        public void playSong(int id) throws IOException{
            Uri contentUri = ContentUris.withAppendedId(
                    android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
            mediaPlayer.reset();
            mediaPlayer.setDataSource(getApplicationContext(), contentUri);
            playerReady = true;
            MediaControls.playerPlaying = true;
            setMediaVolume();
            mediaPlayer.prepareAsync();
        }

        public float getVolume(){
            return volume;
        }
        public void setVolume(float val){
            volume = val;
        }
        public float alterVolume(float step){
            volume += step;
            if(volume >= MAX_VOLUME) volume = MAX_VOLUME;
            else if(volume < 0 ) volume = 0;
            setMediaVolume();
            return volume;
        }

        public void setSongList(ArrayList<Song> _songs, int _songIndex){
            songs = _songs;
            songIndex = _songIndex;
        }

        public void startPlaying() throws IOException{
            playSong(songs.get(songIndex).id);
        }

        public boolean playerReady(){
            return playerReady;
        }

        public void playNextSong(){
            onCompletion(mediaPlayer);
        }

        public void playPreviousSong(){
            songIndex--;
            songIndex = (songIndex<0?songs.size()-1:songIndex);
            try {
                binder.playSong(songs.get(songIndex).id);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void pauseSong(){
            mediaPlayer.pause();
        }
        public void unpauseSong(){
            mediaPlayer.start();
        }

    }

    private void setMediaVolume(){
        if(playerReady)mediaPlayer.setVolume(volume, volume);
    }

}
