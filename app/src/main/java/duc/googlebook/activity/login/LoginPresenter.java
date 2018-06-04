package duc.googlebook.activity.login;

import android.app.Activity;

import com.facebook.CallbackManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.Task;

interface LoginPresenter {
    void loginFb(Activity context, int RC_SIGN_IN, LoginButton loginButton, CallbackManager callbackManager);

    void loginGG(Activity context, int RC_SIGN_IN, SignInButton signInButton, GoogleSignInClient mGoogleSignInClient);

    void fbData(Activity context, LoginResult result, int RC_SIGN_IN);

    void signIn(Activity context, int RC_SIGN_IN, GoogleSignInClient mSignInClient);

    void handleSignInResult(Activity context, int RC_SIGN_IN, Task<GoogleSignInAccount> task);

    void updateUI(Activity context, int RC_SIGN_IN, GoogleSignInAccount account);
}
