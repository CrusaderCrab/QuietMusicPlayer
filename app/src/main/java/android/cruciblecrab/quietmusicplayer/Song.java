package android.cruciblecrab.quietmusicplayer;

/**
 * Created by CrusaderCrab on 28/02/2016.
 */
public class Song {

    public String title;
    public int id;
    //public float volume;
    //public static final float NO_VOLUME = 88.0f;

    public Song(String title, int id){
        this.title = title;
        this.id = id;
    }

    @Override
    public String toString(){
        return id+" : "+title;
    }
}
