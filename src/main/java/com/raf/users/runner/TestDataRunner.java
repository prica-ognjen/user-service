package com.raf.users.runner;

import com.raf.users.domain.*;
import com.raf.users.repository.RankRepository;
import com.raf.users.repository.RoleRepository;
import com.raf.users.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile({"default"})
@Component
public class TestDataRunner implements CommandLineRunner {

    private RoleRepository roleRepository;
    private UserRepository userRepository;
    private RankRepository rankRepository;

    public TestDataRunner(RoleRepository roleRepository, UserRepository userRepository, RankRepository rankRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.rankRepository = rankRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Role roleUser = new Role("ROLE_USER");
        Role roleAdmin = new Role("ROLE_ADMIN");
        Role roleManager = new Role("ROLE_MANAGER");

        roleRepository.save(roleUser);
        roleRepository.save(roleAdmin);
        roleRepository.save(roleManager);

        Rank rankBronze = new Rank("ROLE_BRONZE");
        rankBronze.setPopust(0.0);
        Rank rankSilver = new Rank("ROLE_SILVER");
        rankSilver.setPopust(0.1);
        Rank rankGold = new Rank("ROLE_GOLD");
        rankGold.setPopust(0.15);

        rankRepository.save(rankBronze);
        rankRepository.save(rankSilver);
        rankRepository.save(rankGold);

        HManager admin = new HManager();
        admin.setUsername("admin");
        admin.setPassword("123");
        admin.setRole(roleRepository.findByName("ROLE_ADMIN"));
        admin.setEnabled(true);
        userRepository.save(admin);

        HManager manager = new HManager();
        manager.setUsername("manager");
        manager.setPassword("1234");
        manager.setRole(roleRepository.findByName("ROLE_MANAGER"));
        manager.setEnabled(true);
        userRepository.save(manager);

        Client client1 = new Client();
        client1.setUsername("client1");
        client1.setPassword("12345");
        client1.setRole(roleRepository.findByName("ROLE_USER"));
        client1.setEnabled(true);
        client1.setRank(rankRepository.findByName("ROLE_BRONZE"));
        userRepository.save(client1);

        Client client2 = new Client();
        client2.setUsername("client2");
        client2.setPassword("23456");
        client2.setRole(roleRepository.findByName("ROLE_USER"));
        client2.setEnabled(true);
        client2.setRank(rankRepository.findByName("ROLE_BRONZE"));
        userRepository.save(client2);

    }
}
