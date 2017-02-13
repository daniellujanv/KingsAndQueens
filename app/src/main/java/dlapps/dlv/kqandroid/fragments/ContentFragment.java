package dlapps.dlv.kqandroid.fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import dlapps.dlv.kqandroid.R;
import dlapps.dlv.kqandroid.Utils.ModeType;
import dlapps.dlv.kqandroid.adapters.KQRecyclerAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContentFragment extends Fragment implements TabLayout.OnTabSelectedListener {

    private static final String ARG_SALOON = "kingsqueens:content:saloon";
    private static final String ARG_MODE = "kingsqueens:content:mode";

    private String mSaloon;
    private ModeType mCurrentMode;
    private TabLayout mTabLayout;

    @BindView(R.id.content_progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.content_recycler_view)
    RecyclerView mRecyclerView;
    KQRecyclerAdapter mRecyclerAdapter;

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
        ButterKnife.bind(this, view);

        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                mProgressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), mCurrentMode.toString(), Toast.LENGTH_SHORT).show();
            }
        }, 2000);

        mRecyclerAdapter = new KQRecyclerAdapter(getContext(), mCurrentMode);
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    public void setmTabLayout(TabLayout tabLayout) {
        this.mTabLayout = tabLayout;
        mTabLayout.addOnTabSelectedListener(this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mCurrentMode = ModeType.fromInt(tab.getPosition());
        mRecyclerAdapter.setCurrentMode(mCurrentMode);
        Toast.makeText(getContext(), "selected:: "+mCurrentMode,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
