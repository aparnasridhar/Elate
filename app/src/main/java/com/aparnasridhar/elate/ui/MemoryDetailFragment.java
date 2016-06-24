package com.aparnasridhar.elate.ui;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aparnasridhar.elate.R;
import com.aparnasridhar.elate.data.MemoryLoader;
import com.squareup.picasso.Picasso;

/**
 * A fragment representing a single Article detail screen. This fragment is
 * either contained in a {@link MemoryListActivity} in two-pane mode (on
 * tablets) or a {@link MemoryDetailActivity} on handsets.
 */
public class MemoryDetailFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "ArticleDetailFragment";

    public static final String ARG_ITEM_ID = "item_id";
    private static final float PARALLAX_FACTOR = 1.25f;

    private Cursor mCursor;
    private long mItemId;
    private View mRootView;
    private CollapsingToolbarLayout mLayout;

    private ImageView mPhotoView;
    private Toolbar mToolbar;
    private float alphaVal;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MemoryDetailFragment() {
    }

    public static MemoryDetailFragment newInstance(long itemId) {
        Bundle arguments = new Bundle();
        arguments.putLong(ARG_ITEM_ID, itemId);
        MemoryDetailFragment fragment = new MemoryDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            mItemId = getArguments().getLong(ARG_ITEM_ID);
        }

        setHasOptionsMenu(true);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // In support library r8, calling initLoader for a fragment in a FragmentPagerAdapter in
        // the fragment's onCreate may cause the same LoaderManager to be dealt to multiple
        // fragments because their mIndex is -1 (haven't been added to the activity yet). Thus,
        // we do this in onActivityCreated.
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_memory_detail, container, false);

        mPhotoView = (ImageView) mRootView.findViewById(R.id.article_banner_image);

        mToolbar = (Toolbar) mRootView.findViewById(R.id.toolbar);
        alphaVal = mToolbar.getBackground().getAlpha();

        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.setSupportActionBar(mToolbar);
        mToolbar.getBackground().setAlpha(0);

        if (appCompatActivity.getSupportActionBar() != null) {
            appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }

        mLayout = (CollapsingToolbarLayout) mRootView.findViewById(R.id.collapsing_toolbar);

        mRootView.findViewById(R.id.share_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(getActivity())
                        .setType("text/plain")
                        .setText("Some sample text")
                        .getIntent(), getString(R.string.action_share)));
            }
        });

        bindViews();

        return mRootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mToolbar !=null ){
            mToolbar.setAlpha(alphaVal);
        }
    }

    private void bindViews() {
        if (mRootView == null) {
            return;
        }
        TextView articleTitleView = (TextView) mRootView.findViewById(R.id.article_title);
        TextView bylineView = (TextView) mRootView.findViewById(R.id.article_byline);
        bylineView.setMovementMethod(new LinkMovementMethod());
        TextView bodyView = (TextView) mRootView.findViewById(R.id.article_body);
        bodyView.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "Rosario-Regular.ttf"));

        if (mCursor != null) {
            mRootView.setAlpha(0);
            mRootView.setVisibility(View.VISIBLE);
            mRootView.animate().alpha(1);

            articleTitleView.setText(mCursor.getString(MemoryLoader.Query.TITLE));
           // mLayout.setTitle(mCursor.getString(ArticleLoader.Query.TITLE));
            mLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);
            mLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);

            String author = mCursor.getString(MemoryLoader.Query.AUTHOR);
            String fulldate = DateUtils.getRelativeTimeSpanString(mCursor.getLong(MemoryLoader.Query.PUBLISHED_DATE)).toString();

            bylineView.setText("By " + author + ", " + fulldate);

            bodyView.setText(Html.fromHtml(mCursor.getString(MemoryLoader.Query.BODY)));

            String url = mCursor.getString(MemoryLoader.Query.PHOTO_URL);
            if (url != null) {
                Picasso.with(getActivity()).load(url).into(mPhotoView);
            }
        } else {
            mRootView.setVisibility(View.GONE);
            bylineView.setText("N/A" );
            bodyView.setText("N/A");
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return MemoryLoader.newInstanceForItemId(getActivity(), mItemId);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        if (!isAdded()) {
            if (cursor != null) {
                cursor.close();
            }
            return;
        }

        mCursor = cursor;
        if (mCursor != null && !mCursor.moveToFirst()) {
            Log.e(TAG, "Error reading item detail cursor");
            mCursor.close();
            mCursor = null;
        }

        bindViews();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mCursor = null;
        bindViews();
    }
}
