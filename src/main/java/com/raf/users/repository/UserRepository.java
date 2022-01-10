package com.raf.users.repository;

import com.raf.users.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("Select u from User u where u.email = ?1")
    Optional<User> findUserByEmail(String email);

    @Query("Select u from User u where u.username = ?1")
    Optional<User> findByUsername(String username);

    @Query("Select u from User u where u.username = ?1 and u.password = ?2")
    Optional<User> findUserByUsernameAndPassword(String username, String password);
}
