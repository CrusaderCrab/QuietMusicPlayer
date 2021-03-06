package android.cruciblecrab.quietmusicplayer;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by CrusaderCrab on 17/03/2016.
 */
public class PlaylistManager {

    private static final String PLAYLIST_LIST_OF_PLAYLISTS_FILE = "comCrucibleCrabListOfPlaylistsFile.json";
    private static ArrayList<String> playlistFileNames = null;
    private static ArrayList<String> playlistNames = null;
    private static final String PLAYLIST_NAME_JSON_KEY = "com.cruciblecrab.pl.name";
    private static final String PLAYLIST_FILE_JSON_KEY = "com.cruciblecrab.pl.file";

    public static ArrayList<String> getPlayLists(Context c) throws Exception{
        FileInputStream fis = c.openFileInput(PLAYLIST_LIST_OF_PLAYLISTS_FILE);
        JSONArray js = JSONFunctions.readInputStreamIntoJSONArray(fis);
        ArrayList<String> playListNames = getPlayListsFromJSON(js);
        fis.close();
        return playListNames;
    }

    private static ArrayList<String> getPlayListsFromJSON(JSONArray jsArray) throws JSONException{
        playlistNames = new ArrayList<String>();
        playlistFileNames = new ArrayList<String>();
        for(int i = 0; i < jsArray.length(); i++){
            try {
                JSONObject jso = jsArray.getJSONObject(i);
                playlistNames.add(jso.optString(PLAYLIST_NAME_JSON_KEY));
                playlistFileNames.add(jso.optString(PLAYLIST_FILE_JSON_KEY));
            }catch (Exception e){

            }
        }
        return playlistNames;
    }

    public static void savePlayListsToJSON(Context c) throws Exception{
        JSONArray jsarray = new JSONArray();
        for (int i = 0; i < playlistNames.size(); i++) {
            JSONObject js = new JSONObject();
            js.put(PLAYLIST_NAME_JSON_KEY, playlistNames.get(i));
            js.put(PLAYLIST_FILE_JSON_KEY, playlistFileNames.get(i));
            jsarray.put(js);
        }
        FileOutputStream fos = c.openFileOutput(PLAYLIST_LIST_OF_PLAYLISTS_FILE, Context.MODE_PRIVATE);
        fos.write(jsarray.toString().getBytes());
        fos.close();
    }

    public static ArrayList<Song> getPlaylist(int position) throws Exception{
        String fileName = playlistFileNames.get(position);
        FileInputStream fis = new FileInputStream(fileName);
        JSONArray jsa = JSONFunctions.readInputStreamIntoJSONArray(fis);
        return JSONFunctions.readSongsFromJSONArray(jsa);
    }

    public static void savePlayList(ArrayList<Song> songs, String name, Context c) throws Exception{
        String filename = name+"_playlist.json";
        File file = new File(c.getFilesDir(), filename);
        JSONArray jsArray = JSONFunctions.readSongsIntoJSONArray(songs);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(jsArray.toString().getBytes());
        fos.close();
        playlistNames.add(name);
        playlistFileNames.add(filename);
        savePlayListsToJSON(c);
    }


}
