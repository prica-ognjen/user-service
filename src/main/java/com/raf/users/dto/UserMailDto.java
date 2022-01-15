package com.raf.users.dto;

public class UserMailDto {

    private String email;

    public UserMailDto(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
