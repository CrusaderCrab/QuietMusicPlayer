package android.cruciblecrab.quietmusicplayer;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by CrusaderCrab on 03/03/2016.
 */
public class MediaLogicConnection implements ServiceConnection{

    public static IBinder BINDER;
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        Log.d("XXX_M.L.C_Connected", "got binder = " + service);
        if(BINDER==null)
            BINDER = service;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    public static MediaLogic.LocalBinder getBinder(){
        return (MediaLogic.LocalBinder) BINDER;
    }
}
