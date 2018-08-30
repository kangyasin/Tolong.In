package com.example.kevin.skripsitolongin;

public class Rating {
    Float rating;
    String ratingID;
    String userRatingID;


    public Rating(Float rating,String ratingID,String userRatingID) {
        this.rating =rating;
        this.ratingID=ratingID;
        this.userRatingID= userRatingID;
    }

    public String getUserRatingID() {
        return userRatingID;
    }

    public Float getRating() {
        return rating;
    }

    public String getRatingID() {
        return ratingID;
    }

    public  Rating (){}
}
