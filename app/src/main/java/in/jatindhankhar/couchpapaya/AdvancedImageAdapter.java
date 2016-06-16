package in.jatindhankhar.couchpapaya;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Movie;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.koushikdutta.ion.builder.Builders;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import in.jatindhankhar.couchpapaya.data.MovieContract;

/**
 * Created by jatin on 9/6/16.
 */
public class AdvancedImageAdapter extends RecyclerView.Adapter<AdvancedImageAdapter.ViewHolder> {

    //private ArrayList<MovieFactory> mListMovies;
    private Context mContext;
    private static ArrayList<String> mUrls = new ArrayList<String>();
    private Picasso imgLoader;
    private final Callbacks mCallbacks;

    public interface Callbacks {
        void open(int position);
    }

    public AdvancedImageAdapter(Context context, Callbacks callbacks) {
        this.mContext = context;
        this.imgLoader = Picasso.with(context);
        this.mCallbacks = callbacks;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_content, parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.url = mUrls.get(position);
        imgLoader.load(holder.url).placeholder(R.mipmap.ic_launcher).into(holder.iv);
        holder.rootView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mCallbacks.open(holder.getAdapterPosition());
            }
        });


    }

    @Override
    public void onBindViewHolder(AdvancedImageAdapter.ViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public int getItemCount() {
        return mUrls.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public String url;
        public final View rootView;
        public final ImageView iv;

        public ViewHolder(View itemView) {
            super(itemView);
            this.rootView = itemView;
            this.iv = (ImageView) itemView.findViewById(R.id.thumbnail);
        }


    }

    public void addAll(ArrayList<String> urls) {
        nukeAll();
        mUrls.addAll(urls);
        notifyDataSetChanged();
    }

    public void add(String url) {
        mUrls.add(url);
        notifyDataSetChanged();
    }


    public void nukeAll() {
        mUrls.clear();

    }


}
