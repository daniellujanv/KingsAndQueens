package dlapps.dlv.kqandroid.fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.contentful.java.cda.CDAClient;
import com.contentful.vault.SyncConfig;
import com.contentful.vault.SyncResult;
import com.contentful.vault.Vault;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dlapps.dlv.kqandroid.R;
import dlapps.dlv.kqandroid.Utils.KQContentful;
import dlapps.dlv.kqandroid.Utils.ModeType;
import dlapps.dlv.kqandroid.adapters.KQRecyclerAdapter;
import dlapps.dlv.kqandroid.objects.KQSpace;
import dlapps.dlv.kqandroid.objects.Playdate;
import dlapps.dlv.kqandroid.objects.Promotion;
import dlapps.dlv.kqandroid.objects.Saloon;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContentFragment extends Fragment implements TabLayout.OnTabSelectedListener {

    private final String TAG = getClass().getSimpleName();
    private static final String ARG_SALOON = "kingsqueens:content:saloon";
    private static final String ARG_MODE = "kingsqueens:content:mode";

    private ModeType mCurrentMode;
    private TabLayout mTabLayout;

    @BindView(R.id.content_progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.content_recycler_view)
    RecyclerView mRecyclerView;
    KQRecyclerAdapter mRecyclerAdapter;

    ArrayList<Playdate> mPlaydates = new ArrayList<>();
    ArrayList<Promotion> mPromos = new ArrayList<>();

    Gson gson;

    public ContentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param mode Parameter 2.
     * @return A new instance of fragment ContentFragment.
     */
    public static ContentFragment newInstance(ModeType mode) {
        ContentFragment fragment = new ContentFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_MODE, mode);
        fragment.setArguments(args);
        return fragment;
    }

    /**********/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCurrentMode = (ModeType) getArguments().getSerializable(ARG_MODE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_content, container, false);

        ButterKnife.bind(this, view);

        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                showProgress(false);

                Toast.makeText(getContext(), mCurrentMode.toString(), Toast.LENGTH_SHORT).show();
            }
        }, 2000);

        mRecyclerAdapter = new KQRecyclerAdapter(getContext());
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        onTabSelected(mTabLayout.getTabAt(0));

        return view;
    }

    public void setMode(ModeType mode){
        this.mCurrentMode = mode;
    }

    private void fetchEntriesVault(){
        showProgress(true);
        if(mCurrentMode == ModeType.PLAYDATES){
            fetchPlaydates();
        }else if(mCurrentMode == ModeType.PROMOTIONS){
            fetchPromos();
        }
    }

    private void fetchPlaydates(){
        KQContentful.getInstance(getContext())
                .fetchPlaydateEntriesVault(new KQContentful.OnPlaydatesFetchedListener() {
                    @Override
                    public void onComplete(ArrayList<Playdate> playdates) {
                        mPlaydates = playdates;
                        ArrayList<String> images = new ArrayList<>();
                        for(Playdate playdate : playdates) {
                            images.add(playdate.image.url());
                        }
                        mRecyclerAdapter.setImages(images);
                        showProgress(false);
                    }

                    @Override
                    public void onException(Exception e) {
                        Log.e(TAG, "fetchPlaydates", e);
                    }
                });
    }

    private void fetchPromos(){
        KQContentful.getInstance(getContext())
                .fetchPromoEntriesVault(new KQContentful.OnPromotionsFetchedListener() {
                    @Override
                    public void onComplete(ArrayList<Promotion> promotions) {
                        mPromos = promotions;
                        ArrayList<String> images = new ArrayList<>();
                        for(Promotion promotion : promotions) {
                            images.add(promotion.image.url());
                        }
                        showProgress(false);
                        mRecyclerAdapter.setImages(images);
                    }

                    @Override
                    public void onException(Exception e) {
                        Log.e(TAG, "fetchPromos", e);
                    }
                });
    }

    private void showProgress(boolean show){
        if(show){
            mProgressBar.setVisibility(View.VISIBLE);
        }else{
            mProgressBar.setVisibility(View.GONE);
        }
    }

    public void setmTabLayout(TabLayout tabLayout) {
        this.mTabLayout = tabLayout;
        mTabLayout.addOnTabSelectedListener(this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mCurrentMode = ModeType.fromInt(tab.getPosition());
        fetchEntriesVault();
//        Toast.makeText(getContext(), "selected:: "+mCurrentMode,
//                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
