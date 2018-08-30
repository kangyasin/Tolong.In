package com.example.kevin.skripsitolongin;
//*membuat list dari transaction dalam bentuk listview
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class TransactionList extends ArrayAdapter<Transaction> {
    private Activity context;
    private List<Transaction> transactionList;

    public TransactionList(Activity context,List<Transaction>transactionList){
        super(context,R.layout.list_layout_transaction,transactionList);
        this.context = context;
        this.transactionList=transactionList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem =inflater.inflate(R.layout.list_layout_transaction,null,true);
        TextView textViewTransactionID= (TextView) listViewItem.findViewById(R.id.textViewTransactionID);
      //  TextView textViewRequestID= (TextView) listViewItem.findViewById(R.id.textViewRequestID);
        TextView textViewRequestName= (TextView) listViewItem.findViewById(R.id.textViewRequestNames);
        TextView textViewRequestKeahlian= (TextView) listViewItem.findViewById(R.id.textViewRequestKeahlian);
        TextView textViewRequestPhone= (TextView) listViewItem.findViewById(R.id.textViewRequestPhone);
        TextView textViewRequestAddress= (TextView) listViewItem.findViewById(R.id.textViewUserID);
        TextView textViewRequestEmailOrderBy= (TextView) listViewItem.findViewById(R.id.textViewRequestEmailOrderBy);
        TextView textViewRequestEmailAcceptBy= (TextView) listViewItem.findViewById(R.id.textViewRequestEmailAcceptBy);
        TextView textViewStatusComplete = (TextView) listViewItem.findViewById(R.id.textViewStatusComplete);
        TextView textViewStatusOnP = (TextView) listViewItem.findViewById(R.id.textViewStatusOnP);
        TextView textViewStatusAccepted = (TextView) listViewItem.findViewById(R.id.textViewStatusAccepted);
        Transaction transaction =transactionList.get(position);
        textViewTransactionID.setText(": "+transaction.getTransactionID());
       // textViewRequestID.setText(": "+transaction.getRequestID());
        textViewRequestName.setText(": "+transaction.getRequestName());
        textViewRequestKeahlian.setText(": "+transaction.getRequestSkill());
        textViewRequestPhone.setText(": "+transaction.getRequestPhone());
        textViewRequestAddress.setText(": "+transaction.getRequestAddress());
        textViewRequestEmailOrderBy.setText(": "+transaction.getRequestEmailOrderBy());
        textViewRequestEmailAcceptBy.setText(": "+transaction.getRequestEmailAcceptBy());
        if(transaction.getStatus().equals("Complete"))
        {
            textViewStatusComplete.setText(": "+transaction.getStatus());
        }
        else if(transaction.getStatus().equals("Accepted by User Request"))
        {
            textViewStatusAccepted.setText(": "+transaction.getStatus());
        }

        else{
            textViewStatusOnP.setText(": "+transaction.getStatus());
        }

        return listViewItem;
    }
}



