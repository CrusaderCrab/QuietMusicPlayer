package android.cruciblecrab.quietmusicplayer;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by CrusaderCrab on 28/02/2016.
 */
public class Song implements Serializable{

    public final String title;
    public final int id;
    private final static String TITLE_JSON_KEY = "com.cruciblecrab.Song.JSON.Title";
    private final static String ID_JSON_KEY = "com.cruciblecrab.Song.JSON.ID";
    private final static String TITLE_NO_VALUE = "com.cruciblecrab.Song.JSON.Invalid.Title";
    private final static int ID_NO_VALUE = Integer.MIN_VALUE;
    //public float volume;
    //public static final float NO_VOLUME = 88.0f;

    public Song(String title, int id){
        this.title = title;
        this.id = id;
    }

    public Song(JSONObject js)throws JSONException{
        this.title = js.optString(TITLE_JSON_KEY, TITLE_NO_VALUE);
        Log.d("XXXS got title", title);
        this.id = js.optInt(ID_JSON_KEY, ID_NO_VALUE);
        Log.d("XXXS got id", "" + id);
        if(title.equals(TITLE_NO_VALUE) || id==ID_NO_VALUE){
            throw new JSONException("Invalid JSONObject, doesn't contain Title and/or id mapping");
        }
    }

    @Override
    public String toString(){
        return id+" : "+title;
    }

    public JSONObject toJSONObject() throws JSONException{
        JSONObject js = new JSONObject();
        js.put(TITLE_JSON_KEY, title);
        js.put(ID_JSON_KEY, id);
        return js;
    }
}
