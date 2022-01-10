package com.raf.users.dto;

public class ClientCreateDto extends UserCreateDto{
    private Long passportNumber;

    public ClientCreateDto() {
    }

    public Long getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(Long passportNumber) {
        this.passportNumber = passportNumber;
    }
}
