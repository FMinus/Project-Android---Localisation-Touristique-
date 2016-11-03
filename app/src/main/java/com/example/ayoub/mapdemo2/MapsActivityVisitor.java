package com.example.ayoub.mapdemo2;

import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Models.EventType;
import Models.Marker;
import Services.DatabaseHandlerMarkers;
import Services.HttpConnection;
import Services.PathJSONParser;

public class MapsActivityVisitor extends FragmentActivity implements
        OnMapReadyCallback ,
        GoogleMap.OnMarkerClickListener ,
        OnMapClickListener ,
        GoogleMap.OnInfoWindowClickListener,
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener

{

    private GoogleMap mMap;
    protected GoogleApiClient mGoogleApiClient;
    protected static final String TAG = "Debug Current Location";
    protected Location mLastLocation;
    private LatLng myLaLang;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        buildGoogleApiClient();

    }

    protected synchronized void buildGoogleApiClient()
    {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    @Override
    protected void onStart()
    {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle connectionHint)
    {
        // Provides a simple way of getting a device's location and is well suited for
        // applications that do not require a fine-grained location and that do not need location
        // updates. Gets the best and most recent location currently available, which may be null
        // in rare cases when a location is not available.
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null)
        {
            Log.d(TAG, "Lat : " + mLastLocation.getLatitude() + " ; Log : " + mLastLocation.getLongitude());

            LatLng here = new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());

            myLaLang = here;

            com.google.android.gms.maps.model.Marker MarkerEnsakh = mMap.addMarker(new MarkerOptions()
                    .position(here)
                    .title("Vous Etes Ici")
                    .snippet("Votre Position Actuelle"));
        }
        else
        {
            Toast.makeText(this, "no_location_detected", Toast.LENGTH_LONG).show();
            Log.d(TAG, "no_location_detected");
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result)
    {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.d(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    @Override
    public void onConnectionSuspended(int cause)
    {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.d(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
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

        com.google.android.gms.maps.model.Marker MarkerEnsakh = mMap.addMarker(new MarkerOptions()
                .position(Ensakh)
                .title("ENSAKH")
                .snippet("Ecole National des Sciences Appliquées"));

        loadMarkers();

        mMap.setMyLocationEnabled(true);

        float zoomLevel = 16;

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Ensakh, zoomLevel));

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

        //Location toLocation = new Location()

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
    public void onInfoWindowClick(com.google.android.gms.maps.model.Marker marker)
    {
        Toast toast = Toast.makeText(getApplicationContext(), "Info Window clicked", Toast.LENGTH_SHORT);
        toast.show();

        LatLng To = marker.getPosition();

        String url = getMapsApiDirectionsUrl(myLaLang,To);
        ReadTask downloadTask = new ReadTask();
        downloadTask.execute(url);

        //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(BROOKLYN_BRIDGE,13));


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

    @Override
    public void onLocationChanged(Location location)
    {
        //TextView locationTv = (TextView) findViewById(R.id.latlongLocation);

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);

        mMap.addMarker(new MarkerOptions().position(latLng));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        //locationTv.setText("Latitude:" + latitude + ", Longitude:" + longitude);

        Toast toast = Toast.makeText(getApplicationContext(), "Location Changed", Toast.LENGTH_SHORT);
        toast.show();
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    private void drawPrimaryLinePath( ArrayList<Location> listLocsToDraw )
    {
        if ( mMap == null )
        {
            return;
        }

        if ( listLocsToDraw.size() < 2 )
        {
            return;
        }

        PolylineOptions options = new PolylineOptions();

        options.color( Color.parseColor("#CC0000FF") );
        options.width( 5 );
        options.visible( true );

        for ( Location locRecorded : listLocsToDraw )
        {
            options.add( new LatLng( locRecorded.getLatitude(),
                    locRecorded.getLongitude() ) );
        }

        mMap.addPolyline( options );
    }

    public void drawdirectLine(LatLng From,LatLng To)
    {
        if(From == null || To == null)
            return;

        PolylineOptions rectOptions = new PolylineOptions()
                .add(From)
                .add(To);

        Polyline polyline = mMap.addPolyline(rectOptions);
    }

    private String getMapsApiDirectionsUrl(LatLng From,LatLng To)
    {
        String url = "https://maps.googleapis.com/maps/api/directions/json?" +
                "origin=" + From.latitude + "," + From.longitude+
                "&" +
                "destination=" + To.latitude + "," + To.longitude;

        return url;
    }

    private class ReadTask extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... url)
        {
            String data = "";
            try
            {
                HttpConnection http = new HttpConnection();
                data = http.readUrl(url[0]);
            }
            catch (Exception e)
            {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result)
        {
            try
            {
                super.onPostExecute(result);
                new ParserTask().execute(result);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Service Non Disponible Pour L'instant", Toast.LENGTH_SHORT).show();

            }

        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>>
    {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData)
        {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                PathJSONParser parser = new PathJSONParser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> routes)
        {
            ArrayList<LatLng> points ;
            PolylineOptions polyLineOptions=null;

            // traversing through routes
            for (int i = 0; i < routes.size(); i++)
            {
                points = new ArrayList<LatLng>();
                polyLineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = routes.get(i);

                for (int j = 0; j < path.size(); j++)
                {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                polyLineOptions.addAll(points);
                polyLineOptions.width(2);
                polyLineOptions.color(Color.BLUE);
            }

            mMap.addPolyline(polyLineOptions);
        }
    }

}
