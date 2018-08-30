package com.example.kevin.skripsitolongin;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    TextView textViewEmails,textViewTitle;
    Button buttonLogout,buttonAllUser;
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
    public static final String SKILL  = "com.example.kevin.tolonginskripsi.skill";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        firebaseAuth=FirebaseAuth.getInstance();

        //mendapatkan alamat dari cabang user
        databaseUser = FirebaseDatabase.getInstance().getReference("user");
        //memindahkan nilai array list ke users
        users = new ArrayList<>();
        textViewEmails = (TextView) findViewById(R.id.textViewEmails);
        buttonAllUser = (Button) findViewById(R.id.buttonAllUser);

        buttonLogout =(Button) findViewById(R.id.buttonLogout);
        listViewUser = (ListView) findViewById(R.id.listViewAsk) ;

        //mengambil email dari user yang login saat ini

        textViewEmails.setText("Admin");


        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(AdminActivity.this,MainActivity.class));
            }
        });

    }
    // proses yang dimulai ketika halaman dibuka
    protected void onStart() {
        super.onStart();

        listViewUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //mengambil nilai request berdasarkan yang diklik
                User user = users.get(i);
                //mendapatkan nilai user yang login

                String id = user.getUserID();
                String email = user.getEmail();
                String fullname = user.getFullName();
                String phone = user.getPhone();
                String password = user.getPassword();
                String status = user.getUserStatus();
                String skill = user.getSkill();
                //jika email yg minta request sama dengan email yang login maka akan pindah kehalaman cancel request

                    Intent intent = new Intent(getApplicationContext(),DetailUserActivity.class);
                    //menambahkan masing2 nilai kehalaman berikutnya
                    intent.putExtra(ID, id);
                    intent.putExtra(EMAIL, email);
                    intent.putExtra(FULLNAME, fullname);
                    intent.putExtra(PHONE,phone);
                    intent.putExtra(PASSWORD, password);
                    intent.putExtra(STATUS, status);
                    intent.putExtra(SKILL,skill);
                    startActivity(intent);




            }
        });


        buttonAllUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                startActivity(intent);
            }
        });

        //menambahkan nilai pada databaseRequest yang path nya berada pada user
        databaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //menghapus daftar user yg lama
                users.clear();

                //mengambil cabang2 dari user
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //mendapatkan nilai user
                    User user = postSnapshot.getValue(User.class);
                    //menambahkan user ke list
                    users.add(user);
                }

                //membuat adapter
                UserList userAdapter = new UserList(AdminActivity.this, users);
                //menambahkan adapter ke listview
                listViewUser.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}

