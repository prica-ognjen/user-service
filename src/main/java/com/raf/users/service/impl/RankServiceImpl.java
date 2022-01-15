package com.raf.users.service.impl;

import com.raf.users.domain.Client;
import com.raf.users.domain.Rank;
import com.raf.users.dto.RankDto;
import com.raf.users.repository.ClientRepository;
import com.raf.users.repository.RankRepository;
import com.raf.users.repository.RoleRepository;
import com.raf.users.repository.UserRepository;
import com.raf.users.security.service.TokenService;
import com.raf.users.service.RankService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class RankServiceImpl implements RankService {

    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RankRepository rankRepository;
    private final ClientRepository clientRepository;

    public RankServiceImpl(TokenService tokenService, UserRepository userRepository, RoleRepository roleRepository, RankRepository rankRepository, ClientRepository clientRepository) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.rankRepository = rankRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public void deleteRank(Long rankId) {
        rankRepository.deleteById(rankId);
    }

    @Override
    public void editRank(Long rankId, Double popust) {
        Rank rank = rankRepository.findById(rankId)
                .orElseThrow(() -> new IllegalStateException(
                        "rank with id " + rankId + " does not exist"
                ));
        rank.setPopust(popust);
    }

    @Override
    public Rank addRank(Rank rank) {
        return rankRepository.save(rank);
    }

    @Override
    public Rank findRankByName(String name) {
        return rankRepository.findByName(name);
    }

    @Override
    public Rank findById(Long rankId) {
        return rankRepository.findById(rankId).orElseThrow(() -> new RuntimeException("Nije nadjeno"));
    }

    @Override
    public List<Rank> findAll() {
        return rankRepository.findAll();
    }

    @Override
    public ResponseEntity<RankDto> findDiscount(Long userId) {
        RankDto rankDto = new RankDto();
        rankDto.setName((clientRepository.getOne(userId)).getRank().getName());
        rankDto.setPopust((clientRepository.getOne(userId)).getRank().getPopust());
        return new ResponseEntity<>(rankDto, HttpStatus.OK);
    }
}
