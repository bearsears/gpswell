//package com.example.ssj.gpswell;
//
//import android.Manifest;
//import android.content.Context;
//import android.content.pm.ActivityInfo;
//import android.content.pm.PackageManager;
//import android.location.Location;
//import android.location.LocationManager;
//import android.os.Bundle;
//import android.os.Looper;
//import android.view.View;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationCallback;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationResult;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Locale;
//import java.util.Objects;
//
////import android.annotation.SuppressLint;
//
////import android.support.v4.app.ActivityCompat;
////import android.support.v7.widget.RecyclerView;
//
////import android.support.annotation.NonNull;
////import android.support.v4.content.ContextCompat;
////import android.support.v7.app.AppCompatActivity;
////import android.support.v7.widget.LinearLayoutManager;
//
//
//public class MainActivityBackUp extends AppCompatActivity
//        implements ActivityCompat.OnRequestPermissionsResultCallback {
//
//    private static final int PERMISSION_REQUEST_LOCATION = 0;
//    private FusedLocationProviderClient mFusedLocationClient;
//    protected Location mLastLocation;
//    private TextView mLatitudeText;
//    private Boolean mRequestingLocationUpdates = false;
//
//    //Sets rough interval,
//    //private final LocationRequest mLocationRequest;
//    private LocationCallback mLocationCallback;
//    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
//    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
//            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
//    private final List<Welldata> welldata = getWelldata();
//    private static final double LIMIT = 0.01;
//    private RecyclerView mRecyclerView;
//    private RecyclerView.Adapter mAdapter;
//
//    // use a linear layout manager
//    private final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
//
//    private String[] wellSpecs = new String[10];
//
//
//    //@SuppressLint("SourceLockedOrientationActivity")
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        // To lock the screen in portrait only.
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//
//        mRecyclerView = findViewById(R.id.recyclerView);
//        mLatitudeText = findViewById((R.id.latitude_text));
//        // mLongitudeText = findViewById((R.id.longitude_text));
//
//        /* Sets and checks permission */
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions
//                    (this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                            PERMISSION_REQUEST_LOCATION);
//            finish();
//        }
//
//        /* Checks database, and loads it up.*/
//        //welldata = getWelldata();
//        //debug = getApplicationContext().getAssets();
//        //Toast toast = Toast.makeText(getApplicationContext(),
//        //        getApplicationContext().getDatabasePath("gpsWell2.sqlite").getPath() , Toast.LENGTH_LONG);
//        //toast.show();
//
//        /* sets up location and persistent state.*/
//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//        //getLastLocation();
//        //createLocationCallback();
//        //createLocationRequest();
//
//        // use this setting to improve performance if you know that changes
//        // in content do not change the layout size of the recyclerview
//        mRecyclerView.setHasFixedSize(true);
//
//        mRecyclerView.setLayoutManager(mLayoutManager);
//
//        // specify an adapter
//        mAdapter = new wellSpecAdapter(wellSpecs);
//        mRecyclerView.setAdapter(mAdapter);
//    }
//
//
//    private List<Welldata> getWelldata() {
//
//        DataBaseHelper db = new DataBaseHelper(getApplicationContext());
//
//        try {
//            db.createDatabase();
//        } catch (IOException ioe) {
//            throw new Error("Unable to create database");
//        }
//
//        return db.getWelldata();
//
//    }
//
//    //@SuppressLint("MissingPermission")
//    private void getLastLocation() {
//        /*
//        if (PERMISSION_REQUEST_LOCATION == 0) {
//            mLastLocation = null;
//        } else if (PERMISSION_REQUEST_LOCATION == 1) {
//        */
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        mFusedLocationClient.getLastLocation()
//                .addOnSuccessListener(this, location -> {
//                    if (location != null) {
//                        mLastLocation = location;
//                        updateLocationUI();
//
//                        wellSpecs = findIndex();
//                        //Toast toast = Toast.makeText(getApplicationContext(), welldata.get(24).getLicence(), Toast.LENGTH_LONG);
//                        //toast.show();
//
//                        // if this works i need a better way to refresh the recyclerview
//                        mRecyclerView.setAdapter(new wellSpecAdapter(wellSpecs));
//                        mRecyclerView.invalidate();
//
//
//                    } else {
//                        //Don't make the mLastLocation null!
//                        //mLastLocation = null;
//                        updateLocationUI();
//                    }
//
//                });
//    }
//
//    public void reloadGPSButton(View view) {
//        //this class is for a button, and when pressed it it will do getLastLocation()
//        //again.
//        /*
//        Intent intent = getIntent();
//        finish();
//        startActivity(intent);
//         */
//        mRequestingLocationUpdates = true;
//        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//
//
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
//                 !manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
//                ) {
//            mLastLocation = null;
//            updateLocationUI();
//        } else {
//            startLocationUpdates();
//            getLastLocation();
//
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        // I think that even on resume the location updates should be turned off.
//        // for the case of this app.
//        //if (mRequestingLocationUpdates) {
//        mRequestingLocationUpdates = true;
//        //LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        startLocationUpdates();
//        //}
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//
//        // Remove location updates to save battery
//        stopLocationUpdates();
//    }
//
//    private void createLocationCallback() {
//        mLocationCallback = new LocationCallback() {
//            @Override
//            public void onLocationResult(@NonNull LocationResult locationResult) {
//                super.onLocationResult(locationResult);
//
//            }
//        };
//        //getLastLocation();
//    }
//
//    private void createLocationRequest() {
//        mLocationRequest = new LocationRequest.create();
////        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
////
////        //Sets lowest limit interval, as this will set a interval the
////        //app will not update gps faster than this.
////        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
////
////        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//    }
//
//    @SuppressWarnings("MissingPermission")
//    private void startLocationUpdates() {
//
//        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
//                mLocationCallback, Objects.requireNonNull(Looper.myLooper()));
//
//    }
//
//    private void stopLocationUpdates() {
//        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
//                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        mRequestingLocationUpdates = false;
//
//                    }
//                });
//    }
//
//    private void updateLocationUI() {
//
//        //String mLatitudeLabel = "Lat";
//        //String mLongitudeLabel = "Long";
//
//        if (mLastLocation != null) {
//            mLatitudeText.setText(String.format(Locale.CANADA, "%.6f, %.6f",
//                    mLastLocation.getLatitude(), mLastLocation.getLongitude()));
////            mLatitudeText.setText(String.format(Locale.CANADA, "%s: %.6f",
////                    mLatitudeLabel, mLastLocation.getLatitude()));
////            mLongitudeText.setText(String.format(Locale.CANADA, "%s: %.6f",
////                    mLongitudeLabel, mLastLocation.getLongitude()));
//        } else {
//            mLatitudeText.setText(String.format(Locale.CANADA, "%s",
//                    "Permission or GPS on?"));
//           // mLongitudeText.setText("GPS On?       ");
//
//        }
//    }
//
//    private String[] findIndex() {
//        int low = 0;
//        int high = welldata.size();
//        int mid;
//        int i = -1;
//
//        double latlowBoundary = mLastLocation.getLatitude() - LIMIT;
//        double lathighBoundary = mLastLocation.getLatitude() + LIMIT;
//        double longlowBoundary = mLastLocation.getLongitude() - LIMIT;
//        double longhighBoundary = mLastLocation.getLongitude() + LIMIT;
//
//        // binary search to find the index,
//        while (low < high) {
//            mid = low + (high - low) / 2;
//
//            if (welldata.get(mid).getblat() > lathighBoundary)
//                high = mid - 1;
//            else if (welldata.get(mid).getblat() < latlowBoundary)
//                low = mid + 1;
//            else {// meaning it falls within the boundary
//                i = mid;
//                break;
//            }
//        }
//
//        while (i >= 0 && welldata.get(i).getblat() > latlowBoundary)
//            i--;
//
//        // setting up ten strings and and the related distance
//        String[] wells = new String[8];
//        int[] distance = new int[8];
//        String description;
//        int dist;
//        int bearing;
//        String tdesc;
//        int tdist;
//        Location tempL = new Location("");
//
//        for (int j = 0; j < 8 ; j++) {
//            wells[j] = "Nothing near.\n";
//            distance[j] = 0;
//        }
//
//
//        // starting from the index, works its way to find the appropriate wells
//        while (welldata.get(i).getblat() < lathighBoundary) {
//            if (welldata.get(i).getblong() > longhighBoundary ||
//                    welldata.get(i).getblong() < longlowBoundary ||
//                    welldata.get(i).getStatus().equals("Abandoned")) {
//                i++;
//                continue;
//            }
//
//            tempL.setLatitude(welldata.get(i).getblat());
//            tempL.setLongitude(welldata.get(i).getblong());
//
//            dist = (int) mLastLocation.distanceTo(tempL);
//            bearing = (int) mLastLocation.bearingTo(tempL);
//
//            description = dist + "m, " + bearing + "° : " + welldata.get(i).getLicence() +
//                    "  "  + welldata.get(i).getStatus() + "\n"
//                    + welldata.get(i).getSurface() + "\n" +
//                    welldata.get(i).getUWI();
//
//            for (int j = 0; j < 8 ; j++) {
//                if (distance[j] == 0) {
//                    distance[j] = dist;
//                    wells[j] = description;
//                    break;
//                }
//
//                if (dist < distance[j]) {
//                    tdesc = wells[j];
//                    tdist = distance[j];
//                    wells[j] = description;
//                    distance[j] = dist;
//                    description = tdesc;
//                    dist = tdist;
//                }
//            }
//
//            i++;
//
//        }
//
//        return wells;
//
//    }
//
//
//}
//
//
