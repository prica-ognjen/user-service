package com.raf.users.repository;

import com.raf.users.domain.Rank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RankRepository extends JpaRepository<Rank, Long> {

    @Query("Select r from Rank r where r.name = ?1")
    Rank findByName(String name);
}
