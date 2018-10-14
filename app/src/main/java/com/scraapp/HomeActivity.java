package com.scraapp;

import android.location.Address;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    EditText mPickupLocation;
    MapView mMapView;
    Button mConfirmPickup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mPickupLocation = findViewById(R.id.pickup_location);
        mMapView = findViewById(R.id.mapView);
        mConfirmPickup = findViewById(R.id.confirm_pickup);

        setupListener();

    }

    private void setupListener() {

        mPickupLocation.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    performSearch();

                    return true;
                }

                return false;
            }
        });
    }


    private void performSearch() {

        List<Address> addresses = geoCoder.getFromLocationName(mPickupLocation.getText().toString(),5);

        if(addresses.size() > 0) {
            p = new GeoPoint( (int) (addresses.get(0).getLatitude() * 1E6),
                    (int) (addresses.get(0).getLongitude() * 1E6));

            controller.animateTo(p);
            controller.setZoom(12);

            MapOverlay mapOverlay = new MapOverlay();
            List<Overlay> listOfOverlays = map.getOverlays();
            listOfOverlays.clear();
            listOfOverlays.add(mapOverlay);

            map.invalidate();
            txtsearch.setText("");
        }
        else {
            AlertDialog.Builder adb = new AlertDialog.Builder(HomeActivity.this);
            adb.setTitle("Google Map");
            adb.setMessage("Please Provide the Proper Place");
            adb.setPositiveButton("Close",null);
            adb.show();
        }
    }
}
