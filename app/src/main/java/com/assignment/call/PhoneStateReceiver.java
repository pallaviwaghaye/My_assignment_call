package com.assignment.call;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.assignment.call.SQLiteDB.DatabaseManager;
import com.assignment.call.model.CallEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

import static android.content.Context.MODE_PRIVATE;

public class PhoneStateReceiver extends BroadcastReceiver {
    private DatabaseReference database;
    String NumberNode;
    List<CallEntry> list = new ArrayList<CallEntry>();

    @Override
    public void onReceive(final Context context, Intent intent) {

        try {
            SharedPreferences pref = context.getSharedPreferences("callapppref", 0);
            SharedPreferences.Editor editor = pref.edit();


            if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
                // Toast.makeText(context, "OUTGOING Call", Toast.LENGTH_SHORT).show();
                editor.putBoolean("outgoing", true);
                editor.commit();
            } else {

                System.out.println("Receiver start");
                String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
                final String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

                if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
//                    Toast.makeText(context, "Incoming Call State", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(context, "Ringing State Number is -" + incomingNumber, Toast.LENGTH_SHORT).show();

                    editor.putBoolean("call_received", false);
                    editor.putBoolean("outgoing", false);

                    editor.commit();
                }
                if ((state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))) {
                    //  Toast.makeText(context, "Call Received State", Toast.LENGTH_SHORT).show();

                    editor.putBoolean("call_received", true);
                    editor.commit();


                }
                if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                    // Toast.makeText(context, "Call Idle State", Toast.LENGTH_SHORT).show();

                    boolean contactExists = contactExists(context, incomingNumber);

                    if (pref.getBoolean("call_received", false) == true &&
                            pref.getBoolean("outgoing", false) == false && contactExists == false) {


                        database = FirebaseDatabase.getInstance().getReference();
                        NumberNode = "phoneList";

                        //Toast.makeText(context,"broadcast revciever list",Toast.LENGTH_SHORT).show();

                        database.child(NumberNode).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                                    CallEntry call = noteDataSnapshot.getValue(CallEntry.class);
                                    list.add(call);
                                }
                                    boolean flagToCheckDBNumber = false;
                                    if (list.size() > 0 && list != null) {
                                        for (int i = 0; i < list.size(); i++) {
                                            if (list.get(i).getPhoneNumber().contains(incomingNumber)) { // used contains because, some number may come with 0, some with +91 in starting.
                                                flagToCheckDBNumber = true;
                                            } else {
                                                flagToCheckDBNumber = false;
                                            }
                                        }
                                        if (flagToCheckDBNumber == false) {
                                            new android.os.Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Intent intentone = new Intent(context.getApplicationContext(), PopupActivity.class);
                                                    intentone.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    intentone.putExtra("phoneNo", incomingNumber);
                                                    context.startActivity(intentone);
                                                }
                                            }, 2000);
                                        }

                                    } else {
                                        // first time, if list is not having data

                                        new android.os.Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                Intent intentone = new Intent(context.getApplicationContext(), PopupActivity.class);
                                                intentone.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                intentone.putExtra("phoneNo", incomingNumber);
                                                context.startActivity(intentone);
                                            }
                                        }, 2000);
                                    }



                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                //Log.e("Error", "Error");
                            }

                        });

                        // call received and disconnects

                    } else

                    {

                        // without receiving call, it disconnects
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*                    // Added for inserted calls number to DB
                    DatabaseManager databaseManager = new DatabaseManager(context);
                        List<CallEntry> list = databaseManager.getAllCallEntries();
                        boolean flagToCheckDBNumber = false; // flag to check number is in DB or not,
                        if (list.size() > 0 && list != null) {
                            for (int i = 0; i < list.size(); i++) {
                                if (list.get(i).getPhoneNumber().contains(incomingNumber)) { // used contains because, some number may come with 0, some with +91 in starting.
                                    flagToCheckDBNumber = true;
                                } else{
                                    flagToCheckDBNumber = false;
                                }
                            }

                            if(flagToCheckDBNumber==false)
                            {
                                new android.os.Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intentone = new Intent(context.getApplicationContext(), PopupActivity.class);
                                        intentone.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intentone.putExtra("phoneNo", incomingNumber);
                                        context.startActivity(intentone);
                                    }
                                }, 2000);
                            }

                        } else {
                            // first time, if list is not having data

                            new android.os.Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intentone = new Intent(context.getApplicationContext(), PopupActivity.class);
                                    intentone.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intentone.putExtra("phoneNo", incomingNumber);
                                    context.startActivity(intentone);
                                }
                            }, 2000);
                        }

                        // call received and disconnects

                    } else {

                        // without receiving call, it disconnects
                    }


                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
*/

    public boolean contactExists(Context context, String number) {
/// number is the phone number
        Uri lookupUri = Uri.withAppendedPath(
                ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(number));
        String[] mPhoneNumberProjection = {ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.NUMBER, ContactsContract.PhoneLookup.DISPLAY_NAME};
        Cursor cur = context.getContentResolver().query(lookupUri, mPhoneNumberProjection, null, null, null);
        try {
            if (cur.moveToFirst()) {
                return true;
            }
        } finally {
            if (cur != null)
                cur.close();
        }
        return false;
    }
}


