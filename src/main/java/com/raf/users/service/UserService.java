package com.raf.users.service;

import com.raf.users.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<UserDto> save(UserCreateDto userCreateDto);

    Page<UserDto> getUsers(Pageable pageable);

    ResponseEntity<Void> deleteUser(Long userId);

    void updateUser(Long userId, boolean enabled);

    TokenResponseDto login(TokenRequestDto tokenRequestDto);

    ResponseEntity<ClientDto> updateUserReservation(Long userId);


    ResponseEntity<UserMailDto> getManagerEmail(String hotelName);

    ResponseEntity<CReservationDataDto> getUserEmail(Long id);

    ResponseEntity<Void> updateManager(Long l, ManagerUpdateDto managerUpdateDto);

    ResponseEntity<Void> updateClient(Long l, ClientUpdateDto clientUpdateDto);

    ResponseEntity<ManagerDto> isManager(Long l);

    ResponseEntity<ClientDto> decrUserReservation(Long userId);

    ResponseEntity<UserMailDto> getMailFromUser(Long id);

    ResponseEntity<Void> activate(Long id);
}
