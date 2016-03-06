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

public class VolumeActivity extends AppCompatActivity {

    MediaLogic.LocalBinder binder;
    TextView volumeText;
    SeekBar seekBar;
    android.os.Handler handler;
    MediaControls mediaControls;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volume);
        volumeText = (TextView)findViewById(R.id.volumetext);
        seekBar = (SeekBar) findViewById(R.id.seekBar);

        MediaLogicConnection connection = new MediaLogicConnection();
        Intent serviceIntent = new Intent(this, MediaLogic.class);
        startService(serviceIntent);
        binder = MediaLogicConnection.getBinder();

        mediaControls = new MediaControls();
        handler = new android.os.Handler();
        VolumeActivity.this.runOnUiThread(new SeekbarRunnable(handler, seekBar));
        seekBar.setOnSeekBarChangeListener(mediaControls.seekBarChangeListener());
        TextView durationText = (TextView)findViewById(R.id.timetext);
        VolumeActivity.this.runOnUiThread(new DurationRunnable(handler, durationText));

        Button playButton = (Button) findViewById(R.id.playbutton);
        playButton.setOnClickListener(mediaControls.playButtonListener());
        //mediaControls.preparePlayButton(playButton);
        MediaControls.setAllPlayButtons(MediaControls.playerPlaying);
        MediaControls.addPlayButton(playButton);
        Button prevButton = (Button) findViewById(R.id.prevbutton);
        prevButton.setOnClickListener(mediaControls.prevButtonListener());
        Button nextButton = (Button) findViewById(R.id.nextbutton);
        nextButton.setOnClickListener(mediaControls.nextButtonListener());

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        VolumeController.init(this, volumeText);

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        //VolumeController.removeMenuItem();
        VolumeController.saveVolumes(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Button playButton = (Button) findViewById(R.id.playbutton);
        //mediaControls.preparePlayButton(playButton);
        MediaControls.setAllPlayButtons(MediaControls.playerPlaying);
        MediaControls.setToSongName((TextView)findViewById(R.id.songtext));
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

    public void volumeUpClick(View view) {
        binder = MediaLogicConnection.getBinder();
        float vol = binder.alterVolume(MediaLogic.VOLUME_STEP);
        setVolumeText(vol);
    }

    public void volumeDownClick(View view) {
        binder = MediaLogicConnection.getBinder();
        float vol = binder.alterVolume(-MediaLogic.VOLUME_STEP);
        setVolumeText(vol);
    }

    private void setVolumeText(float vol) {
        int v = (int) (Math.round(vol * 100));
        volumeText.setText(v+"");
    }
}
