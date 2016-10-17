package com.wehelp.wehelp;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.wehelp.wehelp.tabs.FragmentMap;
import com.wehelp.wehelp.tabs.FragmentMyEvents;
import com.wehelp.wehelp.tabs.FragmentTimeline;

public class TabbedActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     *
     */


    private SectionsPagerAdapter mSectionsPagerAdapter;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mDrawerItmes;

    Toolbar mToolbar;
//    NetworkImageView profileImg;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    public GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if(Build.VERSION.SDK_INT>=21){
            mToolbar.setElevation(0);
        }
        mTitle = "WeHelp";
        mDrawerTitle = getTitle();
        mDrawerItmes = getResources().getStringArray(R.array.drawer_titles);
        mDrawerList = (ListView)findViewById(R.id.left_drawer);
        mDrawerLayout.setDrawerShadow(R.drawable.box_shadow, GravityCompat.START);

        LayoutInflater inflater = this.getLayoutInflater();
        View headerView = inflater.inflate(R.layout.drawer_list_header, null);
        mDrawerList.addHeaderView(headerView);

        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mDrawerItmes));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        ) {
            public void onDrawerClosed(View view) {
                mToolbar.setTitle(mTitle);

                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu
            }
            public void onDrawerOpened(View drawerView) {
//                profileImg.setImageUrl(access.getBaseURL() + "pictures/profile/" + settings.getString("picture", "")+"?t="+settings.getString("image_timestamp",""), imageLoader);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu
//                System.out.println(username.getText().toString());
//                System.out.println(settings.getString("username",""));
//                if(!username.getText().toString().equalsIgnoreCase(settings.getString("username",""))){
//                    username.setText(settings.getString("username",""));
//                }
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Implementar criação de novo evento", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    private class DrawerItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            navigateTo(position);
        }
    }

    private void navigateTo(int position) {

//        switch(position) {
//            case 1:
//                getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.content_frame, ProfileActivity.newInstance(), ProfileActivity.TAG).commit();
//                mTitle = "Meu perfil";
//                break;
//            case 2:
//
//                getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.content_frame, TabbedFragmentSocial.newInstance(), TabbedFragmentSocial.TAG).commit();
//                mTitle = "Social";
//                break;
//            case 3:
//                getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.content_frame, TabbedFragmentGames.newInstance(), TabbedFragmentGames.TAG).commit();
//                mTitle = "Games";
//                break;
//
//            case 4:
//
//                getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.content_frame, FragmentFriends.newInstance(), FragmentFriends.TAG).commit();
//                mTitle = "Amigos";
//                break;
//
//            case 5:
//
//                getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.content_frame, TopTenActivity.newInstance(), TopTenActivity.TAG).commit();
//                mTitle = "Top colecionadores";
//                break;
//
//            case 6:
//                getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.content_frame, TalkToActivity.newInstance(), TalkToActivity.TAG).commit();
//                mTitle = "Comente!";
//                break;
//
//            case 7:
//                Intent itLogoff = new Intent(this,LoginActivity.class);
//                SharedPreferences settings = getSharedPreferences("UserInfo", 0);
//
//                SharedPreferences.Editor editor = settings.edit();
//
//                editor.clear();
//                editor.commit();
//
//                EventBus.getDefault().unregister(this);
//                startActivity(itLogoff);
//                finish();
//
//                break;
//
//        }


        mDrawerLayout.closeDrawer(mDrawerList);


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tabbed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_tabbed, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch(position) {
                case 0:
                    return new FragmentMap();
                case 1:
                    return new FragmentTimeline();
                default:
                    return new FragmentTimeline();
            }

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Mapa";
                case 1:
                    return "Eventos";
            }
            return null;
        }
    }
}
