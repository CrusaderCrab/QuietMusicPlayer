package android.cruciblecrab.quietmusicplayer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by CrusaderCrab on 16/03/2016.
 */
public abstract class MediaControlsActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    protected void setupMediaControls(){
        MediaControls mediaControls = new MediaControls();
        //seekbar
        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
        android.os.Handler handler = new android.os.Handler();
        this.runOnUiThread(new SeekbarRunnable(handler, seekBar));
        //current time
        TextView durationText = (TextView)findViewById(R.id.timetext);
        this.runOnUiThread(new DurationRunnable(handler, durationText));
        //song name
        TextView nameText = (TextView)findViewById(R.id.songtext);
        MediaControls.addSongName(nameText);
        //play button
        Button playButton = (Button) findViewById(R.id.playbutton);
        playButton.setOnClickListener(mediaControls.playButtonListener());
        MediaControls.setAllPlayButtons(MediaControls.playerPlaying);
        MediaControls.addPlayButton(playButton);
        //prev button
        Button prevButton = (Button) findViewById(R.id.prevbutton);
        prevButton.setOnClickListener(mediaControls.prevButtonListener());
        //next button
        Button nextButton = (Button) findViewById(R.id.nextbutton);
        nextButton.setOnClickListener(mediaControls.nextButtonListener());


    }

    protected void removeMediaControls(){
        //seekbar, no need
        //current time, no need
        //song name
        TextView nameText = (TextView)findViewById(R.id.songtext);
        MediaControls.removeSongName(nameText);
        //play button
        Button playButton = (Button) findViewById(R.id.playbutton);
        MediaControls.removePlayButton(playButton);
        //next button, no need
        //prev button, no need

    }

}
