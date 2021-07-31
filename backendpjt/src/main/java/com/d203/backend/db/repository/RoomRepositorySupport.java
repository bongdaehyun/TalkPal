package com.d203.backend.db.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

//추후 queryDSL 연습 해볼 예정
@Repository
public class RoomRepositorySupport {
    @Autowired
    private JPAQueryFactory jpaQueryFactory;
}
