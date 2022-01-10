package com.raf.users.controller;

import com.raf.users.domain.Rank;
import com.raf.users.security.CheckSecurity;
import com.raf.users.service.RankService;
import com.raf.users.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ranks")
public class RankController {

    private final UserService userService;
    private final RankService rankService;

    public RankController(UserService userService, RankService rankService) {
        this.userService = userService;
        this.rankService = rankService;
    }

    @CheckSecurity(roles = {"ROLE_ADMIN"})
    @DeleteMapping(path = "{rankId}")
    public ResponseEntity<?> deleteRank(@RequestHeader("Authorization") String authorization, @PathVariable("rankId") Long rankId){
        rankService.deleteRank(rankId);
        return new ResponseEntity<>( HttpStatus.OK);
    }

    @CheckSecurity(roles = {"ROLE_ADMIN"})
    @PostMapping
    public ResponseEntity<Rank> addRank(@RequestHeader("Authorization") String authorization, @RequestBody Rank rank){
        return new ResponseEntity<Rank>(rankService.addRank(rank), HttpStatus.CREATED);
    }

    @CheckSecurity(roles = {"ROLE_ADMIN"})
    @PutMapping(path = "{rankId}")
    public ResponseEntity<?> editRank(@RequestHeader("Authorization") String authorization, @PathVariable("rankId") Long rankId, Double popust){
        rankService.editRank(rankId, popust);
        return new ResponseEntity<>( HttpStatus.CREATED);
    }

    @CheckSecurity(roles = {"ROLE_ADMIN"})
    @GetMapping
    public ResponseEntity<?> getRanks(@RequestHeader("Authorization") String authorization){
        return new ResponseEntity<>(rankService.findAll(), HttpStatus.OK);
    }

    @CheckSecurity(roles = {"ROLE_ADMIN"})
    @GetMapping("{rankId}")
    public ResponseEntity<Rank> getRank(@RequestHeader("Authorization") String authorization, @PathVariable("rankId") Long rankId){
        return new ResponseEntity<Rank>(rankService.findById(rankId), HttpStatus.OK);
    }

}