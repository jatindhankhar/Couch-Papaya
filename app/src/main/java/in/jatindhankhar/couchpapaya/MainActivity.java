package in.jatindhankhar.couchpapaya;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.lang.reflect.Array;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private final String LOG_TAG = this.getClass().getSimpleName();
    private ImageAdapter imageAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private String mSavedCategory;
    private String currentCategory;

    private String getDefaultCategory() {

        //*** Todo - Improve way of getting value  ***//
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String index_val = prefs.getString(getString(R.string.default_category), "");
        String res = getResources().getStringArray(R.array.categoryList)[Integer.parseInt(index_val) - 1];
        return res.toLowerCase().replace(" ", "_");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GridView gridView = (GridView) findViewById(R.id.gridview);
        Log.d(LOG_TAG, "Constructing it with " + getDefaultCategory());
        imageAdapter = new ImageAdapter(this, getDefaultCategory());
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_part);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        gridView.setAdapter(imageAdapter);

        Log.d(LOG_TAG, "My app name is " + getString(getApplicationInfo().labelRes));
        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null && savedInstanceState.getString("Saves") != null) {
            // Restore value of members from saved state
            mSavedCategory = savedInstanceState.getString("Saves");
            Log.d(LOG_TAG, "Optimally current category should be " + savedInstanceState.getString("Saves"));
            Log.d(LOG_TAG, "Optimal condition is happening");
        } else {
            // Probably initialize members with default values for a new instance
            Log.d(LOG_TAG, "Assinging it the default category");
            if (currentCategory != null)
                mSavedCategory = currentCategory;

        }

        Log.d(LOG_TAG, "Default category is " + getDefaultCategory());
        Log.d(LOG_TAG, "Current category is " + mSavedCategory);

        //Toast.makeText(MainActivity.this, "Current category is " + default_category, Toast.LENGTH_SHORT).show();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JsonObject data = imageAdapter.getItem(position);
                Intent intent = new Intent(getApplicationContext(), moviedetails.class);
                intent.putExtra("img_url", imageAdapter.get_image_url(position));
                intent.putExtra("json_data", data.toString());
                startActivity(intent);

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(MainActivity.this,SettingsActivity.class));
                return true;

            case R.id.sort_popularity:
                imageAdapter.get_data("popular");
                mSavedCategory = "popular";
                currentCategory = mSavedCategory;
                Log.d(LOG_TAG, "Switching category to " + mSavedCategory);
                Toast.makeText(MainActivity.this, "Sorting by Popularity", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.sort_top_rated:
                Toast.makeText(MainActivity.this, "Sorting by Top Rated", Toast.LENGTH_SHORT).show();
                mSavedCategory = "top_rated";
                currentCategory = mSavedCategory;
                Log.d(LOG_TAG, "Switching category to " + mSavedCategory);
                imageAdapter.get_data("top_rated");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        //save_Val = savedInstanceState.getString("Saves");
        mSavedCategory = savedInstanceState.getString("Saves");
        Log.d(LOG_TAG, "Restoring the stuff");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.d(LOG_TAG, "Category which we are saving is " + mSavedCategory);
        outState.putString("Saves", mSavedCategory);
        Log.d(LOG_TAG, "Saving the stuff");
    }

    @Override
    protected void onResume() {
        super.onResume();
        //do something
    }

    @Override
    public void onRefresh() {
        Toast.makeText(MainActivity.this, "Refreshing ...", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
                imageAdapter.get_data("popular");
            }
        }, 2000);
    }

}
