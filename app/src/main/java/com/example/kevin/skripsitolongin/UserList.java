package com.example.kevin.skripsitolongin;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class UserList extends ArrayAdapter<User> {
    private Activity context;
    private List<User> userList;


    public UserList(Activity context, List<User>userList){
        super(context,R.layout.list_layout_user,userList);
        this.context = context;
        this.userList=userList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem =inflater.inflate(R.layout.list_layout_user,null,true);
        TextView textViewUserName= (TextView) listViewItem.findViewById(R.id.textViewRequestName);;
        TextView textViewUserApprove= (TextView) listViewItem.findViewById(R.id.textViewUserApprove);
        TextView textViewUserReject= (TextView) listViewItem.findViewById(R.id.textViewUserReject);
        TextView textViewUserEmail= (TextView) listViewItem.findViewById(R.id.textViewRequestEmail);

       ImageView imageView = (ImageView) listViewItem.findViewById(R.id.imageView4);
        User user =userList.get(position);

        textViewUserName.setText(user.getFullName());

        textViewUserEmail.setText(user.getEmail());


        if(user.getUserStatus().equals("Allowed"))
        {
            textViewUserApprove.setText(user.getUserStatus());
        }

        else{
            textViewUserReject.setText(user.getUserStatus());
        }
        return listViewItem;
    }
}



