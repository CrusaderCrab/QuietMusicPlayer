package android.cruciblecrab.quietmusicplayer;

import android.database.CursorWrapper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class PlayListsActivity extends AppCompatActivity {

    ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_lists);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        list = (ListView)findViewById(R.id.playlistactivitylist);

        ArrayList<String> playlists = new ArrayList<String>();
        playlists.add("hi");
        playlists.add("bye");
        playlists.add("Mouth Silences");

        list.setAdapter(
                new ArrayAdapter<String>(this,
                        R.layout.layout_playlists_list_row, R.id.playlistname, playlists)
        );

        list.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
                        Toast.makeText(PlayListsActivity.this, "Pos: "+myItemInt+" clicked.", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    public void playlistsOptionsClick(View v) {
        final int position = list.getPositionForView(v);
        if (position != ListView.INVALID_POSITION) {
            Toast.makeText(PlayListsActivity.this, "Pos of button: "+position, Toast.LENGTH_SHORT).show();
        }
    }

}

