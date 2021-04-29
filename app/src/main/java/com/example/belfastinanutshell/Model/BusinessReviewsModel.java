package com.example.belfastinanutshell.Model;

public class BusinessReviewsModel
{
    public String review, fullName, date, time, rating;

    public BusinessReviewsModel() {
    }

    public BusinessReviewsModel(String review, String fullName, String date, String time, String rating) {
        this.review = review;
        this.fullName = fullName;
        this.date = date;
        this.time = time;
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
