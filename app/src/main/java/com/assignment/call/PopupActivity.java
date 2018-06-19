package com.assignment.call;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.assignment.call.R;

import org.w3c.dom.Text;

public class PopupActivity extends Activity {

    private RelativeLayout relativeLayoutYes;
    private RelativeLayout relativeLayoutNo;
    private String phoneNumber;
    private TextView PopupPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);
         phoneNumber = getIntent().getStringExtra("phoneNo");

        relativeLayoutYes = (RelativeLayout) findViewById(R.id.relativeLayoutYes);
        relativeLayoutNo = (RelativeLayout) findViewById(R.id.relativeLayoutNo);
        PopupPhoneNumber=(TextView)findViewById(R.id.PopupPhoneNumber);
        PopupPhoneNumber.setText(phoneNumber);

        relativeLayoutYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(PopupActivity.this, CallEntryActivity.class);
                intent.putExtra("phoneNo",phoneNumber);
                startActivity(intent);
                finish();

            }
        });
        relativeLayoutNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupActivity.this.finish();
            }
        });
    }
}
