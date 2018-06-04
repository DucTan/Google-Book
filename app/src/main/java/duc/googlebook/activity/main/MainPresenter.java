package duc.googlebook.activity.main;

import android.app.Activity;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import de.hdodenhof.circleimageview.CircleImageView;
import duc.googlebook.json.GetNetworkData;

interface MainPresenter {

    void itemList(Activity context, NavigationView navigationView, GetNetworkData getData);

    void itemBookmark(Activity context, NavigationView navigationView, GetNetworkData getData);

    void itemFavorite(Activity context, NavigationView navigationView, GetNetworkData getData);

    void swipeItemLv(RecyclerView lv, NavigationView navigationView, GetNetworkData getData);

    void signOutFromGplus(GoogleSignInClient mGoogleSignInClient);

    void signOutFromFb();

    void showAlertDialog(Activity context, NavigationView navigationView, GetNetworkData getData, GoogleSignInClient mGoogleSignInClient, FloatingActionButton fab);

    void getUserData(Activity context, TextView name, TextView email, ProfilePictureView avatarFb, CircleImageView avatarGG);

    void setNavItemClick(Activity context, NavigationView navigationView, DrawerLayout drawerLayout, GetNetworkData getData, GoogleSignInClient mGoogleSignInClient, FloatingActionButton fab);

    void scrollRecycleView(RecyclerView recyclerView, FloatingActionButton fab);

    void fabClick(FloatingActionButton fab, GetNetworkData getData);
}
