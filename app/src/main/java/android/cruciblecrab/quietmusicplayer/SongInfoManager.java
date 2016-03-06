package android.cruciblecrab.quietmusicplayer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by CrusaderCrab on 06/03/2016.
 */
public class SongInfoManager {
    private static final String PREFS_NAME = "qmpCrusaderCrabPrefFile";
    private static String songListFileName = "songList.txt";

    public static final String KEY_VOLUME = "qmp.crusadercrab.sim.key.volume";

    public static Bundle retrieveMiscData(Context context){
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        float volume = settings.getFloat(KEY_VOLUME, MediaLogic.DEFAULT_VOLUME);
        Bundle bundle = new Bundle();
        bundle.putFloat(KEY_VOLUME, volume);
        return bundle;
    }

    public static void storeMiscData(Bundle b, Context context){
        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        float volume = b.getFloat(KEY_VOLUME, MediaLogic.DEFAULT_VOLUME);
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat(KEY_VOLUME, volume);
        editor.commit();
    }

    public static SongList getStoredSongList(){
        return null;
    }

    public static void storedSongList(SongList sl){

    }

    public class SongList{
        public ArrayList<Song>songs;
        public int index;
    }
}


