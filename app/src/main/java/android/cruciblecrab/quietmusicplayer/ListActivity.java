package android.cruciblecrab.quietmusicplayer;

import android.content.Context;
import android.content.Intent;
import android.database.CursorWrapper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class ListActivity extends AppCompatActivity {

    public static final String TYPE_KEY = "typeKey1001";
    public static final String SONGS = "song1001";
    ListView list;
    MediaSearcher searcher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        list = (ListView)findViewById(R.id.listactivitylist);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
                CursorWrapper selected = (CursorWrapper)(list.getItemAtPosition(myItemInt));
                String text = selected.getString(2);
                int id_s = selected.getInt(0);
                MediaLogic.iid = id_s;
                listClick(6);
                //searcher.adapter.getItem(2);
                //String text = searcher.cursor.getString(2);


                Toast toast=Toast.makeText(getApplicationContext(), text+" "+myItemInt, Toast.LENGTH_SHORT);
                toast.show();

            }
        });
        Intent intent = getIntent();
        String mode = intent.getStringExtra(TYPE_KEY);
        if(mode.equals(SONGS)){
            if(searcher==null){
                searcher = new MediaSearcher(this, list);
                getLoaderManager().initLoader(0, null, searcher);
            }
        }
    }

    public void listClick(int id){
        Log.d("XXXXXXXXXXX", "HI!");
        Intent i= new Intent(getBaseContext(), MediaLogic.class);
        startService(i);
    }
}
