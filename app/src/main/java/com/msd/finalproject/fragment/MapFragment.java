package com.msd.finalproject.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.msd.finalproject.R;
import com.msd.finalproject.directionhelpers.TaskLoadedCallback;
import com.msd.finalproject.helper.DataBaseHelper;
import com.msd.finalproject.model.CurrentCoordinates;

import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback, TaskLoadedCallback {
    Button getDirection;
    String userActivityId = null;
    Boolean isData = false;
    Cursor cursor = null;
    DataBaseHelper databaseHandler;
    List<CurrentCoordinates> coordinatesList = null;
    private GoogleMap mMap;
    private MarkerOptions place1, place2;
    private Polyline currentPolyline;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_map, container, false);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        databaseHandler = new DataBaseHelper(getContext());
        userActivityId = getArguments().getString("userActivityId");
        coordinatesList = new ArrayList<>();
        Log.e("Activity", "activity id = " + userActivityId);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        return view;
    }

    private void fetchCoordinatebyActivity() {
        Cursor cursor = databaseHandler.fetchActivityCoordinatesById(userActivityId);
        if (cursor == null) {
            Toast.makeText(getActivity(), "No Record found", Toast.LENGTH_SHORT).show();
        } else {
            if (cursor.moveToFirst()) {
                do {
                    CurrentCoordinates coordinates = new CurrentCoordinates();
                    coordinates.setId(cursor.getInt(cursor.getColumnIndex("id")));
                    coordinates.setActivityId(cursor.getInt(cursor.getColumnIndex("activityId")));
                    coordinates.setLatitude(cursor.getDouble(cursor.getColumnIndex("latitude")));
                    coordinates.setLongitude(cursor.getDouble(cursor.getColumnIndex("longitude")));
                    coordinatesList.add(coordinates);
                } while (cursor.moveToNext());
            }
            if (coordinatesList.size() > 0) {
                isData = true;
                generatePlacesFromData();

            } else {
                isData = false;
            }
            cursor.close();
            databaseHandler.close();
        }

    }

    private void generatePlacesFromData() {

        place1 = new MarkerOptions().position(new LatLng(coordinatesList.get(0).getLatitude(), coordinatesList.get(0).getLongitude())).title("Starting Point");
        place2 = new MarkerOptions().position(new LatLng(coordinatesList.get(1).getLatitude(), coordinatesList.get(1).getLongitude())).title("Ending Point");

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        fetchCoordinatebyActivity();
        if (isData) {
            Log.d("mylog", "Added Markers");
            mMap.addMarker(place1);
            mMap.addMarker(place2);

        } else {

            Toast.makeText(getContext(), "No Coordinates Exists", Toast.LENGTH_LONG).show();
        }
    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }
}