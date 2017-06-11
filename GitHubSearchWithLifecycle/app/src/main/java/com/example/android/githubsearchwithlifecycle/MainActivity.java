package com.example.android.githubsearchwithlifecycle;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
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
import android.widget.Toast;

import com.example.android.githubsearchwithlifecycle.utils.GitHubUtils;
import com.example.android.githubsearchwithlifecycle.utils.NetworkUtils;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GitHubSearchAdapter.OnSearchResultClickListener,
        LoaderManager.LoaderCallbacks<String> {

    private static final String SEARCH_RESULTS_LIST_KEY = "searchResultsList";
    private static final String SEARCH_URL = "githubSearchURL";

    //loaderId
    private static final Integer LOADER_ID = 1;

    private RecyclerView mSearchResultsRV;
    private EditText mSearchBoxET;
    private ProgressBar mLoadingIndicatorPB;
    private TextView mLoadingErrorMessageTV;
    private GitHubSearchAdapter mGitHubSearchAdapter;

    //new for lifecycle
    private ArrayList<GitHubUtils.SearchResult> mSearchResultList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchBoxET = (EditText)findViewById(R.id.et_search_box);
        mLoadingIndicatorPB = (ProgressBar)findViewById(R.id.pb_loading_indicator);
        mLoadingErrorMessageTV = (TextView)findViewById(R.id.tv_loading_error_message);
        mSearchResultsRV = (RecyclerView)findViewById(R.id.rv_search_results);

        mSearchResultsRV.setLayoutManager(new LinearLayoutManager(this));
        mSearchResultsRV.setHasFixedSize(true);

        mGitHubSearchAdapter = new GitHubSearchAdapter(this);
        mSearchResultsRV.setAdapter(mGitHubSearchAdapter);

//        if (savedInstanceState != null && savedInstanceState.containsKey(SEARCH_RESULTS_LIST_KEY)){
//            mSearchResultList = (ArrayList) savedInstanceState.getSerializable(SEARCH_RESULTS_LIST_KEY);
//            mGitHubSearchAdapter.updateSearchResults(mSearchResultList);
//        }

        //set Loader to Loader Manager
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);

        Button searchButton = (Button)findViewById(R.id.btn_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchQuery = mSearchBoxET.getText().toString();
                if (!TextUtils.isEmpty(searchQuery)) {
                    doGitHubSearch(searchQuery);
                }
            }
        });
    }

//    @Override
//    protected void onSaveInstanceState(Bundle outState){
//        super.onSaveInstanceState(outState);
//        if(mSearchResultList != null){
//            outState.putSerializable(SEARCH_RESULTS_LIST_KEY, mSearchResultList);
//        }
//    }

    private void doGitHubSearch(String searchQuery) {
        String githubSearchUrl = GitHubUtils.buildGitHubSearchURL(searchQuery);
        //Log.d("MainActivity", "got search url: " + githubSearchUrl);

        //Loader Removed
        //new GitHubSearchTask().execute(githubSearchUrl);

        Bundle argsBundle = new Bundle();
        argsBundle.putString(SEARCH_URL, githubSearchUrl);
        getSupportLoaderManager().restartLoader(LOADER_ID, argsBundle, this);
    }

    @Override
    public void onSearchResultClick(GitHubUtils.SearchResult searchResult) {
        Intent intent = new Intent(this, SearchResultDetailActivity.class);
        intent.putExtra(GitHubUtils.SearchResult.EXTRA_SEARCH_RESULT, searchResult);
        startActivity(intent);
    }

    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(this) {



            String mSearchResultsJSON;

            @Override
            protected void onStartLoading() {
                if(args != null) {
                    //mLoadingIndicatorPB.setVisibility(View.VISIBLE);
                    if(mSearchResultsJSON != null) {
                        Log.d("", "AsyncTaskLoader onStartLoading");
                        deliverResult(mSearchResultsJSON);
                    }
                    else {
                        mLoadingIndicatorPB.setVisibility(View.VISIBLE);
                        forceLoad();
                    }

                }
            }

            @Override
            public String loadInBackground(){
                if(args != null) {
                    String githubSearchUrl = args.getString(SEARCH_URL);
                    String searchResults = null;
                    try {
                        searchResults = NetworkUtils.doHTTPGet(githubSearchUrl);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return searchResults;
                }
                else{
                    return null;
                }
            }

            @Override
            public void deliverResult(String data) {
                mSearchResultsJSON = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        Log.d("", "AsyncTaskLoader's onLoadFinished called");
        mLoadingIndicatorPB.setVisibility(View.INVISIBLE);
        if (data != null) {
            mLoadingErrorMessageTV.setVisibility(View.INVISIBLE);
            mSearchResultsRV.setVisibility(View.VISIBLE);
            mSearchResultList = GitHubUtils.parseGitHubSearchResultsJSON(data);
            mGitHubSearchAdapter.updateSearchResults(mSearchResultList);
        } else {
            mSearchResultsRV.setVisibility(View.INVISIBLE);
            mLoadingErrorMessageTV.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        //nothing to do
    }

    //Change to AsyncTask Loader
//    public class GitHubSearchTask extends AsyncTask<String, Void, String> {
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
////            mLoadingIndicatorPB.setVisibility(View.VISIBLE);
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
////            String githubSearchUrl = params[0];
////            String searchResults = null;
////            try {
////                searchResults = NetworkUtils.doHTTPGet(githubSearchUrl);
////            } catch (IOException e) {
////                e.printStackTrace();
////            }
////            return searchResults;
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
////            mLoadingIndicatorPB.setVisibility(View.INVISIBLE);
////            if (s != null) {
////                mLoadingErrorMessageTV.setVisibility(View.INVISIBLE);
////                mSearchResultsRV.setVisibility(View.VISIBLE);
////                mSearchResultList = GitHubUtils.parseGitHubSearchResultsJSON(s);
////                mGitHubSearchAdapter.updateSearchResults(mSearchResultList);
////            } else {
////                mSearchResultsRV.setVisibility(View.INVISIBLE);
////                mLoadingErrorMessageTV.setVisibility(View.VISIBLE);
////            }
//        }
//    }
}
