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
    private static final String VOLUME_SER_FILE = "qmpCrusaderCrabVolumes.ser";

    public static void init(Context c, TextView text){
        if(!initialized) {
            initialized = true;
        }
        loadVolumes(c);
        volumeText = text;
    }

    public static void saveVolumes(Context c){
        try {
            FileOutputStream fos = c.openFileOutput(VOLUME_SER_FILE, Context.MODE_PRIVATE);
            /*ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(volumes);
            out.close();
            fos.close();*/


            JSONObject js = new JSONObject(volumes);
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

            FileInputStream fis = c.openFileInput(VOLUME_SER_FILE);
            /*ObjectInputStream in = new ObjectInputStream(fis);
            //volumes = (ArrayMap<String, Float>)in.readObject();
            volumes = (TreeMap<String, Double>) in.readObject();
            in.close();
            fis.close();*/



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
            //volumes = new ArrayMap<String, Float>();
            volumes = new TreeMap<String, Float>();
            e.printStackTrace();
            Log.d("XXX_VC_load", "LOAD_BAD");
        }
    }





    public static TreeMap<String, Float> jsonToMap(JSONObject json) throws JSONException {
        TreeMap<String, Float> retMap = new TreeMap<String, Float>();

        if(json != JSONObject.NULL) {
            retMap = toMap(json);
        }
        return retMap;
    }

    public static TreeMap<String, Float> toMap(JSONObject object) throws JSONException {
        TreeMap<String, Float> map = new TreeMap<String, Float>();

        Iterator<String> keysItr = object.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            Double d=  (Double)value;
            Float f = d.floatValue();
            map.put(key, f);
        }
        return map;
    }

    public static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for(int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
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
            //Double volD = volumes.get(song.title);
            //Float vol = null;
            //if(volD!=null)
             //   vol = volD.floatValue();
            if(vol!=null) {
                binder.setVolume(vol);
                volumeText.setText(vol + "");
                Log.d("XXX_VC_newSongStart", "VOL SET");
            }
        }
    }
}
