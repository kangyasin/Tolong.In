package com.example.kevin.skripsitolongin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ShowProfileActivity extends AppCompatActivity {

    TextView textViewUserID, textViewEmails, textViewEmail, textViewRating, textViewStatus, textViewSkill;
    Button buttonEdit, buttonProfile, buttonLogout,buttonCheckMyListRequest;
    Spinner s1, s2;
    EditText editTextFullname, editTextPhone;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase getDatabase;
    DatabaseReference mRef, databaseRating, databaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile);
        s1 = (Spinner) findViewById(R.id.spinnerahli1);
        s2 = (Spinner) findViewById(R.id.spinnerahli2);

        //mendapatkan turunan
        getDatabase = FirebaseDatabase.getInstance();

        //mendapatkan nilai user yg login
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        databaseRating = FirebaseDatabase.getInstance().getReference("rating").child(user.getUid());
        editTextFullname = (EditText) findViewById(R.id.editTextFullname);
        editTextPhone = (EditText) findViewById(R.id.editTextPhone);
        textViewUserID = (TextView) findViewById(R.id.textViewUserID);
        textViewEmail = (TextView) findViewById(R.id.textViewEmail);
        textViewSkill = (TextView) findViewById(R.id.textViewSkill);
        textViewEmail.setText(user.getEmail());
        textViewEmails = (TextView) findViewById(R.id.textViewEmails);
        textViewEmails.setText(user.getEmail());
        textViewRating = (TextView) findViewById(R.id.textViewRating);
        textViewStatus = (TextView) findViewById(R.id.textViewUserStatus);
        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        buttonEdit = (Button) findViewById(R.id.buttonEdit);
        buttonProfile = (Button) findViewById(R.id.buttonProfile);
        buttonCheckMyListRequest= (Button) findViewById(R.id.buttonCheckMyListRequest);
        databaseRating.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                float sum = 0;
                float avg = 0;
                int i = 0;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    i++;
                    Map<String, Object> map = (Map<String, Object>) ds.getValue();
                    Object rating = map.get("rating");
                    float pValue = Float.parseFloat(String.valueOf(rating));
                    sum += pValue;
                    avg = sum / i;

                    double x = Math.floor(avg * 10) / 10;
                    textViewRating.setText(" Rating: " + String.valueOf(x) + " ");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    protected void onStart() {
        super.onStart();
        //mendapatkan nilai user yg login
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser usert = firebaseAuth.getCurrentUser();
        //mendapatkan nilai anak dari cabang user->userid
        String keyss = usert.getUid();
        databaseUser = FirebaseDatabase.getInstance().getReference("user").child(keyss);
        databaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                textViewStatus.setText(": "+user.getUserStatus());
                textViewUserID.setText(": "+user.getUserID());
                textViewEmail.setText(": "+user.getEmail());
                editTextFullname.setText(user.getFullName());
                editTextPhone.setText(user.getPhone());
                textViewSkill.setText(": "+user.getSkill());

                String key = user.getSkill();
                if (key.equals("Service Motor") || key.equals("Service Mobil") || key.equals("Service AC") || key.equals("Mesin Air")
                        || key.equals("Mesin Cuci") || key.equals("Ahli Kunci")) {
                    String keys = "Mechanic";

                    updateSkill(keys);
                } else if (key.equals("Handphone") || key.equals("Laptop/Computer") || key.equals("Televisi")) {
                    String keys = "Electronic";

                    updateSkill(keys);
                } else if (key.equals("Assistant") || key.equals("Cleaning Service") || key.equals("Cooking") || key.equals("Gardener")
                        || key.equals("Massage") || key.equals("Make up artist") || key.equals("Private Teacher")) {
                    String keys = "Daily Skill";

                    updateSkill(keys);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowProfileActivity.this, ShowProfileActivity.class);
                startActivity(intent);
            }

        });
        buttonCheckMyListRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowProfileActivity.this, CheckMyRequestActivity.class);
                startActivity(intent);
            }

        });
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(ShowProfileActivity.this, MainActivity.class));
            }
        });

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser user = firebaseAuth.getCurrentUser();
                //inisialisasi
                String phone = editTextPhone.getText().toString().trim();
                String fullName = editTextFullname.getText().toString().trim();
                String users = user.getUid();
                String skill = s2.getSelectedItem().toString();
                //menyimpan nilai phone yang terbaru pada turunan phone
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("user").child(users);
                mDatabase.child("phone").setValue(phone);
                //menyimpan nilai fullname yang terbaru pada turunan fullname
                DatabaseReference mDatabase2 = FirebaseDatabase.getInstance().getReference("user").child(users);
                mDatabase2.child("fullName").setValue(fullName);
                DatabaseReference mDatabase3 = FirebaseDatabase.getInstance().getReference("user").child(users);
                mDatabase3.child("skill").setValue(skill);
                Toast.makeText(getApplicationContext(), "Success Update Profile", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(ShowProfileActivity.this, ProfileActivity.class));

            }
        });




    }


    private void updateSkill(String keys){
        String sp1 = keys;
        if (sp1.contentEquals("Electronic")) {
            List<String> list = new ArrayList<String>();
            list.add("Handphone");
            list.add("Laptop/Computer");
            list.add("Televisi");
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter.notifyDataSetChanged();
            s2.setAdapter(dataAdapter);
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
            s2.setAdapter(dataAdapter2);
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
            s2.setAdapter(dataAdapter3);

        }

    }
}
