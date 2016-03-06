package android.cruciblecrab.quietmusicplayer;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by CrusaderCrab on 28/02/2016.
 */
public class MediaLogic extends Service implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener, AudioManager.OnAudioFocusChangeListener{

    public static float VOLUME_STEP = 0.01f;
    public static float MAX_VOLUME = 0.999f;
    public static float DEFAULT_VOLUME = VOLUME_STEP * 30;
    private static final String ACTION_PLAY = "crucibleCrab.qmp.PLAY";

    private float volume;
    private MediaPlayer mediaPlayer = null;
    private ArrayList<Song> songs;
    private int songIndex;
    private LocalBinder binder = null;
    private boolean playerReady = false;
    private boolean songsReady = false;
    private boolean musicWanted = false;

    public static boolean hasPermissions;

    @Override
    public void onDestroy(){
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        Bundle b = new Bundle();
        b.putFloat(SongInfoManager.KEY_VOLUME, volume);
        SongInfoManager.storeMiscData(b, this);
        SongInfoManager.storeSongList(songs, songIndex, this);
        Log.d("XXX_DESTORYED","XXXXXXXXXXXX");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("XXX_M.L._START_COM", "started");
        if(binder==null){
            binder = new LocalBinder();
            MediaLogicConnection.BINDER = binder;
        }
        if(mediaPlayer==null){
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK); //allowed to play even when phone is "sleeping"

            Bundle miscValues = SongInfoManager.retrieveMiscData(this);
            float savedVolume = miscValues.getFloat(SongInfoManager.KEY_VOLUME, DEFAULT_VOLUME);
            volume = savedVolume;
            SongInfoManager.SongList sl = SongInfoManager.getStoredSongList(this);
            if (sl != null && sl.songs != null) {
                songs = sl.songs;
                songIndex = sl.index;
                songsReady = true;
            }
        }
        Log.d("XXX_M.L_START_COM","Player is: "+mediaPlayer);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    /** Called when MediaPlayer is ready */
    public void onPrepared(MediaPlayer player) {
        if(musicWanted) {
            player.start();
            MediaControls.setAllPlayButtons(true);
        }
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


    private int NOTIFICATION_ID = 1;
    private void setAsForegroundService(){

        Intent notificationIntent = new Intent(getBaseContext(), VolumeActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
        builder.setAutoCancel(false);
        builder.setTicker("QMP: Song playing");
        builder.setContentTitle("QMP: Song playing");
        builder.setContentText("QMP: Song playing");
        builder.setContentIntent(pendingIntent);
        builder.setOngoing(true);
        builder.setNumber(100);
        startForeground(NOTIFICATION_ID,builder.build());

    }

    private boolean requestAudioFocus(){
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);
        return result != AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
    }

    public void onAudioFocusChange(int focusChange) {
        Log.d("XXX_M.L. AudioFoc", "CALLED");
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                // resume playback
                //if (mediaPlayer == null) initMediaPlayer();
                //else if (!mMediaPlayer.isPlaying()) mMediaPlayer.start();
                setMediaVolume();
                Log.d("XXX_M.L. AudioFoc", "GAINED");
                break;

            case AudioManager.AUDIOFOCUS_LOSS:
                // Lost focus for an unbounded amount of time: stop playback and release media player
                if (mediaPlayer.isPlaying())
                    binder.pauseSong();//mMediaPlayer.stop();
                //mMediaPlayer.release();
                //mMediaPlayer = null;
                Log.d("XXX_M.L. AudioFoc","LOSS");
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                // Lost focus for a short time, but we have to stop
                // playback. We don't release the media player because playback
                // is likely to resume
                if (mediaPlayer.isPlaying()) binder.pauseSong();
                Log.d("XXX_M.L. AudioFoc","TRANSJIEN");
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                // Lost focus for a short time, but it's ok to keep playing
                // at an attenuated level
                float lowVol = volume / 10;
                if (mediaPlayer.isPlaying()) mediaPlayer.setVolume(lowVol, lowVol);
                Log.d("XXX_M.L. AudioFoc","DUCK");
                break;
        }
    }

    private void unsetAsForegroundService(){
        stopForeground(true);
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
            setMediaVolume();
            setAsForegroundService();
            requestAudioFocus();
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
            songsReady = true;
        }

        public void startPlaying() throws IOException{
            playSong(songs.get(songIndex).id);
        }

        public boolean playerReady(){return playerReady;}
        public boolean songsReady(){return songsReady; }
        public boolean musicWanted(){return musicWanted; }
        public void setMusicWanted(boolean val){ musicWanted = val;}

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
            unsetAsForegroundService();
        }
        public void unpauseSong(){
            mediaPlayer.start();
            setAsForegroundService();
            requestAudioFocus();
        }

    }

    private void setMediaVolume(){
        if(playerReady)mediaPlayer.setVolume(volume, volume);
    }

}
