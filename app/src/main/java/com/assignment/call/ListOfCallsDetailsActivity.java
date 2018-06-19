package com.assignment.call;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.assignment.call.SQLiteDB.DatabaseManager;
import com.assignment.call.adapter.DetailsAdapter;
import com.assignment.call.model.CallEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ListOfCallsDetailsActivity extends AppCompatActivity {
    private DatabaseReference database;
    String NumberNode;

    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManger;
    private TextView textViewNoData;

    List<CallEntry> list = new ArrayList<CallEntry>();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_calls_details);
        askPermission();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
       // DatabaseReference phoneRef = FirebaseDatabase.getInstance().getReference("phoneList");
        //phoneRef.keepSynced(true);

        database = FirebaseDatabase.getInstance().getReference();
        //final List<CallEntry> list = new ArrayList<CallEntry>();
        NumberNode = "phoneList";

        //phoneRef.keepSynced(false);
        showList();




        //   DatabaseManager databaseManager = new DatabaseManager(ListOfCallsDetailsActivity.this);
        // List<CallEntry> list = databaseManager.getAllCallEntries();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        textViewNoData = (TextView) findViewById(R.id.textViewNoData);
        recyclerView.setHasFixedSize(true);
        mLayoutManger = new LinearLayoutManager(ListOfCallsDetailsActivity.this);
        recyclerView.setLayoutManager(mLayoutManger);
    }


    private void showList()
    {
        progressDialog = new ProgressDialog(ListOfCallsDetailsActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();



        database.child(NumberNode).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    CallEntry call = noteDataSnapshot.getValue(CallEntry.class);
                    list.add(call);
                }

                progressDialog.cancel();



                if (list != null && list.size() > 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setAdapter(new DetailsAdapter(ListOfCallsDetailsActivity.this, list, recyclerView, textViewNoData));
                    textViewNoData.setVisibility(View.GONE);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    textViewNoData.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error", "Failed to read data");
                progressDialog.cancel();
            }

        });

    }

/*
        if (list != null && list.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setAdapter(new DetailsAdapter(ListOfCallsDetailsActivity.this, list, recyclerView, textViewNoData));
            textViewNoData.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            textViewNoData.setVisibility(View.VISIBLE);
        }
        */

    private void askPermission() {
        if (!checkPermission()) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, READ_PHONE_STATE) && ActivityCompat.shouldShowRequestPermissionRationale(this, READ_CONTACTS)) {
                new TedPermission(getApplicationContext())
                        .setPermissionListener(permissionlistener)
                        .setRationaleConfirmText("ALLOW")
                        .setRationaleMessage("This Requires Permission")
                        .setPermissions(Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CONTACTS)//permission to read_contacts
                        .check();
            } else {
                new TedPermission(getApplicationContext())
                        .setPermissionListener(permissionlistener)
                        .setDeniedCloseButtonText("Cancel")
                        .setDeniedMessage("If you reject permission,you can not use this service \n Please turn on permissions from Settings")
                        .setGotoSettingButtonText("Settings")
                        .setPermissions(Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CONTACTS)//permission to read_contacts
                        .check();
            }
        } else {


        }


    }

    private boolean checkPermission() {
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_PHONE_STATE);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_CONTACTS);//permission to read_contacts

        return result1 == PackageManager.PERMISSION_GRANTED &&
                result2 == PackageManager.PERMISSION_GRANTED;//permission to read_contacts
    }

    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {

        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {

        }
    };


    @Override
    public void onBackPressed() {
        finish();
    }
}