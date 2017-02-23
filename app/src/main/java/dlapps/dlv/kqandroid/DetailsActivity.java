package dlapps.dlv.kqandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import dlapps.dlv.kqandroid.objects.Saloon;
import okhttp3.internal.http.StreamAllocation;

public class DetailsActivity extends AppCompatActivity {

    public static final String EXTRA_SALOON = "detailsactivity::saloon";

    @BindView(R.id.details_saloon_image)
    ImageView mSaloonImageView;
    @BindView(R.id.details_saloon_directions)
    ImageView mDirectionsView;
    @BindView(R.id.details_saloon_name)
    TextView mNameView;
    @BindView(R.id.details_saloon_hours)
    TextView mHoursView;
    @BindView(R.id.details_saloon_description)
    TextView mDescriptionView;

    private Saloon mSaloon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        if(getIntent().getExtras() == null){

            Toast.makeText(getApplicationContext(), "Error loading details... Please try again"
                    , Toast.LENGTH_SHORT).show();
            onStop();
        }

        mSaloon = getIntent().getParcelableExtra(EXTRA_SALOON);

        loadSaloon(mSaloon);
    }

    private void loadSaloon(Saloon saloon){
        Glide.with(getApplicationContext())
                .load(saloon.getImageUrl())
                .placeholder(R.drawable.branded_logo)
                .centerCrop()
                .into(mSaloonImageView);

        mNameView.setText(saloon.getName());
        //TODO add to model
//        mHoursView.setText();
        mDescriptionView.setText(saloon.getDescription());
        mDirectionsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
