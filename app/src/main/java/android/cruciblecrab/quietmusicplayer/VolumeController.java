package android.cruciblecrab.quietmusicplayer;
import android.content.Context;
import android.util.ArrayMap;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by CrusaderCrab on 06/03/2016.
 */
public class VolumeController {

    private static ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();
    private static TreeMap<String, Float> volumes;
    //private static ArrayMap<String, Float> volumes;
    private static TextView volumeText;
    private static boolean initialized = false;
    private static boolean doLoadVolumes = false;
    private static final String VOLUME_JSON_FILE = "qmpCrusaderCrabVolumes.json";

    public static void init(Context c, TextView text){
        if(!initialized) {
            initialized = true;
        }
        loadVolumes(c);
        volumeText = text;
    }

    public static void saveVolumes(Context c){
        try {
            FileOutputStream fos = c.openFileOutput(VOLUME_JSON_FILE, Context.MODE_PRIVATE);
            JSONObject js = new JSONObject(volumes);
            Log.v("XXX_JSON",js.toString());
            fos.write(js.toString().getBytes());
            fos.close();
            Log.d("XXX_VC_save", "SAVE_GOOD");
        } catch (Exception e) {
            Log.d("XXX_VC_save", "SAVE_BAD");
            e.printStackTrace();
        }
    }

    private static void loadVolumes(Context c){
        try {

            FileInputStream fis = c.openFileInput(VOLUME_JSON_FILE);
            StringBuffer fileContent = new StringBuffer("");
            byte[] buffer = new byte[1024];
            int n;
            while ((n = fis.read(buffer)) != -1)
            {
                fileContent.append(new String(buffer, 0, n));
            }
            JSONObject js = new JSONObject(new String(buffer));
            volumes = jsonToMap(js);
            fis.close();
            Log.d("XXX_VC_load", "LOAD_GOOD");
        } catch (Exception e) {
            volumes = new TreeMap<String, Float>();
            e.printStackTrace();
            Log.d("XXX_VC_load", "LOAD_BAD");
        }
    }

    public static TreeMap<String, Float> jsonToMap(JSONObject json) throws JSONException {
        TreeMap<String, Float> retMap = new TreeMap<String, Float>();
        Iterator<String> keysItr = json.keys();
        while(keysItr.hasNext()){
            String key = keysItr.next();
            double value = 0;
            try {
                value = json.getDouble(key);
            }catch(JSONException noValidValue){

            }
            float floatValue = new Double(value).floatValue();
            retMap.put(key, floatValue);
        }
        return retMap;
    }

    public static void setLoadVolumes(boolean val){
        doLoadVolumes = val;
        updateMenuItems();
    }

    public static void menuItemClicked(){
        setLoadVolumes(!doLoadVolumes);
    }

    private static void updateMenuItems(){
        String t = (doLoadVolumes?"Don't Load Volumes":"Do Load Volumes");
        for(MenuItem mi : menuItems){
            mi.setTitle(t);
        }
    }

    public static void addMenuItem(MenuItem item){
        menuItems.add(item);
        updateMenuItems();
    }

    public static void removeMenuItem(MenuItem item){
        menuItems.remove(item);
    }

    public static float getVolumeForSong(Song song){
        return MediaLogic.DEFAULT_VOLUME;
    }

    public static void setVolumeForSong(Song song, float vol){
        if(doLoadVolumes) {
            volumes.put(song.title, vol);
        }
    }

    public static void newSongStarting(Song song, MediaLogic.LocalBinder binder){
        Log.d("XXX_VC_newSongStart", "START");
        if(doLoadVolumes && binder!=null){
            Float vol = volumes.get(song.title);
            if(vol!=null) {
                binder.setVolume(vol);
                volumeText.setText(vol + "");
                Log.d("XXX_VC_newSongStart", "VOL SET");
            }
        }
    }
}
