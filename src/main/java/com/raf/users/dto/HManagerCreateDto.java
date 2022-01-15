package com.raf.users.dto;

import java.util.Date;

public class HManagerCreateDto extends UserCreateDto{

    private String hotelName;
    private Date start;

    public HManagerCreateDto() {

    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }
}
