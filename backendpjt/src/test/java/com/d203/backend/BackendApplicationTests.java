package com.d203.backend;

import com.d203.backend.db.entity.Follow;
import com.d203.backend.db.entity.User;
import com.d203.backend.db.repository.FollowRepository;
import com.d203.backend.db.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

@SpringBootTest
@Transactional
class BackendApplicationTests {
    @Autowired
    UserRepository userRepository;

    @Autowired
    FollowRepository followRepository;


    @Test
    void contextLoads() {
    }
    @Test
    void test1(){

    }

}
