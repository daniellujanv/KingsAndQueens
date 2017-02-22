package dlapps.dlv.kqandroid.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import dlapps.dlv.kqandroid.R;
import dlapps.dlv.kqandroid.Utils.ModeType;
import dlapps.dlv.kqandroid.fragments.ContentFragment;

/**
 * Created by DanielLujanApps on Sunday29/01/17.
 *
 */

public class KQRecyclerAdapter extends RecyclerView.Adapter<KQRecyclerAdapter.KQHolder> {

    private Context mContext;
    private ArrayList<String> mImages = new ArrayList<>();

    public KQRecyclerAdapter(Context context){
        mContext = context;
    }

    @Override
    public KQHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.recycler_item, parent, false);
        return new KQHolder(view);
    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }

    @Override
    public void onBindViewHolder(KQHolder holder, int position) {
        Glide.with(mContext)
                .load(mImages.get(position))
                .fitCenter()
                .into(holder.imageView);
    }

    static class KQHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        KQHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.recycler_image_item);
        }
    }

    public void setImages(ArrayList<String> images){
        mImages = images;
        notifyDataSetChanged();
    }
}
