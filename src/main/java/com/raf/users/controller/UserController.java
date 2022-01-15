package com.raf.users.controller;

import com.raf.users.dto.*;
import com.raf.users.security.CheckSecurity;
import com.raf.users.security.service.TokenService;
import com.raf.users.service.UserService;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final TokenService tokenService;

    public UserController(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @PostMapping(path = "/clients")
    public ResponseEntity<UserDto> createUser(@RequestBody ClientCreateDto clientCreateDto) {
        return userService.save(clientCreateDto);
    }

    @GetMapping(path = "/activate/{id}")
    public ResponseEntity<Void> activateUser(WebRequest request, @PathVariable("id") Long id) {
        return userService.activate(id);
    }

    @GetMapping(path = "/passResetReq/{id}")
    public ResponseEntity<Void> requestPasswordReset(WebRequest request, @PathVariable("id") Long id) {
        return userService.requestPasswordReset(id);
    }

    @GetMapping(path = "/resetPassword/{id}")
    public ResponseEntity<Void> allowPasswordReset(WebRequest request, @PathVariable("id") Long id) {
        return userService.allowPasswordChange(id);
    }

    @GetMapping(path = "/account/{id}/resetPassword")
    public ResponseEntity<Void> resetPassword(@PathVariable("id") Long id, String newPassword) {
        return userService.changePassword(id, newPassword);
    }

    @PostMapping(path = "/managers")
    public ResponseEntity<UserDto> createManager(@RequestBody HManagerCreateDto hManagerCreateDto) {
        return userService.save(hManagerCreateDto);
    }

    @CheckSecurity(roles = {"ROLE_ADMIN"})
    @GetMapping
    public ResponseEntity<Page<UserDto>> getUsers(@RequestHeader("Authorization") String authorization, Pageable pageable){
        return new ResponseEntity<Page<UserDto>>(userService.getUsers(pageable), HttpStatus.OK);
    }

    @CheckSecurity(roles = {"ROLE_ADMIN"})
    @DeleteMapping(path = "{userId}")
    public ResponseEntity<Void> deleteUser(@RequestHeader("Authorization") String authorization,@PathVariable("userId") Long userId){
        return userService.deleteUser(userId);
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

    @CheckSecurity(roles = {"ROLE_MANAGER"})
    @PutMapping("/updateManager")
    public ResponseEntity<Void> updateManager(
            @RequestHeader("Authorization") String authorization,
            @RequestBody ManagerUpdateDto managerUpdateDto){
        Claims claims = tokenService.parseToken(authorization.split(" ")[1]);

        Long l = new Long((Integer)claims.get("id"));
        return userService.updateManager(l,managerUpdateDto);
    }

    @CheckSecurity(roles = {"ROLE_USER"})
    @PutMapping("/updateUser")
    public ResponseEntity<Void> updateClient(
            @RequestHeader("Authorization") String authorization,
            @RequestBody ClientUpdateDto clientUpdateDto){
        Claims claims = tokenService.parseToken(authorization.split(" ")[1]);

        Long l = new Long((Integer)claims.get("id"));
        return userService.updateClient(l,clientUpdateDto);
    }


}
