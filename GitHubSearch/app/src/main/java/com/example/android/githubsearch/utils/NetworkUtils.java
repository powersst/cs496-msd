package com.example.android.githubsearch.utils;

import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by hessro on 4/25/17.
 */

public class NetworkUtils {

    //okhttp client
    private static final OkHttpClient mHTTPClient = new OkHttpClient();


    public static String doHTTPGet(String url) throws IOException {

        Request request = new Request.Builder().url(url).build();

        Response response = mHTTPClient.newCall(request).execute();


        try {
            return response.body().string();
        } finally {
            response.close();
        }


        //Old Version
//        HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
//        try {
//            InputStream inputStream = urlConnection.getInputStream();
//
//            Scanner scanner = new Scanner(inputStream);
//            scanner.useDelimiter("\\A");
//
//            if (scanner.hasNext()) {
//                return scanner.next();
//            } else {
//                return null;
//            }
//        } finally {
//            urlConnection.disconnect();
//        }
        //End Old Version

    }
}
