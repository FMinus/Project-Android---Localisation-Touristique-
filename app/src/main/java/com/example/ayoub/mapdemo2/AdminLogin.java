package com.example.ayoub.mapdemo2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import Models.User;
import Services.DatabaseHandlerLogin;

public class AdminLogin extends ActionBarActivity
{
    Button map;
    EditText editUsername;
    EditText editPassword;
    DatabaseHandlerLogin db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_login);

        map = (Button) findViewById(R.id.mapLauncher);
        editUsername = (EditText) findViewById(R.id.usernameLogin);
        editPassword = (EditText) findViewById(R.id.passwordLogin);

        db = new DatabaseHandlerLogin(getApplicationContext());

        //AddTestAdmin();

        map.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(editPassword.getText().toString()!="" && editUsername.getText().toString()!="")
                {
                    String username = editUsername.getText().toString();
                    String password = editPassword.getText().toString();

                    //User user = db.login(username,password);

                    if(username.equals("testAdmin") && password.equals("testAdmin"))
                    {
                        Toast.makeText(getApplicationContext(), "Logged in as Admin : "+editUsername.getText().toString(), Toast.LENGTH_SHORT).show();
                        startAdminActions();
                        finish();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "No Such User", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    public void startMapAdmin()
    {
        Intent intent = new Intent(this, MapsActivityAdmin.class);
        startActivity(intent);
    }

    public void startAdminActions()
    {
        Intent intent = new Intent(this, AdminActions.class);
        startActivity(intent);
    }

    public void AddTestAdmin()
    {
        try
        {
            User user = db.login("testAdmin","testAdmin");
        }
        catch (Exception e)
        {
            db.addrow("testAdmin","testAdmin");
        }
    }

}
