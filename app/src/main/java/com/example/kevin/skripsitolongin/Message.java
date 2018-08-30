package com.example.kevin.skripsitolongin;


public class Message {
    private String content;
    private String email;
    private String date;
    public Message(){

    }
    public Message(String content,String email,String date){
        this.content = content;
        this.email= email;
    }

    public String getContent() {
        return content;
    }

    public String getEmail() {
        return email;
    }
    public String getDate(){
        return date;
    }
    public void setContent(String content) {
        this.content = content;

    }
    public void setEmail(String email) {
        this.email = email;

    }
    public void setDate(String date){
        this.date = date;
    }
}
