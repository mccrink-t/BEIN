package com.example.belfastinanutshell.Model;

public class Users
{
    private String email, fullName, password, phone, image, institution, yearOfStudy, degreeOfStudy;

    public Users()
    {

    }

    public Users(String email, String fullName, String password, String phone, String image, String institution, String yearOfStudy, String degreeOfStudy) {
        this.email = email;
        this.fullName = fullName;
        this.password = password;
        this.phone = phone;
        this.image = image;
        this.institution = institution;
        this.yearOfStudy = yearOfStudy;
        this.degreeOfStudy = degreeOfStudy;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getYearOfStudy() {
        return yearOfStudy;
    }

    public void setYearOfStudy(String yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }

    public String getDegreeOfStudy() {
        return degreeOfStudy;
    }

    public void setDegreeOfStudy(String degreeOfStudy) {
        this.degreeOfStudy = degreeOfStudy;
    }
}
