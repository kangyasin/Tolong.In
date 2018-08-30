package com.example.kevin.skripsitolongin;
//*untuk menyimpan nilai dari request
public class Request {
    String requestID;
    String requestSkill;
    String requestName;
    String requestPhone;
    String requestAddress;
    String requestEmail;
    String userID;


    public Request(String requestID, String requestName, String requestSkill,
                   String requestPhone, String requestAddress,String requestEmail,String userID)
    {

        this.requestID = requestID;
        this.requestSkill=requestSkill;
        this.requestName=requestName;
        this.requestPhone=requestPhone;
        this.requestAddress= requestAddress;
        this.requestEmail= requestEmail;
        this.userID= userID;
    }

    public String getRequestID() {
        return requestID;
    }
    public String getRequestSkill() {
        return requestSkill;
    }
    public String getRequestName() { return requestName; }
    public String  getRequestPhone() {
        return requestPhone;
    }
    public String getRequestAddress() {
        return requestAddress;
    }
    public String getRequestEmail() {
        return requestEmail;
    }
    public String getUserID() { return userID; }

    public Request(){

    }
}
