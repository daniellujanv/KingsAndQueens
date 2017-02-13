package dlapps.dlv.kqandroid;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.box.androidsdk.content.BoxApi;
import com.box.androidsdk.content.BoxApiFile;
import com.box.androidsdk.content.BoxApiFolder;
import com.box.androidsdk.content.BoxConfig;
import com.box.androidsdk.content.BoxConstants;
import com.box.androidsdk.content.BoxException;
import com.box.androidsdk.content.auth.BoxAuthentication;
import com.box.androidsdk.content.models.BoxItem;
import com.box.androidsdk.content.models.BoxIteratorItems;
import com.box.androidsdk.content.models.BoxSession;
import com.box.androidsdk.content.models.BoxUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import dlapps.dlv.kqandroid.Utils.ModeType;
import dlapps.dlv.kqandroid.adapters.KQPagerAdapter;
import dlapps.dlv.kqandroid.fragments.ContentFragment;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, TabLayout.OnTabSelectedListener, BoxAuthentication.AuthListener {

    private final String TAG = getClass().getSimpleName();
    @BindView(R.id.main_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.main_collapsable_header)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.main_appbar)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.main_tablayout)
    TabLayout mTabLayout;
    @BindView(R.id.main_content_view_pager)
    ViewPager mViewPager;
    @BindView(R.id.main_content_info_button)
    ImageView mInfoButton;

    private KQPagerAdapter mKqAdapter;
    private ModeType mCurrentMode;
    private int mCurrentSaloon = 0;


    private BoxApiFolder mFolderApi;
    private BoxApiFile mFileApi;
    private BoxSession mSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);


        setSupportActionBar(mToolbar);

        mCollapsingToolbarLayout.setTitle("Kings&Queens Plaza Serena");
        mToolbar.setTitle(getString(R.string.app_name));

        onModeSelected("Kings&Queens Plaza Serena", ModeType.PLAYDATES);

        mKqAdapter = new KQPagerAdapter(getApplicationContext(), mCollapsingToolbarLayout);
        mViewPager.setAdapter(mKqAdapter);

        mViewPager.addOnPageChangeListener(this);
        mTabLayout.addOnTabSelectedListener(this);

        mInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, mKqAdapter.mSaloons[mCurrentSaloon],
                        Toast.LENGTH_SHORT).show();
            }
        });

        configureBox();
    }

    public void onModeSelected(String saloon, ModeType mode){

        mCurrentMode = mode;
        Fragment contentFragment = ContentFragment.newInstance(saloon, mode);
        ((ContentFragment) contentFragment).setmTabLayout(mTabLayout);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_content, contentFragment)
                .commit();

    }

    BoxAuthentication.BoxAuthenticationInfo refreshedInfo;
    private final BoxAuthentication.AuthenticationRefreshProvider refreshProvider = new BoxAuthentication.AuthenticationRefreshProvider() {
        @Override
        public BoxAuthentication.BoxAuthenticationInfo refreshAuthenticationInfo(BoxAuthentication.BoxAuthenticationInfo info) throws BoxException {
// Do things to retrieve updated access token from the previous info.
            Log.v(TAG, "refreshAuthenticationInfo");
            return refreshedInfo;
        }

        @Override
        public boolean launchAuthUi(String userId, BoxSession session) {
// return true if developer wishes to launch their own activity to interact with user for login.
// Activity should call BoxAuthentication. BoxAuthentication.getInstance().onAuthenticated() or onAuthenticationFailure() as appropriate.
// Make sure to use an application context here when starting your activity to avoid memory leaks.

            BoxAuthentication.BoxAuthenticationInfo info  = session.getAuthInfo();
            BoxUser user = info.getUser();
//            BoxAuthentication.getInstance().onAuthenticated(info, getApplicationContext());
            onAuthCreated(info);
            Log.v(TAG, "launchAuthUi");

            return true;
        }
    };

    private void configureBox(){
        BoxAuthentication.getInstance().setRefreshProvider(refreshProvider);
        BoxConfig.CLIENT_ID = "usq8po8mab1en2nyx33n773ja905tpgw";
        BoxConfig.CLIENT_SECRET = "NMFZR3EsZYGnm8KeVJe9ZDNzpzTZSE2Z";
        initBoxSession();
    }

    private void initBoxSession(){

        BoxAuthentication.BoxAuthenticationInfo info = new BoxAuthentication.BoxAuthenticationInfo();
// Populate info with as much information known as possible. If the access token is not provided the refresh provider implementation's launchAuthUi method will be invoked.
        info.setAccessToken("qtEKJGIepTRtOXNnntDAQ4Z6CPI6iarf");
        info.setClientId("1027287452");
//        info.setUser(BoxUser.createFromId("1027287452"));


        mSession = new BoxSession(getApplicationContext(), info, null);
        mSession.setBoxAccountEmail("AppUser_329082_dNJt5ufSKl@boxdevedition.com");
        mSession.setSessionAuthListener(this);

        mSession.authenticate(this);

    }

    private void loadRootFolder(){
        new Thread() {
            @Override
            public void run() {
                try {

                    final BoxIteratorItems folderItems = mFolderApi.getItemsRequest(BoxConstants.ROOT_FOLDER_ID).send();
                    for (BoxItem boxItem: folderItems) {

                        Log.v(TAG, "FILENAME::: "+boxItem.getName());
                    }
                } catch (BoxException e) {
                    e.printStackTrace();
                }
            }
        }.start();
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


    /********  Box Auth Listener  *********/
    @Override
    public void onRefreshed(BoxAuthentication.BoxAuthenticationInfo info) {
        Log.v(TAG, "onRefreshed");

    }

    @Override
    public void onAuthCreated(BoxAuthentication.BoxAuthenticationInfo info) {
//Init file, and folder apis; and use them to fetch the root folder
        Log.v(TAG, "onAuthCreated");
        mFolderApi = new BoxApiFolder(mSession);
        mFileApi = new BoxApiFile(mSession);
        loadRootFolder();
    }

    @Override
    public void onAuthFailure(BoxAuthentication.BoxAuthenticationInfo info, Exception ex) {
        Log.v(TAG, "onAuthFailure");

    }

    @Override
    public void onLoggedOut(BoxAuthentication.BoxAuthenticationInfo info, Exception ex) {
        Log.v(TAG, "onLoggedOut");

    }
}
