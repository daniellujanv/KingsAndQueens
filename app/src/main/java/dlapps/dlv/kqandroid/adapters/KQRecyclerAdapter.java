package dlapps.dlv.kqandroid.adapters;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import dlapps.dlv.kqandroid.R;
import dlapps.dlv.kqandroid.Utils.ModeType;
import dlapps.dlv.kqandroid.fragments.ContentFragment;

/**
 * Created by DanielLujanApps on Sunday29/01/17.
 */

public class KQRecyclerAdapter extends RecyclerView.Adapter<KQRecyclerAdapter.KQHolder> {

    private int[] mPlaydateResources = new int[]{R.drawable.pd1, R.drawable.pd2, R.drawable.pd3};
    private int[] mPromosResources = new int[]{R.drawable.promo1, R.drawable.promo1, R.drawable.promo1};
    private ModeType mCurrentMode = ModeType.PLAYDATES;

    private Context mContext;

    public KQRecyclerAdapter(Context context, ModeType currentMode){
        mCurrentMode = currentMode;
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
        if(mCurrentMode == ModeType.PLAYDATES){
            return mPlaydateResources.length;
        }else{
            return mPromosResources.length;
        }
    }

    @Override
    public void onBindViewHolder(KQHolder holder, int position) {
        int resource;
        if(mCurrentMode == ModeType.PLAYDATES) {
            resource = mPlaydateResources[position];
        }else{
            resource = mPromosResources[position];
        }

        Glide.with(mContext)
                .load(resource)
                .fitCenter()
                .into(holder.imageView);
    }

    public static class KQHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.recycler_image_item)
        public ImageView imageView;
        public KQHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setCurrentMode(ModeType currentMode) {
        this.mCurrentMode = currentMode;
        this.notifyDataSetChanged();
    }
}
