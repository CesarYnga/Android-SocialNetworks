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
        switch (position) {
            case 0:
                Intent intent = new Intent(this, FacebookLoginActivity.class);
                startActivity(intent);
                break;
        }
    }
}
