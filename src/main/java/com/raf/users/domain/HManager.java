package com.raf.users.domain;

import javax.persistence.Entity;
import java.util.Date;

@Entity
public class HManager extends User{

    private String hotelName;
    private Date startDate;

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
}
