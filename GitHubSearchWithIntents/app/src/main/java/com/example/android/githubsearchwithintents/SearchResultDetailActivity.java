package com.example.android.githubsearchwithintents;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.TextView;

import com.example.android.githubsearchwithintents.utils.GitHubUtils;

import java.io.Serializable;

public class SearchResultDetailActivity extends AppCompatActivity {

    private TextView mSearchResultName;
    private TextView mSearchResultDesc;
    private TextView mSearchResultStars;
    private GitHubUtils.SearchResult mSearchResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result_detail);

        mSearchResultName = (TextView)findViewById(R.id.tv_search_result_name);
        mSearchResultDesc = (TextView)findViewById(R.id.tv_search_result_desc);
        mSearchResultStars = (TextView)findViewById(R.id.tv_search_result_stars);

        //Gets the intent that created this activity
        Intent intent = getIntent();
        if(intent != null && intent.hasExtra(GitHubUtils.SearchResult.EXTRA_SEARCH_RESULT)){
            mSearchResult = (GitHubUtils.SearchResult) intent.getSerializableExtra(GitHubUtils.SearchResult.EXTRA_SEARCH_RESULT);

            mSearchResultName.setText(mSearchResult.fullName);
            mSearchResultDesc.setText(mSearchResult.description);
            mSearchResultStars.setText(Integer.toString(mSearchResult.stars));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
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
        if(mSearchResult != null){
            Uri repoUri = Uri.parse(mSearchResult.htmlURL);

            //implicit intent to open a webpage
            Intent webIntent = new Intent(Intent.ACTION_VIEW, repoUri);

            //Check to see if a web browser is available
            if(webIntent.resolveActivity(getPackageManager()) != null){
                startActivity(webIntent);
            }
        }
    }

    public void shareRepo() {
        if(mSearchResult != null){
            String shareText = mSearchResult.fullName + ": " + mSearchResult.htmlURL;
            ShareCompat.IntentBuilder.from(this)
                    .setType("text/plain")
                    .setText(shareText)
                    .setChooserTitle(R.string.share_chooser_title)
                    .startChooser();
        }
    }
}
