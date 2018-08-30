package com.example.kevin.skripsitolongin;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {
    private EditText editMessage,editEmail;
    private DatabaseReference mDatabase,mDatabase2;
    private RecyclerView mMesssageList;
    private Button buttonSend;
    FirebaseAuth firebaseAuth;
    //mengambil nilai dari halaman sblmnya
    public static final String CHAT_ROOM_ID= "com.example.kevin.tolonginskripsi.chats";
    public static final String ASK= "com.example.kevin.tolonginskripsi.ASKS";
    public static final String GIVE= "com.example.kevin.tolonginskripsi.GIVES";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        final String chatRoomID= getIntent().getExtras().get(CHAT_ROOM_ID).toString();
        final String askID= getIntent().getExtras().get(ASK).toString();
        final String giveID= getIntent().getExtras().get(GIVE).toString();
        firebaseAuth= FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        editMessage = (EditText) findViewById(R.id.editMessageE);
        //menetapkan lokasi pengambilan nilai
        mDatabase = FirebaseDatabase.getInstance().getReference().child("message").child(chatRoomID).child(askID);
        mDatabase2 = FirebaseDatabase.getInstance().getReference().child("message").child(chatRoomID).child(giveID);
        mMesssageList = (RecyclerView) findViewById(R.id.messageRec);
        buttonSend = (Button) findViewById(R.id.buttonSend);
        mMesssageList.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);

        mMesssageList.setLayoutManager(linearLayoutManager);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth firebaseAuth;
                firebaseAuth= FirebaseAuth.getInstance();
                FirebaseUser user = firebaseAuth.getCurrentUser();
                final String messageValue = editMessage.getText().toString().trim();
                String date = new SimpleDateFormat("MMMM dd, yyyy HH:mm:ss").format(new Date());
                //jika message tidak kosong
                if (!TextUtils.isEmpty(messageValue)){
                    //membuat chatRoomID
                    final DatabaseReference newPost = mDatabase.push();
                    //menetapkan nilai childen dari content dan email
                    newPost.child("content").setValue(messageValue);
                    newPost.child("email").setValue(user.getEmail());
                    newPost.child("date").setValue(date);
                    //membuat chatRoomID
                    final DatabaseReference newPost2 = mDatabase2.push();
                    //menetapkan nilai childen dari content dan email
                    newPost2.child("content").setValue(messageValue);
                    newPost2.child("email").setValue(user.getEmail());
                    newPost2.child("date").setValue(date);
                    editMessage.setText("");

                }


            }

        });
    }

//ketika tombol disend maka akan dijalanka fungsi
//    public void sendButtonClicked(View view) {
//
//    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter <Message,MessageViewHolder> FBRA = new FirebaseRecyclerAdapter<Message, MessageViewHolder>(
                Message.class,R.layout.singlemessagelayout,MessageViewHolder.class,mDatabase
        ) {
            @Override
            protected void populateViewHolder(MessageViewHolder viewHolder, Message model, int position) {
                viewHolder.setContent(model.getContent(),model.getEmail(),model.getDate());
            }
        };
        mMesssageList.setAdapter(FBRA);

    }

    //membuat recycle view
    public static class MessageViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public MessageViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        //menyimpan nilai2 chat
        public void setContent(String content,String emaill,String date){


            TextView message_content = (TextView) mView.findViewById(R.id.MessageText);
            message_content.setText(content);
            TextView emails  = (TextView) mView.findViewById(R.id.emailText);
            emails.setText(emaill);
            TextView dates  = (TextView) mView.findViewById(R.id.dateText);
            dates.setText(date);

        }
    }

}
