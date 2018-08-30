package com.example.kevin.skripsitolongin;

import android.content.Intent;
import android.net.Uri;
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
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
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

public class TransactionActivity extends AppCompatActivity {

    TextView textViewEmail,textViewTitle,textViewEmails,textViewRating,textViewRatingValue;
    Button buttonProfile,buttonLogout,buttonTransaction,buttonMyReqList,buttonHistory;
    ImageButton buttonHome;
    ListView listViewTransaction;
    List<Transaction> transactions;
    DatabaseReference databaseTransaction,databaseRequest,databaseRequest2,databaseHistoryTransaction,databaseUser,databaseRating,databaseRatings;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase getDatabase;
    DatabaseReference mRef;

    //mengambil nilai dari halaman sebelah
    public static final String transaction = "com.example.kevin.tolonginskripsi.transaction";
    public static final String titles = "com.example.kevin.tolonginskripsi.titles";
    public static final String CHAT_ROOM_ID= "com.example.kevin.tolonginskripsi.chats";
    public static final String ASK= "com.example.kevin.tolonginskripsi.ASKS";
    public static final String GIVE= "com.example.kevin.tolonginskripsi.GIVES";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        firebaseAuth= FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
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
        listViewTransaction= (ListView) findViewById(R.id.listViewTransaction);
        databaseRating = FirebaseDatabase.getInstance().getReference("rating").child(user.getUid());
        getDatabase= FirebaseDatabase.getInstance();
        mRef = getDatabase.getReference();
        String users = user.getUid();
        //mendapatkan nilai title dr halaman sblmnya
        String key = getIntent().getExtras().get(transaction).toString();
        final String title= getIntent().getExtras().get(titles).toString();
        textViewTitle.setText(title);
        databaseRatings = FirebaseDatabase.getInstance().getReference("rating");
        //mendapatkan alamat dari cabang request
        databaseRequest = FirebaseDatabase.getInstance().getReference("request");
        databaseRequest2 = FirebaseDatabase.getInstance().getReference("myRequest");
        //mendapatkan alamat dari cabang transaction
        databaseTransaction = FirebaseDatabase.getInstance().getReference("transaction").child(users).child(key);
        //mendapatkan alamat dari cabang historytransaction
        databaseHistoryTransaction= FirebaseDatabase.getInstance().getReference("historyTransaction");
        //memindahkan kedalam array list
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


        transactions = new ArrayList<>();
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(TransactionActivity.this,MainActivity.class));
            }
        });

        buttonProfile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransactionActivity.this,ShowProfileActivity.class);
                startActivity(intent);
            }

        });

        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TransactionActivity.this,ProfileActivity.class));
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
        buttonHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (title.equals("My Transaction List"))
                {
                    Intent intent = new Intent(getApplicationContext(), HistoryTransactionActivity.class);
                    intent.putExtra(transaction, "GiveHelp");
                    intent.putExtra(titles, "My History Transaction List");
                    startActivity(intent);
                } else if(title.equals("My Request List"))
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
        //mendapatkan nilai transaction berdasarkan yang mana yg dipilih
        listViewTransaction.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Transaction transaction = transactions.get(i);
                //membawa nilai transaction ke showdialog
                showDialog(transaction.getTransactionID(), transaction.getRequestName(),transaction.getUserID_Req(),
                        transaction.getUserID_GiveHelp(),transaction.getStatus(),transaction.getRequestSkill(),
                        transaction.getRequestPhone(),transaction.getRequestEmailAcceptBy(),transaction.getRequestEmailOrderBy(),
                        transaction.getRequestAddress(),transaction.getRequestID(),transaction.getChatRoomID());
                return true;
            }
        });

        databaseTransaction.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //menghapus transaction list sblmnya
                transactions.clear();
                //mendapatkan semua nilai dari masing2 anak
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //mengdapatkan nilai dari class Transaction
                    Transaction transaction = postSnapshot.getValue(Transaction.class);
                    //menambahkan transaction
                    transactions.add(transaction);
                }
                //membuat adapter
                TransactionList transactionAdapter = new TransactionList(TransactionActivity.this, transactions);
                //menambahkan adapter ke listview
                listViewTransaction.setAdapter(transactionAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });



    }
    private boolean addRating(final  String userID_GiveHelp , final Float ratingGiveHelp)
    {

                String usersID_GiveHelp = userID_GiveHelp;
                 float transactionRating = ratingGiveHelp;
                String ratingID = databaseRatings.push().getKey();
               // mDatabase3.child("rating").setValue(ratingGiveHelp);
                Rating rating = new Rating(transactionRating,ratingID,usersID_GiveHelp);
                //menyimpan nilai request
                databaseRatings.child(userID_GiveHelp).child(ratingID).setValue(rating);

        finish();
        return true;
    }
    //fungsi menghapus chat
    private boolean deleteChat(String chatRoomID)
    {
        DatabaseReference dRs = FirebaseDatabase.getInstance().getReference("message")
                .child(chatRoomID);
        dRs.removeValue();
        return true;
    }
    //fungsi jika transaction dibatalkan oleh user request maka nilai akan pindah ke request
    private boolean cancelRequest(String userID_Req,String requestName,String requestKeahlian,String requestPhone
            ,String requestAddress,String requestEmailOrderBy,String requestID) {
        String reqID = requestID;
        String userID = userID_Req;
        String name = requestName;
        String skill= requestKeahlian;
        String  phone = requestPhone;
        String address = requestAddress;
        String email = requestEmailOrderBy;
        //membuat request baru
        Request request = new Request(reqID, name, skill, phone, address, email,userID);
        //menyimpan nilai request
       databaseRequest.child(skill).child(reqID).setValue(request);
        databaseRequest2.child(userID).child(reqID).setValue(request);
        Toast.makeText(this, "Your transaction has been processed to Request List", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(TransactionActivity.this, ProfileActivity.class));
        return true;
    }
    //fungsi jika transaksi sudah selesai maka nilai pada transaction masing2 user(askhelp/givehelp)akan dihapus
    private boolean deleteTransaction(String transactionID,String userID_Req,String userID_GiveHelp,String status) {

        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("transaction")
                .child(userID_Req).child("AskHelp").child(transactionID);
        DatabaseReference dR2 = FirebaseDatabase.getInstance().getReference("transaction")
                .child(userID_GiveHelp).child("GiveHelp").child(transactionID);
        //menghapus nilai dr ask help dan give help
        dR.removeValue();
        dR2.removeValue();
        return true;
    }
    //fungsi jika transaksi sudah selesai maka nilai pada transaction masing2 user(askhelp/givehelp)akan dipindahkan ke history masing2
    private boolean addIntoHistoryTransaction(String transactionID,String userID_Req,String userID_GiveHelp,String status, String requestAddress,
                                              String requestEmailAcceptBy,String requestEmailOrderBy,String requestID,
                                              String requestKeahlian,String requestName,String  requestPhone,Float ratingGiveHelp) {
        String reqID = requestID;
        String name = requestName;
        String keahlian = requestKeahlian;
        String  phone = requestPhone;
        String address = requestAddress;
        String emailOrderBy = requestEmailOrderBy;
        String emailAcceptBy = requestEmailAcceptBy;
        String id = transactionID;
        String IDReq= userID_Req;
        String statusTrans = "Accepted by User Request";
        String  ID_GiveHelp= userID_GiveHelp;
        Float ratingGiveHelps = ratingGiveHelp;
        //membuat historyTrans baru
        HistoryTransaction historyTransaction = new HistoryTransaction(IDReq,ID_GiveHelp,reqID,name,keahlian,
                phone,address,emailOrderBy,emailAcceptBy,id,statusTrans,ratingGiveHelps);

        //menyimpan historyTrans ke masing2 user
        databaseHistoryTransaction.child(ID_GiveHelp).child("GiveHelp").child(id).setValue(historyTransaction);
        databaseHistoryTransaction.child(IDReq).child("AskHelp").child(id).setValue(historyTransaction);

        finish();
        return true;
    }
    //menampilkan dialog berdasarkan halaman mana yang user pilih(askhelp/givehelp)
    private void showDialog(final String transactionID, final String requestName, final String userID_Req,
                            final String userID_GiveHelp , final String status, final String requestKeahlian,
                            final String  requestPhone,final String requestEmailAcceptBy, final String requestEmailOrderBy ,
                            final String requestAddress,final String requestID,final String chatRoomID) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        AlertDialog.Builder dialogBuilder2 = new AlertDialog.Builder(this);
        AlertDialog.Builder dialogBuilder3 = new AlertDialog.Builder(this);
        AlertDialog.Builder dialogBuilder4 = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        LayoutInflater inflater2 = getLayoutInflater();
        LayoutInflater inflater3 = getLayoutInflater();
        LayoutInflater inflater4 = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.delete_dialog, null);
        final View dialogView2 = inflater2.inflate(R.layout.complete_dialog, null);
        final View dialogView3 = inflater3.inflate(R.layout.cancel_dialog, null);
        final View dialogView4 = inflater4.inflate(R.layout.chat_complete, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder2.setView(dialogView2);
        dialogBuilder3.setView(dialogView3);
        dialogBuilder4.setView(dialogView4);

        final Button buttonChat = (Button) dialogView.findViewById(R.id.buttonChat);
        final Button buttonChat2 = (Button) dialogView2.findViewById(R.id.buttonChat);
        final Button buttonChat3 = (Button) dialogView3.findViewById(R.id.buttonChat);
        final Button buttonChat4 = (Button) dialogView4.findViewById(R.id.buttonChat);
        final Button buttonCancel= (Button) dialogView3.findViewById(R.id.buttonCancel);
        final Button buttonFinishTransaction = (Button)dialogView.findViewById(R.id.buttonFinishTransaction);
        final Button buttonCancelTransaction = (Button) dialogView.findViewById(R.id.buttonCancelTransaction);
        final Button buttonComplete = (Button) dialogView2.findViewById(R.id. buttonComplete);
        final Button buttonMapGive = (Button) dialogView2.findViewById(R.id. buttonMap);
        final RatingBar ratingBars = (RatingBar) dialogView.findViewById(R.id.ratingBarValue);
        final TextView textViewRatingValue= (TextView) dialogView.findViewById(R.id.textViewRatingValue);
        dialogBuilder.setTitle(requestName);
        dialogBuilder2.setTitle(requestName);
        dialogBuilder3.setTitle(requestName);
        dialogBuilder4.setTitle(requestName);

        final AlertDialog a = dialogBuilder.create();
        final AlertDialog b = dialogBuilder2.create();
        final AlertDialog c = dialogBuilder3.create();
        final AlertDialog d = dialogBuilder4.create();
        String key = getIntent().getExtras().get(transaction).toString();
        //validasi untuk menentukan status dan dialog apa yang akan muncul
        if(key.equals("AskHelp"))
        {

            if(status.equals("Accepted by User Request"))
            {
                deleteTransaction(transactionID,userID_Req,userID_GiveHelp,status);
            }
            else if(status.equals("Complete"))
            {
                a.show();
            }
            else{
                c.show();
            }
        }
        else if(key.equals("GiveHelp"))
        {
            //jika status oleh give help = on progres maka layout complete_dialog yg akan muncul
            if(status.equals("On Progress"))
            {
                b.show();
            }
            else{
                d.show();
            }

        }
        ratingBars.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                textViewRatingValue.setText(String.valueOf(rating));

            }
        });
        buttonMapGive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyLoc = requestAddress;
                Uri mapUri  = Uri.parse(("geo:0,0?q="+Uri.encode(keyLoc)));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW,mapUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
        //tombol ke halaman chat
        buttonChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransactionActivity.this,ChatActivity.class);
                intent.putExtra(CHAT_ROOM_ID,chatRoomID);
                intent.putExtra(ASK,userID_Req);
                intent.putExtra(GIVE,userID_GiveHelp);
                startActivity(intent);
            }
        });
        //tombol ke halaman chat
        buttonChat2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransactionActivity.this,ChatActivity.class);
                intent.putExtra(CHAT_ROOM_ID,chatRoomID);
                intent.putExtra(ASK,userID_Req);
                intent.putExtra(GIVE,userID_GiveHelp);

                startActivity(intent);
            }
        });
        //tombol ke halaman chat
        buttonChat3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransactionActivity.this,ChatActivity.class);
                intent.putExtra(CHAT_ROOM_ID,chatRoomID);
                intent.putExtra(ASK,userID_Req);
                intent.putExtra(GIVE,userID_GiveHelp);
                startActivity(intent);
            }
        });
        //tombol ke halaman chat
        buttonChat4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransactionActivity.this,ChatActivity.class);
                intent.putExtra(CHAT_ROOM_ID,chatRoomID);
                intent.putExtra(ASK,userID_Req);
                intent.putExtra(GIVE,userID_GiveHelp);
                startActivity(intent);
            }
        });
        //jika transaction dibatalkan oleh user request,maka pada transaction hilang ,
        // lalu nilai akan ditambahkan kembali kehalaman request
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog2(userID_Req, requestName, requestKeahlian,requestPhone
                        , requestAddress, requestEmailOrderBy, requestID,transactionID,userID_GiveHelp,status,chatRoomID);

                c.dismiss();
            }
        });
        //jika transaction sudah selesai
        buttonFinishTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth= FirebaseAuth.getInstance();
                FirebaseUser user = firebaseAuth.getCurrentUser();
                String usersID_GiveHelp = userID_GiveHelp;
                String usersID_Reqs = userID_Req;

                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("transaction")
                        .child(usersID_GiveHelp).child("GiveHelp").child(transactionID);

                DatabaseReference mDatabase2 = FirebaseDatabase.getInstance().getReference("transaction")
                        .child(usersID_Reqs).child("AskHelp").child(transactionID);

                mDatabase.child("status").setValue("Accepted by User Request");
                mDatabase2.child("status").setValue("Accepted by User Request");
               final String rating_GiveHelp = textViewRatingValue.getText().toString().trim();
                Float ratingGiveHelp = Float.parseFloat(rating_GiveHelp);
                addIntoHistoryTransaction(transactionID,userID_Req,userID_GiveHelp,status,requestAddress,
                        requestEmailAcceptBy,requestEmailOrderBy,requestID,requestKeahlian,requestName,requestPhone,ratingGiveHelp);
                deleteTransaction(transactionID,userID_Req,userID_GiveHelp,status);
                deleteChat(chatRoomID);
                addRating(userID_GiveHelp,ratingGiveHelp);


                a.dismiss();
                Toast.makeText(getApplicationContext(), "Transaction Success", Toast.LENGTH_LONG).show();
                startActivity(new Intent(TransactionActivity.this,MainActivity.class));
            }
        });
        //jika transaction sudah complete oleh user givehelp tetapi dibatalkan oleh user askhelp
        buttonCancelTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDialog2(userID_Req, requestName, requestKeahlian,requestPhone
                        , requestAddress, requestEmailOrderBy, requestID,transactionID,userID_GiveHelp,status,chatRoomID);
                a.dismiss();
            }
        });
        //jika transaction sudah dikerjakan oleh user givehelp
        buttonComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth= FirebaseAuth.getInstance();
                FirebaseUser user = firebaseAuth.getCurrentUser();
                String users = user.getUid();
                String userID_Reqs = userID_Req;

                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("transaction")
                        .child(users).child("GiveHelp").child(transactionID);

                DatabaseReference mDatabase2 = FirebaseDatabase.getInstance().getReference("transaction")
                        .child(userID_Reqs).child("AskHelp").child(transactionID);

                mDatabase.child("status").setValue("Complete");
                mDatabase2.child("status").setValue("Complete");
                b.dismiss();

            }
        });
    }
    public void showDialog2(final String userID_Req,final String requestName,final String requestKeahlian,final String requestPhone
            ,final String requestAddress,final String requestEmailOrderBy,final String requestID,final String transactionID,
                            final String userID_GiveHelp,final String status,final String chatRoomID) {


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


                cancelRequest(userID_Req, requestName, requestKeahlian, requestPhone
                        , requestAddress, requestEmailOrderBy, requestID);
                deleteTransaction(transactionID,userID_Req,userID_GiveHelp,status);
                deleteChat(chatRoomID);
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
    }
