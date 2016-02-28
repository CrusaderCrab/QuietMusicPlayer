package android.cruciblecrab.quietmusicplayer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class VolumeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volume);
    }



    public void songButtonClick(View view){
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra(ListActivity.TYPE_KEY, ListActivity.SONGS);
        startActivity(intent);
    }
}
