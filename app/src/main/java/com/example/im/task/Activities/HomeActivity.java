package com.example.im.task.Activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.im.task.Database.Databasehelper;
import com.example.im.task.Fragments.RideFragment;
import com.example.im.task.Model.Contact;
import com.example.im.task.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.im.task.R.id.nav_view;
import static com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, View.OnClickListener {
    Marker mCurrLocationMarker;
    private GoogleMap mMap;
    LocationManager locationManager;
    private ArrayList<LatLng> points;
    Polyline line;
    MarkerOptions markerOptions = new MarkerOptions();
    ToggleButton rideBn;
    int activated = 0;
    ImageView imageView;
    TextView names, emails;
    long INTERVAL = 1000 * 30 * 1; // 30 sec
    float SMALLEST_DISPLACEMENT = 0.1F; //100 meter
    FragmentManager manager = getFragmentManager();    //Initializing Fragment Manager.
    RideFragment Fragment = new RideFragment();        //Initializing RideFragment.
    String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.im.task.R.layout.activity_home);

        points = new ArrayList<LatLng>();

        rideBn = (ToggleButton) findViewById(R.id.rideBn);
        rideBn.setOnClickListener(this);
        //Getting Map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        // this class automatically initializes the maps system and the view.
        mapFragment.getMapAsync(this);
        //LocationManager class provides access to the system location services.
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //Checking if Permission is given in Manifest File or not.
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //IN case if Internet is Available.
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, INTERVAL, SMALLEST_DISPLACEMENT, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    //get the latitude
                    double latitude = location.getLatitude();
                    //get the longitude
                    double longitude = location.getLongitude();
                    LatLng latLng = new LatLng(latitude, longitude);
                    // Geocoder class for handling geocoding and reverse geocoding. Geocoding is the process of transforming a street address or other description of a location into a (latitude, longitude) coordinate. Reverse geocoding is the process of transforming a (latitude, longitude) coordinate into a (partial) address.
                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    try {
                        mMap.clear();
                        List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);  //Geocoding i.e., Converting Coordinates into Address.
                        str = addressList.get(0).getLocality() + ",";
                        str += addressList.get(0).getCountryName();
                        mMap.moveCamera(newLatLngZoom(latLng, 15));  //to move camera towards the user location.
                        mMap.animateCamera(newLatLngZoom(latLng, 15));
                        markerOptions.position(latLng).title(str);
                        mCurrLocationMarker = mMap.addMarker(markerOptions);

                        points.add(latLng); //Collecting Location Points.

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        }
        //In case if Gps is available.
        else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, INTERVAL, SMALLEST_DISPLACEMENT, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    Marker currentLocationMarker = null;
                    currentLocationMarker.remove();
                    double latitude = location.getLatitude();
                    //get the longitude
                    double longitude = location.getLongitude();
                    LatLng latLng = new LatLng(latitude, longitude);
                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    try {
                        mMap.clear();
                        List<android.location.Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
                        str = addressList.get(0).getLocality() + ",";
                        str += addressList.get(0).getCountryName();
                        mMap.moveCamera(newLatLngZoom(latLng, 15));
                        mMap.animateCamera(newLatLngZoom(latLng, 15));
                        markerOptions.position(latLng).title(str);
                        mCurrLocationMarker = mMap.addMarker(markerOptions);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(nav_view);
        Bundle bundle = getIntent().getExtras();
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        String name = bundle.getString("name");
        String email = bundle.getString("email");
        String userPic = bundle.getString("userPic");
        //setting data in Navigation Bar Header.
        imageView = (ImageView) header.findViewById(R.id.imageView);
        emails = (TextView) header.findViewById(R.id.emails);
        names = (TextView) header.findViewById(R.id.names);
        names.setText(name);
        emails.setText(email);
        Glide.with(getApplicationContext()).load(userPic)
                .thumbnail(0.5f)
                .crossFade()
                .transform(new CircleTransform(HomeActivity.this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    //Funtion to handle when Back Button is pressed.
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        //In case if fragment is open then remove it.
        else if (Fragment != null) {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.remove(Fragment);
            transaction.commit();
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.rideHistory: {
                // Show the history of rides
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.main, Fragment).commit();
                transaction.show(Fragment);
                break;
            }
            case R.id.home: {
                //takes to HomeActivity(Remove Fragment if it is there.).
                if (Fragment != null) {
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.remove(Fragment);
                    transaction.commit();
                    break;
                }
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Function to Draw the Polyline on the Map.
    private void redrawLine() {
        mMap.clear();  //clears all Markers and Polylines
         PolylineOptions options = new PolylineOptions().width(7).color(Color.BLUE).geodesic(true); //Defining Properties of Polyline. A geodesic is the shortest path between two points on the Earth's surface. The geodesic curve is constructed assuming the Earth is a sphere.
        if (points.size() <= 1)
            Toast.makeText(getApplicationContext(), "No Location Selected to Display!", Toast.LENGTH_SHORT).show();
        for (int i = 0; i < points.size(); i++) {
            LatLng point = points.get(i);
            options.add(point);
            markerOptions.position(point).title(str);
        }
        mMap.addMarker(markerOptions); //add Marker in current position
        line = mMap.addPolyline(options); //add Polyline
    }

    //onMapReady(GoogleMap) method is triggered when the map is ready to be used.
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
    }

    //onClick method to perform particular Actions in case a button is pressed.
    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.rideBn: {
                if (activated == 0) {
                    int s;
                    LatLng l;
                    s = points.size();
                    l = points.get(s - 1);
                    points.clear();
                    points.add(l);
                    activated = 1;
                } else {
                    mMap.clear();
                    redrawLine();
                    activated = 0;

                }
            }
        }
    }
}
