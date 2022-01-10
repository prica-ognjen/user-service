package com.raf.users.controller;

import com.raf.users.dto.*;
import com.raf.users.security.CheckSecurity;
import com.raf.users.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/clients")
    public void addNewUser(@RequestBody ClientCreateDto clientCreateDto) {
        userService.save(clientCreateDto);
    }

    @PostMapping(path = "/managers")
    public void addNewUser(@RequestBody HManagerCreateDto hManagerCreateDto) {
        userService.save(hManagerCreateDto);
    }

    @CheckSecurity(roles = {"ROLE_ADMIN"})
    @GetMapping
    public ResponseEntity<Page<UserDto>> getUsers(@RequestHeader("Authorization") String authorization, Pageable pageable){
        return new ResponseEntity<Page<UserDto>>(userService.getUsers(pageable), HttpStatus.OK);
    }

    @DeleteMapping(path = "{userId}")
    public void deleteUser(@PathVariable("userId") Long userId){
        userService.deleteUser(userId);
    }

    @CheckSecurity(roles = {"ROLE_ADMIN"})
    @PutMapping(path="{userId}")
    public void updateUser(
            @RequestHeader("Authorization") String authorization,
            @PathVariable("userId") Long userId,
            boolean enabled){
        userService.updateUser(userId,enabled);
    }

    @ApiOperation(value = "Login")
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> loginUser(@RequestBody @Valid TokenRequestDto tokenRequestDto) {
        return new ResponseEntity<>(userService.login(tokenRequestDto), HttpStatus.OK);
    }
    //Kasnije Promeniti da onaj koji je ulogovan da se njegov id koristi
    @CheckSecurity(roles = {"ROLE_USER"})
    @GetMapping("/{userId}")
    public ResponseEntity<ReservationDto> getReservationDto(@RequestHeader("Authorization") String authorization, @PathVariable("userId") Long id){
        return new ResponseEntity<>(userService.getReservations(id), HttpStatus.OK);
    }

}
