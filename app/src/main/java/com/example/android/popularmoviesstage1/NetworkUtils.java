package com.example.android.popularmoviesstage1;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

class NetworkUtils {
    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String BY_POPULARITY = "popular";
    private static final String BY_RATING = "top_rated";
    private static final String REVIEWS_URL = "reviews";
    private static final String TRAILERS_URL = "videos";
    private static final String API_KEY = BuildConfig.TMDB_API_KEY;

    static URL buildListURL(String filter) {
        //Build Uri based on whether is sorting type, get poster, etc
        String filterURL;

        if (filter.equals("Best Rated")) {
            filterURL = BASE_URL + BY_RATING;
        } else {
            filterURL = BASE_URL + BY_POPULARITY;
        }

        Uri builtUri = Uri.parse(filterURL).buildUpon()
                .appendQueryParameter("api_key", API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    static URL buildReviewsURL(int movieId) {
        //Build Uri to get a movie's reviews
        String reviewURL;

        if (movieId == 0) {
            return null;
        } else {
            reviewURL = BASE_URL + Integer.toString(movieId) + "/" + REVIEWS_URL;
        }

        Uri builtUri = Uri.parse(reviewURL).buildUpon()
                .appendQueryParameter("api_key", API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    static URL buildTrailersURL(int movieId) {
        //Build Uri to get a movie's trailers
        String trailerURL;

        if (movieId == 0) {
            return null;
        } else {
            trailerURL = BASE_URL + Integer.toString(movieId) + "/" + TRAILERS_URL;
        }

        Uri builtUri = Uri.parse(trailerURL).buildUpon()
                .appendQueryParameter("api_key", API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    static Boolean isNetworkAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }
}