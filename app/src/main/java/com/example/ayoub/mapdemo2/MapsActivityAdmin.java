package com.example.ayoub.mapdemo2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import Models.EventType;
import Models.Marker;
import Models.User;
import Services.DatabaseHandlerMarkers;

public class MapsActivityAdmin extends FragmentActivity implements
        OnMapReadyCallback ,
        GoogleMap.OnMarkerClickListener ,
        OnMapClickListener ,
        GoogleMap.OnInfoWindowClickListener
{


    private GoogleMap mMap;
    int i = 0;
    User user;
    final Context context = this;
    String selectEventType;

    Marker m;
    LatLng templatLng;
    DatabaseHandlerMarkers db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;

        LatLng Ensakh = new LatLng(32.897151,-6.913716);


        //mMap.addMarker(new MarkerOptions().position(Ensakh).title("Marker in ENSAKH"));
        com.google.android.gms.maps.model.Marker MarkerEnsakh = mMap.addMarker(new MarkerOptions()
                .position(Ensakh)
                .title("ENSAKH")
                .snippet("Ecole National des Sciences Appliquées"));

        loadMarkers();

        float zoomLevel = 16;

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Ensakh, zoomLevel));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(Ensakh));

        mMap.setOnMapClickListener(this);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
    }

    @Override
    public boolean onMarkerClick(com.google.android.gms.maps.model.Marker marker)
    {
        Toast toast = Toast.makeText(getApplicationContext(), "Marker Clicked", Toast.LENGTH_SHORT);
        toast.show();

        marker.showInfoWindow();
        //Log.i("Marker", "Clicked");
        return false;
    }

    @Override
    public void onMapClick(LatLng latLng)
    {
        Toast.makeText(getApplicationContext(), "Map Clicked", Toast.LENGTH_SHORT).show();

        db = new DatabaseHandlerMarkers(getApplicationContext());

        templatLng = latLng;

        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.prompt_add_marker, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);


        alertDialogBuilder.setView(promptsView);

        final EditText eventNameEdit = (EditText) promptsView.findViewById(R.id.dialogEventName);
        final EditText eventDescEdit = (EditText) promptsView.findViewById(R.id.dialogEventDesc);

        final Spinner spinner = (Spinner) promptsView.findViewById(R.id.eventTypeSpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.event_type, android.R.layout.simple_spinner_item);


        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);



        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
            {
                selectEventType = parent.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog,int id)
                            {
                                // get user input and set it to result
                                // edit text


                                Toast toast = Toast.makeText(getApplicationContext(), "Nom Marker : "+eventNameEdit.getText()+" - type : "+selectEventType, Toast.LENGTH_SHORT);
                                toast.show();

                                m = new Marker(eventNameEdit.getText().toString(),
                                        EventType.valueOf(selectEventType),
                                        eventDescEdit.getText().toString(),
                                        templatLng.latitude,
                                        templatLng.longitude);

                                Log.d("test prompt",""+m);

                                db.addMarker(m);
                                loadMarkers();


                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();



    }

    @Override
    public void onInfoWindowClick(com.google.android.gms.maps.model.Marker marker)
    {
        Toast.makeText(getApplicationContext(), "Info Window clicked", Toast.LENGTH_SHORT).show();
    }

    public void loadMarkers()
    {
        DatabaseHandlerMarkers db = new DatabaseHandlerMarkers(getApplicationContext());
        LatLng tempLatng;
        try
        {
            List<Marker> coords = db.getAllCoords();

            for (Marker p : coords)
            {
                System.out.println("Key = " + p.getEvent() + ", Value : x = "+p.getLat()+" ; y = "+p.getLog() );
                //tempLatng = new LatLng(entry.getValue().,-6.913716);
                tempLatng = new LatLng(p.getLat(),p.getLog());

                mMap.addMarker(new MarkerOptions()
                        .position(tempLatng)
                        .title(p.getEvent())
                        .snippet(p.getDesc())
                        .icon(BitmapDescriptorFactory.defaultMarker(getMarkerColor(p.getEventType())))
                );
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public float getMarkerColor(EventType type)
    {
        switch (type)
        {
            case Educationel:
                return BitmapDescriptorFactory.HUE_GREEN;

            case Music:
                return BitmapDescriptorFactory.HUE_BLUE;

            case Sportif:
                return BitmapDescriptorFactory.HUE_ORANGE;
        }

        return BitmapDescriptorFactory.HUE_RED;
    }



}
