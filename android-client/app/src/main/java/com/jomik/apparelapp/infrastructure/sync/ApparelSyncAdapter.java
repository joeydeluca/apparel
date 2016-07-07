package com.jomik.apparelapp.infrastructure.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Joe Deluca of house targaryen, first his name, mother of dragons and breaker of chains on 7/1/2016.
 */
public class ApparelSyncAdapter extends AbstractThreadedSyncAdapter {

    private final AccountManager accountManager;

    public ApparelSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        accountManager = AccountManager.get(context);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.i(ApparelSyncAdapter.class.getCanonicalName(), "Start Sync Adapter");

        try {
            //String authToken = accountManager.blockingGetAuthToken(account, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS, true);


        } catch(Exception e) {

        }

        Log.i(ApparelSyncAdapter.class.getCanonicalName(), "End Sync Adapter");
    }
}
