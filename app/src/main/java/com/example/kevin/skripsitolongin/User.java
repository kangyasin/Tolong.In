package com.example.kevin.skripsitolongin;
//*untuk menyimpan nilai dari user
public class User {
    String userID;
    String fullName;
    String password;
    String email;
    String phone;
    String userStatus;
    String skill;



    public User(String userID, String fullName, String password, String email, String phone, String userStatus,  String skill) {
        this.userID = userID;
        this.fullName = fullName;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.userStatus =userStatus;
        this.skill= skill;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getUserID() {
        return userID;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getUserStatus() {
        return userStatus;
    }


    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public User(){

    }
}

