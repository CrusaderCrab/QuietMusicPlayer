package android.cruciblecrab.quietmusicplayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by CrusaderCrab on 17/03/2016.
 */
public class JSONFunctions {

    public static JSONArray readInputStreamIntoJSONArray(FileInputStream fis) throws Exception{
        StringBuffer fileContent = new StringBuffer("");
        byte[] buffer = new byte[1024];
        int n;
        while ((n = fis.read(buffer)) != -1) {
            fileContent.append(new java.lang.String(buffer, 0, n));
        }
        java.lang.String s = new java.lang.String(fileContent);
        JSONArray js = new JSONArray(s);
        return js;
    }

    public static JSONArray readSongsIntoJSONArray(ArrayList<Song> songs, int start, int end){
        JSONArray jsArray = new JSONArray();
        for(Song s : songs){
            try {
                jsArray.put(s.toJSONObject());
            }catch(JSONException e){}
        }
        return jsArray;
    }

    public static ArrayList<Song> readSongsFromJSONArray(JSONArray jsArray, int start, int end){
        java.util.ArrayList<Song> songs = new ArrayList<Song>();
        for(int i = 0; i < jsArray.length(); i++){
            try {
                JSONObject js = jsArray.getJSONObject(i);
                songs.add(new Song(js));
            }catch (Exception e){

            }
        }
        return songs;
    }

    public static ArrayList<Song> readSongsFromJSONArray(JSONArray jsArray, int start){
        return readSongsFromJSONArray(jsArray, start, jsArray.length());
    }

    public static ArrayList<Song> readSongsFromJSONArray(JSONArray jsArray){
        return readSongsFromJSONArray(jsArray, 0, jsArray.length());
    }

    public static JSONArray readSongsIntoJSONArray(ArrayList<Song> songs, int start){
        return readSongsIntoJSONArray(songs, start, songs.size());
    }

    public static JSONArray readSongsIntoJSONArray(ArrayList<Song> songs){
        return readSongsIntoJSONArray(songs, 0, songs.size());
    }
}
