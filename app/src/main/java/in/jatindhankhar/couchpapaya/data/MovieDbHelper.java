package in.jatindhankhar.couchpapaya.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jatin on 7/6/16.
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movies.db";
    public static final int DATABASE_VERSION = 1;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    private static String setNotNullProp(String type) {
        return type + " " + "NOT NULL,";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE" + MovieContract.MovieEntry.TABLE_NAME + "(" +
                MovieContract.MovieEntry._ID + "INTEGER PRIMARY KEY," +
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + setNotNullProp("INTEGER") +
                MovieContract.MovieEntry.COLUMN_MOVIE_TITLE + setNotNullProp("TEXT") +
                MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH + setNotNullProp("TEXT") +
                MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW + setNotNullProp("TEXT") +
                MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE + setNotNullProp("TEXT") +
                MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE + setNotNullProp("TEXT") +
                MovieContract.MovieEntry.COLUMN_MOVIE_BACKDROP_PATH + setNotNullProp("TEXT") +
                " );";
        db.execSQL(SQL_CREATE_MOVIES_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
