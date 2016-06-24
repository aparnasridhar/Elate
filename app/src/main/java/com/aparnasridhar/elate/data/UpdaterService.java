package com.aparnasridhar.elate.data;

import android.app.IntentService;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.RemoteException;
import android.text.format.Time;
import android.util.Log;

import com.aparnasridhar.elate.remote.RemoteEndpointUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UpdaterService extends IntentService {
    private static final String TAG = "UpdaterService";

    public static final String BROADCAST_ACTION_STATE_CHANGE
            = "com.aparnasridhar.com.intent.action.STATE_CHANGE";
    public static final String EXTRA_REFRESHING
            = "com.aparnasridhar.com.intent.extra.REFRESHING";

    public UpdaterService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Time time = new Time();

        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null || !ni.isConnected()) {
            Log.w(TAG, "Not online, not refreshing.");
            return;
        }

        sendStickyBroadcast(
                new Intent(BROADCAST_ACTION_STATE_CHANGE).putExtra(EXTRA_REFRESHING, true));

        // Don't even inspect the intent, we only do one thing, and that's fetch content.
        ArrayList<ContentProviderOperation> cpo = new ArrayList<ContentProviderOperation>();

        Uri dirUri = MemoryContract.Items.buildDirUri();

        // Delete all items
        cpo.add(ContentProviderOperation.newDelete(dirUri).build());

        try {
            JSONArray array = RemoteEndpointUtil.fetchJsonArray();
            if (array == null) {
                throw new JSONException("Invalid parsed item array" );
            }

            for (int i = 0; i < array.length(); i++) {
                ContentValues values = new ContentValues();
                JSONObject object = array.getJSONObject(i);
                values.put(MemoryContract.Items.SERVER_ID, object.getString("id" ));
                values.put(MemoryContract.Items.AUTHOR, object.getString("author" ));
                values.put(MemoryContract.Items.TITLE, object.getString("title" ));
                values.put(MemoryContract.Items.BODY, object.getString("body" ));
                values.put(MemoryContract.Items.THUMB_URL, object.getString("thumb" ));
                values.put(MemoryContract.Items.PHOTO_URL, object.getString("photo" ));
                values.put(MemoryContract.Items.ASPECT_RATIO, object.getString("aspect_ratio" ));
                time.parse3339(object.getString("published_date"));
                values.put(MemoryContract.Items.PUBLISHED_DATE, time.toMillis(false));
                values.put(MemoryContract.Items.TYPE, object.getString("type"));
                cpo.add(ContentProviderOperation.newInsert(dirUri).withValues(values).build());
            }

            getContentResolver().applyBatch(MemoryContract.CONTENT_AUTHORITY, cpo);

        } catch (JSONException | RemoteException | OperationApplicationException e) {
            Log.e(TAG, "Error updating content.", e);
        }

        sendStickyBroadcast(
                new Intent(BROADCAST_ACTION_STATE_CHANGE).putExtra(EXTRA_REFRESHING, false));
    }
}
