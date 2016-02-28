package android.cruciblecrab.quietmusicplayer;

import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by CrusaderCrab on 28/02/2016.
 */
public class MediaLogic extends Service implements MediaPlayer.OnPreparedListener{

    public static float VOLUME_STEP = 0.1f;
    public static float MAX_VOLUME = 0.999f;
    public static int iid = 200;

    private static final String ACTION_PLAY = "com.example.action.PLAY";
    MediaPlayer mediaPlayer = null;

    @Override
    public void onCreate(){
        Log.d("XXXXXXXXXXXXXX", "onStartZ");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Uri contentUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, iid);

        //if (intent.getAction().equals(ACTION_PLAY)) {
            mediaPlayer = new MediaPlayer(); // initialize it here
            mediaPlayer.setOnPreparedListener(this);
            Log.d("XXXXXXXXXXXXXX", "onStartA");
            try {
                mediaPlayer.setDataSource(getApplicationContext(), contentUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        Log.d("XXXXXXXXXXXXXX", "onStartB");
            mediaPlayer.prepareAsync(); // prepare async to not block main thread
        //}
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new LocalBinder();
    }

    /** Called when MediaPlayer is ready */
    public void onPrepared(MediaPlayer player) {
        Log.d("XXXXXXXXXXXXXX", "onStartC");
        player.start();
    }

    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class LocalBinder extends Binder {
        MediaLogic getService() {
            return MediaLogic.this;
        }
    }

}
