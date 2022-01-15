package com.raf.users.dto;

public class ClientCreateDto extends UserCreateDto{
    private String passport;

    public ClientCreateDto() {
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }
}
