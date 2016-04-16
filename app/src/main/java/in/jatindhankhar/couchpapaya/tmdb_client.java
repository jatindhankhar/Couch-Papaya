package in.jatindhankhar.couchpapaya;


import android.net.Uri;

/**
 * Created by jatin on 27/3/16.
 */

public class tmdb_client {

    private final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    private final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w342/";
    private final String API_KEY = BuildConfig.TMDB_API_TOKEN;

    public void tmdb_client() {

    }

    public String get_api_path_for(String path) {
        return Uri.parse(BASE_URL).
                buildUpon().
                appendPath(path).
                appendQueryParameter("api_key", API_KEY).
                build().
                toString();
    }

    public String get_image_path(String image_url) {
        return IMAGE_BASE_URL + image_url;
    }


}



