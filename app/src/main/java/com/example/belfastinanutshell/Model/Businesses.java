package com.example.belfastinanutshell.Model;

public class Businesses
{
    private String bName, description, location, image, openingHrs, bID, category, contactInfo, date, time, website, rating;

    public Businesses(){
        
    }

    public Businesses(String bName, String description, String location, String image, String openingHrs, String bID, String category, String contactInfo, String date, String time, String website, String rating) {
        this.bName = bName;
        this.description = description;
        this.location = location;
        this.image = image;
        this.openingHrs = openingHrs;
        this.bID = bID;
        this.category = category;
        this.contactInfo = contactInfo;
        this.date = date;
        this.time = time;
        this.website = website;
    }

    public String getbName() {
        return bName;
    }

    public void setbName(String bName) {
        this.bName = bName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOpeningHrs() {
        return openingHrs;
    }

    public void setOpeningHrs(String openingHrs) {
        this.openingHrs = openingHrs;
    }

    public String getbID() {
        return bID;
    }

    public void setbID(String bID) {
        this.bID = bID;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
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

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
