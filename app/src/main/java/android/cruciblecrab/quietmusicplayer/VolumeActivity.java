package android.cruciblecrab.quietmusicplayer;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.logging.Handler;

public class VolumeActivity extends MediaControlsActivity {

    TextView volumeText;
    SeekBar seekBar;
    android.os.Handler handler;
    MediaControls mediaControls;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volume);

        Intent serviceIntent = new Intent(this, MediaLogic.class);
        startService(serviceIntent);

        volumeText = (TextView)findViewById(R.id.volumetext);
        setupMediaControls();
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        VolumeController.init(this, volumeText);
        if(!MediaLogic.ready()) {
            Bundle miscValues = SongInfoManager.retrieveMiscData(this);
            float savedVolume = miscValues.getFloat(SongInfoManager.KEY_VOLUME, MediaLogic.DEFAULT_VOLUME);
            setVolumeText(savedVolume);
        }else{
            setVolumeText(MediaLogic.getInterface().getVolume());
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d("XXX_VA", "DEAD_DEAD");
        //VolumeController.removeMenuItem();
        removeMediaControls();
        VolumeController.saveVolumes(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        setVolumeButtonText();
        Button playButton = (Button) findViewById(R.id.playbutton);
        //mediaControls.preparePlayButton(playButton);
        MediaControls.setAllPlayButtons(MediaControls.playerPlaying);
        MediaControls.setToSongName((TextView) findViewById(R.id.songtext));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.volume_volume, menu);
        MenuItem mi = menu.findItem(R.id.action_save_volumes);
        VolumeController.addMenuItem(mi);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_save_volumes:
                VolumeController.menuItemClicked();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(menuItem);

        }
    }



    public void songButtonClick(View view) {
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra(ListActivity.TYPE_KEY, ListActivity.KEY_SONGS);
        intent.putExtra(ListActivity.EXTRA_INFO, ListActivity.NO_INFO);
        intent.putExtra(ListActivity.FILTER_MODE, MediaSearcher.MODE_NO_FILTER);
        startActivity(intent);
    }

    public void albumButtonClick(View view) {
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra(ListActivity.TYPE_KEY, ListActivity.KEY_ALBUMS);
        intent.putExtra(ListActivity.EXTRA_INFO, ListActivity.NO_INFO);
        intent.putExtra(ListActivity.FILTER_MODE, MediaSearcher.MODE_NO_FILTER);
        startActivity(intent);
    }

    public void artistButtonClick(View view) {
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra(ListActivity.TYPE_KEY, ListActivity.KEY_ARTISTS);
        intent.putExtra(ListActivity.EXTRA_INFO, ListActivity.NO_INFO);
        intent.putExtra(ListActivity.FILTER_MODE, MediaSearcher.MODE_NO_FILTER);
        startActivity(intent);
    }

    public void playListButtonClick(View view){
        Intent intent = new Intent(this, PlayListsActivity.class);
        startActivity(intent);
    }

    public void volumeUpClick(View view) {
        if(MediaLogic.ready()) {
            float vol = MediaLogic.getInterface().alterVolume(MediaLogic.VOLUME_STEP);
            setVolumeText(vol);
        }
    }

    public void volumeDownClick(View view) {
        if(MediaLogic.ready()) {
            float vol = MediaLogic.getInterface().alterVolume(-MediaLogic.VOLUME_STEP);
            setVolumeText(vol);
        }
    }

    private void setVolumeText(float vol) {
        int v = (int) (Math.round(vol * 100));
        volumeText.setText(v+"");
    }

    private void setVolumeButtonText(){
        if(MediaLogic.ready()){
            float vol = MediaLogic.getInterface().getVolume();
            setVolumeText(vol);
        }
    }
}
