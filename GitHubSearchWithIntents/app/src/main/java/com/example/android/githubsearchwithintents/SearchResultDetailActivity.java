package com.example.android.githubsearchwithintents;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.android.githubsearchwithintents.utils.GitHubUtils;

import java.io.Serializable;

public class SearchResultDetailActivity extends AppCompatActivity {

    private TextView mSearchResultName;
    private TextView mSearchResultDesc;
    private TextView mSearchResultStars;

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
            GitHubUtils.SearchResult searchResult = (GitHubUtils.SearchResult) intent.getSerializableExtra(GitHubUtils.SearchResult.EXTRA_SEARCH_RESULT);

            mSearchResultName.setText(searchResult.fullName);
            mSearchResultDesc.setText(searchResult.description);
            mSearchResultStars.setText(Integer.toString(searchResult.stars));
        }
    }

}
