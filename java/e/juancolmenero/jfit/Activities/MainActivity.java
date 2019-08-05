package e.juancolmenero.jfit.Activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Set;

import e.juancolmenero.jfit.Adapter.DrawerAdapter;
import e.juancolmenero.jfit.Adapter.TabAdapter;
import e.juancolmenero.jfit.Fragment.NewsFeedFragment;
import e.juancolmenero.jfit.Fragment.PostFragment;
import e.juancolmenero.jfit.Fragment.ProfileFragment;
import e.juancolmenero.jfit.Models.NavItemModel;
import e.juancolmenero.jfit.R;
import e.juancolmenero.jfit.SharedPrefManager;

public class MainActivity extends AppCompatActivity
implements NavigationView.OnNavigationItemSelectedListener{

    private TabAdapter tabAdapter;
    private TabLayout tabLayout;

    private int[] tabIcons = {
            R.drawable.baseline_home_white_24,
            R.drawable.baseline_calendar_today_white_24,
            R.drawable.baseline_person_outline_white_24
    };

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager;

        drawer = findViewById(R.id.drawer_layout);

        drawer.addDrawerListener(
                new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        // Respond when the drawer's position changes
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        // Respond when the drawer is opened
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        // Respond when the drawer is closed
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                        // Respond when the drawer motion state changes
                    }
                }
        );

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //SET TOOLBAR TO APPEAR
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        //actionbar.setHomeAsUpIndicator(R.drawable.baseline_menu_white_24);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        //CREATE TABS AND SEND TO TAB_ADAPTER
        viewPager = findViewById(R.id.content_frame);
        tabLayout = findViewById(R.id.tabLayout);
        tabAdapter = new TabAdapter(getSupportFragmentManager(), this);
        tabAdapter.addFragment(new NewsFeedFragment(), "Home", tabIcons[0]);
        tabAdapter.addFragment(new PostFragment(), "Planner", tabIcons[1]);
        tabAdapter.addFragment(new ProfileFragment(), "Profile", tabIcons[2]);
        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);
        highLightCurrentTab(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                highLightCurrentTab(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    /*OPEN DRAWER WHEN BUTTON IS PRESSED*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;
            case R.id.logout:
                SharedPrefManager.getInstance(getApplicationContext()).logout();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // set item as selected to persist highlight
        //item.setChecked(true);

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            
            Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_gallery) {
            Toast.makeText(this, "Favorites", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, FavoriteExerciseActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_slideshow) {
            Toast.makeText(this, "Workout List", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, SavedNamesActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_messages) {
            Toast.makeText(this, "Messages", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_manage) {
            Toast.makeText(this, "Stat of the Day", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_send) {
            Toast.makeText(this, "Send", Toast.LENGTH_SHORT).show();

        }else if (id == R.id.nav_share) {
            Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();

        }

        // close drawer when item is tapped
        drawer.closeDrawers();

        // set item as selected to persist highlight
        item.setChecked(false);

        return true;
    }

    /* HIGHLIGHT SELECTED TAB */
    private void highLightCurrentTab(int position) {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            assert tab != null;
            tab.setCustomView(null);
            tab.setCustomView(tabAdapter.getTabView(i));
        }
        TabLayout.Tab tab = tabLayout.getTabAt(position);
        assert tab != null;
        tab.setCustomView(null);
        tab.setCustomView(tabAdapter.getSelectedTabView(position));
    }


}
