package dlapps.dlv.kqandroid;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.contentful.java.cda.CDAArray;
import com.contentful.java.cda.CDAAsset;
import com.contentful.java.cda.CDAClient;
import com.contentful.java.cda.CDAEntry;
import com.contentful.java.cda.CDAResource;
import com.contentful.java.cda.CDASpace;
import com.contentful.vault.SyncCallback;
import com.contentful.vault.SyncConfig;
import com.contentful.vault.SyncResult;
import com.contentful.vault.Vault;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Collections;
import java.util.List;

import dlapps.dlv.kqandroid.Utils.ModeType;
import dlapps.dlv.kqandroid.adapters.KQPagerAdapter;
import dlapps.dlv.kqandroid.fragments.ContentFragment;
import dlapps.dlv.kqandroid.objects.KQSpace;
import dlapps.dlv.kqandroid.objects.Playdate;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, TabLayout.OnTabSelectedListener {

    private final String TAG = getClass().getSimpleName();
    Toolbar mToolbar;
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    AppBarLayout mAppBarLayout;
    TabLayout mTabLayout;
    ViewPager mViewPager;
    ImageView mInfoButton;

    private KQPagerAdapter mKqAdapter;
    private ModeType mCurrentMode;
    private int mCurrentSaloon = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        //TODO implement splashscreen
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mCollapsingToolbarLayout  = (CollapsingToolbarLayout) findViewById(R.id.main_collapsable_header);
        mAppBarLayout  = (AppBarLayout) findViewById(R.id.main_appbar);
        mTabLayout  = (TabLayout) findViewById(R.id.main_tablayout);
        mViewPager  = (ViewPager) findViewById(R.id.main_content_view_pager);
        mInfoButton  = (ImageView) findViewById(R.id.main_content_info_button);

//        mToolbar.setTitle(getString(R.string.app_name));

        onModeSelected("Kings&Queens Plaza Serena", ModeType.PLAYDATES);

        mKqAdapter = new KQPagerAdapter(getApplicationContext(), mCollapsingToolbarLayout);
        mViewPager.setAdapter(mKqAdapter);

        mCollapsingToolbarLayout.setTitle("Kings&Queens Plaza Serena");

        mViewPager.addOnPageChangeListener(this);
        mTabLayout.addOnTabSelectedListener(this);

        mInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, mKqAdapter.mSaloons[mCurrentSaloon],
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void onModeSelected(String saloon, ModeType mode){

        mCurrentMode = mode;
        Fragment contentFragment = ContentFragment.newInstance(saloon, mode);
        ((ContentFragment) contentFragment).setmTabLayout(mTabLayout);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_content, contentFragment)
                .commit();
    }

    /********  ViewPager Methods ***********/

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        mCurrentSaloon = position;
        mKqAdapter.onPageChanged(mCurrentSaloon);
        onModeSelected(mKqAdapter.mSaloons[mCurrentSaloon], mCurrentMode);
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