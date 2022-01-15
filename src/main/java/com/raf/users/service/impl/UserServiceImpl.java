package com.raf.users.service.impl;


import com.raf.users.domain.Client;
import com.raf.users.domain.HManager;
import com.raf.users.domain.User;
import com.raf.users.dto.*;
import com.raf.users.exception.NotFoundException;
import com.raf.users.listener.helper.MessageHelper;
import com.raf.users.mapper.UserMapper;
import com.raf.users.repository.*;
import com.raf.users.security.service.TokenService;
import com.raf.users.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
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
    private final ClientRepository clientRepository;
    private final ManagerRepository managerRepository;
    private final JmsTemplate jmsTemplate;
    private String createUserEmailDestination;
    private final MessageHelper messageHelper;
    private final AdminRepository adminRepository;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository, UserMapper userMapper, TokenService tokenService, RankRepository rankRepository, ClientRepository clientRepository, ManagerRepository managerRepository, JmsTemplate jmsTemplate, @Value("${destination.createUserEmail}") String createUserEmailDestination, MessageHelper messageHelper, AdminRepository adminRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.tokenService = tokenService;
        this.rankRepository = rankRepository;
        this.clientRepository = clientRepository;
        this.managerRepository = managerRepository;
        this.jmsTemplate = jmsTemplate;
        this.createUserEmailDestination = createUserEmailDestination;
        this.messageHelper = messageHelper;
        this.adminRepository = adminRepository;
    }

    @Override
    public Page<UserDto> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::userToUserDto);
    }

    @Override
    public ResponseEntity<UserDto> save(UserCreateDto userCreateDto) {
        User user = userMapper.userCreateDtoToUser(userCreateDto);
        Optional<User> userOptional = userRepository
                .findUserByEmail(userCreateDto.getEmail());

        if(userOptional.isPresent()){
            throw new IllegalStateException("Email taken");
        }
        if(user.getEmail() == null || user.getEmail().equals("")){
            throw new IllegalStateException("Email mora lol");
        }

        User u = userRepository.save(user);

        RegisterNotificationDto registerNotificationDto = new RegisterNotificationDto();
        registerNotificationDto.setEmail(user.getEmail());
        registerNotificationDto.setFirstName(user.getFirstName());
        registerNotificationDto.setLastName(user.getLastName());
        registerNotificationDto.setId(user.getId());

        jmsTemplate.convertAndSend(createUserEmailDestination, messageHelper.createMessage(registerNotificationDto));
/*
        UserMailDto userMailDto = new UserMailDto(user.getEmail());
            jmsTemplate.convertAndSend(createUserEmailDestination, messageHelper.createMessage(userMailDto));

*/

        UserDto userDto = userMapper.userToUserDto(u);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteUser(Long userId) {
        boolean exists = userRepository.existsById(userId);
        if(!exists){
            throw new IllegalStateException(
                    "user with id " + userId + " does not exist");
        }

        userRepository.deleteById(userId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @javax.transaction.Transactional
    public void updateUser(Long userId, boolean enabled) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException(
                        "user with id " + userId + " does not exist"
                ));
        if(adminRepository.existsById(userId)){
            throw new RuntimeException("Admin si, ne mozes da se banujes, moras da radis.");
        }
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
    public ResponseEntity<ClientDto> updateUserReservation(Long userId) {
        Client client = clientRepository.getOne(userId);
        client.setNumberOfReservations(client.getNumberOfReservations() + 1);
        if(client.getNumberOfReservations() > 4 && client.getNumberOfReservations() < 9){
            //System.out.println("USO SILVER");
            client.setRank(rankRepository.findByName("ROLE_SILVER"));
        }else if(client.getNumberOfReservations() > 9){
            //System.out.println("USO GOLD");
            client.setRank(rankRepository.findByName("ROLE_GOLD"));
        }
        ClientDto dto = new ClientDto();
        dto.setId(client.getId());
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<UserMailDto> getManagerEmail(String hotelName) {
        System.out.println(hotelName + "LOOOOOOOOOOOOOOOOOOOOOL");
        HManager hManager = managerRepository.getMangerFromHotel(hotelName);
        if(hManager == null){
            return new ResponseEntity<>(new UserMailDto("NEMA MEJL"), HttpStatus.BAD_REQUEST);
        }
        //System.out.println(hManager.getId() + " " + hManager.getHotelName());
        User user = userRepository.getOne(hManager.getId());
        System.out.println(user.getEmail());
        //System.out.println("USO U USESRSERVICE" + managerRepository.getMangerFromHotel(hotelName).getEmail());
        return new ResponseEntity<>(new UserMailDto(user.getEmail()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CReservationDataDto> getUserEmail(Long id) {
        //System.out.println("USO U USESRSERVICE" + userRepository.getOne(id).getEmail());

        //System.out.println("JEL SI USO " + userRepository.getOne(id).getEmail());

        CReservationDataDto cReservationDataDto = new CReservationDataDto();
        cReservationDataDto.setEmail(userRepository.getOne(id).getEmail());
        cReservationDataDto.setFirstName(userRepository.getOne(id).getFirstName());
        cReservationDataDto.setLastName(userRepository.getOne(id).getLastName());

        return new ResponseEntity<>(cReservationDataDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> updateManager(Long l, ManagerUpdateDto managerUpdateDto) {
        User user = userRepository.getOne(l);
        if(!managerUpdateDto.getLastName().equals("")){
            user.setLastName(managerUpdateDto.getLastName());
        }
        if(!managerUpdateDto.getPhone().equals("")){
            user.setPhone(managerUpdateDto.getPhone());
        }
        if(!managerUpdateDto.getUsername().equals("")){
            user.setUsername(managerUpdateDto.getUsername());
        }
        if(!managerUpdateDto.getFirstName().equals("")){
            user.setFirstName(managerUpdateDto.getFirstName());
        }
        userRepository.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> updateClient(Long l, ClientUpdateDto clientUpdateDto) {
        User user = userRepository.getOne(l);
        if(!clientUpdateDto.getLastName().equals("")){
            user.setLastName(clientUpdateDto.getLastName());
        }
        if(!clientUpdateDto.getPhone().equals("")){
            user.setPhone(clientUpdateDto.getPhone());
        }
        if(!clientUpdateDto.getUsername().equals("")){
            user.setUsername(clientUpdateDto.getUsername());
        }
        if(!clientUpdateDto.getFirstName().equals("")){
            user.setFirstName(clientUpdateDto.getFirstName());
        }
        if(!clientUpdateDto.getPassport().equals("")){
            Client client = clientRepository.getOne(l);
            client.setPassport(clientUpdateDto.getPassport());
            clientRepository.save(client);
        }

        userRepository.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ManagerDto> isManager(Long l) {
        HManager manager = managerRepository.getOne(l);
        System.out.println("Ulazi u Da Trazi Managera");
        if(manager == null){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        ManagerDto managerDto = new ManagerDto(manager.getHotelName());
        return new ResponseEntity<>(managerDto, HttpStatus.OK);

    }

    @Override
    public ResponseEntity<ClientDto> decrUserReservation(Long userId) {
        Client client = clientRepository.getOne(userId);
        client.setNumberOfReservations(client.getNumberOfReservations() - 1);
        if(client.getNumberOfReservations() > 4 && client.getNumberOfReservations() < 9){
            //System.out.println("USO SILVER");
            client.setRank(rankRepository.findByName("ROLE_SILVER"));
        }else if(client.getNumberOfReservations() > 9){
            //System.out.println("USO GOLD");
            client.setRank(rankRepository.findByName("ROLE_GOLD"));
        }else if(client.getNumberOfReservations() <=4 ){
            client.setRank(rankRepository.findByName("ROLE_BRONZE"));
        }
        ClientDto dto = new ClientDto();
        dto.setId(client.getId());
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<UserMailDto> getMailFromUser(Long id) {
        return new ResponseEntity<>(new UserMailDto(userRepository.getOne(id).getEmail()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> activate(Long id) {

        boolean activated = false;

        boolean present = userRepository.findById(id).isPresent();

        if(present) {
            userRepository.findById(id).get().setEnabled(true);
            return new ResponseEntity<>(HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
