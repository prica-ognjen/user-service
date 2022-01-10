package com.raf.users.service;

import com.raf.users.domain.Rank;

import java.util.List;

public interface RankService {

    void deleteRank(Long rankId);
    void editRank(Long rankId, Double popust);
    Rank addRank(Rank rank);
    Rank findRankByName(String name);


    Rank findById(Long rankId);

    List<Rank> findAll();
}
