package com.raf.users.controller;

import com.raf.users.dto.CReservationDataDto;
import com.raf.users.dto.ClientDto;
import com.raf.users.dto.ManagerDto;
import com.raf.users.dto.UserMailDto;
import com.raf.users.security.CheckSecurity;
import com.raf.users.security.service.TokenService;
import com.raf.users.service.UserService;
import io.jsonwebtoken.Claims;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/service")
public class InterServiceController {

    private final UserService userService;
    private final TokenService tokenService;

    public InterServiceController(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @CheckSecurity(roles = {"ROLE_USER","USER_MANAGER"})
    @GetMapping("/userEmail/{userId}")
    public ResponseEntity<CReservationDataDto> getUserEmail(@RequestHeader("Authorization") String authorization, @PathVariable("userId") Long id){
        return userService.getUserEmail(id);
    }

    @CheckSecurity(roles = {"ROLE_USER", "ROLE_MANAGER"})
    @GetMapping("/forArchive/{userId}")
    public ResponseEntity<UserMailDto> getMailFromUser(@RequestHeader("Authorization") String authorization, @PathVariable("userId") Long id){
        return userService.getMailFromUser(id);
    }

    @CheckSecurity(roles = {"ROLE_USER", "ROLE_MANAGER"})
    @GetMapping("/managerEmail/{hotelName}")
    public ResponseEntity<UserMailDto> getManagerEmail(@RequestHeader("Authorization") String authorization, @PathVariable("hotelName") String hotelName){
        return userService.getManagerEmail(hotelName);
    }

    @CheckSecurity(roles = {"ROLE_USER", "ROLE_MANAGER"})
    @PutMapping(path="/incRez/{userId}")
    public ResponseEntity<ClientDto> updateUserRez(
            @RequestHeader("Authorization") String authorization,
            @PathVariable("userId") Long userId){
        return userService.updateUserReservation(userId);
    }

    @CheckSecurity(roles = {"ROLE_USER", "ROLE_MANAGER"})
    @PutMapping(path="/decr/{userId}")
    public ResponseEntity<ClientDto> decrUserRez(
            @RequestHeader("Authorization") String authorization,
            @PathVariable("userId") Long userId){
        return userService.decrUserReservation(userId);
    }

    @CheckSecurity(roles = {"ROLE_MANAGER", "ROLE_USER"})
    @GetMapping("/daLiJeManager")
    public ResponseEntity<ManagerDto> getUsers(@RequestHeader("Authorization") String authorization){
        Claims claims = tokenService.parseToken(authorization.split(" ")[1]);

        Long l = new Long((Integer)claims.get("id"));
        return userService.isManager(l);
    }

}
