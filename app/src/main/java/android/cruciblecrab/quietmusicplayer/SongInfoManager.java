package android.cruciblecrab.quietmusicplayer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by CrusaderCrab on 06/03/2016.
 */
public class SongInfoManager {
    private static final String PREFS_NAME = "qmpCrusaderCrabPrefFile";
    private static final String SONG_LIST_SER_FILE = "qmpCrusaderCrabSongList.ser";
    private static final String SONG_POSITION_JSON_KEY = "com.cruciblecrab.songlist.json.position";

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

    /*public static SongList getStoredSongList( Context c){
        SongList sl = null;
        try {
            FileInputStream fis = c.openFileInput(SONG_LIST_SER_FILE);
            ObjectInputStream in = new ObjectInputStream(fis);
            sl = (SongList) in.readObject();
            in.close();
            fis.close();
        } catch (Exception e) {
            Log.d("XXX_S.I.M.load", "load FAILED");
            return null;
        }
        return sl;
    }*/

    public static SongList getStoredSongList( Context c ){
        try{
        FileInputStream fis = c.openFileInput(SONG_LIST_SER_FILE);
        StringBuffer fileContent = new StringBuffer("");
        byte[] buffer = new byte[1024];
        int n;
        while ((n = fis.read(buffer)) != -1) {
            fileContent.append(new String(buffer, 0, n));
        }
            String s = new String(fileContent);
            Log.d("XXXZ readSongs", s);
        JSONArray js = new JSONArray(s);
        SongList songs = jsonToArrayList(js);
        fis.close();
        return songs;
        }
        catch (Exception e){
            e.printStackTrace(); return null;
        }
    }

    public static SongList jsonToArrayList(JSONArray json) throws JSONException {
        ArrayList<Song> songs = new ArrayList<Song>();
        JSONObject jsPosition = json.getJSONObject(0);
        int pos = jsPosition.getInt(SONG_POSITION_JSON_KEY);
        for(int i = 1; i < json.length(); i++){
            JSONObject js = json.getJSONObject(i);
            songs.add(new Song(js));
        }
        return new SongList(songs, pos);
    }

    public static void storeSongList(ArrayList<Song> songs, int position, Context c){
        try {
            JSONArray jsarray = new JSONArray();
            JSONObject jsonPosition = new JSONObject();
            jsonPosition.put(SONG_POSITION_JSON_KEY, position);
            jsarray.put(jsonPosition);
            for (Song s : songs) {
                try {
                    jsarray.put(s.toJSONObject());
                } catch (JSONException e) {

                }
            }
            FileOutputStream fos = c.openFileOutput(SONG_LIST_SER_FILE, Context.MODE_PRIVATE);
            fos.write(jsarray.toString().getBytes());
            Log.d("XXXZ loadSongs", jsarray.toString());
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static class SongList{
        public final ArrayList<Song>songs;
        public final int index;

        public SongList(ArrayList<Song> songs, int position){
            this.songs = songs;
            index = position;
        }
    }
}


