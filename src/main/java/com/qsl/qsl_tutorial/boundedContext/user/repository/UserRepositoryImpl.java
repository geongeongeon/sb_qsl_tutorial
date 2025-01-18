package com.qsl.qsl_tutorial.boundedContext.user.repository;

import com.qsl.qsl_tutorial.boundedContext.user.entity.SiteUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.qsl.qsl_tutorial.boundedContext.user.entity.QSiteUser.siteUser;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public SiteUser getQslUser(Long id) {
        /*
        SELECT *
        FROM site_user
        WHERE id = 1;
        */

        return jpaQueryFactory
                .selectFrom(siteUser) // SELECT * FROM site_user
                .where(siteUser.id.eq(id)) // WHERE id = 1
                .fetchOne(); // 단일 결과를 반환
    }

    @Override
    public long getQslCount() {
        /*
        SELECT COUNT(*)
        FROM site_user
        */

        return jpaQueryFactory
                .select(siteUser.count())
                .from(siteUser)
                .fetchOne();
    }

    @Override
    public SiteUser getQslUserOrderByIdAscOne() {
        /*
        SELECT *
        FROM site_user
        ORDER BY id asc
        LIMIT 1;
        */

        return jpaQueryFactory
                .selectFrom(siteUser)
                .orderBy(siteUser.id.asc())
                .limit(1)
                .fetchOne();
    }

    @Override
    public List<SiteUser> getQslUserOrderByIdAsc() {
        /*
        SELECT *
        FROM site_user
        ORDER BY id ASC;
        */

        return jpaQueryFactory
                .selectFrom(siteUser)
                .orderBy(siteUser.id.asc())
                .fetch();
    }

    @Override
    public List<SiteUser> searchQsl(String kw) {
        return List.of();
    }
}
