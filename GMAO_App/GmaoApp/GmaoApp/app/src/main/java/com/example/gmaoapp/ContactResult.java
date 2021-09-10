package com.example.gmaoapp;

public class ContactResult {
    public static int userID;
    public static int employeID;
    public static Boolean result;
    public static String role;
    /*Web service*/
    public static String NAMESPACE = "http://tempuri.org/";
    public static String URL = "http://192.168.1.3/WSGMAO/WebService.asmx?WSDL";


    public ContactResult() {
    }
    public ContactResult(int user,int emp,boolean result,String role) {
        userID=user;
        employeID=emp;
        this.result=result;
        this.role=role;
    }
    public ContactResult(int user,int emp,boolean result,String namespace,String url) {
        userID=user;
        employeID=emp;
        this.result=result;
        NAMESPACE=namespace;
        URL=url;
    }
    public String getNAMESPACE() {
        return NAMESPACE;
    }

    public String getURL() {
        return URL;
    }
}
