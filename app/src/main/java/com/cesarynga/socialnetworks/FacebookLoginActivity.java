package com.cesarynga.socialnetworks;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FacebookLoginActivity extends AppCompatActivity {

    private static final String TAG = "FacebookLoginActivity";

    @BindView(R.id.btn_fb_login)
    LoginButton btnFbLogin;

    @BindView(R.id.img_fb_profile)
    ProfilePictureView imgProfile;

    @BindView(R.id.txt_user_name)
    TextView txtUserName;

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook);

        ButterKnife.bind(this);

        callbackManager = CallbackManager.Factory.create();
//        btnFbLogin.setReadPermissions("email");
        btnFbLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(FacebookLoginActivity.this, R.string.fb_login_success, Toast.LENGTH_SHORT).show();
                updateUI();
            }

            @Override
            public void onCancel() {
                Toast.makeText(FacebookLoginActivity.this, R.string.fb_login_cancel, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Log.e(TAG, "Facebook login error", error);
                Toast.makeText(FacebookLoginActivity.this, R.string.fb_login_error, Toast.LENGTH_SHORT).show();
            }
        });
        new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(
                    final Profile oldProfile,
                    final Profile currentProfile) {
                updateUI();
            }
        };

        GraphRequest.Callback callback = new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {
                try {
                    if(response.getError() != null) {
                        Toast.makeText(
                                FacebookLoginActivity.this,
                                "not logged",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                    else if (response.getJSONObject().getBoolean("success")) {
                        LoginManager.getInstance().logOut();
                        // updateUI();?
                    }
                } catch (JSONException ex) { /* no op */ }
            }
        };
        GraphRequest request = new GraphRequest(AccessToken.getCurrentAccessToken(),
                "me/permissions", new Bundle(), HttpMethod.DELETE, callback);
        request.executeAsync();

        updateUI();
    }

    private void updateUI() {
        Profile profile = Profile.getCurrentProfile();
        if (profile != null) {
            imgProfile.setProfileId(profile.getId());
            String userName = String.format("%s %s", profile.getFirstName(), profile.getLastName());
            txtUserName.setText(userName);
            getUserInfo();
        } else {
            imgProfile.setProfileId(null);
            txtUserName.setText(null);
        }
    }

    private void getUserInfo() {
        GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d(TAG, "onCompleted: " + object);
                    }
                });
        Bundle params = new Bundle();
        params.putString("fields", "email,picture");
        graphRequest.setParameters(params);
        graphRequest.executeAsync();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
