package com.jomik.apparelapp.infrastructure.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Joe Deluca of house targaryen, first his name, mother of dragons and breaker of chains on 7/1/2016.
 */
public class ApparelSyncService extends Service {
    private static final Object lock = new Object();
    private static ApparelSyncAdapter syncAdapter = null;

    @Override
    public void onCreate() {
        super.onCreate();

        synchronized (lock) {
            if (syncAdapter == null) {
                syncAdapter = new ApparelSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return syncAdapter.getSyncAdapterBinder();
    }
}
