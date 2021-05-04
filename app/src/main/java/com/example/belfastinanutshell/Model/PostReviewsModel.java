package com.example.belfastinanutshell.Model;

public class PostReviewsModel {

    public String review, fullName, date, time, rating, reviewID;

    public PostReviewsModel() {

    }

    public PostReviewsModel(String review, String fullName, String date, String time, String rating, String reviewID) {
        this.review = review;
        this.fullName = fullName;
        this.date = date;
        this.time = time;
        this.rating = rating;
        this.reviewID = reviewID;
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

    public String getReviewID() {
        return reviewID;
    }

    public void setReviewID(String reviewID) {
        this.reviewID = reviewID;
    }
}
