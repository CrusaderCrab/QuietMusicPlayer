package android.cruciblecrab.quietmusicplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ListActivity extends AppCompatActivity {

    public static final String TYPE_KEY = "typeKey1001";
    public static final String SONGS = "song1001";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
    }
}
