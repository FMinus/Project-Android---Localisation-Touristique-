package com.example.ayoub.mapdemo2;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import Models.Point;
import Services.DatabaseHandlerMarkers;

public class MapsActivityVisitor extends FragmentActivity implements
        OnMapReadyCallback ,
        GoogleMap.OnMarkerClickListener ,
        OnMapClickListener ,
        GoogleMap.OnInfoWindowClickListener
{


    private GoogleMap mMap;

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


        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);

        LatLng Ensakh = new LatLng(32.897151,-6.913716);


        //mMap.addMarker(new MarkerOptions().position(Ensakh).title("Marker in ENSAKH"));
        Marker MarkerEnsakh = mMap.addMarker(new MarkerOptions()
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
    public boolean onMarkerClick(Marker marker)
    {
        Toast toast = Toast.makeText(getApplicationContext(), "Marker Clicked", Toast.LENGTH_SHORT);
        toast.show();

        marker.showInfoWindow();
        Log.i("Marker", "Clicked");

        return false;
    }

    @Override
    public void onMapClick(LatLng latLng)
    {
        Toast toast = Toast.makeText(getApplicationContext(), "Map Clicked", Toast.LENGTH_SHORT);
        toast.show();

        //mMap.addMarker(new MarkerOptions().position(latLng).title("New Marker"));

    }

    @Override
    public void onInfoWindowClick(Marker marker)
    {
        Toast toast = Toast.makeText(getApplicationContext(), "Info Window clicked", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void loadMarkers()
    {
        DatabaseHandlerMarkers db = new DatabaseHandlerMarkers(getApplicationContext());
        LatLng tempLatng;
        try
        {
            List<Point> coords = db.getAllCoords();

            for (Point p : coords)
            {
                System.out.println("Key = " + p.getEvent() + ", Value : x = "+p.getX()+" ; y = "+p.getY() );
                //tempLatng = new LatLng(entry.getValue().,-6.913716);
                tempLatng = new LatLng(p.getX(),p.getY());
                mMap.addMarker(new MarkerOptions().position(tempLatng).title(p.getEvent()));

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
