package com.weblibrary.domain.user.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.weblibrary.domain.user.model.RoleType;
import com.weblibrary.domain.user.model.User;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.querydsl.jpa.JPAExpressions.select;
import static com.weblibrary.domain.user.model.QRole.role;
import static com.weblibrary.domain.user.model.QUser.user;

@Repository
@Slf4j
public class UserQueryRepository {

    private final JPAQueryFactory query;

    public UserQueryRepository(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    public List<User> findAll(UserSearchCond cond, Number limit, Number offset) {

        log.debug("cond={}", cond);

        return query.selectFrom(user)
                .limit(limit.longValue())
                .where(
                        likeUsername(cond.getUsername()),
                        eqRole(cond.getRoleType())
                )
                .orderBy(user.userId.desc())
                .limit(limit.longValue())
                .offset(offset.longValue())
                .fetch();
    }

    private BooleanExpression eqRole(RoleType roleType) {
        return roleType != null ? user.userId.in( // roleType에 해당하는 모든 userId를 찾아서 user.userId에 포함되는지 확인
                select(role.userId)
                        .from(role)
                        .where(role.roleType.eq(roleType)) // roleType에 해당하는 userId를 찾음
        ) : null;
    }

    private BooleanExpression likeUsername(String username) {
        return StringUtils.hasText(username) ? user.username.contains(username) : null;
    }

    public long count() {
        return query.selectFrom(user)
                .fetch()
                .size();
    }

}
