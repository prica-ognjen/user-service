package com.raf.users.dto;

public class ReservationDto {

    private Long id;
    private Long numberOfReservations;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNumberOfReservations() {
        return numberOfReservations;
    }

    public void setNumberOfReservations(Long numberOfReservations) {
        this.numberOfReservations = numberOfReservations;
    }
}
