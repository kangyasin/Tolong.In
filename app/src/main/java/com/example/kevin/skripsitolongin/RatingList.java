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

public class RatingList extends ArrayAdapter<Rating> {
    private Activity context;
    private List<Rating> ratingList;

    public RatingList(Activity context, List<Rating>ratingList){
        super(context,R.layout.list_layout_rating,ratingList);
        this.context = context;
        this.ratingList=ratingList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem =inflater.inflate(R.layout.list_layout_user,null,true);

        TextView textViewRating= (TextView) listViewItem.findViewById(R.id.textViewRating);
        Rating rating =ratingList.get(position);



        String key = String.valueOf(rating.rating);
        textViewRating.setText("Rating "+key);
        return listViewItem;
    }
}



