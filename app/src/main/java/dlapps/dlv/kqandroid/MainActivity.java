package dlapps.dlv.kqandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import dlapps.dlv.kqandroid.Utils.KQContentful;
import dlapps.dlv.kqandroid.Utils.ModeType;
import dlapps.dlv.kqandroid.adapters.KQPagerAdapter;
import dlapps.dlv.kqandroid.fragments.ContentFragment;
import dlapps.dlv.kqandroid.objects.Saloon;

import static dlapps.dlv.kqandroid.DetailsActivity.EXTRA_SALOON;

public class MainActivity extends AppCompatActivity implements
        ViewPager.OnPageChangeListener, TabLayout.OnTabSelectedListener {

    private final String TAG = getClass().getSimpleName();

    @BindView(R.id.main_toolbar) Toolbar mToolbar;
    @BindView(R.id.main_collapsable_header) CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.main_appbar) AppBarLayout mAppBarLayout;
    @BindView(R.id.main_tablayout) TabLayout mTabLayout;
    @BindView(R.id.main_content_view_pager) ViewPager mViewPager;
    @BindView(R.id.main_content_info_button) ImageView mInfoButton;

    private ArrayList<Saloon> mSaloons = new ArrayList<>();

    private GestureDetectorCompat mDetector;

    private KQPagerAdapter mKqAdapter;
    private ModeType mCurrentMode = ModeType.PLAYDATES; //init w/playdates
    private ContentFragment contentFragment;

    private int mCurrentSaloon = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseMessaging.getInstance().subscribeToTopic("all");

        ButterKnife.bind(this);

        mDetector = new GestureDetectorCompat(this, new KQGestureListener());

        KQContentful.getInstance(getApplicationContext())
                .initVault(new KQContentful.OnVaulListener() {
                    @Override
                    public void onComplete(boolean result) {
                        if(result){
                            fetchSaloons();
                        }else{
                            Log.e(TAG, "Result false");
                        }
                    }

                    @Override
                    public void onException(Exception e) {
                        Log.e(TAG, "initVault", e);
                    }
                });

        setSupportActionBar(mToolbar);

        mKqAdapter = new KQPagerAdapter(getApplicationContext(), mCollapsingToolbarLayout, mDetector);
        mViewPager.setAdapter(mKqAdapter);

//        mViewPager.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                mDetector.onTouchEvent(motionEvent);
//                return false;
//            }
//        });


        mViewPager.addOnPageChangeListener(this);
        mTabLayout.addOnTabSelectedListener(this);

        mInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Saloon saloon = mKqAdapter.getCurrentSaloon(mViewPager.getCurrentItem());
                initDetailsActivity(saloon);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void initContentFragment(Saloon saloon, ModeType mode){
        mCurrentMode = mode;
//        if(contentFragment == null){
            contentFragment = ContentFragment.newInstance(mode);
            contentFragment.setmTabLayout(mTabLayout);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_content, contentFragment)
                    .commit();
//        }else {
//            contentFragment.changeMode(mCurrentMode);
//        }
    }

    public void initDetailsActivity(Saloon saloon) {
        if(saloon != null) {
            Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
            intent.putExtra(EXTRA_SALOON, saloon);
            overridePendingTransition(R.anim.slide_down, R.anim.slide_up);

            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(this, mViewPager, getString(R.string.transition_image));

            startActivity(intent, options.toBundle());
        }else{
            Toast.makeText(getApplicationContext(), "Error loading K&Q", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchSaloons(){
        KQContentful.getInstance(getApplicationContext())
            .fetchSaloonsVault(new KQContentful.OnSaloonsFetchedListener() {
                @Override
                public void onComplete(ArrayList<Saloon> saloons) {
                    mSaloons = saloons;
                    mKqAdapter.setSaloons(mSaloons);
                    onPageSelected(0);
                    initContentFragment(mSaloons.get(0), mCurrentMode);
                }

                @Override
                public void onException(Exception e) {
                    Log.e(TAG, "fetchSaloons", e);
                }
            });
    }

    /********  ViewPager Methods ***********/
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        mCurrentSaloon = position;
        mKqAdapter.onPageChanged(mCurrentSaloon);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /********  ViewPager Methods ***********/

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mCurrentMode = ModeType.fromInt(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    class KQGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final String DEBUG_TAG = "Gestures";

        @Override
        public boolean onDown(MotionEvent event) {
            Log.d(DEBUG_TAG,"onDown: " + event.toString());
            return false;
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            Log.d(DEBUG_TAG, "onFling: " + event1.toString()+event2.toString());
            if(event2.getY() < event1.getY()){
                initDetailsActivity(mKqAdapter.getCurrentSaloon(mViewPager.getCurrentItem()));
            }
            return false;
        }
    }
}