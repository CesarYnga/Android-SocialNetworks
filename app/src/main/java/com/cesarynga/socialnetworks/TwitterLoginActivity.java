package com.cesarynga.socialnetworks;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TwitterLoginActivity extends AppCompatActivity {

    private static final String TAG = "TwitterLoginActivity";

    @BindView(R.id.btn_twitter_login)
    TwitterLoginButton btnTwitterLogin;

    @BindView(R.id.btn_twitter_logout)
    Button btnTwitterLogout;

    @BindView(R.id.img_twitter_profile)
    ImageView imgProfile;

    @BindView(R.id.txt_user_name)
    TextView txtUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_login);

        ButterKnife.bind(this);

        btnTwitterLogin.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Toast.makeText(TwitterLoginActivity.this, R.string.twitter_login_success, Toast.LENGTH_SHORT).show();
                updateUI(TwitterCore.getInstance().getSessionManager().getActiveSession());
                getAccountData();
            }

            @Override
            public void failure(TwitterException exception) {
                Log.e(TAG, "Twitter login error", exception);
                Toast.makeText(TwitterLoginActivity.this, R.string.twitter_login_error, Toast.LENGTH_SHORT).show();
            }
        });

        btnTwitterLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TwitterCore.getInstance().getSessionManager().clearActiveSession();
                updateUI(null);
            }
        });

        getTwitterSession();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        btnTwitterLogin.onActivityResult(requestCode, resultCode, data);
    }

    private void getTwitterSession() {
        TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
        if (session != null) {
            getAccountData();
        } else {
            Toast.makeText(this, "Not logged in on Twitter", Toast.LENGTH_SHORT).show();
        }
        updateUI(session);
    }

    private void getAccountData() {
        TwitterCore.getInstance().getApiClient().getAccountService().verifyCredentials(true, true, false).enqueue(new Callback<User>() {
            @Override
            public void success(Result<User> result) {
                Log.d(TAG, "Twitter account success");
                Glide.with(TwitterLoginActivity.this)
                        .load(result.data.profileImageUrl)
                        .into(imgProfile);

                txtUserName.setText(result.data.name);
            }

            @Override
            public void failure(TwitterException exception) {
                Log.e(TAG, "Twitter account error", exception);
            }
        });
    }

    private void updateUI(TwitterSession session) {
        if (session != null) {
            btnTwitterLogin.setVisibility(View.GONE);
            btnTwitterLogout.setVisibility(View.VISIBLE);
        } else {
            btnTwitterLogin.setVisibility(View.VISIBLE);
            btnTwitterLogout.setVisibility(View.GONE);
            imgProfile.setImageBitmap(null);
            txtUserName.setText(null);
        }
    }
}
