package com.example.kevin.skripsitolongin;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class RequestList extends ArrayAdapter<Request> {
    private Activity context;
    private List<Request> requestList;

    public RequestList(Activity context,List<Request>requestList){
        super(context,R.layout.list_layout_request,requestList);
        this.context = context;
        this.requestList=requestList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem =inflater.inflate(R.layout.list_layout_request,null,true);
//        TextView textViewRequestID= (TextView) listViewItem.findViewById(R.id.textViewRequestID);
        TextView textViewRequestName= (TextView) listViewItem.findViewById(R.id.textViewRequestName);
        TextView textViewRequestKeahlian= (TextView) listViewItem.findViewById(R.id.textViewRequestKeahlian);
       // TextView textViewRequestPhone= (TextView) listViewItem.findViewById(R.id.textViewRequestPhone);
        TextView textViewRequestAddress= (TextView) listViewItem.findViewById(R.id.textViewUserID);
        TextView textViewRequestEmail= (TextView) listViewItem.findViewById(R.id.textViewRequestEmail);
        Request request =requestList.get(position);
//        textViewRequestID.setText(request.getRequestID());
        textViewRequestName.setText("  "+request.getRequestName());
        textViewRequestKeahlian.setText("  "+request.getRequestSkill());
       // textViewRequestPhone.setText("Phone: "+request.getRequestPhone());
        textViewRequestAddress.setText("  "+request.getRequestAddress());
        textViewRequestEmail.setText("  "+request.getRequestEmail());
        return listViewItem;
    }
}



