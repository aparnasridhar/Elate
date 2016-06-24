package com.aparnasridhar.elate.data;

import android.content.Context;
import android.content.CursorLoader;
import android.net.Uri;

/**
 * Helper for loading a list of articles or a single article.
 */
public class MemoryLoader extends CursorLoader {
    public static MemoryLoader newAllArticlesInstance(Context context) {
        return new MemoryLoader(context, MemoryContract.Items.buildDirUri());
    }

    public static MemoryLoader newInstanceForItemId(Context context, long itemId) {
        return new MemoryLoader(context, MemoryContract.Items.buildItemUri(itemId));
    }

    private MemoryLoader(Context context, Uri uri) {
        super(context, uri, Query.PROJECTION, null, null, MemoryContract.Items.DEFAULT_SORT);
    }

    public interface Query {
        String[] PROJECTION = {
                MemoryContract.Items._ID,
                MemoryContract.Items.TITLE,
                MemoryContract.Items.PUBLISHED_DATE,
                MemoryContract.Items.AUTHOR,
                MemoryContract.Items.THUMB_URL,
                MemoryContract.Items.PHOTO_URL,
                MemoryContract.Items.ASPECT_RATIO,
                MemoryContract.Items.BODY,
                MemoryContract.Items.TYPE,
        };

        int _ID = 0;
        int TITLE = 1;
        int PUBLISHED_DATE = 2;
        int AUTHOR = 3;
        int THUMB_URL = 4;
        int PHOTO_URL = 5;
        int ASPECT_RATIO = 6;
        int BODY = 7;
        int TYPE = 8;
    }
}
