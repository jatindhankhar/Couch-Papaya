package in.jatindhankhar.couchpapaya;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;

public class moviedetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moviedetails);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        Intent intent = getIntent();
        ImageView imageView = (ImageView) findViewById(R.id.header);
        Picasso.with(this).load(intent.getStringExtra("img_url")).into(imageView);
        // Following lines creates a Gson object from the Json data passed as string via intent, we could have
        // used Parcelables but would needed to write more code. So ...
        JsonObject movie_data = new JsonParser().parse(intent.getStringExtra("json_data")).getAsJsonObject();
        //Sets Movie title from the json blob received
        toolbar.setTitle(movie_data.get("original_title").getAsString());
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorBlack));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.newlayout);


        // Poster setting
        ImageView poster_view = (ImageView) relativeLayout.findViewById(R.id.movie_poster);
        Picasso.with(this).load(intent.getStringExtra("img_url")).into(poster_view);

        // Rating bar
        float votes = movie_data.get("vote_average").getAsFloat();
        RatingBar rb = (RatingBar) relativeLayout.findViewById(R.id.ratingBar);
        rb.setRating(votes);


        //Sets movie title
        ((TextView) relativeLayout.findViewById(R.id.movie_title)).setText(movie_data.get("original_title").getAsString());

        //Sets Rating text, Spannable is used to style text
        SpannableString rating_text = new SpannableString(Float.toString(votes) + "/10");
        rating_text.setSpan(new RelativeSizeSpan(1.5f), 0, rating_text.length() - "/10".length(), 0);
        ((TextView) relativeLayout.findViewById(R.id.rating_text)).setText(rating_text);


        // To Set Release Date
        ((TextView) relativeLayout.findViewById(R.id.release_Date)).setText(movie_data.get("release_date").getAsString());

        //To Set Overviews
        ((TextView) relativeLayout.findViewById(R.id.overview_text)).setText(movie_data.get("overview").getAsString());


    }

}
