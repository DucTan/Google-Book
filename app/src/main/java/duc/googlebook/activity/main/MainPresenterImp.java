package duc.googlebook.activity.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import duc.googlebook.R;
import duc.googlebook.activity.login.LoginActivity;
import duc.googlebook.activity.mybook.MyBookActivity;
import duc.googlebook.database.DataBase;
import duc.googlebook.json.GetNetworkData;
import duc.googlebook.model.Book;

import static com.facebook.FacebookSdk.getApplicationContext;

public class MainPresenterImp implements MainPresenter {

    private final DataBase dataBase = new DataBase(getApplicationContext());

    @Override
    public void itemList(Activity context, NavigationView navigationView, GetNetworkData getData) {
        navigationView.getMenu().getItem(0).setChecked(true);
        context.setTitle(R.string.app_name);
        getData.refesh("");
    }

    @Override
    public void itemBookmark(Activity context, NavigationView navigationView, GetNetworkData getData) {
        navigationView.getMenu().getItem(1).setChecked(true);
        context.setTitle("Bookmark");
        getData.refesh("bookmark");
    }

    @Override
    public void itemFavorite(Activity context, NavigationView navigationView, GetNetworkData getData) {
        navigationView.getMenu().getItem(2).setChecked(true);
        context.setTitle("Favorites");
        getData.refesh("favorite");
    }

    @Override
    public void swipeItemLv(RecyclerView lv, final NavigationView navigationView, final GetNetworkData getData) {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                if (navigationView.getMenu().getItem(1).isChecked()) {
                    Book book = dataBase.getAllBookmark().get(position);
                    dataBase.deleteBookmark(book);
                    getData.refesh("bookmark");
                    Toast.makeText(getApplicationContext(), "Remove success from bookmarks", Toast.LENGTH_SHORT).show();
                } else if (navigationView.getMenu().getItem(2).isChecked()) {
                    Book book = dataBase.getAllFavorite().get(position);
                    dataBase.deleteFavorite(book);
                    getData.refesh("favorite");
                    Toast.makeText(getApplicationContext(), "Remove success from favorites", Toast.LENGTH_SHORT).show();
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

    @Override
    public void signOutFromGplus(GoogleSignInClient mGoogleSignInClient) {
        mGoogleSignInClient.revokeAccess();
    }

    @Override
    public void signOutFromFb() {
        LoginManager.getInstance().logOut();
    }

    @Override
    public void getUserData(Activity context, TextView name, TextView email, ProfilePictureView avatarFb, CircleImageView avatarGG) {
        Intent i = context.getIntent();
        name.setText(i.getStringExtra("NAME"));
        email.setText(i.getStringExtra("EMAIL"));
        if (AccessToken.getCurrentAccessToken() != null) {
            avatarFb.setVisibility(View.VISIBLE);
            avatarFb.setProfileId(i.getStringExtra("ID"));
        } else {
            if (GoogleSignIn.getLastSignedInAccount(getApplicationContext()) != null) {
                avatarFb.setVisibility(View.INVISIBLE);
                Picasso.with(getApplicationContext()).load(Uri.parse(i.getStringExtra("AVATAR"))).into(avatarGG);
            }
        }
    }

    public void showAlertDialog(final Activity context, final NavigationView navigationView, final GetNetworkData getData, final GoogleSignInClient mGoogleSignInClient, final FloatingActionButton fab) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setMessage("Bạn có muốn đăng xuất không ??");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (AccessToken.getCurrentAccessToken() != null) {
                    signOutFromFb();
                } else {
                    signOutFromGplus(mGoogleSignInClient);
                }
                context.startActivity(new Intent(context, LoginActivity.class));
                context.finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                itemList(context, navigationView, getData);
                fab.setClickable(true);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void setNavItemClick(final Activity context, final NavigationView navigationView, final DrawerLayout drawerLayout, final GetNetworkData getData, final GoogleSignInClient mGoogleSignInClient, final FloatingActionButton fab) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                switch (item.getItemId()) {
                    case R.id.nav_list:
                        itemList(context, navigationView, getData);
                        fab.setClickable(true);
                        break;
                    case R.id.nav_bookmark:
                        itemBookmark(context, navigationView, getData);
                        fab.setClickable(false);
                        break;
                    case R.id.nav_favorite:
                        itemFavorite(context, navigationView, getData);
                        fab.setClickable(false);
                        break;
                    case R.id.nav_my:
                        drawerLayout.closeDrawers();
                        context.startActivity(new Intent(context, MyBookActivity.class));
                        fab.setClickable(true);
                        break;
                    case R.id.nav_logout:
                        showAlertDialog(context, navigationView, getData, mGoogleSignInClient, fab);
                        break;
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    @Override
    public void scrollRecycleView(RecyclerView recyclerView, final FloatingActionButton fab) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    // Scroll Down
                    if (fab.isShown()) {
                        fab.hide(true);
                    }
                } else {
                    // Scroll Up
                    if (!fab.isShown()) {
                        fab.show(true);
                    }
                }
            }
        });
    }

    @Override
    public void fabClick(FloatingActionButton fab, final GetNetworkData getData) {
        final boolean[] fabCheck = {true};
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fabCheck[0]) {
                    getData.refesh("za");
                    Toast.makeText(getApplicationContext(), "a - z", Toast.LENGTH_LONG).show();
                    fabCheck[0] = false;
                } else {
                    getData.refesh("");
                    Toast.makeText(getApplicationContext(), "z - a", Toast.LENGTH_LONG).show();
                    fabCheck[0] = true;
                }

            }
        });
    }

}
