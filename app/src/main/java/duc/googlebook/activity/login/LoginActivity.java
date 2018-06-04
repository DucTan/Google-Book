package duc.googlebook.activity.login;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.Task;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import duc.googlebook.R;
import duc.googlebook.timetask.MyTimerTask;
import duc.googlebook.viewpager.ViewPagerAdapter;

public class LoginActivity extends AppCompatActivity {

    private LoginPresenter presenter;

    private ViewPager viewPager;

    private ViewPagerAdapter adapter;

    private LinearLayout sliderDots;

    private int dotCounts;

    private ImageView[] dots;

    private LoginButton loginButton;

    private SignInButton signInButton;

    private CallbackManager callbackManager;

    private GoogleSignInClient mGoogleSignInClient;

    private static final int RC_SIGN_IN = 234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initLoad();
        checkInternetConnection();
        slide();
        process();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            presenter.handleSignInResult(LoginActivity.this, RC_SIGN_IN, task);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.updateUI(LoginActivity.this, RC_SIGN_IN, GoogleSignIn.getLastSignedInAccount(this));
    }

    private void initLoad() {
        loginButton = findViewById(R.id.login_button);
        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));

        //Google
        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //Slide
        viewPager = findViewById(R.id.viewPager);
        sliderDots = findViewById(R.id.sliderdots);
        adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        presenter = new LoginPresenterImp();
    }

    private void checkInternetConnection() {
        @SuppressWarnings("AccessStaticViaInstance") ConnectivityManager connec = (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
        assert connec != null;
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED || connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING || connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING || connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
        } else if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED || connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            Toast.makeText(this, " Vui lòng kiểm tra kết nối mạng ", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * LoginPresenterImp
     */
    private void process() {
        presenter.loginFb(LoginActivity.this, RC_SIGN_IN, loginButton, callbackManager);
        presenter.loginGG(LoginActivity.this, RC_SIGN_IN, signInButton, mGoogleSignInClient);
    }

    /**
     * Slide
     */
    private void slide() {
        dotCounts = adapter.getCount();
        dots = new ImageView[dotCounts];
        for (int i = 0; i < dotCounts; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot_design));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);
            sliderDots.addView(dots[i], params);
        }
        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot_design));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < dotCounts; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot_design));
                }
                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot_design));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MyTimerTask(this, viewPager), 2000, 4000);
    }
}
