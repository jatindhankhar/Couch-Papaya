package in.jatindhankhar.couchpapaya;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Movie;
import android.net.Uri;

import java.util.ArrayList;

import in.jatindhankhar.couchpapaya.data.MovieContract;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by jatin on 11/6/16.
 */
public class  MovieDatabaseService {
    private Context mContext;
    public MovieDatabaseService(Context context)
    {
        this.mContext = context;
    }
    public boolean isFavorite(int id) {
        Cursor movieCursor = mContext.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                new String[]{MovieContract.MovieEntry.COLUMN_MOVIE_ID},
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = " + id,
                null,
                null);

        if (movieCursor != null && movieCursor.moveToFirst()) {
            movieCursor.close();
            return true;
        } else {
            return false;
        }
    }

    public void addMovie( MovieFactory movie){
        ContentValues movieValues = new ContentValues();
        movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie.getId());
        movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, movie.getTitle());
        movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH, movie.getPoster());
        movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW, movie.getOverview());
        movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE, movie.getUserRating());
        movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE, movie.getReleaseDate());
        movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_BACKDROP_PATH, movie.getBackdrop());
        mContext.getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, movieValues);
    }

    public void removeMovie(int id)
    {
        mContext.getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI,
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = " + id, null);
    }

    public ArrayList<MovieFactory> getFavouriteMovies()
    {
       Cursor cursor =  mContext.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,null,null,null,null);
        ArrayList<MovieFactory> Movies = new ArrayList<MovieFactory>();

        if (cursor != null && cursor.moveToFirst()){
            do {

                int id = cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID));
                String title = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE));
                String poster = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH));
                String overview = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW));
                String rating  = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE));
                String released_date = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE));
                String backdrop = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_BACKDROP_PATH));
                MovieFactory movie = new MovieFactory(id,title,poster,overview,rating,released_date,backdrop);
                Movies.add(movie);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return Movies;
    }


}
