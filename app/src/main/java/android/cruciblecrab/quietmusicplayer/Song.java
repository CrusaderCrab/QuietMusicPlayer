package android.cruciblecrab.quietmusicplayer;

/**
 * Created by CrusaderCrab on 28/02/2016.
 */
public class Song {

    public String title;
    public long id;

    public Song(String title, long id){
        this.title = title;
        this.id = id;
    }

    @Override
    public String toString(){
        return id+" : "+title;
    }
}
