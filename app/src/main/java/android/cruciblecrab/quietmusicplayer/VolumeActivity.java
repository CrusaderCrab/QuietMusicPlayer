package android.cruciblecrab.quietmusicplayer;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
        boolean res = bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE);
        binder = MediaLogicConnection.getBinder();
        Log.d("XXX_V.A_create", "res= " + res + " binder= " + binder + " BINDER= " + connection.BINDER);

        mediaControls = new MediaControls();
        handler = new android.os.Handler();
        //Make sure you update Seekbar on UI thread
        VolumeActivity.this.runOnUiThread(new SeekbarRunnable(handler, seekBar));
        seekBar.setOnSeekBarChangeListener(mediaControls.seekBarChangeListener());

        Button playButton = (Button) findViewById(R.id.playbutton);
        playButton.setOnClickListener(mediaControls.playButtonListener());
        mediaControls.preparePlayButton(playButton);
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
        volumeText.setText(v + " : " + vol);
    }
}
