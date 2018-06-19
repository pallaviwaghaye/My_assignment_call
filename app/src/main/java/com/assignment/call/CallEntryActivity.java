package com.assignment.call;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.assignment.call.R;
import com.assignment.call.SQLiteDB.DatabaseManager;
import com.assignment.call.Util.NetworkUtil;
import com.assignment.call.model.CallEntry;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CallEntryActivity extends AppCompatActivity {

    private DatabaseReference database;
    private String numberNode;

    private EditText editTextFirstName;
    private EditText editTextEmailId;
    private EditText editTextPhoneNumber;
    private Spinner spinnerQuery;
    private Button buttonSubmit;
    String selectedQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_entry);

        String phoneNumber = getIntent().getStringExtra("phoneNo");

        editTextFirstName = (EditText) findViewById(R.id.editTextFirstName);
        editTextEmailId = (EditText) findViewById(R.id.editTextEmailId);
        editTextPhoneNumber = (EditText) findViewById(R.id.editTextPhoneNumber);
        spinnerQuery = (Spinner) findViewById(R.id.spinnerQuery);
        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);

        if (phoneNumber != null) {
            editTextPhoneNumber.setText(phoneNumber);
        }

        // set spinner data
        String[] queryList = getResources().getStringArray(R.array.query);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, queryList);
        spinnerQuery.setAdapter(adapter);

        spinnerQuery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedQuery = (String) adapterView.getItemAtPosition(i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (editTextFirstName.getText().toString().length() > 0) {
                    if (selectedQuery.equalsIgnoreCase("Select")) {
                        Toast.makeText(CallEntryActivity.this, "Please select query", Toast.LENGTH_SHORT).show();
                    } else {
                        if (editTextEmailId.getText().toString().length() > 0) {
                            if (isValidEmailAddress(editTextEmailId.getText().toString().trim())) {
                                insertToDB(editTextEmailId.getText().toString());
                            } else {
                                Toast.makeText(CallEntryActivity.this, "Please enter valid email id", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            insertToDB("N/A");
                        }
                    }
                } else {
                    Toast.makeText(CallEntryActivity.this, "Please enter name", Toast.LENGTH_SHORT).show();
                }
                insertToDB(editTextEmailId.getText().toString());
            }
        });
        if (NetworkUtil.isConnectionAvailable(CallEntryActivity.this)) {
            //Toast.makeText(CallEntryActivity.this, "call entry added locally", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(CallEntryActivity.this, "You are offline,Call entry added locally", Toast.LENGTH_LONG).show();
        }
    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    public void insertToDB(String emailId) { // as email id is not compulsory, we are passing if data, else N/A
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd MMM, yyyy hh:mm a");
        String formattedDate = df.format(c);
        CallEntry callEntry = new CallEntry();
        callEntry.setName(editTextFirstName.getText().toString());
        callEntry.setPhoneNumber(editTextPhoneNumber.getText().toString());
        callEntry.setEmailId(emailId);
        callEntry.setQuery(selectedQuery);
        callEntry.setTimeStamp(formattedDate);
        //DatabaseManager databaseManager = new DatabaseManager(CallEntryActivity.this);
        // databaseManager.insertCallEntryDetails(callEntry);


        database = FirebaseDatabase.getInstance().getReference();
        numberNode = "phoneList";
        database.child(numberNode).child(callEntry.getPhoneNumber()).setValue(callEntry);
        //database.child("numberList").setValue(callEntry);

        Intent intent = new Intent(CallEntryActivity.this, ListOfCallsDetailsActivity.class);
        startActivity(intent);
        finish();
    }
}
