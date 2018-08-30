package com.example.kevin.skripsitolongin;
//*membuat list dari history dalam bentuk listview
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class HistoryTransactionList extends ArrayAdapter<HistoryTransaction> {
    private Activity context;
    private List<HistoryTransaction> historyTransactionList;

    public HistoryTransactionList(Activity context, List<HistoryTransaction>historyTransactionList){
        super(context,R.layout.list_layout_transaction,historyTransactionList);
        this.context = context;
        this.historyTransactionList=historyTransactionList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem =inflater.inflate(R.layout.list_layout_transaction,null,true);
        TextView textViewTransactionID= (TextView) listViewItem.findViewById(R.id.textViewTransactionID);
       // TextView textViewRequestID= (TextView) listViewItem.findViewById(R.id.textViewRequestID);
        TextView textViewRequestName= (TextView) listViewItem.findViewById(R.id.textViewRequestNames);
        TextView textViewRequestKeahlian= (TextView) listViewItem.findViewById(R.id.textViewRequestKeahlian);
        TextView textViewRequestPhone= (TextView) listViewItem.findViewById(R.id.textViewRequestPhone);
        TextView textViewRequestAddress= (TextView) listViewItem.findViewById(R.id.textViewUserID);
        TextView textViewRequestEmailOrderBy= (TextView) listViewItem.findViewById(R.id.textViewRequestEmailOrderBy);
        TextView textViewRequestEmailAcceptBy= (TextView) listViewItem.findViewById(R.id.textViewRequestEmailAcceptBy);
        TextView textViewStatusComplete = (TextView) listViewItem.findViewById(R.id.textViewStatusComplete);
        TextView textViewStatusOnP = (TextView) listViewItem.findViewById(R.id.textViewStatusOnP);
        TextView textViewStatusAccepted = (TextView) listViewItem.findViewById(R.id.textViewStatusAccepted);
        TextView textViewRating_GiveHelp  = (TextView) listViewItem.findViewById(R.id.textViewRating);
        HistoryTransaction historyTransaction =historyTransactionList.get(position);
        textViewTransactionID.setText(": "+historyTransaction.getTransactionID());
        //textViewRequestID.setText(": "+historyTransaction.getRequestID());
        textViewRequestName.setText(": "+historyTransaction.getRequestName());
        textViewRequestKeahlian.setText(": "+historyTransaction.getRequestSkill());
        textViewRequestPhone.setText(": "+historyTransaction.getRequestPhone());
        textViewRequestAddress.setText(": "+historyTransaction.getRequestAddress());
        textViewRequestEmailOrderBy.setText(": "+historyTransaction.getRequestEmailOrderBy());
        textViewRequestEmailAcceptBy.setText(": "+historyTransaction.getRequestEmailAcceptBy());
        textViewRating_GiveHelp.setText(""+historyTransaction.getRating_GiveHelp());
        if(historyTransaction.getStatus().equals("Complete"))
        {
            textViewStatusComplete.setText(": "+historyTransaction.getStatus());
        }
        else if(historyTransaction.getStatus().equals("Accepted by User Request"))
        {
            textViewStatusAccepted.setText(": "+historyTransaction.getStatus());
        }

        else{
            textViewStatusOnP.setText(": "+historyTransaction.getStatus());
        }

        return listViewItem;
    }
}



