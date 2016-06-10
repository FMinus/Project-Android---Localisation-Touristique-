package com.example.ayoub.mapdemo2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class AdminActionActivity extends AppCompatActivity
{
    Button toMap;
    Button listMarkers;
    Button deleteTestMarkers;
    Button quitter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_action);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        toMap = (Button) findViewById(R.id.toMap);
        deleteTestMarkers = (Button) findViewById(R.id.deleteTestMarkers);

        toMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
                startMapAdmin();
                finish();
            }
        });

        deleteTestMarkers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
            }
        });
    }

    public void startMapAdmin()
    {
        Intent intent = new Intent(this, MapsActivityAdmin.class);
        startActivity(intent);
    }

}
