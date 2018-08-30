package com.example.kevin.skripsitolongin;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class RequestActivity extends AppCompatActivity {

    DatabaseReference databaseTransaction,databaseRequest,databaseRequest2,databaseUser,databaseRating;
    Button buttonAcceptRequest,buttonLogout,buttonProfile;
    TextView textViewEmails,textViewRequestEmail,textViewRequestName,textViewRequestPhone,
            textViewRequestKeahlian,textViewRequestAddress,textViewRequestID,textViewRating;
    FirebaseAuth firebaseAuth;
    //mendapatkan nilai dari halaman sebelumnya
    public static final String REQUEST_ID = "com.example.kevin.tolonginskripsi.requestid";
    public static final String USER_ID = "com.example.kevin.tolonginskripsi.userid";
    public static final String CHECK3 = "3";
    public static final String LOCATION = "location";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        databaseTransaction= FirebaseDatabase.getInstance().getReference("transaction");
        firebaseAuth= FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        if(firebaseAuth.getCurrentUser()== null){
            finish();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }

        textViewRequestAddress = (TextView) findViewById(R.id.textViewUserID);
        textViewRequestEmail = (TextView) findViewById(R.id.textViewEmail);
        textViewRequestID = (TextView) findViewById(R.id.textViewUserID);
        textViewRequestKeahlian = (TextView) findViewById(R.id.textViewRequestKeahlian);
        textViewRequestName = (TextView) findViewById(R.id.textViewRequestNames);
        textViewRequestPhone = (TextView) findViewById(R.id.textViewRequestPhone);
        buttonAcceptRequest = (Button) findViewById(R.id.buttonAcceptRequest);
        buttonLogout =(Button) findViewById(R.id.buttonLogout);
        buttonProfile =(Button) findViewById(R.id.buttonProfile);
        textViewEmails = (TextView) findViewById(R.id.textViewEmails);
        textViewEmails.setText(user.getEmail());
        textViewRating= (TextView) findViewById(R.id.textViewRating);
        //mengambil nilai request id padahalaman sebelumnya
        Intent intent = getIntent();
        String key = getIntent().getExtras().get(REQUEST_ID).toString();
        textViewRequestID.setText(intent.getStringExtra(ProfileActivity.REQUEST_ID));
        textViewRequestName.setText(intent.getStringExtra(ProfileActivity.REQUEST_NAME));
        textViewRequestAddress.setText(intent.getStringExtra(ProfileActivity.REQUEST_ADDRESS));
        final String keyLoc  = intent.getStringExtra(ProfileActivity.REQUEST_ADDRESS);
        textViewRequestEmail.setText(intent.getStringExtra(ProfileActivity.REQUEST_EMAIL));
        textViewRequestKeahlian.setText(intent.getStringExtra(ProfileActivity.REQUEST_KEAHLIAN));
        textViewRequestPhone.setText(intent.getStringExtra(ProfileActivity.REQUEST_PHONE));
        Button Mapbtn = (Button) findViewById(R.id.Mapbtn);
        Mapbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri mapUri  = Uri.parse(("geo:0,0?q="+Uri.encode(keyLoc)));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW,mapUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
        String skill = intent.getStringExtra(ProfileActivity.REQUEST_KEAHLIAN);
        String idrequest =  intent.getStringExtra(ProfileActivity.USER_ID);
        databaseRating = FirebaseDatabase.getInstance().getReference("rating").child(user.getUid());
        //mendapatkan nilai anak dari cabang request-requestid
       databaseRequest = FirebaseDatabase.getInstance().getReference().child("request").child(skill).child(key);
       databaseRequest2 = FirebaseDatabase.getInstance().getReference().child("myRequest").child(idrequest).child(key);
        //memasukan nilai request dr halaman sblmnya
        databaseUser= FirebaseDatabase.getInstance().getReference();

        databaseRating.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                float sum =0 ;
                float avg = 0;
                int i = 0;
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    i++;
                    Map<String,Object> map = (Map<String,Object>)ds.getValue();
                    Object rating = map.get("rating");
                    float pValue = Float.parseFloat(String.valueOf(rating));
                    sum += pValue;
                    avg  = sum/i;

                    double  x  = Math.floor(avg*10)/10;
                    textViewRating.setText(" Rating: "+String.valueOf(x)+" ");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final  String rat = textViewRating.getText().toString().trim();
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(RequestActivity.this,MainActivity.class));
            }
        });

        buttonProfile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RequestActivity.this,ShowProfileActivity.class);
                FirebaseUser user = firebaseAuth.getCurrentUser();
                intent.putExtra(USER_ID, user.getUid());
                startActivity(intent);
            }

        });

        buttonAcceptRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDialog();

            }
        });

    }
    public void showDialog(){


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


                    acceptRequest();
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
    public void acceptRequest(){

        FirebaseUser user = firebaseAuth.getCurrentUser();
        String reqID = textViewRequestID.getText().toString().trim();
        String name = textViewRequestName.getText().toString().trim();
        String skill = textViewRequestKeahlian.getText().toString().trim();
        String phone = textViewRequestPhone.getText().toString().trim();
        String address = textViewRequestAddress.getText().toString().trim();
        String emailOrderBy = textViewRequestEmail.getText().toString().trim();
        String emailAcceptBy = textViewEmails.getText().toString().trim();
        String id = databaseTransaction.push().getKey();
        String userID_Req= getIntent().getExtras().get(USER_ID).toString();
        String status = "On Progress";
        String userID_GiveHelp = user.getUid();
        String chatRoomID= databaseTransaction.push().getKey();
        float rating_GiveHelp = 0;







        //menambahkan transaction baru
        Transaction transaction = new Transaction(userID_Req,userID_GiveHelp,reqID,name,skill, phone,address,
                emailOrderBy,emailAcceptBy,id,status,chatRoomID,rating_GiveHelp);
        //menyimpan nilai transaction
        databaseTransaction.child(user.getUid()).child("GiveHelp").child(id).setValue(transaction);
        databaseTransaction.child(userID_Req).child("AskHelp").child(id).setValue(transaction);
        //menghapus request  karna sudah dipindahkan ke transaction
        databaseRequest.removeValue();
        databaseRequest2.removeValue();
        //Message message = new Message()



        finish();
        Toast.makeText(getApplicationContext(), "Request Accepted", Toast.LENGTH_SHORT).show();
    }
}