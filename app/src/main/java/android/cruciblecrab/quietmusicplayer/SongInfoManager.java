package android.cruciblecrab.quietmusicplayer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by CrusaderCrab on 06/03/2016.
 */
public class SongInfoManager {
    private static final String PREFS_NAME = "qmpCrusaderCrabPrefFile";
    private static final String SONG_LIST_SER_FILE = "qmpCrusaderCrabSongList.ser";

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

    public static SongList getStoredSongList( Context c){
        SongList sl = null;
        try {
            FileInputStream fis = c.openFileInput(SONG_LIST_SER_FILE);
            ObjectInputStream in = new ObjectInputStream(fis);
            sl = (SongList) in.readObject();
            in.close();
            fis.close();
        } catch (Exception e) {
            return null;
        }
        return sl;
    }

    public static void storeSongList(ArrayList<Song> songs, int position, Context c){
        SongList sl = new SongList(songs, position);
        try {
            FileOutputStream fos = c.openFileOutput(SONG_LIST_SER_FILE, Context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(sl);
            out.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class SongList  implements java.io.Serializable{
        public ArrayList<Song>songs;
        public int index;

        public SongList(ArrayList<Song> songs, int position){
            this.songs = songs;
            index = position;
        }
    }
}


