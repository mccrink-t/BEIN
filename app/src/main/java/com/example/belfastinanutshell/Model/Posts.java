package com.example.belfastinanutshell.Model;

public class Posts
{
    private String postTitle, postDescription, postText, image, rating, userID, usersFullName, date, time, postID;

    public Posts() {
    }

    public Posts(String postTitle, String postDescription, String postText, String image, String rating, String userID, String usersFullName, String date, String time, String postID) {
        this.postTitle = postTitle;
        this.postDescription = postDescription;
        this.postText = postText;
        this.image = image;
        this.rating = rating;
        this.userID = userID;
        this.usersFullName = usersFullName;
        this.date = date;
        this.time = time;
        this.postID = postID;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsersFullName() {
        return usersFullName;
    }

    public void setUsersFullName(String usersFullName) {
        this.usersFullName = usersFullName;
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

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }
}
