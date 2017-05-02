package com.example.android.githubsearch.utils;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GitHubUtils {

    private final static String GITHUB_SEARCH_BASE_URL = "https://api.github.com/search/repositories";
    private final static String GITHUB_SEARCH_QUERY_PARAM = "q";
    private final static String GITHUB_SEARCH_SORT_PARAM = "sort";
    private final static String GITHUB_SEARCH_DEFAULT_SORT = "stars";

    public static class SearchResult {
        public String fullName;
        public String description;
        public String htmlURL;
        public int stars;
    }

    public static String buildGitHubSearchURL(String searchQuery) {
         return Uri.parse(GITHUB_SEARCH_BASE_URL).buildUpon()
                .appendQueryParameter(GITHUB_SEARCH_QUERY_PARAM, searchQuery)
                .appendQueryParameter(GITHUB_SEARCH_SORT_PARAM, GITHUB_SEARCH_DEFAULT_SORT)
                .build()
                .toString();


    }

    public static ArrayList<SearchResult> parseGitHubSearchResultsJSON(String searchResultsJSON){
        try {
            JSONObject searchResultsObj = new JSONObject(searchResultsJSON);
            JSONArray searchResultsItems = searchResultsObj.getJSONArray("items");
            ArrayList<SearchResult> searchResultArrayList = new ArrayList<SearchResult>();

            for (int i = 0; i < searchResultsItems.length(); i++)
            {
                SearchResult searchResult = new SearchResult();
                JSONObject searchResultItem = searchResultsItems.getJSONObject(i);

                searchResult.fullName = searchResultItem.getString("full_name");
                searchResult.description = searchResultItem.getString("description");
                searchResult.htmlURL = searchResultItem.getString("html_url");
                searchResult.stars = searchResultItem.getInt("stargazers_count");

                searchResultArrayList.add(searchResult);
            }

            return searchResultArrayList;
        } catch (JSONException e) {
            return null;
        }
    }

}
