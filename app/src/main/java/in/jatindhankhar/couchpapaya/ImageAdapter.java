package in.jatindhankhar.couchpapaya;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by jatin on 27/3/16.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private tmdb_client TMDB;
    private final Picasso imageLoader;
    private static ArrayList<String> URLS = new ArrayList<String>();
    private static ArrayList<JsonObject> JSON_BLOBS = new ArrayList<JsonObject>();

    public ImageAdapter(Context c, String defaultCategory) {
        mContext = c;
        this.TMDB = new tmdb_client();
        this.get_data(defaultCategory);
        this.imageLoader = Picasso.with(mContext);

    }

    public int getCount() {
        return URLS.size();

    }

    public JsonObject getItem(int position) {
        return JSON_BLOBS.get(position);
    }

    public long getItemId(int position) {
        return position;
    }


    public String get_image_url(int position) {
        return URLS.get(position);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null) {
            // if it's not recycled, initialize some attributes

            imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setAdjustViewBounds(true);
            imageView.setPadding(2, 2, 2, 2);
        } else {
            imageView = (ImageView) convertView;
        }

        imageLoader.load(URLS.get(position)).placeholder(R.drawable.ic_image).into(imageView);

        return imageView;
    }


    public void error_toast(String error_message) {
        Toast.makeText(this.mContext, error_message, Toast.LENGTH_SHORT).show();
    }

    public void get_data(String path_for) {
        Ion.with(this.mContext).
                load(TMDB.get_api_path_for(path_for)).
                asJsonObject().
                setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e == null) {
                            JsonArray results = result.getAsJsonArray("results");
                            URLS.clear();
                            JSON_BLOBS.clear();
                            for (JsonElement el : results) {

                                URLS.add(TMDB.get_image_path(el.getAsJsonObject().get("poster_path").getAsString()));
                                JSON_BLOBS.add(el.getAsJsonObject());


                            }
                            notifyDataSetChanged();
                        } else {
                            error_toast("Sorry!, There was some error");


                        }
                    }

                });
    }

    @Override
    public int getItemViewType(int position) {
        return Adapter.IGNORE_ITEM_VIEW_TYPE;
    }

}
