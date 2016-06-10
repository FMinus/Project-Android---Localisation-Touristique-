package com.example.ayoub.mapdemo2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity
{
    Button userBtn;
    Button adminBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        userBtn = (Button) findViewById(R.id.user);
        adminBtn = (Button) findViewById(R.id.admin);

        adminBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast toast = Toast.makeText(getApplicationContext(), "Admin Login", Toast.LENGTH_SHORT);
                toast.show();

                toLoginAdmin();
                finish();
            }
        });

        userBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast toast = Toast.makeText(getApplicationContext(), "Visitor", Toast.LENGTH_SHORT);
                toast.show();

                toVisitorMap();
                finish();
            }
        });

    }

    public void toLoginAdmin()
    {
        Intent intent = new Intent(this, AdminLogin.class);
        startActivity(intent);
    }

    public void toVisitorMap()
    {
        Intent intent = new Intent(this, MapsActivityVisitor.class);
        startActivity(intent);
    }
}
