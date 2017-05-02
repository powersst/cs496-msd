package com.example.android.githubsearch;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.githubsearch.utils.GitHubUtils;
import com.example.android.githubsearch.utils.NetworkUtils;

import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private RecyclerView mSearchResultsRV;
    private EditText mSearchBoxET;
    private ProgressBar mLoadingIndicatorPB;
    private TextView mLoadingErrorMessageTV;
    private GitHubSearchAdapter mGitHubSearchAdapter;


    private String[] dummySearchResults = {
            "Dummy search results",
            "Dummy search results",
            "Dummy search results",
            "Dummy search results",
            "Dummy search results",
            "Dummy search results",
            "Dummy search results",
            "Dummy search results",
            "Dummy search results",
            "Dummy search results"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchBoxET = (EditText)findViewById(R.id.et_search_box);
        mLoadingIndicatorPB = (ProgressBar)findViewById(R.id.pb_loading_indicator);
        mLoadingErrorMessageTV = (TextView)findViewById(R.id.tv_load_error);
        mSearchResultsRV = (RecyclerView)findViewById(R.id.rv_search_results);

        mSearchResultsRV.setLayoutManager(new LinearLayoutManager(this));
        mSearchResultsRV.setHasFixedSize(true);

        mGitHubSearchAdapter = new GitHubSearchAdapter();
        mSearchResultsRV.setAdapter(mGitHubSearchAdapter);

        Button searchButton = (Button)findViewById(R.id.btn_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchQuery = mSearchBoxET.getText().toString();
                if (!TextUtils.isEmpty(searchQuery)) {
                    doGitHubSearch(searchQuery);
                    mSearchBoxET.setText("");
                }
            }
        });
    }

    private void doGitHubSearch(String searchQuery) {
        String githubSearchUrl = GitHubUtils.buildGitHubSearchURL(searchQuery);
        Log.d("MainActivity", "got search url: " + githubSearchUrl.toString());
        new GitHubSearchTask().execute(githubSearchUrl);
    }

    public class GitHubSearchTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicatorPB.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {
            String githubSearchUrl = params[0];
            String searchResults = null;
            try {
                searchResults = NetworkUtils.doHTTPGet(githubSearchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return searchResults;
        }

        @Override
        protected void onPostExecute(String s) {
            mLoadingIndicatorPB.setVisibility(View.INVISIBLE);
            if (s != null) {
                mLoadingErrorMessageTV.setVisibility(View.INVISIBLE);
                mSearchResultsRV.setVisibility(View.VISIBLE);
                ArrayList<GitHubUtils.SearchResult> searchResultsList = GitHubUtils.parseGitHubSearchResultsJSON(s);
//                ArrayList<String> searchResultsList = new ArrayList<String>();
//                searchResultsList.add(s);
                mGitHubSearchAdapter.updateSearchResults(searchResultsList);
            }
            {
                mLoadingErrorMessageTV.setVisibility(View.VISIBLE);
                mSearchResultsRV.setVisibility(View.INVISIBLE);
            }
        }
    }
}
