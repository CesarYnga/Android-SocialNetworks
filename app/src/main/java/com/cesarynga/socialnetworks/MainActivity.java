package com.cesarynga.socialnetworks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnItemClick(R.id.lst_networks)
    public void onNetworkClick(int position) {
        Intent intent = null;
        switch (position) {
            case 0:
                intent = new Intent(this, FacebookLoginActivity.class);
                break;
            case 1:
                intent = new Intent(this, TwitterLoginActivity.class);
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }
}
