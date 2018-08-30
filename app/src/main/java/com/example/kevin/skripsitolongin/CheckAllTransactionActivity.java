package com.example.kevin.skripsitolongin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class CheckAllTransactionActivity extends AppCompatActivity {

    TextView textViewEmails,textViewTitle;
    Button buttonLogout,buttonAllUser;
    ListView listViewHistoryTransaction;
    ListView listViewHistoryTransaction2;
    List<HistoryTransaction> historyTransactions;
    List<HistoryTransaction> historyTransactions2;
    DatabaseReference databaseTransaction,databaseTransaction2;
    FirebaseAuth firebaseAuth;
    //untuk mengcopy nilai untuk dibawa kehalaman tertentu
    public static final String test = "com.example.kevin.tolonginskripsi.titles";
    public static final String test2= "com.example.kevin.tolonginskripsi.titless";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_all_transaction);

        firebaseAuth=FirebaseAuth.getInstance();

        //mendapatkan alamat dari cabang user

        //memindahkan nilai array list ke users
        historyTransactions = new ArrayList<>();
        historyTransactions2 = new ArrayList<>();
        textViewEmails = (TextView) findViewById(R.id.textViewEmails);

        buttonAllUser = (Button) findViewById(R.id.buttonAllUser);
        buttonLogout =(Button) findViewById(R.id.buttonLogout);
        listViewHistoryTransaction = (ListView) findViewById(R.id.listViewAsk) ;
        listViewHistoryTransaction2 = (ListView) findViewById(R.id.listViewGive) ;
        //mengambil email dari user yang login saat ini

        textViewEmails.setText("Admin");
        String key = getIntent().getExtras().get(test).toString();
        String key2 = getIntent().getExtras().get(test2).toString();

        databaseTransaction = FirebaseDatabase.getInstance().getReference("historyTransaction").child(key).child("AskHelp");
        databaseTransaction2 = FirebaseDatabase.getInstance().getReference("historyTransaction").child(key).child("GiveHelp");
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(CheckAllTransactionActivity.this,MainActivity.class));
            }
        });

    }
    // proses yang dimulai ketika halaman dibuka
    protected void onStart() {
        super.onStart();

        buttonAllUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                startActivity(intent);
            }
        });

        //menambahkan nilai pada databaseRequest yang path nya berada pada user
        databaseTransaction.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //menghapus daftar user yg lama
                historyTransactions.clear();

                //mengambil cabang2 dari user
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //mendapatkan nilai user
                    HistoryTransaction historyTransaction = postSnapshot.getValue(HistoryTransaction.class);
                    //menambahkan user ke list
                    historyTransactions.add(historyTransaction);
                }

                //membuat adapter
                HistoryTransactionList historyTransactionAdapter = new HistoryTransactionList(CheckAllTransactionActivity.this, historyTransactions);
                //menambahkan adapter ke listview
                listViewHistoryTransaction.setAdapter(historyTransactionAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        databaseTransaction2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //menghapus daftar user yg lama
                historyTransactions2.clear();

                //mengambil cabang2 dari user
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //mendapatkan nilai user
                    HistoryTransaction historyTransaction2 = postSnapshot.getValue(HistoryTransaction.class);
                    //menambahkan user ke list
                    historyTransactions2.add(historyTransaction2);
                }

                //membuat adapter
                HistoryTransactionList historyTransactionAdapter2 = new HistoryTransactionList(CheckAllTransactionActivity.this, historyTransactions2);
                //menambahkan adapter ke listview
                listViewHistoryTransaction2.setAdapter(historyTransactionAdapter2);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



}


