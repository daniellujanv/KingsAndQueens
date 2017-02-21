package dlapps.dlv.kqandroid;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import dlapps.dlv.kqandroid.Utils.KQContentful;
import dlapps.dlv.kqandroid.Utils.ModeType;
import dlapps.dlv.kqandroid.adapters.KQPagerAdapter;
import dlapps.dlv.kqandroid.fragments.ContentFragment;
import dlapps.dlv.kqandroid.objects.Saloon;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, TabLayout.OnTabSelectedListener {

    private final String TAG = getClass().getSimpleName();

    @BindView(R.id.main_toolbar) Toolbar mToolbar;
    @BindView(R.id.main_collapsable_header) CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.main_tablayout) TabLayout mTabLayout;
    @BindView(R.id.main_content_view_pager) ViewPager mViewPager;
    @BindView(R.id.main_content_info_button) ImageView mInfoButton;

    private ArrayList<Saloon> mSaloons = new ArrayList<>();

    private KQPagerAdapter mKqAdapter;
    private ModeType mCurrentMode = ModeType.PLAYDATES; //init w/playdates
    private ContentFragment contentFragment;

    private int mCurrentSaloon = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

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

        mKqAdapter = new KQPagerAdapter(getApplicationContext(), mCollapsingToolbarLayout);
        mViewPager.setAdapter(mKqAdapter);

        mViewPager.addOnPageChangeListener(this);
        mTabLayout.addOnTabSelectedListener(this);

        mInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, mKqAdapter.mSaloons.get(mCurrentSaloon).name,
                        Toast.LENGTH_SHORT).show();
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

}