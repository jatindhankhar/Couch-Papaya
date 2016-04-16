package in.jatindhankhar.couchpapaya;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.JsonObject;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private final String LOG_TAG = this.getClass().getSimpleName();
    private ImageAdapter imageAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GridView gridView = (GridView) findViewById(R.id.gridview);
        imageAdapter = new ImageAdapter(this);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_part);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        gridView.setAdapter(imageAdapter);

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
                Toast.makeText(MainActivity.this, "Settings were clicked", Toast.LENGTH_SHORT).show();

                return true;
            case R.id.sort_popularity:
                imageAdapter.get_data("popular");
                Toast.makeText(MainActivity.this, "Sorting by Popularity", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.sort_top_rated:
                Toast.makeText(MainActivity.this, "Sorting by Top Rated", Toast.LENGTH_SHORT).show();
                imageAdapter.get_data("top_rated");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

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
