package com.example.kevin.skripsitolongin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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

public class MainActivity extends AppCompatActivity {

    //inisialisasi
    EditText editTextEmail,editTextPassword;
    Button buttonRegister,buttonLogin;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    public static final String TITLE= "com.example.kevin.tolonginskripsi.title";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //mengecek kalau user belum login maka otomatis kembali kehalaman main,jika tidak maka akan langsung ke halaman profile
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()!= null){
            finish();
            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
        }

        progressDialog = new ProgressDialog(this);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonLogin = (Button)findViewById(R.id.buttonLogin);
        buttonRegister = (Button)findViewById(R.id.buttonRegister);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();


            }
        });
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });



    }
    private void check()
    {

        firebaseAuth= FirebaseAuth.getInstance();
        FirebaseUser usert = firebaseAuth.getCurrentUser();
        //mendapatkan nilai anak dari cabang user->userid
        String  key = usert.getUid();
        final DatabaseReference  databaseUser = FirebaseDatabase.getInstance().getReference("user").
                child(key);
        databaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                       String userStatus = user.getUserStatus();
                if(userStatus.equals("Rejected"))
               {
                 Toast.makeText(MainActivity.this, "Your Account have been suspend " + ",email to admin@gmail.com to request Unlock your account", Toast.LENGTH_LONG).show();
                                                   }
                                                   else {
//          Toast.makeText(MainActivity.this, "Login Success.", Toast.LENGTH_SHORT).show();
       finish();
         startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                                                   }
                                               }

                                               @Override
                                               public void onCancelled(DatabaseError databaseError) {

                                               }

                                           }
        );
    }
    private  void login(){



        final String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        progressDialog.setMessage("Login...");
        progressDialog.show();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(MainActivity.this, "Email cannot be Empty", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return;
        }
        else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Password cannot be Empty", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return;
        }



        else if(email.equals("a")&& password.equals("a"))
        {
            Toast.makeText(MainActivity.this, "Welcome Admin", Toast.LENGTH_SHORT).show();
            finish();
            Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
            startActivity(intent);
            progressDialog.dismiss();
        }

        else{
            //mengecek apakah email dan password sudah benar
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {

                        @Override

                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                check();

                            }
                            else {
                                Toast.makeText(MainActivity.this, "Email or Password not match!", Toast.LENGTH_SHORT).show();
                            }
                            progressDialog.dismiss();
                        }
                    });

        }
    }

}



