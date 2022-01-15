package com.raf.users.mapper;

import com.raf.users.domain.Client;
import com.raf.users.domain.HManager;
import com.raf.users.domain.User;
import com.raf.users.dto.*;
import com.raf.users.repository.RankRepository;
import com.raf.users.repository.RoleRepository;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    private RoleRepository roleRepository;
    private RankRepository rankRepository;

    public UserMapper(RoleRepository roleRepository,RankRepository rankRepository) {
        this.roleRepository = roleRepository;
        this.rankRepository = rankRepository;
    }
    public User userCreateDtoToUser(UserCreateDto userCreateDto){

        if(userCreateDto instanceof HManagerCreateDto){
            HManager hManager = new HManager();
            hManager.setPassword(userCreateDto.getPassword());
            hManager.setEmail(userCreateDto.getEmail());
            hManager.setBirthDate(userCreateDto.getBirthDate());
            hManager.setPhone(userCreateDto.getPhone());
            hManager.setFirstName(userCreateDto.getFirstName());
            hManager.setLastName(userCreateDto.getLastName());
            hManager.setRole(roleRepository.findByName("ROLE_MANAGER"));
            hManager.setHotelName(((HManagerCreateDto)userCreateDto).getHotelName());
            hManager.setUsername(userCreateDto.getUsername());
            hManager.setStartDate(((HManagerCreateDto) userCreateDto).getStart());
            hManager.setEnabled(false);
            return hManager;
        }else if(userCreateDto instanceof ClientCreateDto){
            Client client = new Client();
            client.setPassword(userCreateDto.getPassword());
            client.setEmail(userCreateDto.getEmail());
            client.setBirthDate(userCreateDto.getBirthDate());
            client.setPhone(userCreateDto.getPhone());
            client.setFirstName(userCreateDto.getFirstName());
            client.setLastName(userCreateDto.getLastName());
            client.setRole(roleRepository.findByName("ROLE_USER"));
            client.setRank(rankRepository.findByName("ROLE_BRONZE"));
            client.setUsername(userCreateDto.getUsername());
            client.setPassport(((ClientCreateDto) userCreateDto).getPassport());
            client.setEnabled(false);
            return client;

        }
       return null;
    }

    public UserDto userToUserDto(User user){
        UserDto userDto= new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setBirthDate(user.getBirthDate());
        userDto.setPhone(user.getPhone());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        return userDto;
    }

    public ReservationDto userToReservationDto(User user){
        ReservationDto resDto = new ReservationDto();
        resDto.setId(user.getId());
        resDto.setNumberOfReservations(((Client)user).getNumberOfReservations());
        return resDto;
    }

}
