package dlapps.dlv.kqandroid.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dlapps.dlv.kqandroid.R;
import dlapps.dlv.kqandroid.objects.Saloon;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SaloonDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SaloonDetailsFragment extends Fragment {

    private static final String ARG_SALOON = "saloondetails::saloon";

    private Saloon mSaloon;


    public SaloonDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param saloon Saloon for details.
     * @return A new instance of fragment SaloonDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SaloonDetailsFragment newInstance(Saloon saloon) {
        SaloonDetailsFragment fragment = new SaloonDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_SALOON, saloon);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSaloon = (Saloon) getArguments().getSerializable(ARG_SALOON);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saloon_details, container, false);
    }

}
