package com.aparnasridhar.elate.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aparnasridhar.elate.R;
import com.aparnasridhar.elate.data.MemoryLoader;
import com.aparnasridhar.elate.data.MemoryContract;
import com.squareup.picasso.Picasso;

/**
 * Created by saparna on 9/19/15.
 */
public class MemoryListAdapter extends RecyclerView.Adapter<MemoryListAdapter.ViewHolder> {
    private Cursor mCursor;
    private Context mContext;

    public MemoryListAdapter(Cursor cursor, Context context) {
        mCursor = cursor;
        mContext = context;
    }

    @Override
    public long getItemId(int position) {
        mCursor.moveToPosition(position);
        return mCursor.getLong(MemoryLoader.Query._ID);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_memory, parent, false);
        final ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        MemoryContract.Items.buildItemUri(getItemId(vh.getAdapterPosition())));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        String type = mCursor.getString(MemoryLoader.Query.TYPE);
        mCursor.moveToPosition(position);
        holder.titleView.setText(mCursor.getString(MemoryLoader.Query.TITLE));
        holder.subtitleView.setText(
                DateUtils.getRelativeTimeSpanString(
                        mCursor.getLong(MemoryLoader.Query.PUBLISHED_DATE),
                        System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
                        DateUtils.FORMAT_ABBREV_ALL).toString());
        String url = mCursor.getString(MemoryLoader.Query.PHOTO_URL);



        if (url != null && type.equalsIgnoreCase("picture")) {
            Picasso.with(mContext).load(url).into(holder.thumbnailView);
        } else if (type.equalsIgnoreCase("video")){
            holder.thumbnailView.setImageResource(R.drawable.videoplayer);
        }else if (type.equalsIgnoreCase("audio")){
            holder.thumbnailView.setImageResource(R.drawable.audio);
        }else if (type.equalsIgnoreCase("text")){
            holder.thumbnailView.setImageResource(R.drawable.note);
        }
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnailView;
        public TextView titleView;
        public TextView subtitleView;

        public ViewHolder(View view) {
            super(view);
            thumbnailView = (ImageView) view.findViewById(R.id.article_image);
            titleView = (TextView) view.findViewById(R.id.article_title);
            subtitleView = (TextView) view.findViewById(R.id.article_subtitle);

        }
    }
}
