package android.cruciblecrab.quietmusicplayer;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
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

    private String[] SONG_PROJECTION = {
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DURATION
    };

    private int[] TO_VIEWS = {android.R.id.text1};

    private String[] FROM_COLUMNS = {
            MediaStore.Audio.Media.TITLE
    };



    public MediaSearcher(Context context, ListView list){
        this.list = list;
        this.context = context;
        adapter = new SimpleCursorAdapter(context,
                android.R.layout.simple_list_item_1, null,
                FROM_COLUMNS, TO_VIEWS, 0);
        list.setAdapter(adapter);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(context, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                SONG_PROJECTION, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
        cursor = data;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
