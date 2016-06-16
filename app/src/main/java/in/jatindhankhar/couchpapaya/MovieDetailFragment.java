package in.jatindhankhar.couchpapaya;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.koushikdutta.ion.builder.LoadBuilder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A fragment representing a single Movie detail screen.
 * This fragment is either contained in a {@link MovieListActivity}
 * in two-pane mode (on tablets) or a {@link MovieDetailActivity}
 * on handsets.
 */
public class MovieDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */

    public static final String ARG_MOVIE = "ARG_MOVIE";


    private MovieFactory mListMovie;
    private LoadBuilder<Builders.Any.B> mdataLoader;
    private Context mContext;
    private Picasso mImageLoader;
    private tmdb_client TMDB;
    private TrailerAdapter mTrailerAdapter;
    private ArrayList<TrailerFactory> Trailers;

    private android.support.v7.widget.ShareActionProvider mShareActionProvider;


    @BindView(R.id.trailers_list)
    LinearLayout mTrailerLayout;
    @BindView(R.id.reviews_list)
    LinearLayout mReviewLayout;
    @BindView(R.id.movie_poster)
    ImageView moviePoster;
    @BindView(R.id.movie_title)
    TextView movieTitle;
    @BindView(R.id.release_Date)
    TextView releaseDate;
    @BindView(R.id.ratingBar)
    RatingBar ratingBar;
    @BindView(R.id.rating_text)
    TextView ratingText;
    @BindView(R.id.overview_text)
    TextView overviewText;
    @BindView(R.id.button)
    Button favButton;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(ARG_MOVIE)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            //   mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
            if (getArguments().containsKey(ARG_MOVIE)) {

                mListMovie = getArguments().getParcelable(ARG_MOVIE);

            }
            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mListMovie.getTitle());
            }
        }
        this.mContext = getContext();
        this.TMDB = new tmdb_client(mContext);
        this.mdataLoader = Ion.with(mContext);
        this.mImageLoader = Picasso.with(mContext);
        this.Trailers = new ArrayList<TrailerFactory>();

        get_trailers(String.valueOf(mListMovie.getId()));
        get_reviews(String.valueOf(mListMovie.getId()));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mTrailerAdapter = new TrailerAdapter(getActivity());
        setHasOptionsMenu(true);
        //setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate menu resource file.
        inflater.inflate(R.menu.menu_share, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);

        // Fetch and store ShareActionProvider
        //http://stackoverflow.com/questions/18714322/how-to-add-action-bar-options-menu-in-android-fragments
        mShareActionProvider = (android.support.v7.widget.ShareActionProvider) MenuItemCompat.getActionProvider(item);
        //mShareActionProvider.setShareIntent(setShareIntent());
        //mShareActionProvider.setShareIntent(Intent share);

        // Return true to display menu
        //return true;
    }

    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
        else
        {
           // Log.d("Yolopad","mShareAction is null");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movie_detail, container, false);
        ButterKnife.bind(this, rootView);

        //Initiliaze Layout
        Picasso po = Picasso.with(getContext());
        po.load(mListMovie.getPosterUrl()).into(moviePoster);
        movieTitle.setText(mListMovie.getTitle());
        releaseDate.setText(mListMovie.getReleaseDate());
        float votes = Float.parseFloat(mListMovie.getUserRating());
        ratingBar.setRating(votes);
        //Sets Rating text, Spannable is used to style text
        SpannableString rating_text = new SpannableString(Float.toString(votes) + "/10");
        rating_text.setSpan(new RelativeSizeSpan(1.5f), 0, rating_text.length() - "/10".length(), 0);
        ratingText.setText(rating_text);
        // Handle favourite
        update_Favourite();
        favButton.setOnClickListener(new FavClick());
        //Overview
        overviewText.setText(mListMovie.getOverview());

        return rootView;
    }

    public void get_trailers(String id) {
        mdataLoader.load(TMDB.get_trailer_path(id)).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                if (e == null) {
                    JsonArray results = result.getAsJsonArray("results");
                    Trailers.clear();
                    for (JsonElement el : results) {
                        Gson gson = new Gson();
                        mTrailerAdapter.add(gson.fromJson(el, TrailerFactory.class));
                    }
                    // http://stackoverflow.com/a/12405779/3455743 Why LinearLayout over List and reason
                    for (int i = 0; i < mTrailerAdapter.getCount(); i++) {
                        mTrailerLayout.addView(mTrailerAdapter.getView(i, null, null));
                    }
                    TrailerFactory tr = mTrailerAdapter.getFirstTrailer();
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    if(tr != null) {
                        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, mListMovie.getTitle());
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Check out " + tr.getName() + " for movie " + mListMovie.getTitle() + " "  + "http://www.youtube.com/watch?v=" +tr.getKey());

                    }
                    else
                    {
                        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"No Trailers found");
                        Toast.makeText(getContext(), "No trailers found", Toast.LENGTH_SHORT).show();
                    }
                    setShareIntent(sharingIntent);

                } else {
                    Toast.makeText(mContext, "Parsing error :(", Toast.LENGTH_SHORT).show();


                }
            }

        });
    }

    public void get_reviews(String id) {

        mdataLoader.load(TMDB.get_review_path(id)).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                if (e == null) {
                    JsonArray results = result.getAsJsonArray("results");
                    View v;
                    for (JsonElement el : results) {
                        Gson gson = new Gson();
                        ReviewFactory review = gson.fromJson(el, ReviewFactory.class);
                        v = View.inflate(getContext(), R.layout.review_item, null);
                        ((TextView) v.findViewById(R.id.author)).setText(review.getAuthor());
                        ((TextView) v.findViewById(R.id.review_text)).setText(review.getContent());

                        mReviewLayout.addView(v);
                    }
                } else {
                    Toast.makeText(mContext, "Parsing error :(", Toast.LENGTH_SHORT).show();


                }
            }

        });
    }


    class FavClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            MovieDatabaseService mds = new MovieDatabaseService(getContext());
            String toastMessage;
            if (mds.isFavorite((int) mListMovie.getId())) {
                toastMessage = "Removed from Favorites";
                mds.removeMovie((int) mListMovie.getId());
            } else {
                mds.addMovie(mListMovie);
                toastMessage = "Added to favorites";

            }
            update_Favourite();

            Toast.makeText(getContext(), toastMessage, Toast.LENGTH_SHORT).show();
        }
    }

    public void update_Favourite()
    {
        MovieDatabaseService mds = new MovieDatabaseService(getContext());

        if(mds.isFavorite((int) mListMovie.getId()))
        {

       //favButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
            favButton.setBackgroundResource(R.drawable.ic_favorite_black_24dp);
        }
        else
        {
          //favButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_heart_off));
            favButton.setBackgroundResource(R.drawable.ic_heart_off);
        }


    }
}
