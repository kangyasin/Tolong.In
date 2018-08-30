
package com.example.kevin.skripsitolongin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Map;

public class DetailUserActivity extends AppCompatActivity {

    TextView textViewEmails,textViewTitle,textViewUserID,textViewUserName,textViewUserPhone,
            textViewUserStatus,textViewUserEmail,textViewUserPassword,textViewRating,textViewSkill;
    Button buttonLogout,buttonAllUser,buttonCheckHistory;
    ListView listViewUser;
    List<User> users;
    DatabaseReference databaseUser;
    FirebaseAuth firebaseAuth;
    //untuk mengcopy nilai untuk dibawa kehalaman tertentu
    public static final String test = "com.example.kevin.tolonginskripsi.titles";
    public static final String test2 = "com.example.kevin.tolonginskripsi.titless";
    public static final String ID= "com.example.kevin.tolonginskripsi.id ";
    public static final String FULLNAME = "com.example.kevin.tolonginskripsi.fullname";
    public static final String EMAIL = "com.example.kevin.tolonginskripsi.email";
    public static final String PHONE = "com.example.kevin.tolonginskripsi.phone";
    public static final String PASSWORD = "com.example.kevin.tolonginskripsi.password";
    public static final String STATUS = "com.example.kevin.tolonginskripsi.status";
    public static final String SKILL = "com.example.kevin.tolonginskripsi.skill";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_user);
        firebaseAuth=FirebaseAuth.getInstance();

        textViewEmails = (TextView) findViewById(R.id.textViewEmails);
        buttonAllUser = (Button) findViewById(R.id.buttonAllUser);
        buttonCheckHistory = (Button) findViewById(R.id.buttonCheckHistory);
        buttonLogout =(Button) findViewById(R.id.buttonLogout);
        textViewUserID =  (TextView) findViewById(R.id.textViewUserID);
        textViewSkill =  (TextView) findViewById(R.id.editTextPhone);
        textViewUserName =  (TextView) findViewById(R.id.textViewRequestNames);
        textViewUserPhone =  (TextView) findViewById(R.id.textViewRequestPhone);
        textViewUserStatus =  (TextView) findViewById(R.id.textViewUserStatus);
        textViewUserEmail =  (TextView) findViewById(R.id.textViewEmail);
        textViewUserPassword =  (TextView) findViewById(R.id.editTextFullname);
        textViewRating =  (TextView) findViewById(R.id.textViewRating);

        //mengambil email dari user yang login saat ini

        textViewEmails.setText("Admin");

        final String id= getIntent().getExtras().get(ID).toString();
        final String name= getIntent().getExtras().get(FULLNAME).toString();
        String email= getIntent().getExtras().get(EMAIL).toString();
        String phone= getIntent().getExtras().get(PHONE).toString();
        String status= getIntent().getExtras().get(STATUS).toString();
        String password= getIntent().getExtras().get(PASSWORD).toString();
        String skill= getIntent().getExtras().get(SKILL).toString();
        textViewUserID.setText(" "+id);
        textViewUserName.setText(" "+name);
        textViewUserPhone.setText(" "+phone);
        textViewUserStatus.setText(" "+status);
        textViewUserEmail.setText(" "+email);
        textViewUserPassword.setText(" "+password);
        textViewSkill.setText(" "+skill);
        DatabaseReference databaseRating = FirebaseDatabase.getInstance().getReference("rating").child(id);
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
                    textViewRating.setText(String.valueOf(x));

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        buttonCheckHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CheckAllTransactionActivity.class);
                intent.putExtra(test,id);
                intent.putExtra(test2,name);
                startActivity(intent);
            }
        });

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(DetailUserActivity.this,MainActivity.class));
            }
        });

    }
    // proses yang dimulai ketika halaman dibuka
    protected void onStart() {
        super.onStart();



        buttonAllUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id= getIntent().getExtras().get(ID).toString();
                String name= getIntent().getExtras().get(FULLNAME).toString();
                String status= getIntent().getExtras().get(STATUS).toString();
                showDialog(name,status,id);
            }
        });

        //menambahkan nilai pada databaseRequest yang path nya berada pada user


    }

    private void showDialog(final String fullname, final String userStatus,final String userID) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.change_status_dialog, null);
        dialogBuilder.setView(dialogView);
        final Button buttonAllowed = (Button) dialogView.findViewById(R.id.buttonYes);
        final Button buttonRejected = (Button) dialogView.findViewById(R.id.buttonRejected);
        final Button buttonUserHistory = (Button) dialogView.findViewById(R.id.buttonCheckHistory);
        dialogBuilder.setTitle(fullname+"  "+userStatus);
        final AlertDialog a = dialogBuilder.create();


        a.show();


        buttonAllowed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("user")
                        .child(userID);
                mDatabase.child("userStatus").setValue("Allowed");

                DatabaseReference mDatabase2 = FirebaseDatabase.getInstance().getReference("rating");
                mDatabase2.child(userID).removeValue();

                Toast.makeText(DetailUserActivity.this, "User Status "+fullname+": Allowed", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(DetailUserActivity.this,AdminActivity.class));
                a.dismiss();


            }
        });
        buttonRejected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("user")
                        .child(userID);
                mDatabase.child("userStatus").setValue("Rejected");


                Toast.makeText(DetailUserActivity.this, "User Status "+fullname+": Rejected", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(DetailUserActivity.this,AdminActivity.class));
                a.dismiss();


            }
        });



    }




}
