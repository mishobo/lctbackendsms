package com.compulynx.compas.models;

/**
 * Created by Kibet on 5/21/2016.
 */
public class CardSocketRequestVO {

    private String requestType;

    private String request;

    public CardSocketRequestVO() {
        super();
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }
}
