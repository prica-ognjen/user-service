package com.raf.users.dto;

public class HManagerCreateDto extends UserCreateDto{
    private String hotelName;

    public HManagerCreateDto() {

    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }
}
