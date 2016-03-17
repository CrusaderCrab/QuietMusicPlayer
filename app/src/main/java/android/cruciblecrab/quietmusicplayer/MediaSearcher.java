package android.cruciblecrab.quietmusicplayer;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * Created by CrusaderCrab on 28/02/2016.
 */
public class MediaSearcher implements LoaderManager.LoaderCallbacks<Cursor> {

    public SimpleCursorAdapter adapter;
    private ListView list;
    private Context context;
    public Cursor cursor;
    private int mode;
    private String filter;
    private int filterMode;

    public static final int MODE_SONG = 0;
    public static final int MODE_ALBUM = 1;
    public static final int MODE_ARTIST = 3;
    public static final int MODE_NO_FILTER = 2;
    public static final int MODE_PLAYLIST = 5;
    public static final String NO_INFO = "qmp.cruciblecrab.noinfo";

    private static final String[] PROJECTION_SONG = {
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ALBUM
    };

    private static final String[] PROJECTION_ALBUM = {
            MediaStore.Audio.Albums._ID,
            MediaStore.Audio.Albums.ALBUM,
            MediaStore.Audio.Albums.ARTIST,
            MediaStore.Audio.Albums.ALBUM_ART,
            MediaStore.Audio.Albums.NUMBER_OF_SONGS
    };

    private static final String[] PROJECTION_ARTIST = {
            MediaStore.Audio.Artists._ID,
            MediaStore.Audio.Artists.ARTIST,
            MediaStore.Audio.Artists.NUMBER_OF_TRACKS,
            MediaStore.Audio.Artists.NUMBER_OF_ALBUMS
    };

    private int[] TO_VIEWS = {android.R.id.text1};

    private static final String[] FROM_COLUMNS_SONG = {
            MediaStore.Audio.Media.TITLE
    };

    private static final String[] FROM_COLUMNS_ALBUM = {
            MediaStore.Audio.Albums.ALBUM
    };

    private static final String[] FROM_COLUMNS_ARTIST = {
            MediaStore.Audio.Artists.ARTIST
    };

    public MediaSearcher(Context context, ListView list, int mode, String filter, int filterMode){
        this.mode = mode;
        this.filter = filter;
        this.filterMode = filterMode;
        this.list = list;
        this.context = context;
        String[] columns = getCorrectColumn();
        adapter = new SimpleCursorAdapter(context,
                android.R.layout.simple_list_item_1, null,
                columns, TO_VIEWS, 0);
        list.setAdapter(adapter);
    }

    public MediaSearcher(Context context, ListView list){
        this.list = list;
        this.context = context;
        adapter = new SimpleCursorAdapter(context,
                android.R.layout.simple_list_item_1, null,
                FROM_COLUMNS_SONG, TO_VIEWS, 0);
        list.setAdapter(adapter);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri tableLocation = getCorrectTableLocal();
        String[] projection = getCorrectProjection();
        String selection = getCorrectSelection();
        String[] selectionArgs = getCorrectSelectionArgs();
        String sort = getCorrectSortingString();
        return new CursorLoader(context, tableLocation,
                projection, selection, selectionArgs, sort);
    }//MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
        cursor = data;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    private String[] getCorrectColumn(){
        switch(mode){
            case MODE_SONG: return FROM_COLUMNS_SONG;
            case MODE_ALBUM: return FROM_COLUMNS_ALBUM;
            case MODE_ARTIST: return FROM_COLUMNS_ARTIST;
            default: return null;
        }
    }

    private String[] getCorrectProjection(){
        switch(mode){
            case MODE_SONG: return PROJECTION_SONG;
            case MODE_ALBUM: return PROJECTION_ALBUM;
            case MODE_ARTIST: return PROJECTION_ARTIST;
            default: return null;
        }
    }

    private Uri getCorrectTableLocal(){
        switch(mode){
            case MODE_SONG: return MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            case MODE_ALBUM: return MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
            case MODE_ARTIST:  return MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
            default: return null;
        }
    }

    private String[] getCorrectSelectionArgs(){
        switch(filterMode){
            case MODE_SONG: return new String[]{filter};
            case MODE_ALBUM: return new String[]{filter};
            case MODE_ARTIST: return new String[]{filter};
            case MODE_NO_FILTER: return null;
            default: return null;
        }
    }
    private String getCorrectSelection(){
        switch (filterMode){
            //case MODE_SONG: return MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            case MODE_ALBUM: return MediaStore.Audio.Media.ALBUM + "=?";
            case MODE_ARTIST: return MediaStore.Audio.Media.ARTIST + "=?";
            case MODE_NO_FILTER: return null;
            default: return null;
        }
    }

    private String getCorrectSortingString(){
        if(filterMode!=MODE_NO_FILTER){
            switch(filterMode){
                case MODE_ARTIST: return MediaStore.Audio.Media.TITLE + " ASC";
                default: return null;
            }
        }
        switch(mode){
            case MODE_SONG: return MediaStore.Audio.Media.TITLE + " ASC";
            case MODE_ALBUM: return MediaStore.Audio.Albums.ALBUM + " ASC";
            case MODE_ARTIST: return MediaStore.Audio.Artists.ARTIST + " ASC";
            default: return null;
        }
    }
}
