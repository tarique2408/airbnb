package com.airbnb.dto;

public class TokenResponseDto {

    //this class is used to send the token in json format not as string

    private String type="Bearer";//Bearer
    private String token;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
