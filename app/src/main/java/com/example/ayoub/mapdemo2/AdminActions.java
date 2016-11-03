package com.example.ayoub.mapdemo2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AdminActions extends ActionBarActivity
{
    Button toMap;
    Button toListMarkers;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_actions);

        toMap = (Button) findViewById(R.id.toMapAdmin);
        toListMarkers = (Button) findViewById(R.id.listMarkers);

        toMap.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startMapAdmin();

            }
        });

        toListMarkers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCrudMarkers();
                finish();
            }
        });

    }

    public void startMapAdmin()
    {
        Intent intent = new Intent(this, MapsActivityAdmin.class);
        startActivity(intent);
    }

    public void startCrudMarkers()
    {
        Intent intent = new Intent(this, CrudMarkersActivity.class);
        startActivity(intent);
    }

}
