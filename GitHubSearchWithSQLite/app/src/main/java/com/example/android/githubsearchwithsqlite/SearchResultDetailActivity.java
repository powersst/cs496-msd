package com.example.android.githubsearchwithsqlite;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.githubsearchwithsqlite.utils.GitHubUtils;

public class SearchResultDetailActivity extends AppCompatActivity {
    private ImageView mSearchResultBookmarkIV;
    private TextView mSearchResultNameTV;
    private TextView mSearchResultDescriptionTV;
    private TextView mSearchResultStarsTV;
    private GitHubUtils.SearchResult mSearchResult;
    private boolean mIsBookmarked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result_detail);

        mSearchResultBookmarkIV = (ImageView)findViewById(R.id.iv_search_result_bookmark);
        mSearchResultNameTV = (TextView)findViewById(R.id.tv_search_result_name);
        mSearchResultDescriptionTV = (TextView)findViewById(R.id.tv_search_result_description);
        mSearchResultStarsTV = (TextView)findViewById(R.id.tv_search_result_stars);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(GitHubUtils.SearchResult.EXTRA_SEARCH_RESULT)) {
            mSearchResult = (GitHubUtils.SearchResult)intent.getSerializableExtra(GitHubUtils.SearchResult.EXTRA_SEARCH_RESULT);
            mSearchResultNameTV.setText(mSearchResult.fullName);
            mSearchResultDescriptionTV.setText(mSearchResult.description);
            mSearchResultStarsTV.setText(Integer.toString(mSearchResult.stars));
        }

        mSearchResultBookmarkIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsBookmarked = !mIsBookmarked;
                if (mIsBookmarked) {
                    mSearchResultBookmarkIV.setImageResource(R.drawable.ic_bookmark_black_48dp);
                } else {
                    mSearchResultBookmarkIV.setImageResource(R.drawable.ic_bookmark_border_black_48dp);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_view_repo:
                viewRepoOnWeb();
                return true;
            case R.id.action_share:
                shareRepo();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_result_detail, menu);
        return true;
    }

    public void viewRepoOnWeb() {
        if (mSearchResult != null) {
            Uri repoUri = Uri.parse(mSearchResult.htmlURL);
            Intent webIntent = new Intent(Intent.ACTION_VIEW, repoUri);
            if (webIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(webIntent);
            }
        }
    }

    public void shareRepo() {
        if (mSearchResult != null) {
            String shareText = mSearchResult.fullName + ": " + mSearchResult.htmlURL;
            ShareCompat.IntentBuilder.from(this)
                    .setType("text/plain")
                    .setText(shareText)
                    .setChooserTitle(R.string.share_chooser_title)
                    .startChooser();
        }
    }
}
