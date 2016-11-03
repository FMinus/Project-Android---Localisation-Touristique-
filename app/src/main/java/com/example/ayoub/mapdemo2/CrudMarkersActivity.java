package com.example.ayoub.mapdemo2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import Models.EventType;
import Models.Marker;
import Models.MarkerAdapter;
import Services.DatabaseHandlerMarkers;

/**
 * Created by Ayoub on 6/11/2016.
 */
public class CrudMarkersActivity extends Activity
{
    private ListView listview;

    ImageButton searchBtn ;
    ImageButton updateButton;
    ImageButton deleteBtn ;
    ImageButton resetBtn ;
    Button toMapBtn ;

    EditText editName;

    TextView eventView;

    DatabaseHandlerMarkers db;
    MarkerAdapter adapter;
    List<Marker> marker_data;

    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crud_markers);

        /*
         marker_data = new Marker[]
                {
                        new Marker("Educationel", EventType.Educationel, 10.0,20.0),
                        new Marker("Music",EventType.Music, 10.0,20.0),
                        new Marker("Sport",EventType.Sportif, 10.0,20.0)

                };
        */

        db = new DatabaseHandlerMarkers(getApplicationContext());

        marker_data =  db.getAllCoords();

        searchBtn = (ImageButton) findViewById(R.id.buttonRecherche);
        updateButton = (ImageButton) findViewById(R.id.buttonUpdate);
        deleteBtn = (ImageButton) findViewById(R.id.buttonSupp);
        resetBtn = (ImageButton) findViewById(R.id.buttonReset);
        toMapBtn = (Button) findViewById(R.id.buttonToMap);

        editName = (EditText) findViewById(R.id.eventNameEdit);
        eventView = (TextView) findViewById(R.id.event);

        adapter = new MarkerAdapter(this, R.layout.listview_item_row, marker_data);
        listview = (ListView) findViewById(R.id.listMarkers);

        View header = (View) getLayoutInflater().inflate(R.layout.listview_header_row, null);

        listview.addHeaderView(header);
        listview.setAdapter(adapter);


        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editName.getText().toString() != "")
                {
                    marker_data = db.getAllCoordsByEvent(editName.getText().toString());

                    adapter.clear();
                    adapter.addAll(marker_data);
                    adapter.notifyDataSetChanged();

                    if(marker_data.isEmpty())
                        eventView.setText("Aucun Resultat");
                }
                else
                {
                    eventView.setText("veuillez remplire le champs");
                }

            }
        });


        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (editName.getText().toString() != "") {
                    try
                    {
                        db.deleteMarkersByName(editName.getText().toString());
                        marker_data = db.getAllCoords();

                        marker_data = db.getAllCoords();
                        refreshList(marker_data);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }
                else
                {
                    eventView.setText("veuillez remplire le champs");
                }
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast toast = Toast.makeText(getApplicationContext(), "listClick " + adapter.getItem(position-1).getEvent(), Toast.LENGTH_SHORT);
                toast.show();

                eventView.setText("Selected Item : " + adapter.getItem(position-1).getEvent()
                                + " : "
                                + adapter.getItem(position-1).getEventType()
                                + " - \n"
                                + adapter.getItem(position-1).getDesc()
                );

                editName.setText(adapter.getItem(position-1).getEvent());

                index = position;

            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                if (editName.getText().toString() != "")
                {

                    db.updaterow(adapter.getItem(index-1).getEvent(),editName.getText().toString());
                    marker_data = db.getAllCoords();
                    refreshList(marker_data);
                }
                else
                {
                    eventView.setText("veuillez remplire le champs");
                }
            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                marker_data = db.getAllCoords();
                refreshList(marker_data);
            }
        });

        toMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startMapAdmin();
                finish();
            }
        });

    }

    public void startMapAdmin()
    {
        Intent intent = new Intent(this, MapsActivityAdmin.class);
        startActivity(intent);
    }

    public void refreshList(List<Marker> marker_data)
    {


        adapter.clear();
        adapter.addAll(marker_data);
        adapter.notifyDataSetChanged();

    }
}
