package com.example.kevin.skripsitolongin;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CancelRequestActivity extends AppCompatActivity {

    DatabaseReference ref,ref2;
    Button buttonCancelRequest,buttonLogout,buttonProfile;
    TextView textViewEmails,textViewRequestEmail,textViewRequestName,textViewRequestPhone,
            textViewRequestKeahlian,textViewRequestAddress,textViewRequestID;
    FirebaseAuth firebaseAuth;
    //mendapatkan nilai dari halaman sblmnya dengan kata kunci
    public static final String REQUEST_ID = "com.example.kevin.tolonginskripsi.requestid";
    public static final String USER_ID = "com.example.kevin.tolonginskripsi.userid";
    public static final String CHECK2 = "2";
    public static final String LOCATION = "location";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_request);
        firebaseAuth= FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        textViewRequestAddress = (TextView) findViewById(R.id.textViewUserID);
        textViewRequestEmail = (TextView) findViewById(R.id.textViewEmail);
        textViewRequestID = (TextView) findViewById(R.id.textViewUserID);
        textViewRequestKeahlian = (TextView) findViewById(R.id.textViewRequestKeahlian);
        textViewRequestName = (TextView) findViewById(R.id.textViewRequestNames);
        textViewRequestPhone = (TextView) findViewById(R.id.textViewRequestPhone);
        textViewEmails = (TextView) findViewById(R.id.textViewEmails);
        textViewEmails.setText(user.getEmail());
        buttonCancelRequest = (Button) findViewById(R.id.buttonCancelRequest);
        buttonLogout =(Button) findViewById(R.id.buttonLogout);
        buttonProfile =(Button) findViewById(R.id.buttonProfile);

        //mengecek jika belum ada user yang login maka akan pindah ke halaman main
        firebaseAuth= FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()== null){
            finish();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }
        //mengambil nilai request id padahalaman sebelumnya
        Intent intent = getIntent();
        final String key = getIntent().getExtras().get(REQUEST_ID).toString();
        String skill = intent.getStringExtra(ProfileActivity.REQUEST_KEAHLIAN);
        ref = FirebaseDatabase.getInstance().getReference().child("request").child(skill).child(key);
        ref2 = FirebaseDatabase.getInstance().getReference().child("myRequest").child(user.getUid()).child(key);

        //memasukan nilai request dr halaman sblmnya
        textViewRequestID.setText(intent.getStringExtra(ProfileActivity.REQUEST_ID));
        textViewRequestName.setText(": "+intent.getStringExtra(ProfileActivity.REQUEST_NAME));
        textViewRequestAddress.setText(": "+intent.getStringExtra(ProfileActivity.REQUEST_ADDRESS));
        textViewRequestEmail.setText(": "+intent.getStringExtra(ProfileActivity.REQUEST_EMAIL));
        textViewRequestKeahlian.setText(": "+intent.getStringExtra(ProfileActivity.REQUEST_KEAHLIAN));
        textViewRequestPhone.setText(": "+intent.getStringExtra(ProfileActivity.REQUEST_PHONE));
        //final String keyLoc  = intent.getStringExtra(ProfileActivity.REQUEST_ADDRESS);


        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(CancelRequestActivity.this,MainActivity.class));
            }
        });

        buttonProfile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CancelRequestActivity.this,ShowProfileActivity.class);
                FirebaseUser user = firebaseAuth.getCurrentUser();
                intent.putExtra(USER_ID, user.getUid());
                startActivity(intent);
            }

        });


        buttonCancelRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             showDialog();
            }
        });
    }
    public void cancelRequest(){
        //menghapus nilai dari ref

        ref.removeValue();
        ref2.removeValue();
        finish();
        Toast.makeText(getApplicationContext(), "Request deleted", Toast.LENGTH_SHORT).show();
    }
    public void showDialog() {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.agreement_status_dialog, null);
        dialogBuilder.setView(dialogView);
        final Button buttonYes = (Button) dialogView.findViewById(R.id.buttonYes);
        final Button buttonNo = (Button) dialogView.findViewById(R.id.buttonNo);

        final AlertDialog a = dialogBuilder.create();


        a.show();


        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                cancelRequest();
                a.dismiss();


            }
        });
        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                finish();
                a.dismiss();


            }
        });

    }

    }