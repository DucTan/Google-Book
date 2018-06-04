package duc.googlebook.activity.login;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import duc.googlebook.activity.main.MainActivity;

public class LoginPresenterImp implements LoginPresenter {

    @Override
    public void loginFb(final Activity context, final int RC_SIGN_IN, LoginButton loginButton, CallbackManager callbackManager) {
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                fbData(context, loginResult, RC_SIGN_IN);
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    @Override
    public void loginGG(final Activity context, final int RC_SIGN_IN, SignInButton signInButton, final GoogleSignInClient mGoogleSignInClient) {
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(context, RC_SIGN_IN, mGoogleSignInClient);
            }
        });
    }

    @Override
    public void fbData(final Activity context, LoginResult result, final int RC_SIGN_IN) {
        GraphRequest request = GraphRequest.newMeRequest(result.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                if (response != null)
                    try {
                        Intent i = new Intent(context, MainActivity.class);
                        i.putExtra("EMAIL", object.getString("email"));
                        i.putExtra("NAME", object.getString("name"));
                        i.putExtra("ID", object.getString("id"));
                        context.startActivityForResult(i, RC_SIGN_IN);
                        context.finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "email,name,id");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    public void signIn(final Activity context, final int RC_SIGN_IN, GoogleSignInClient mGoogleSignInClient) {
        context.startActivityForResult(mGoogleSignInClient.getSignInIntent(), RC_SIGN_IN);
    }

    @Override
    public void handleSignInResult(final Activity context, final int RC_SIGN_IN, Task<GoogleSignInAccount> task) {
        try {
            updateUI(context, RC_SIGN_IN, task.getResult(ApiException.class));
        } catch (ApiException e) {
            updateUI(context, RC_SIGN_IN, null);
        }
    }

    @Override
    public void updateUI(final Activity context, final int RC_SIGN_IN, GoogleSignInAccount account) {
        if (account != null) {
            String personName = account.getDisplayName();
            String personEmail = account.getEmail();
            Uri personImage = account.getPhotoUrl();
            Intent i = new Intent(context, MainActivity.class);
            i.putExtra("EMAIL", personEmail);
            i.putExtra("NAME", personName);
            i.putExtra("AVATAR", String.valueOf(personImage));
            context.startActivityForResult(i, RC_SIGN_IN);
            context.finish();
        }
    }
}
