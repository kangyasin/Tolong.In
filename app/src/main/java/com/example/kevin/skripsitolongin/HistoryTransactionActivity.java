package com.example.kevin.skripsitolongin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
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

public class HistoryTransactionActivity extends AppCompatActivity {

    TextView textViewEmail,textViewTitle,textViewEmails,textViewRating;
    Button buttonProfile,buttonLogout,buttonTransaction,buttonMyReqList,buttonHistory;
    ImageButton buttonHome;
    ListView listViewHistoryTransaction;
    List<HistoryTransaction> historyTransactions;
    DatabaseReference databaseHistoryTransaction,databaseRating;
    FirebaseAuth firebaseAuth;
    //mengambil nilai dari halaman sebelah
    public static final String transaction = "com.example.kevin.tolonginskripsi.transaction";
    public static final String titles = "com.example.kevin.tolonginskripsi.titles";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_transaction);

        firebaseAuth= FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        final String key = getIntent().getExtras().get(transaction).toString();
        final String title= getIntent().getExtras().get(titles).toString();
        buttonProfile =(Button) findViewById(R.id.buttonProfile);
        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        buttonHistory = (Button) findViewById(R.id.buttonHistory);
        buttonMyReqList = (Button) findViewById(R.id.buttonMyRequestList);
        buttonTransaction =(Button) findViewById(R.id.buttonAllUser);
        buttonHome =(ImageButton) findViewById(R.id.buttonHome);
        textViewRating= (TextView) findViewById(R.id.textViewRating);
        textViewEmails = (TextView) findViewById(R.id.textViewEmails);
        textViewEmails.setText(user.getEmail());
        textViewTitle =(TextView) findViewById(R.id.textViewTitle);
        textViewTitle.setText(title);
        listViewHistoryTransaction= (ListView) findViewById(R.id.listViewTransaction);
        databaseRating = FirebaseDatabase.getInstance().getReference("rating").child(user.getUid());
        String users = user.getUid();
        //mendapatkan alamat dari cabang historytransaction
        databaseHistoryTransaction= FirebaseDatabase.getInstance().getReference("historyTransaction").child(users).child(key);
        //memindahkan kedalam array list
        historyTransactions = new ArrayList<>();

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
                startActivity(new Intent(HistoryTransactionActivity.this,MainActivity.class));
            }
        });

        buttonProfile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HistoryTransactionActivity.this,ShowProfileActivity.class);
                startActivity(intent);
            }

        });

        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HistoryTransactionActivity.this,ProfileActivity.class));
            }
        });
        buttonTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TransactionActivity.class);
                intent.putExtra(transaction,"GiveHelp");
                intent.putExtra(titles,"My Transaction List");
                startActivity(intent);
            }
        });
        buttonMyReqList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TransactionActivity.class);
                intent.putExtra(transaction,"AskHelp");
                intent.putExtra(titles,"My Request List");
                startActivity(intent);
            }
        });
        //history akan tampil berdasarkan tombol mana yang kita klik sebelumnya
        buttonHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (title.equals("My Transaction List")) {
                    Intent intent = new Intent(getApplicationContext(), HistoryTransactionActivity.class);
                    intent.putExtra(transaction, "GiveHelp");
                    intent.putExtra(titles, "My History Transaction List");
                    startActivity(intent);
                }
                else if(title.equals("My Request List"))
                {
                    Intent intent = new Intent(getApplicationContext(), HistoryTransactionActivity.class);
                    intent.putExtra(transaction, "AskHelp");
                    intent.putExtra(titles, "My History Request List");
                    startActivity(intent);
                }
            }
        });
    }


    protected void onStart() {
        super.onStart();
        listViewHistoryTransaction.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                showDialog();
                return true;
            }
        });

        databaseHistoryTransaction.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //menghapus transaction list sblmnya
                historyTransactions.clear();
                //mendapatkan semua nilai dari masing2 anak
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //mengdapatkan nilai dari class HistoryTransaction
                    HistoryTransaction historyTransaction = postSnapshot.getValue(HistoryTransaction.class);
                    //menambahkan history
                    historyTransactions.add(historyTransaction);
                }
                //membuat adapter
                HistoryTransactionList historyTransactionAdapter = new HistoryTransactionList(HistoryTransactionActivity.this, historyTransactions);
                //menambahkan adapter ke listview
                listViewHistoryTransaction.setAdapter(historyTransactionAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void showDialog() {
        //membuat dialog
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.clear_history_dialog, null);
        dialogBuilder.setView(dialogView);

        final Button buttonClear = (Button)dialogView.findViewById(R.id.buttonClearHistory);
        final AlertDialog a = dialogBuilder.create();
        a.show();
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteHistory();
                a.dismiss();
            }
        });

    }
    private boolean deleteHistory() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String users = user.getUid();
        String key = getIntent().getExtras().get(transaction).toString();
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("historyTransaction")
                .child(users).child(key);

        //menghapus history
        dR.removeValue();
        Toast.makeText(HistoryTransactionActivity.this, "Your History Request List has been delete",
                Toast.LENGTH_SHORT).show();


        return true;
    }
}
