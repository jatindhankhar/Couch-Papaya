package in.jatindhankhar.couchpapaya;

import android.content.Context;
import android.graphics.Movie;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by jatin on 9/6/16.
 */
public class MovieFactory implements Parcelable {

    public static final float POSTER_ASPECT_RATIO = 1.5f;

    private tmdb_client mTMDB_CLIENT;

    @SerializedName("id")
    private long mId;
    @SerializedName("original_title")
    private String mTitle;
    @SerializedName("poster_path")
    private String mPoster;
    @SerializedName("overview")
    private String mOverview;
    @SerializedName("vote_average")
    private String mUserRating;
    @SerializedName("release_date")
    private String mReleaseDate;
    @SerializedName("backdrop_path")
    private String mBackdrop;


    private MovieFactory() {
    }

    public MovieFactory(long id, String title, String poster, String overview, String userRating,
                 String releaseDate, String backdrop) {
        mId = id;
        mTitle = title;
        mPoster = poster;
        mOverview = overview;
        mUserRating = userRating;
        mReleaseDate = releaseDate;
        mBackdrop = backdrop;
    }

    @Nullable
    public String getTitle() {
        return mTitle;
    }

    public long getId() {
        return mId;
    }

    @Nullable
    public String getPosterUrl() {
        if (mPoster != null && !mPoster.isEmpty()) {
            //return mTMDB_CLIENT.get_image_path(mPoster);
            return "http://image.tmdb.org/t/p/w342/" + mPoster;

        }

        return null;
    }

    public String getPoster() {
        return mPoster;
    }

    public String getReleaseDate() {
        SimpleDateFormat df = new SimpleDateFormat("dd MMM, yyyy");
        SimpleDateFormat dfInput = new SimpleDateFormat("yyyy-MM-dd");
        String releasedDate;

        try {
            releasedDate = df.format(dfInput.parse(mReleaseDate));
        } catch (ParseException e){
            e.printStackTrace();
            releasedDate = mReleaseDate;
        }
        return releasedDate;
    }





    @Nullable
    public String getOverview() {
        return mOverview;
    }

    @Nullable
    public String getUserRating() {
        return mUserRating;
    }

    @Nullable
    public String getBackdropUrl() {
            //return mTMDB_CLIENT.get_image_path(mBackdrop);
        return "http://image.tmdb.org/t/p/w342/" + mBackdrop;
    }

    public String getBackdrop() {
        return mBackdrop;
    }

    public static final Parcelable.Creator<MovieFactory> CREATOR = new Creator<MovieFactory>() {
        public MovieFactory createFromParcel(Parcel source) {
            MovieFactory movie = new MovieFactory();
            movie.mId = source.readLong();
            movie.mTitle = source.readString();
            movie.mPoster = source.readString();
            movie.mOverview = source.readString();
            movie.mUserRating = source.readString();
            movie.mReleaseDate = source.readString();
            movie.mBackdrop = source.readString();
            return movie;
        }

        public MovieFactory[] newArray(int size) {
            return new MovieFactory[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(mId);
        parcel.writeString(mTitle);
        parcel.writeString(mPoster);
        parcel.writeString(mOverview);
        parcel.writeString(mUserRating);
        parcel.writeString(mReleaseDate);
        parcel.writeString(mBackdrop);
    }
}