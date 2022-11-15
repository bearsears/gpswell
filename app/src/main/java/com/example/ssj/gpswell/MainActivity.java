package com.example.ssj.gpswell;

import android.Manifest;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int PERMISSION_REQUEST_LOCATION = 0;
    private FusedLocationProviderClient mFusedLocationClient;
    protected Location mLastLocation;
    private TextView mLatitudeText;

    //Sets rough interval,
    private List<Welldata> welldata;
    private static final double LIMIT = 0.01;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private Toast toast;
    private String fileName = "record.txt";
    private String gibberish = "blahblahe";

    // use a linear layout manager
    private final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);

    private String[] wellSpecs = new String[10];
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // To lock the screen in portrait only.
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mRecyclerView = findViewById(R.id.recyclerView);
        mLatitudeText = findViewById((R.id.latitude_text));
        // mLongitudeText = findViewById((R.id.longitude_text));

        /* Sets and checks permission */
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions
                    (this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            PERMISSION_REQUEST_LOCATION);
            finish();
        }



        /* Checks database, and loads it up.*/
        welldata = getWelldata();
        //Toast toast = Toast.makeText(getApplicationContext(), welldata.get(0).getLicence(), Toast.LENGTH_LONG);
        //toast.show();
        //debug = getApplicationContext().getAssets();
        //Toast toast = Toast.makeText(getApplicationContext(),
        //        getApplicationContext().getDatabasePath("gpsWell2.sqlite").getPath() , Toast.LENGTH_LONG);
        //toast.show();

        /* sets up location and persistent state.*/
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the recyclerview
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter
        mAdapter = new wellSpecAdapter(wellSpecs);
        mRecyclerView.setAdapter(mAdapter);



    }

    private List<Welldata> getWelldata() {
        DataBaseHelper db = new DataBaseHelper(getApplicationContext());
        try {
            db.createDatabase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
        return db.getWelldata();
    }

    public void reloadGPSButton(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions
                    (this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            PERMISSION_REQUEST_LOCATION);
            return;
        }

        mLatitudeText.setText(String.format(Locale.CANADA, "%s", "Searching"));

        //Below will create a task that will complete the task, and depending on the success bring
        //the result.
        mFusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            mLastLocation = location;
                            updateLocationUI();
                            wellSpecs = findIndex();
                            // if this works i need a better way to refresh the recyclerview
                            mRecyclerView.setAdapter(new wellSpecAdapter(wellSpecs));
                            mRecyclerView.invalidate();
                            // Logic to handle location object
                        } else {
                            mLastLocation = null;
                            updateLocationUI();
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    private void updateLocationUI() {
        if (mLastLocation != null) {
            mLatitudeText.setText(String.format(Locale.CANADA, "%.6f, %.6f",
                    mLastLocation.getLatitude(), mLastLocation.getLongitude()));
        } else {
            mLatitudeText.setText(String.format(Locale.CANADA, "%s",
                    "Permission or GPS on?"));
        }
    }

    private String[] findIndex() {
        int low = 0;
        int high = welldata.size();
        int mid;
        int i = -1;

        double latlowBoundary = mLastLocation.getLatitude() - LIMIT;
        double lathighBoundary = mLastLocation.getLatitude() + LIMIT;
        double longlowBoundary = mLastLocation.getLongitude() - LIMIT;
        double longhighBoundary = mLastLocation.getLongitude() + LIMIT;

        //toast = Toast.makeText(getApplicationContext(), String.valueOf(high), Toast.LENGTH_LONG);
        //toast.show();
        // binary search to find the index,
        //i don't know why changing low < high to high > low fixes the issue. buggy or something
        //i don't understand.
        while (high > low) {
            mid = low + (high - low) / 2;
            if (welldata.get(mid).getblat() > lathighBoundary)
                high = mid - 1;
            else if (welldata.get(mid).getblat() < latlowBoundary)
                low = mid + 1;
            else {// meaning it falls within the boundary
                i = mid;
                break;
            }
        }

        while (i > 0 && welldata.get(i).getblat() > latlowBoundary)
            i--;

        // setting up ten strings and and the related distance
        String[] wells = new String[8];
        int[] distance = new int[8];
        String description;
        int dist;
        int bearing;
        String tdesc;
        int tdist;
        Location tempL = new Location("");

        for (int j = 0; j < 8; j++) {
            wells[j] = "Nothing near.\n";
            distance[j] = 0;
        }


        // starting from the index, works its way to find the appropriate wells
        while (welldata.get(i).getblat() < lathighBoundary) {
            if (welldata.get(i).getblong() > longhighBoundary ||
                    welldata.get(i).getblong() < longlowBoundary ||
                    welldata.get(i).getStatus().equals("Abandoned")) {
                i++;
                continue;
            }

            tempL.setLatitude(welldata.get(i).getblat());
            tempL.setLongitude(welldata.get(i).getblong());

            dist = (int) mLastLocation.distanceTo(tempL);
            bearing = (int) mLastLocation.bearingTo(tempL);

            description = dist + "m, " + bearing + "° : " + welldata.get(i).getLicence() +
                    "  " + welldata.get(i).getStatus() + "\n"
                    + welldata.get(i).getSurface() + "\n" +
                    welldata.get(i).getUWI();

            for (int j = 0; j < 8; j++) {
                if (distance[j] == 0) {
                    distance[j] = dist;
                    wells[j] = description;
                    break;
                }

                if (dist < distance[j]) {
                    tdesc = wells[j];
                    tdist = distance[j];
                    wells[j] = description;
                    distance[j] = dist;
                    description = tdesc;
                    dist = tdist;
                }
            }
            i++;
        }
        return wells;
    }
}