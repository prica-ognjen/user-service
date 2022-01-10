package com.raf.users.service.impl;


import com.raf.users.domain.Client;
import com.raf.users.domain.User;
import com.raf.users.dto.*;
import com.raf.users.exception.NotFoundException;
import com.raf.users.mapper.UserMapper;
import com.raf.users.repository.RankRepository;
import com.raf.users.repository.RoleRepository;
import com.raf.users.repository.UserRepository;
import com.raf.users.security.service.TokenService;
import com.raf.users.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RankRepository rankRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository, UserMapper userMapper, TokenService tokenService, RankRepository rankRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.tokenService = tokenService;
        this.rankRepository = rankRepository;
    }

    @Override
    public Page<UserDto> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::userToUserDto);
    }

    @Override
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException(
                        "user with id " + userId + " does not exist"
                ));
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException(
                        "user with username " + username + " does not exist"
                ));
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new IllegalStateException(
                        "user with email " + email + " does not exist"
                ));
    }

    @Override
    public void save(UserCreateDto userCreateDto) {
        User user = userMapper.userCreateDtoToUser(userCreateDto);
        Optional<User> userOptional = userRepository
                .findUserByEmail(userCreateDto.getEmail());

        if(userOptional.isPresent()){
            throw new IllegalStateException("Email taken");
        }
        userRepository.save(user);
    }

    @Override
    public void deleteUser(Long userId) {
        boolean exists = userRepository.existsById(userId);
        if(!exists){
            throw new IllegalStateException(
                    "user with id " + userId + " does not exist");
        }
        userRepository.deleteById(userId);
    }

    @Override
    @javax.transaction.Transactional
    public void updateUser(Long userId, boolean enabled) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException(
                        "user with id " + userId + " does not exist"
                ));
        user.setEnabled(enabled);
    }

    @Override
    public TokenResponseDto login(TokenRequestDto tokenRequestDto) {
        //Try to find active user for specified credentials
        User user = userRepository
                .findUserByUsernameAndPassword(tokenRequestDto.getUsername(), tokenRequestDto.getPassword())
                .orElseThrow(() -> new NotFoundException(String
                        .format("User with username: %s and password: %s not found.", tokenRequestDto.getUsername(),
                                tokenRequestDto.getPassword())));
        //Create token payload
        Claims claims = Jwts.claims();
        claims.put("id", user.getId());
        claims.put("role", user.getRole().getName());
        //Generate token
        if(!user.isEnabled()){
            throw new RuntimeException("Banovan user");
        }else{
            return new TokenResponseDto(tokenService.generate(claims));
        }


    }

    @Override
    public ReservationDto getReservations(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("No user")));
        if(((Client)user).getNumberOfReservations() + 1 > 10){
            user.setRank(rankRepository.findByName("ROLE_SILVER"));
        }else if(((Client)user).getNumberOfReservations() + 1 > 20){
            user.setRank(rankRepository.findByName("ROLE_GOLD"));
        }
        return userRepository.findById(id).map(userMapper::userToReservationDto).orElseThrow(() -> new NotFoundException(String.format("No reservations")));
    }
}
