package com.raf.users.service;

import com.raf.users.domain.User;
import com.raf.users.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    User findById(Long userId);

    User findByUsername(String username);

    User findByEmail(String email);

    void save(UserCreateDto userCreateDto);

    Page<UserDto> getUsers(Pageable pageable);

    void deleteUser(Long userId);

    void updateUser(Long userId, boolean enabled);

    TokenResponseDto login(TokenRequestDto tokenRequestDto);

    ReservationDto getReservations(Long pageable);
}
