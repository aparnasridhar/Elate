package com.aparnasridhar.elate.ui;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.aparnasridhar.elate.R;
import com.aparnasridhar.elate.data.MemoryContract;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by aparnasridhar on 5/24/16.
 */
public class CreateMemoryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_memory);
        // Set up the login form.
        ImageButton pictureButton = (ImageButton)findViewById(R.id.pictureButton);
        Button saveButton =(Button)findViewById(R.id.saveButton);

        pictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        final ImageButton noteButton = (ImageButton)findViewById(R.id.noteButton);
        final EditText noteText = (EditText)findViewById(R.id.noteEntry);
        final EditText title = (EditText)findViewById(R.id.memory_title);


        noteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noteText.setVisibility(View.VISIBLE);
                noteButton.setSelected(true);
                noteButton.setBackgroundColor(getResources().getColor(R.color.color_theme_primary_dark));
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String quote = noteText.getText().toString();
                // Don't even inspect the intent, we only do one thing, and that's fetch content.
                ArrayList<ContentProviderOperation> cpo = new ArrayList<ContentProviderOperation>();

                Uri dirUri = MemoryContract.Items.buildDirUri();

                ContentValues values = new ContentValues();
                values.put(MemoryContract.Items.SERVER_ID, 5);
                values.put(MemoryContract.Items.AUTHOR, "aparna");
                values.put(MemoryContract.Items.TITLE, title.getText().toString());
                values.put(MemoryContract.Items.BODY, quote);
                values.put(MemoryContract.Items.THUMB_URL, "http://www.google.com");
                values.put(MemoryContract.Items.PHOTO_URL, "http://www.google.com");
                Date date = new Date();
                values.put(MemoryContract.Items.PUBLISHED_DATE, date.getTime());
                values.put(MemoryContract.Items.TYPE, "Text");
                cpo.add(ContentProviderOperation.newInsert(dirUri).withValues(values).build());

                try {
                    getContentResolver().applyBatch(MemoryContract.CONTENT_AUTHORITY, cpo);
                }catch ( RemoteException | OperationApplicationException e) {
                    Log.e("Problem", "Error updating content.", e);
                }
            }
        });
    }


}
