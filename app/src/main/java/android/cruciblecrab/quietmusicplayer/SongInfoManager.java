package android.cruciblecrab.quietmusicplayer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

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


    public static SongList getStoredSongList( Context c ) throws Exception{
        FileInputStream fis = c.openFileInput(SONG_LIST_SER_FILE);
        StringBuffer fileContent = new StringBuffer("");
        byte[] buffer = new byte[1024];
        int n;
        while ((n = fis.read(buffer)) != -1) {
            fileContent.append(new String(buffer, 0, n));
        }
        String s = new String(fileContent);
        JSONArray js = new JSONArray(s);
        SongList songs = jsonToSongList(js);
        fis.close();
        return songs;
    }

    public static SongList jsonToSongList(JSONArray json) throws JSONException {
        ArrayList<Song> songs = new ArrayList<Song>();
        JSONObject jsPosition = json.getJSONObject(0);
        int pos = jsPosition.getInt(SONG_POSITION_JSON_KEY);
        for(int i = 1; i < json.length(); i++){
            JSONObject js = json.getJSONObject(i);
            songs.add(new Song(js));
        }
        return new SongList(songs, pos);
    }

    public static void storeSongList(ArrayList<Song> songs, int position, Context c) throws Exception{
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
        fos.close();
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


