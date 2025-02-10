package com.weblibrary.domain.user.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.weblibrary.domain.user.model.User;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.weblibrary.domain.user.model.QUser.user;

@Repository
public class UserQueryRepository {

    private final JPAQueryFactory query;

    public UserQueryRepository(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    public List<User> findAll(Number limit, Number offset) {
        return query.selectFrom(user)
                .limit(limit.longValue())
                .offset(offset.longValue())
                .fetch();
    }

    public long count() {
        return query.selectFrom(user)
                .fetch()
                .size();
    }

}
