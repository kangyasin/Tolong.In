package com.example.kevin.skripsitolongin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    EditText editTextFullName,editTextPassword,editTextEmail,editTextPhone;
    Button buttonRegister;
    ProgressDialog progressDialog;
    Spinner s1,s2;
    DatabaseReference databaseUser;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editTextFullName = (EditText) findViewById(R.id.editTextFullname);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPhone = (EditText) findViewById(R.id.editTextPhone);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        progressDialog = new ProgressDialog(this);
        //spinner
        s1= (Spinner)findViewById(R.id.spinnerahli1);
        s2= (Spinner)findViewById(R.id.spinnerahli2);
        s1.setOnItemSelectedListener(this);



        //----end of spinner

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser();
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        databaseUser = FirebaseDatabase.getInstance().getReference("user");

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser();
            }
        });


    }

    private void addUser() {
        //getting the values to save
        final String fullName = editTextFullName.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String phone = editTextPhone.getText().toString().trim();
        final String spinners = s2.getSelectedItem().toString();
        //checking if the value is provided

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(RegisterActivity.this, "Email cannot be Empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (email.length()<5 ) {
            Toast.makeText(RegisterActivity.this, "Email name must 5++", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!email.contains("@"))
        {
            Toast.makeText(RegisterActivity.this, "Email must contains @ ", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!email.contains(".com"))
        {
            Toast.makeText(RegisterActivity.this, "Email must ends with .com ", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(RegisterActivity.this, "Password cannot be Empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length()<6 ) {
            Toast.makeText(RegisterActivity.this, "Password must 6++", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(fullName)) {
            Toast.makeText(RegisterActivity.this,"Fullname cannot be Empty",Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(RegisterActivity.this, "Phone cannot be Empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if(spinners.equals("")){
            Toast.makeText(RegisterActivity.this, "Category cannot be Empty", Toast.LENGTH_SHORT).show();
            return;
        }


        progressDialog.setMessage("Registering User...");
        progressDialog.show();


//memasukan email dan password ke firebase
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            String userStatus = "Allowed";
                            final String spinner = s2.getSelectedItem().toString();
                            String userID = (String)firebaseAuth.getCurrentUser().getUid();
                            User user = new User(userID, fullName, password,email,phone,userStatus,spinner);
                            databaseUser.child(userID).setValue(user);
                            Toast.makeText(RegisterActivity.this, "Register is Success  "+fullName, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(RegisterActivity.this, "Email Already Exist", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                            startActivity(intent);
                        }
                    }
                });




    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String sp1 = String.valueOf(s1.getSelectedItem());
        if (sp1.contentEquals("-Select Category-")) {
            List<String> list = new ArrayList<String>();
            list.add("");

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter.notifyDataSetChanged();
            s2.setAdapter(dataAdapter);
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

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
