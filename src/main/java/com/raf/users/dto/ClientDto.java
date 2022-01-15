package com.raf.users.dto;

public class ClientDto extends UserDto{
    private String passport;

    public ClientDto() {

    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }
}
