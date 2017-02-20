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

import com.contentful.java.cda.CDAArray;
import com.contentful.java.cda.CDAAsset;
import com.contentful.java.cda.CDAClient;
import com.contentful.java.cda.CDAEntry;
import com.contentful.java.cda.CDAResource;
import com.contentful.vault.SyncConfig;
import com.contentful.vault.SyncResult;
import com.contentful.vault.Vault;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import dlapps.dlv.kqandroid.R;
import dlapps.dlv.kqandroid.Utils.ModeType;
import dlapps.dlv.kqandroid.adapters.KQRecyclerAdapter;
import dlapps.dlv.kqandroid.objects.KQSpace;
import dlapps.dlv.kqandroid.objects.Playdate;
import dlapps.dlv.kqandroid.objects.Promotion;
import dlapps.dlv.kqandroid.objects.Saloon;
import rx.Observer;
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

    private String mSaloon;
    private ModeType mCurrentMode;
    private TabLayout mTabLayout;

    ProgressBar mProgressBar;
    RecyclerView mRecyclerView;
    KQRecyclerAdapter mRecyclerAdapter;

    ArrayList<Playdate> mPlaydates = new ArrayList<>();
    ArrayList<Promotion> mPromos = new ArrayList<>();
    ArrayList<Saloon> mSaloons = new ArrayList<>();

    private Vault mVault;

    private final String CDA_TOKEN = "d71705494e6f2d89baede9c4460703694fa04d6c8984ca20d6e2b2b836789c84";
    //    private final String CDA_TOKEN = "dca5148bd95794963a5c08d8cd0b7aaa0760a2e79fd920d7b1a5ea2420d7271f";
    private final String SPACE_ID = "tymhb56x18eu";
    Gson gson;

    /*
      * Create the client
      *
      * This client will abstract the communication to Contentful. Use it to make your requests to
      * Contentful.
      *
      * For initialization it needs the {@link #CDA_TOKEN} and {@link #SPACE_ID} from above.
      */
    private final CDAClient client = CDAClient
            .builder()
            .setToken(CDA_TOKEN)
            .setSpace(SPACE_ID)
            .build();

    public ContentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param saloon Parameter 1.
     * @param mode Parameter 2.
     * @return A new instance of fragment ContentFragment.
     */
    public static ContentFragment newInstance(String saloon, ModeType mode) {
        ContentFragment fragment = new ContentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SALOON, saloon);
        args.putSerializable(ARG_MODE, mode);
        fragment.setArguments(args);
        return fragment;
    }

    /************* Contentful *******************/

    private void fetchContentful(){
        syncVault();
//        fetchEntries();
    }

    private void fetchSaloonsVault(){
        //TODO move all vault & contenful to singleton
        mVault.observe(Saloon.class)
                .all()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toList()
                .subscribe(new Subscriber<List<Saloon>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted");

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError", e);
                    }

                    @Override
                    public void onNext(List<Saloon> saloons) {
                        Log.i(TAG, "onNext "+saloons.size());
                        mSaloons = (ArrayList<Saloon>) saloons;
                        ArrayList<String> images = new ArrayList<>();
                        for(Saloon saloon : saloons) {

                        }
                        mRecyclerAdapter.setImages(images);
                        showProgress(false);
                    }
                });
    }

    private void fetchEntriesVault(){
        showProgress(true);
        if(mCurrentMode == ModeType.PLAYDATES){
            fetchPlaydateEntriesVault();
        }else if(mCurrentMode == ModeType.PROMOTIONS){
            fetchPromoEntriesVault();
        }
    }

    private void fetchPlaydateEntriesVault(){
        mVault.observe(Playdate.class)
                .all()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toList()
                .subscribe(new Subscriber<List<Playdate>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted");

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError", e);
                    }

                    @Override
                    public void onNext(List<Playdate> playdates) {
                        Log.i(TAG, "onNext "+playdates.size());
                        mPlaydates = (ArrayList<Playdate>) playdates;
                        ArrayList<String> images = new ArrayList<>();
                        for(Playdate playdate : playdates) {
                            images.add(playdate.image.url());
                        }
                        mRecyclerAdapter.setImages(images);
                        showProgress(false);
                    }
                });
    }

    private void fetchPromoEntriesVault(){
        mVault.observe(Promotion.class)
                .all()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toList()
                .subscribe(new Subscriber<List<Promotion>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted");

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError", e);

                    }

                    @Override
                    public void onNext(List<Promotion> promotions) {
                        Log.i(TAG, "onNext "+promotions.size());
                        mPromos = (ArrayList<Promotion>) promotions;
                        ArrayList<String> images = new ArrayList<>();
                        for(Promotion promotion : promotions) {
                            images.add(promotion.image.url());
                        }
                        showProgress(false);
                        mRecyclerAdapter.setImages(images);
                    }
                });
    }

    private void syncVault(){

// Get instance of Vault
        mVault = Vault.with(getContext(), KQSpace.class);
        // Trigger sync
        mVault.requestSync(SyncConfig.builder().setClient(client).build());

        Vault.observeSyncResults()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SyncResult>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "oncomplete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError", e);
                    }

                    @Override
                    public void onNext(SyncResult syncResult) {
                        Log.i(TAG, "onNext SyncResult "+syncResult.isSuccessful());
                        fetchSaloonsVault();
                        fetchEntriesVault();
                    }
                });
    }

    /**
     * This internal method will be used to print out messages on the logger and on screen.
     */
    private void info(String title, List<String> descriptions) {
        Log.i(TAG, Html.fromHtml(title).toString() + "[["+ Html.fromHtml(descriptions.toString()).toString()+"]]");
        StringBuilder text = new StringBuilder();
        for (final String description : descriptions) {
            text.append(Html.fromHtml(description));
            text.append(Html.fromHtml("<br/>"));
        }
//        Log.i(TAG, "{{ INFO"+text.toString()+" }}");
    }

    /**********/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSaloon = getArguments().getString(ARG_SALOON);
            mCurrentMode = (ModeType) getArguments().getSerializable(ARG_MODE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_content, container, false);

        mProgressBar = (ProgressBar) view.findViewById(R.id.content_progress_bar);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.content_recycler_view);

        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                showProgress(false);

                Toast.makeText(getContext(), mCurrentMode.toString(), Toast.LENGTH_SHORT).show();
            }
        }, 2000);

        mRecyclerAdapter = new KQRecyclerAdapter(getContext(), mCurrentMode);
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fetchContentful();

        return view;
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
