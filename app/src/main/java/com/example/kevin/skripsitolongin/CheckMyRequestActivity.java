package com.example.kevin.skripsitolongin;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
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

public class CheckMyRequestActivity extends AppCompatActivity {
    //textViewEmails = nama email yg muncul ketika user login
    TextView textViewEmails,textViewRating;
    Button buttonLogout,buttonProfile,buttonTransaction,buttonMyReqList;
    ImageButton buttonRequest;
    ListView listViewRequest;
    List<Request> requests;
    DatabaseReference databaseRequest,databaseRating;
    FirebaseAuth firebaseAuth;
    //untuk mengcopy nilai untuk dibawa kehalaman tertentu
    public static final String USER_ID = "com.example.kevin.tolonginskripsi.userid";
    public static final String REQUEST_NAME = "com.example.kevin.tolonginskripsi.requestname";
    public static final String REQUEST_ID = "com.example.kevin.tolonginskripsi.requestid";
    public static final String REQUEST_PHONE = "com.example.kevin.tolonginskripsi.requestphone";
    public static final String REQUEST_KEAHLIAN= "com.example.kevin.tolonginskripsi.requestkeahlian";
    public static final String REQUEST_ADDRESS = "com.example.kevin.tolonginskripsi.requestaddress";
    public static final String REQUEST_EMAIL = "com.example.kevin.tolonginskripsi.requestemail";
    public static final String transaction= "com.example.kevin.tolonginskripsi.transaction";
    public static final String titles= "com.example.kevin.tolonginskripsi.titles";
    public static final String RATING= "com.example.kevin.tolonginskripsi.rating";
    public static final String CHECK = "1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_my_request);
        rating();
        check();



//mengecek jika belum ada user yang login maka akan pindah ke halaman main
//        checkGPSStatus();
        firebaseAuth=FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        if(firebaseAuth.getCurrentUser()== null){
            finish();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }
        //mendapatkan alamat dari cabang request


        //memindahkan nilai array list ke requests
        requests = new ArrayList<>();
        textViewRating= (TextView) findViewById(R.id.textViewRating);
        textViewEmails = (TextView) findViewById(R.id.textViewEmails);
        buttonMyReqList = (Button) findViewById(R.id.buttonMyRequestList);
        buttonTransaction =(Button) findViewById(R.id.buttonAllUser);
        buttonLogout =(Button) findViewById(R.id.buttonLogout);
        buttonRequest =(ImageButton) findViewById(R.id.buttonRequest);
        buttonProfile =(Button) findViewById(R.id.buttonProfile);

        listViewRequest = (ListView) findViewById(R.id.listViewRequest) ;


        //mengambil email dari user yang login saat ini

        textViewEmails.setText(user.getEmail());


        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(CheckMyRequestActivity.this,MainActivity.class));
            }
        });

        buttonProfile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckMyRequestActivity.this,ShowProfileActivity.class);
                startActivity(intent);
            }

        });



        listViewRequest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //mengambil nilai request berdasarkan yang diklik
                Request request = requests.get(i);
                //mendapatkan nilai user yang login
                FirebaseUser user = firebaseAuth.getCurrentUser();

                String userEmail = user.getEmail();
                String emailRequest = request.getRequestEmail();
                //jika email yg minta request sama dengan email yang login maka akan pindah kehalaman cancel request
                if(userEmail.equals(emailRequest)){
                    Intent intent = new Intent(getApplicationContext(), CancelRequestActivity.class);
                    //menambahkan masing2 nilai kehalaman berikutnya
                    intent.putExtra(USER_ID, request.getUserID());
                    intent.putExtra(REQUEST_ID, request.getRequestID());
                    intent.putExtra(REQUEST_NAME, request.getRequestName());
                    intent.putExtra(REQUEST_PHONE,request.getRequestPhone());
                    intent.putExtra(REQUEST_KEAHLIAN, request.getRequestSkill());
                    intent.putExtra(REQUEST_ADDRESS, request.getRequestAddress());
                    intent.putExtra(REQUEST_EMAIL, request.getRequestEmail());
                    //memulai activity
                    startActivity(intent);
                }
                //jika email yang minta request berbeda dengan email yang login saat ini maka akan pindah ke requestactivty
                else{
                    Intent intent = new Intent(getApplicationContext(), RequestActivity.class);
                    //menambahkan masing2 nilai kehalaman berikutnya
                    intent.putExtra(USER_ID, request.getUserID());
                    intent.putExtra(REQUEST_ID, request.getRequestID());
                    intent.putExtra(REQUEST_NAME, request.getRequestName());
                    intent.putExtra(REQUEST_PHONE,request.getRequestPhone());
                    intent.putExtra(REQUEST_KEAHLIAN, request.getRequestSkill());
                    intent.putExtra(REQUEST_ADDRESS, request.getRequestAddress());
                    intent.putExtra(REQUEST_EMAIL, request.getRequestEmail());
                    //memulai activity
                    startActivity(intent);
                }

            }
        });

    }
    private void rating()
    {
        firebaseAuth=FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseRating = FirebaseDatabase.getInstance().getReference("rating").child(user.getUid());
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

    }
    private void check()
    {
        firebaseAuth= FirebaseAuth.getInstance();
        FirebaseUser usert = firebaseAuth.getCurrentUser();
        String  key = usert.getUid();
        final DatabaseReference  databaseUser = FirebaseDatabase.getInstance().getReference("user").
                child(key);
        databaseUser.addValueEventListener(new ValueEventListener() {
                                               @Override
                                               public void onDataChange(DataSnapshot dataSnapshot) {
                                                   User user = dataSnapshot.getValue(User.class);
                                                   final String SkillUser = user.getSkill();
                                                   String userStatus = user.getUserStatus();
                                                   if(userStatus.equals("Rejected"))
                                                   {
                                                       Toast.makeText(CheckMyRequestActivity.this, "Your Account have been suspend " +
                                                               ",email to admin@gmail.com to request Unlock your account", Toast.LENGTH_LONG).show();
                                                       firebaseAuth.signOut();
                                                       finish();
                                                       startActivity(new Intent(CheckMyRequestActivity.this,MainActivity.class));
                                                   }
                                               }
                                               @Override
                                               public void onCancelled(DatabaseError databaseError) {
                                               }
                                           }
        );
    }



    // proses yang dimulai ketika halaman dibuka
    protected void onStart() {
        super.onStart();

        firebaseAuth= FirebaseAuth.getInstance();
        FirebaseUser usert = firebaseAuth.getCurrentUser();
        String  key = usert.getUid();
        final DatabaseReference  databaseUser = FirebaseDatabase.getInstance().getReference("user").
                child(key);
        databaseUser.addValueEventListener(new ValueEventListener() {
                                               @Override
                                               public void onDataChange(DataSnapshot dataSnapshot) {
                                                   User user = dataSnapshot.getValue(User.class);
                                                   final String SkillUser = user.getSkill();
                                                   String key = user.getUserID();
                                                   databaseRequest = FirebaseDatabase.getInstance().getReference("myRequest").child(key);
                                                   //menambahkan nilai pada databaseRequest yang path nya berada pada request
                                                   databaseRequest.addValueEventListener(new ValueEventListener() {
                                                       @Override
                                                       public void onDataChange(DataSnapshot dataSnapshot) {

                                                           //menghapus daftar request yg lama
                                                           requests.clear();

                                                           //mengambil cabang2 dari request
                                                           for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                                               //mendapatkan nilai request
                                                               Request request = postSnapshot.getValue(Request.class);
                                                               //menambahkan request ke list
                                                               requests.add(request);
                                                           }

                                                           //membuat adapter
                                                           RequestList requestAdapter = new RequestList(CheckMyRequestActivity.this, requests);
                                                           //menambahkan adapter ke listview
                                                           listViewRequest.setAdapter(requestAdapter);
                                                       }

                                                       @Override
                                                       public void onCancelled(DatabaseError databaseError) {

                                                       }
                                                   });

                                               }
                                               @Override
                                               public void onCancelled(DatabaseError databaseError) {

                                               }
                                           }
        );




    }




}


