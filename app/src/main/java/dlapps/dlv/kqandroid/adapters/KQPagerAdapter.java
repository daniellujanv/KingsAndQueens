package dlapps.dlv.kqandroid.adapters;

import android.content.Context;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.data.StreamAssetPathFetcher;

import java.util.ArrayList;

import dlapps.dlv.kqandroid.R;
import dlapps.dlv.kqandroid.objects.Saloon;
import okhttp3.internal.http.StreamAllocation;

/**
 * Created by DanielLujanApps on Sunday29/01/17.
 *
 */

public class KQPagerAdapter extends PagerAdapter {

    public ArrayList<Saloon> mSaloons = new ArrayList<>();
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public KQPagerAdapter(Context context, CollapsingToolbarLayout collapsingToolbarLayout){
        mCollapsingToolbarLayout = collapsingToolbarLayout;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.header_pager_item, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.header_image_view);
        container.addView(itemView);

        Glide.with(mContext)
                .load(mSaloons.get(position).image.url())
                .centerCrop()
                .placeholder(R.drawable.background_gradient)
                . into(imageView);

        return itemView;
    }

    @Override
    public int getCount() {
        return mSaloons.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((FrameLayout) object);
    }

    public void onPageChanged(int position){
        mCollapsingToolbarLayout.setTitle(mSaloons.get(position).name);
    }

    public void setSaloons(ArrayList<Saloon> saloons){
        if(saloons != null){
            mSaloons = saloons;
        }else{
            mSaloons = new ArrayList<>();
        }
        notifyDataSetChanged();
    }
}
