package android.cruciblecrab.quietmusicplayer;

import android.content.Context;
import android.content.Intent;

/**
 * Created by CrusaderCrab on 06/03/2016.
 */
public class MusicIntentReceiver extends android.content.BroadcastReceiver {
    @Override
    public void onReceive(Context ctx, Intent intent) {
        if (intent.getAction().equals(
                android.media.AudioManager.ACTION_AUDIO_BECOMING_NOISY)) {
            if(MediaLogic.ready()) {
                MediaLogic.LocalBinder binder = MediaLogic.getInterface();
                binder.pauseSong();
            }
        }
    }
}
