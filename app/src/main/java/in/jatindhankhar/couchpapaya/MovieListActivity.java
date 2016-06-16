package in.jatindhankhar.couchpapaya;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.koushikdutta.ion.builder.LoadBuilder;
import com.koushikdutta.ion.gson.GsonParser;

import java.util.ArrayList;

/**
 * An activity representing a list of Movies. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MovieDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class MovieListActivity extends AppCompatActivity implements AdvancedImageAdapter.Callbacks {
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */

    private boolean mTwoPane;
    private AdvancedImageAdapter mAdapter;
    private tmdb_client TMDB;
    private LoadBuilder<Builders.Any.B> mdataLoader;
    private Context mContext;

    public ArrayList<MovieFactory> Movies = new ArrayList<MovieFactory>();
    public ArrayList<String> Urls = new ArrayList<String>();
    public String category = "popular";  // Default to popularity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        this.mContext = getApplicationContext();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.TMDB = new tmdb_client(mContext);
        this.mdataLoader = Ion.with(mContext);


        View recyclerView = findViewById(R.id.movie_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

    }


    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        int grid_col = 2;
        //Default to 2 cols as GridLayoutManager.DEFAULT_SPAN_COUNT resolves to -1 and crashes the whole app
        recyclerView.setLayoutManager(new GridLayoutManager(this, grid_col));

        mAdapter = new AdvancedImageAdapter(getApplicationContext(), this);
        recyclerView.setAdapter(mAdapter);
        get_movies(category);
       // Log.d("Yolopad","On create category is  " + category);

    }

    @Override
    public void open(int position) {
        MovieFactory m = Movies.get(position);
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(MovieDetailFragment.ARG_MOVIE, Movies.get(position));
            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.putExtra(MovieDetailFragment.ARG_MOVIE, Movies.get(position));
            startActivity(intent);
        }
    }


    public void get_movies(String category) {
        String query = TMDB.get_api_path_for(category);
        mdataLoader.load(query).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                if (e == null) {
                    JsonArray results = result.getAsJsonArray("results");
                    Movies.clear();
                    Urls.clear();
                    for (JsonElement el : results) {
                        Gson gson = new Gson();
                        MovieFactory movie = gson.fromJson(el,MovieFactory.class);
                        Movies.add(movie);
                        Urls.add(TMDB.get_image_path(el.getAsJsonObject().get("poster_path").getAsString()));


                    }
                    mAdapter.addAll(Urls);
                } else {
                    Toast.makeText(mContext, "Parsing error :(", Toast.LENGTH_SHORT).show();


                }
            }

        });
    }


    public void get_favourites()
    {

        MovieDatabaseService mds = new MovieDatabaseService(this);
        ArrayList<MovieFactory> favMovies = mds.getFavouriteMovies();
        if(! favMovies.isEmpty()){
        Movies.clear();
        Movies.addAll(mds.getFavouriteMovies());
        Urls.clear();
        for(MovieFactory el : Movies)
        {
            Urls.add(TMDB.get_image_path(el.getPosterUrl()));
        }
            mAdapter.addAll(Urls);
        }

        else
        {
            Toast.makeText(this, "No Favourites to show", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        category =  savedInstanceState.getString("category");
       // Log.d("Yolopad","Restore Category is " + category);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("category",category);
      //  Log.d("Yolopad"," Saved Category is " + category);
    }

    @Override
public boolean onCreateOptionsMenu(Menu menu) {

    getMenuInflater().inflate(R.menu.menu_detail, menu);
    return true;
}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                //startActivity(new Intent(MainActivity.this,SettingsActivity.class));
                return true;

            case R.id.sort_popularity:
                category = "popular";
                get_movies("popular");
                Toast.makeText(this, "Sorting by Popularity", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.sort_top_rated:
                category = "top_rated";
                get_movies("top_rated");
                Toast.makeText(this, "Sorting by Top Rated", Toast.LENGTH_SHORT).show();

                return true;
            case R.id.sort_favourites:
                category = "favourites";
                get_favourites();
                Toast.makeText(this,"Sorting by Favourites",Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

}

