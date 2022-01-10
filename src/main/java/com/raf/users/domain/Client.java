package com.raf.users.domain;

import javax.persistence.Entity;

@Entity
public class Client extends User{

    private Long passportNumber;
    private Long numberOfReservations;
    private Rank rank;

    public Long getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(Long passportNumber) {
        this.passportNumber = passportNumber;
    }

    public Long getNumberOfReservations() {
        return numberOfReservations;
    }

    public void setNumberOfReservations(Long numberOfReservations) {
        this.numberOfReservations = numberOfReservations;
    }
}
