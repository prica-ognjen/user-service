package com.raf.users.repository;

import com.raf.users.domain.HManager;
import com.raf.users.dto.UserMailDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerRepository extends JpaRepository<HManager, Long> {
    @Query("select m from HManager m where m.hotelName = ?1")
    HManager getMangerFromHotel(String hotelName);
}
