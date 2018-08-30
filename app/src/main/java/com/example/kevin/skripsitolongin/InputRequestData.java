package com.example.kevin.skripsitolongin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.flags.impl.DataUtils;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InputRequestData extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText editTextRequestName,editTextRequestPhone;
    Button buttonAddRequest,buttonLogout,buttonProfile;
    TextView textViewEmails,textViewRating,textViewMap;
    Spinner s3,s4;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseRequest,databaseRating,databaseRequest2;
    private static final String TAG = "MainActivity";

    public static final String USER_ID = "com.example.kevin.tolonginskripsi.userid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_request_data);
        s3 =(Spinner) findViewById(R.id.spinnerahli3);
        s4 =(Spinner) findViewById(R.id.spinnerahli4);
        s3.setOnItemSelectedListener(this);
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(firebaseAuth.getCurrentUser()== null){
            finish();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }
        databaseRating = FirebaseDatabase.getInstance().getReference("rating").child(user.getUid());
        databaseRequest = FirebaseDatabase.getInstance().getReference("request");
        databaseRequest2 = FirebaseDatabase.getInstance().getReference("myRequest");
        editTextRequestName = (EditText) findViewById(R.id.editTextRequestName);
        editTextRequestPhone = (EditText) findViewById(R.id.editTextRequestPhone);
        textViewMap= (TextView) findViewById(R.id.textViewMap);
       // textViewEmail = (TextView) findViewById(R.id.textViewEmail);
        textViewEmails = (TextView) findViewById(R.id.textViewEmails);
       // textViewEmail.setText(user.getEmail());
        textViewEmails.setText(user.getEmail());
        textViewRating= (TextView) findViewById(R.id.textViewRating);
        buttonLogout =(Button) findViewById(R.id.buttonLogout);
        buttonProfile =(Button) findViewById(R.id.buttonProfile);
        buttonAddRequest = (Button) findViewById(R.id.buttonAddRequest);
        Intent intent = getIntent();
        textViewMap.setText(intent.getStringExtra(MapActivity.MAPS));
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

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(InputRequestData.this,MainActivity.class));
            }
        });

        buttonProfile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InputRequestData.this,ShowProfileActivity.class);
                FirebaseUser user = firebaseAuth.getCurrentUser();
                intent.putExtra(USER_ID, user.getUid());
                startActivity(intent);
            }

        });

        buttonAddRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRequest();
            }
        });
    }

    //ini spiner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String sp1 = String.valueOf(s3.getSelectedItem());
        if (sp1.contentEquals("-Select Category-")) {
            List<String> list = new ArrayList<String>();
            list.add("");

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter.notifyDataSetChanged();
            s4.setAdapter(dataAdapter);
        }
        if (sp1.contentEquals("Electronic")) {
            List<String> list = new ArrayList<String>();
            list.add("Handphone");
            list.add("Laptop/Computer");
            list.add("Televisi");
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter.notifyDataSetChanged();
            s4.setAdapter(dataAdapter);
        }
        if (sp1.contentEquals("Mechanic")) {
            List<String> list = new ArrayList<String>();
            list.add("Ahli Kunci");
            list.add("Service AC");
            list.add("Service Mesin Air");
            list.add("Service Mesin Cuci");
            list.add("Service Mobil");
            list.add("Service Motor");

            ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter2.notifyDataSetChanged();
            s4.setAdapter(dataAdapter2);
        }
        if (sp1.contentEquals("Daily Skill")) {
            List<String> list = new ArrayList<String>();
            list.add("Assistant");
            list.add("Cleaning Service");
            list.add("Cooking");
            list.add("Gardener");
            list.add("Massage");
            list.add("Make up artist");
            list.add("Private Teacher");
            ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter3.notifyDataSetChanged();
            s4.setAdapter(dataAdapter3);

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



    private void addRequest() {
        String name = editTextRequestName.getText().toString().trim();

        String phone = editTextRequestPhone.getText().toString().trim();
        String address = textViewMap.getText().toString().trim();
        final String spinners = s4.getSelectedItem().toString();

       //validasi apakah ada yg kosong
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();

        }

        else if(TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Please enter a phone", Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(address))
        {
            Toast.makeText(this, "Please enter a address", Toast.LENGTH_LONG).show();
        }
        else  if(spinners.equals("")){
            Toast.makeText(InputRequestData.this, "Category cannot be Empty", Toast.LENGTH_SHORT).show();
            return;
        }
        else{


         //jika sudah tidak ada yang kosong maka:
            String id = databaseRequest.push().getKey();
            FirebaseUser user = firebaseAuth.getCurrentUser();
            String email = user.getEmail();
            String userID = user.getUid();
            String skill = s4.getSelectedItem().toString();
            //memasukan request yang baru
            Request request = new Request(id, name,skill,phone,address,email,userID);
            //menyimpan request
            databaseRequest.child(skill).child(id).setValue(request);
            databaseRequest2.child(userID).child(id).setValue(request);
            Toast.makeText(this, "Request added", Toast.LENGTH_LONG).show();
            startActivity(new Intent(InputRequestData.this,ProfileActivity.class));
        }

    }

}
