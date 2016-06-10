package com.example.ayoub.mapdemo2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AdminLogin extends ActionBarActivity
{
    Button map;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_login);

        map = (Button) findViewById(R.id.mapLauncher);

        map.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast toast = Toast.makeText(getApplicationContext(), "texttoast", Toast.LENGTH_SHORT);
                toast.show();

                Intent intent = new Intent(getApplicationContext(), AdminActionActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }

    public void startMapAdmin()
    {
        Intent intent = new Intent(this, MapsActivityAdmin.class);
        startActivity(intent);
    }

}
