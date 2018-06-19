package com.assignment.call;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.assignment.call.R;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

import static android.Manifest.permission.READ_PHONE_STATE;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        askPermission();

    }


    private void askPermission() {
        if (!checkPermission()) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, READ_PHONE_STATE)) {
                new TedPermission(getApplicationContext())
                        .setPermissionListener(permissionlistener)
                        .setRationaleConfirmText("ALLOW")
                        .setRationaleMessage("This Requires Permission")
                        .setPermissions(Manifest.permission.READ_PHONE_STATE)
                        .check();
            } else {
                new TedPermission(getApplicationContext())
                        .setPermissionListener(permissionlistener)
                        .setDeniedCloseButtonText("Cancel")
                        .setDeniedMessage("If you reject permission,you can not use this service \n Please turn on permissions from Settings")
                        .setGotoSettingButtonText("Settings")
                        .setPermissions(Manifest.permission.READ_PHONE_STATE)
                        .check();
            }
        } else {


        }


    }

    private boolean checkPermission() {
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_PHONE_STATE);
        return result1 == PackageManager.PERMISSION_GRANTED;
    }

    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {

        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {

        }
    };

}