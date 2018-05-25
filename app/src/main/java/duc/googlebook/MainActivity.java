package duc.googlebook;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import duc.googlebook.database.DataBase;
import duc.googlebook.json.GetNetworkData;
import duc.googlebook.model.Book;

public class MainActivity extends AppCompatActivity {

    private RecyclerView lv;

    private ActionBarDrawerToggle drawerToggle;

    private NavigationView navigationView;

    private DataBase dataBase;

    private CircleImageView avatar;

    private TextView name, email;

    ProfilePictureView avatarFb;

    Intent i;

    DrawerLayout drawerLayout;

    GetNetworkData getData;

    GoogleSignInOptions gso;

    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initLoad();
        getUserData();
        setNavItemClick();
        swipeItemLv();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        signOutFromFb();
        signOutFromGplus();
    }

    @Override
    protected void onStop() {
        super.onStop();
        signOutFromFb();
        signOutFromGplus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (navigationView.getMenu().getItem(3).isChecked())
            itemList();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void initLoad() {
        drawerLayout = findViewById(R.id.activity_main_drawer);
        navigationView = findViewById(R.id.nav_view);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.list);

        lv = findViewById(R.id.list);
        avatarFb = navigationView.getHeaderView(0).findViewById(R.id.activity_main_fb_avatar);
        avatar = navigationView.getHeaderView(0).findViewById(R.id.activity_main_imv_avatar);
        name = navigationView.getHeaderView(0).findViewById(R.id.activity_main_tv_user_name);
        email = navigationView.getHeaderView(0).findViewById(R.id.activity_main_tv_email);
        dataBase = new DataBase(this);
        getData = new GetNetworkData(this, lv);
        getData.execute("https://www.googleapis.com/books/v1/volumes?q=a");
        navigationView.getMenu().getItem(0).setChecked(true);
    }

    /**
     * Set click
     */
    public void setNavItemClick() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                switch (item.getItemId()) {
                    case R.id.nav_list:
                        itemList();
                        break;
                    case R.id.nav_bookmark:
                        itemBookmark();
                        break;
                    case R.id.nav_favorite:
                        itemFavorite();
                        break;
                    case R.id.nav_my:
                        startActivity(new Intent(MainActivity.this, MyBookActivity.class));
                        break;
                    case R.id.nav_logout:
                        showAlertDialog();
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    /**
     * Data
     */
    private void getUserData() {
        i = getIntent();
        name.setText(i.getStringExtra("NAME"));
        email.setText(i.getStringExtra("EMAIL"));
        if (AccessToken.getCurrentAccessToken() != null) {
            avatarFb.setVisibility(View.VISIBLE);
            avatarFb.setProfileId(i.getStringExtra("ID"));
        } else {
            avatarFb.setVisibility(View.INVISIBLE);
            Picasso.with(getApplicationContext()).load(Uri.parse(i.getStringExtra("AVATAR"))).into(avatar);
        }
    }

    /**
     * Sign out account
     */
    private void signOutFromGplus() {
        mGoogleSignInClient.revokeAccess();
    }

    private void signOutFromFb() {
        LoginManager.getInstance().logOut();
    }

    public void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Bạn có muốn đăng xuất không ??");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (AccessToken.getCurrentAccessToken() != null) {
                    signOutFromFb();
                } else {
                    signOutFromGplus();
                }
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                itemList();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Swipe
     */
    public void swipeItemLv() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                if (navigationView.getMenu().getItem(1).isChecked()) {
                    Book book = dataBase.getAllNote("bookmark").get(position);
                    book.setBookmark("false");
                    dataBase.updateNote(book);
                    getData.refesh("bookmark");
                    Toast.makeText(getApplicationContext(), "Remove success from bookmarks", Toast.LENGTH_LONG).show();
                } else if (navigationView.getMenu().getItem(2).isChecked()) {
                    Book book = dataBase.getAllNote("favorite").get(position);
                    book.setFav("false");
                    dataBase.updateNote(book);
                    getData.refesh("favorite");
                    Toast.makeText(getApplicationContext(), "Remove success from favorites", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                if (navigationView.getMenu().getItem(0).isChecked() || navigationView.getMenu().getItem(3).isChecked())
                    return 0;
                return super.getSwipeDirs(recyclerView, viewHolder);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(lv);
    }

    /**
     * Item navigation
     */
    private void itemList() {
        navigationView.getMenu().getItem(0).setChecked(true);
        setTitle(R.string.app_name);
        getData.refesh("");
    }

    private void itemBookmark() {
        navigationView.getMenu().getItem(1).setChecked(true);
        setTitle("Bookmark");
        getData.refesh("bookmark");
    }

    private void itemFavorite() {
        navigationView.getMenu().getItem(2).setChecked(true);
        setTitle("Favorites");
        getData.refesh("favorite");
    }
}
