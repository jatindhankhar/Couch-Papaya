package in.jatindhankhar.couchpapaya;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by jatin on 13/6/16.
 */
public class TrailerAdapter extends BaseAdapter {
    public ArrayList<TrailerFactory> mTrailers = new ArrayList<TrailerFactory>();
    private Context mContext;
    private Picasso mImgLoader;

    public TrailerAdapter(Context context)
    {
        this.mContext = context;
        this.mImgLoader = Picasso.with(mContext);
    }

    public String get_trailer_photo(String key)
    {
        // http://stackoverflow.com/a/2068371/3455743
        return "http://img.youtube.com/vi/"  + key + "/default.jpg";
    }

    @Override
    public int getCount() {
        return mTrailers.size();
    }

    @Override
    public Object getItem(int position) {
        return mTrailers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View trailer_item;
        if(convertView == null)
        { // if it's not recycled, initialize some attributes
            trailer_item = View.inflate(mContext,R.layout.trailer_item,null);

        }
        else
        {
            trailer_item = convertView;
        }

        ((TextView) trailer_item.findViewById(R.id.trailer_name)).setText(mTrailers.get(position).getName());
        mImgLoader.load(get_trailer_photo(mTrailers.get(position).getKey())).placeholder(R.drawable.ic_image).
                into((ImageView)trailer_item.findViewById(R.id.trailer_thumbnail));

        trailer_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // http://stackoverflow.com/a/12439378/3455743 Launch Youtube Intent
                // http://stackoverflow.com/a/4197364/3455743 Launch activity from Adapter
                    String id = mTrailers.get(position).getKey();
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
                        mContext.startActivity(intent);
                    } catch (ActivityNotFoundException ex) {
                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://www.youtube.com/watch?v=" + id));
                        mContext.startActivity(intent);
                    }
                }

        });
        return trailer_item;
    }

    public void add(TrailerFactory trailer)
    {
     mTrailers.add(trailer);
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<TrailerFactory> trailers)
    {
        nukeAll();
        mTrailers.addAll(trailers);
        notifyDataSetChanged();
    }
    public void nukeAll()
    {
        mTrailers.clear();
    }

    public TrailerFactory getFirstTrailer()
    {
        if(! mTrailers.isEmpty())
            return mTrailers.get(0);
        return null;
    }
}
