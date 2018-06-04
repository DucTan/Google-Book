package duc.googlebook.activity.main;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import duc.googlebook.R;
import duc.googlebook.database.DataBase;
import duc.googlebook.json.GetNetworkData;

@SuppressWarnings("deprecation")
public class MainActivity extends AppCompatActivity {

    private SearchView searchView;

    private DataBase dataBase;

    private String query = "a";

    private RecyclerView lv;

    private GoogleSignInClient mGoogleSignInClient;

    private GetNetworkData getData;

    private ActionBarDrawerToggle drawerToggle;

    private NavigationView navigationView;

    private MainPresenter mainPresenter;

    private DrawerLayout drawerLayout;

    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initLoad();
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search_view).getActionView();
        assert searchManager != null;
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainPresenter.signOutFromFb();
        mainPresenter.signOutFromGplus(mGoogleSignInClient);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mainPresenter.signOutFromFb();
        mainPresenter.signOutFromGplus(mGoogleSignInClient);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (navigationView.getMenu().getItem(3).isChecked())
            mainPresenter.itemList(this, navigationView, getData);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("deprecation")
    private void initLoad() {
        //Navigation
        drawerLayout = findViewById(R.id.activity_main_drawer);
        navigationView = findViewById(R.id.nav_view);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //noinspection deprecation
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
        navigationView.getMenu().getItem(0).setChecked(true);

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        assert actionBar != null;
        actionBar.setHomeAsUpIndicator(R.drawable.list);

        //UI
        lv = findViewById(R.id.list);
        ProfilePictureView avatarFb = navigationView.getHeaderView(0).findViewById(R.id.activity_main_fb_avatar);
        CircleImageView avatar = navigationView.getHeaderView(0).findViewById(R.id.activity_main_imv_avatar);
        TextView name = navigationView.getHeaderView(0).findViewById(R.id.activity_main_tv_user_name);
        TextView email = navigationView.getHeaderView(0).findViewById(R.id.activity_main_tv_email);
        fab = findViewById(R.id.fab_sort);

        //Data
        mGoogleSignInClient = GoogleSignIn.getClient(this, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build());
        dataBase = new DataBase(getApplicationContext());
        getData = new GetNetworkData(dataBase, getApplicationContext(), lv);
        mainPresenter = new MainPresenterImp();
        dataBase.deleteAll();
        if (Objects.equals(query, "a"))
            mainPresenter.getUserData(this, name, email, avatarFb, avatar);
        getData.execute(query);
        mainPresenter.setNavItemClick(this, navigationView, drawerLayout, getData, mGoogleSignInClient, fab);
        mainPresenter.swipeItemLv(lv, navigationView, getData);
        mainPresenter.scrollRecycleView(lv, fab);
        mainPresenter.fabClick(fab, getData);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        dataBase.deleteAll();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            GetNetworkData data = new GetNetworkData(dataBase, getApplicationContext(), lv);
            data.execute(query);
            mainPresenter.setNavItemClick(MainActivity.this, navigationView, drawerLayout, data, mGoogleSignInClient, fab);
            mainPresenter.swipeItemLv(lv, navigationView, data);
            mainPresenter.scrollRecycleView(lv, fab);
            mainPresenter.fabClick(fab, data);
            navigationView.getMenu().getItem(0).setChecked(true);
            searchView.setIconified(true);
            searchView.clearFocus();
        }
    }
}