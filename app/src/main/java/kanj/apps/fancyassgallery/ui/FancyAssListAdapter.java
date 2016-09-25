package kanj.apps.fancyassgallery.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import kanj.apps.fancyassgallery.R;
import kanj.apps.fancyassgallery.network.VolleyInstance;

/**
 * Created by voldemort on 25/9/16.
 */

public class FancyAssListAdapter extends RecyclerView.Adapter<FancyAssListAdapter.AssHolder> {
    private ArrayList<String> urls;
    private Context mContext;

    public FancyAssListAdapter(Context mContext, ArrayList<String> urls) {
        this.urls = urls;
        this.mContext = mContext;
    }

    @Override
    public AssHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new AssHolder(v);
    }

    @Override
    public void onBindViewHolder(AssHolder holder, int position) {
        String url = urls.get(position);
        holder.url = url;
        holder.pic.setImageUrl(url, VolleyInstance.getInstance(mContext).getImageLoader());
    }

    @Override
    public int getItemCount() {
        return urls.size();
    }

    class AssHolder extends RecyclerView.ViewHolder {
        ExpandingImage pic;
        String url;

        public AssHolder(View itemView) {
            super(itemView);
            pic = (ExpandingImage) itemView.findViewById(R.id.pic);
        }
    }
}
